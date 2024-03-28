package externalservices;

import externalservices.apiservicio2.entities.ResponseData;
import externalservices.apiservicio2.entities.UsuarioOutput;
import java.io.IOException;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import layers.models.domain.Entidad;
import layers.models.domain.GradoDeConfianza;
import layers.models.domain.OrganismoDeControl;
import layers.models.domain.PrestadoraDeServicio;
import layers.models.domain.Usuario;
import layers.models.repositories.OrganismoDeControlRepository;
import layers.models.repositories.PrestadoraDeServicioRepository;
import layers.models.repositories.UsuarioRepository;
import layers.services.IncidenteService;
import layers.services.ServicioAsociadoService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import retrofit2.Response;
import utils.rankingincidente.CriterioRanking;
import utils.rankingincidente.MayorCantidadDeIncidentes;
import utils.rankingincidente.MayorGradoDeImpacto;
import utils.rankingincidente.MayorPromedioDeCierre;

public class ServicioConfianzaScheduler {

  public static ZonedDateTime getNextWeekStart() {
    LocalDate now = LocalDate.now();
    LocalDate nextMonday = now.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
    LocalDateTime nextWeekStart = nextMonday.atTime(13, 00, 00);

    ZoneId zone = ZoneId.systemDefault();
    return nextWeekStart.atZone(zone);
  }

  public void scheduleConfianzaSemanal() throws SchedulerException {
    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    Scheduler scheduler = schedulerFactory.getScheduler();

    JobDetail jobDetail = JobBuilder.newJob(ConfianzaJob.class)
        .withIdentity("confianzaJob", "group1")
        .build();

    // Se obtiene el proximo domingo a las 13:00
    ZonedDateTime desiredTime = getNextWeekStart();

    // Si la hora deseada ya pasó para esta semana, programamos para la próxima semana
    if (desiredTime.isBefore(ZonedDateTime.now())) {
      desiredTime = desiredTime.plusWeeks(1);
    }

    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("ConfianzaTrigger", "group1")
        .startAt(Date.from(desiredTime.toInstant()))
        .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(7 * 24 * 60)) // Cada semana
        .build();

    scheduler.scheduleJob(jobDetail, trigger);

    scheduler.start();
  }

  public static class ConfianzaJob implements Job {
    CalculadorDeConfianza calculadorDeConfianza = new CalculadorDeConfianza();

    UsuarioRepository usuarioRepository = new UsuarioRepository();

    public ConfianzaJob() {
    }

    @Override
    public void execute(JobExecutionContext context) {

      System.out.println("Calculando confianza de las entidades");

      try {
        Response<ResponseData> response = calculadorDeConfianza.calcularConfianza();

        ResponseData responseBody = response.body();

        List<UsuarioOutput> usuariosOutput = responseBody.getUsuariosOutput();

        for (UsuarioOutput usuarioOutput : usuariosOutput) {
          int idUsuario = usuarioOutput.getId();

          Usuario usuario = usuarioRepository.getById(idUsuario, Usuario.class);

          usuario.setPuntosDeConfianza(usuarioOutput.getPuntajeFinal());

          usuario.setGradoDeConfianza(GradoDeConfianza.fromString(usuarioOutput.getNivelDeConfianza()));

          usuarioRepository.update(usuario);

        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
