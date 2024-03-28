package externalservices;

import java.time.LocalTime;
import java.util.List;
import layers.models.domain.Incidente;
import layers.models.domain.Usuario;
import layers.services.UsuarioService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class UsuarioScheduler {
  public void scheduleNotificaciones(Usuario usuario, LocalTime horario) throws SchedulerException {
    System.out.println("Instanciando el schedulerr");
    Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

    //List<LocalTime> horarios = usuario.getHorariosNotificacion();
    System.out.println("Creando el job...");
    JobDetail job = JobBuilder.newJob(NotificacionJob.class)
        .withIdentity("job-" + usuario.getId())
        .usingJobData("usuarioId", usuario.getId())
        .storeDurably()
        .build();

    //add job to the scheduler
    scheduler.addJob(job, true);

    //for (LocalTime horario : horarios) {
    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("trigger-" + usuario.getId() + "-" + horario.toString())
        .withSchedule(CronScheduleBuilder.cronSchedule(getCronExpression(horario)))
        .startNow()
        .forJob(job)
        .build();
    scheduler.scheduleJob(trigger);
    System.out.println("se chedulea el job" + job.getKey() + "con el trigger" + trigger.getKey());
    //}
    scheduler.start();

  }

  private String getCronExpression(LocalTime horario) {
    int minutos = horario.getMinute();
    int hora = horario.getHour();

    return String.format("0 %d %d ? * *", minutos, hora);
  }

  public static class NotificacionJob implements Job {
    private UsuarioService usuarioService = UsuarioService.getInstance();
    private Notificador notificador = Notificador.getInstance();

    public NotificacionJob() {
    }

    @Override
    public void execute(JobExecutionContext context) {
      int usuarioId = context.getJobDetail().getJobDataMap().getInt("usuarioId");
      Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioId);
      System.out.println("Notificando usuario con id: " + usuarioId + " y usuario: " + usuario.getUserName());

      List<Incidente> incidentesANotificar = usuario.getNotificacionIncidentesPendientes();
      System.out.println("Los incidentes a notificar son: " + incidentesANotificar.size());

      incidentesANotificar.forEach(incidente -> {
        System.out.println("El incidente a notificar es: " + incidente.getDescripcion() + " con id: " + incidente.getId());
        notificador.notificarUsuario(usuario, incidente, TipoMensaje.APERTURA_CIERRE);
      });
      usuarioService.removeAllNotificacionIncidentePendiente(usuario, incidentesANotificar);
    }
  }
}
