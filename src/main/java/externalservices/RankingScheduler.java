package externalservices;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import externalservices.mensajeria.EmailSender;
import externalservices.mensajeria.JavaxMailSender;
import layers.models.domain.Entidad;
import layers.models.domain.OrganismoDeControl;
import layers.models.domain.PrestadoraDeServicio;
import layers.models.exportadapter.PDFAdapter.AdapterApachePDFBox;
import layers.models.repositories.*;
import layers.services.IncidenteService;
import layers.services.ServicioAsociadoService;
import layers.services.ServicioService;
import layers.services.UsuarioService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import utils.rankingincidente.CriterioRanking;
import utils.rankingincidente.MayorCantidadDeIncidentes;
import utils.rankingincidente.MayorGradoDeImpacto;
import utils.rankingincidente.MayorPromedioDeCierre;

public class RankingScheduler {

  public static ZonedDateTime getNextWeekStart() {
    LocalDate now = LocalDate.now();
    LocalDate nextMonday = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
    LocalDateTime nextWeekStart = nextMonday.atStartOfDay();

    ZoneId zone = ZoneId.systemDefault(); // O usa la zona horaria que necesites
    return nextWeekStart.atZone(zone);
  }

  public void scheduleNotificacionSemanal() throws SchedulerException {
    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    Scheduler scheduler = schedulerFactory.getScheduler();

    JobDetail jobDetail = JobBuilder.newJob(RankingJob.class)
        .withIdentity("rankingJob", "group1")
        .build();

    // Se obtiene el proximo lunes a las 00:00
    ZonedDateTime desiredTime = getNextWeekStart().withHour(0).withMinute(0).withSecond(0);

    // Si la hora deseada ya pasó para esta semana, programamos para la próxima semana
    if (desiredTime.isBefore(ZonedDateTime.now())) {
      desiredTime = desiredTime.plusWeeks(1);
    }

    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("rankingTrigger", "group1")
        .startAt(Date.from(desiredTime.toInstant()))
        .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(7 * 24 * 60)) // Cada semana
        .build();

//    Trigger trigger = TriggerBuilder.newTrigger()
//        .withIdentity("rankingTrigger", "group1")
//        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10))
//            .startNow()
//        .build();

    scheduler.scheduleJob(jobDetail, trigger);

    scheduler.start();
  }

  public static class RankingJob implements Job {
    IncidenteService incidenteService = new IncidenteService(new IncidenteRepository(), new UsuarioService(new UsuarioRepository(),new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository()))));
    ServicioAsociadoService servicioAsociadoService = new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository()));
    OrganismoDeControlRepository organismoDeControlRepository = new OrganismoDeControlRepository();
    PrestadoraDeServicioRepository prestadoraDeServicioRepository = new PrestadoraDeServicioRepository();

    public RankingJob() {
    }

    @Override
    public void execute(JobExecutionContext context) {
      System.out.println("...scheduler...");

      List<OrganismoDeControl> organismosDeControl = organismoDeControlRepository.getAll(OrganismoDeControl.class);
      List<PrestadoraDeServicio> prestadorasDeServicio = prestadoraDeServicioRepository.getAll(PrestadoraDeServicio.class);

      CriterioRanking mayorPromedioDeCierre = new MayorPromedioDeCierre("mayorPromedioDeCierre");
      CriterioRanking mayorCantidadDeIncidentes = new MayorCantidadDeIncidentes("mayorCantidadDeIncidentes");
//      CriterioRanking mayorGradoDeImpacto = new MayorGradoDeImpacto();

      EmailSender emailSender = new JavaxMailSender();



      organismosDeControl.forEach(organismoDeControl -> {

        mayorPromedioDeCierre.generarRankingDe(organismoDeControl);
        mayorCantidadDeIncidentes.generarRankingDe(organismoDeControl);
//        mayorGradoDeImpacto.generarRankingDe(organismoDeControl);

        System.out.println("scheduler generando pdfs...");
        AdapterApachePDFBox adapterApachePDFBox = new AdapterApachePDFBox();
        PDDocument documento = adapterApachePDFBox.exportarPorMail(mayorPromedioDeCierre, "mayorPromedioCierre");
        PDDocument documento2 = adapterApachePDFBox.exportarPorMail(mayorCantidadDeIncidentes, "mayorCantidadIncidentes");
        try {
          emailSender.sendPDFEmail(documento, "lucho.schirripa@gmail.com");
          emailSender.sendPDFEmail(documento2, "lucho.schirripa@gmail.com");

        } catch (Exception e) {
          e.printStackTrace();
        }
        System.out.println("pdfs generado por scheduler para los organismoDeControl");


        //notificar al organismo con los rankings
      });

      prestadorasDeServicio.forEach(prestadoraDeServicio -> {
        mayorPromedioDeCierre.generarRankingDe(prestadoraDeServicio);
        mayorCantidadDeIncidentes.generarRankingDe(prestadoraDeServicio);
//      mayorGradoDeImpacto.generarRankingDe(prestadoraDeServicio);

        System.out.println("scheduler generando pdfs...");
        AdapterApachePDFBox adapterApachePDFBox = new AdapterApachePDFBox();
        PDDocument documento = adapterApachePDFBox.exportarPorMail(mayorPromedioDeCierre, "mayorPromedioCierre");
        PDDocument documento2 = adapterApachePDFBox.exportarPorMail(mayorCantidadDeIncidentes, "mayorCantidadIncidentes");

        try {
          emailSender.sendPDFEmail(documento, "lucho.schirripa@gmail.com");
          emailSender.sendPDFEmail(documento2, "lucho.schirripa@gmail.com");

        } catch (Exception e) {
          e.printStackTrace();
        }

        System.out.println("pdfs generado por scheduler para las prestadoraDeServicio");
      });

      System.out.println("Rankings generados automáticamente.");
    }
  }
}
