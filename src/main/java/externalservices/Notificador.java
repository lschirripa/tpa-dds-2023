package externalservices;

import java.time.LocalDateTime;
import layers.models.domain.*;
import externalservices.mensajeria.EmailSender;
import externalservices.mensajeria.JavaxMailSender;
import externalservices.mensajeria.TwilioWhatsappSender;
import externalservices.mensajeria.WhatsappSender;
import layers.services.UsuarioService;
import utils.enums.ModoNotificacion;

public class Notificador {
  private static Notificador instance = null;
  private static UsuarioService usuarioService;
  WhatsappSender whatsappSender = new TwilioWhatsappSender();
  EmailSender emailSender = new JavaxMailSender();

  public Notificador(UsuarioService usuarioService) {
    Notificador.usuarioService = usuarioService;
  }

  public static Notificador getInstance() {
    if (instance == null) {
      instance = new Notificador(usuarioService);
    }
    return instance;
  }

  public void notificarUsuarioSiCorresponde(Usuario miembro, Incidente incidente) {
    System.out.println("Verificamos si el usuario: " + miembro.getUserName() + " debe ser notificado");
    if (usuarioService.debeSerNotificadoEl(miembro, incidente)) {
      System.out.println("El usuario: " + miembro.getUserName() + " debe ser notificado");
      verificarFormaANotificar(miembro, incidente);
    } else {
      System.out.println("El usuario: " + miembro.getUserName() + " no debe ser notificado");
    }
  }

  public void verificarFormaANotificar(Usuario usuario, Incidente incidente) {
    switch (usuario.getFormaNotificacion()) {
      case LATER -> {
        System.out.println("Notificando al usuario: " + usuario.getUserName() + " luego por: " + usuario.getModoNotificacion());
        usuarioService.addNotificacionIncidentePendienteA(usuario, incidente);
      }
      case NOW -> {
        System.out.println("Notificando al usuario: " + usuario.getUserName() + " ahora por: " + usuario.getModoNotificacion());
        notificarUsuario(usuario, incidente, TipoMensaje.APERTURA_CIERRE);
      }
    }
  }

  public void notificarUsuario(Usuario usuario, Incidente incidente, TipoMensaje tipoMensaje) {
    String mensaje = getMessageDe(incidente, usuario, tipoMensaje);

    System.out.println("El mensaje a enviar es: " + mensaje);

    switch (usuario.getModoNotificacion()) {
      case WHATSAPP -> whatsappSender.sendWhatsapp(usuario, incidente, mensaje);
      case EMAIL -> emailSender.sendEmail(usuario, incidente, mensaje);
    }
  }

  public String getMessageDe(Incidente incidente, Usuario usuario, TipoMensaje tipoMensaje) {

    if (usuario.getModoNotificacion() == ModoNotificacion.EMAIL) {
      return getMessageDeEmail(incidente, tipoMensaje);
    } else {
      return getMessageDeWhatsapp(incidente, tipoMensaje);
    }
  }

  private String getMessageDeWhatsapp(Incidente incidente, TipoMensaje tipoMensaje) {
    ServicioAsociado servicioIncidentado = incidente.getServicioIncidentado();
    String mensaje = "";
    LocalDateTime fechaCreacion = incidente.getFechaCreacion();
    LocalDateTime fechaResolucion = incidente.getFechaResolucion();
    String fechaCreacionString = fechaCreacion.getDayOfMonth() + "/" + fechaCreacion.getMonthValue() + "/" + fechaCreacion.getYear() + " " + fechaCreacion.getHour() + ":" + fechaCreacion.getMinute();
    String fechaResolucionString = "";
    if (fechaResolucion != null) {
      fechaResolucionString = fechaResolucion.getDayOfMonth() + "/" + fechaResolucion.getMonthValue() + "/" + fechaResolucion.getYear() + " " + fechaResolucion.getHour() + ":" + fechaResolucion.getMinute();
    }


    if (tipoMensaje == TipoMensaje.REVISION) {
      mensaje = "Se ha detecta que se encuentra proximo a un incidente. Podria acercarse a revisarlo?"
          + "dicho incidente es:\n";
    }

    mensaje += "El servicio *" + servicioIncidentado.getServicio().getNombre()
        + "* del establecimiento *" + servicioIncidentado.getEstablecimiento().getNombre()
        + "* de la entidad *" + servicioIncidentado.getEntidad().getNombre()
        + "*\n\n"
    ;

    if (tipoMensaje == TipoMensaje.APERTURA_CIERRE) {
      if (servicioIncidentado.isIncidentado()) {
        mensaje += "\n\nHa sido INCIDENTADO.\n\nMotivo del Incidente: " + incidente.getDescripcion()
            + "\n\nFecha del incidente: " + fechaCreacionString;
      } else {
        mensaje += "\n\nHa sido SOLUCIONADO.\nEl problema era: " + incidente.getDescripcion()
            + "\nFecha de la solución: " + fechaResolucionString;
      }
      mensaje += "\n\nPara mas informacion, ingrese a: ";
    }

    if (tipoMensaje == TipoMensaje.REVISION) {
      mensaje += "\n\nSi dicho Incidente se encuentra en funcionamiento, por favor notificarlo en: ";
    }

    // TODO -> en el momento de deployearlo, cambiar el localhsot por el link correcto
    //mensaje += "http://localhost:8080/incidentes?id=" + Integer.toString(incidente.getId());
    mensaje += "https://grupo-2-dds-2023.onrender.com/incidentes";
    mensaje += "\n\nGracias por su colaboracion.";

    return mensaje;

  }

  private String getMessageDeEmail(Incidente incidente, TipoMensaje tipoMensaje) {
    ServicioAsociado servicioIncidentado = incidente.getServicioIncidentado();
    String mensaje = "";
    LocalDateTime fechaCreacion = incidente.getFechaCreacion();
    LocalDateTime fechaResolucion = incidente.getFechaResolucion();
    String fechaCreacionString = fechaCreacion.getDayOfMonth() + "/" + fechaCreacion.getMonthValue() + "/" + fechaCreacion.getYear() + " " + fechaCreacion.getHour() + ":" + fechaCreacion.getMinute();
    String fechaResolucionString = "";
    if (fechaResolucion != null) {
      fechaResolucionString = fechaResolucion.getDayOfMonth() + "/" + fechaResolucion.getMonthValue() + "/" + fechaResolucion.getYear() + " " + fechaResolucion.getHour() + ":" + fechaResolucion.getMinute();
    }


    if (tipoMensaje == TipoMensaje.REVISION) {
      mensaje = "Se ha detecta que se encuentra proximo a un incidente. Podria acercarse a revisarlo?\n"
          + "dicho incidente es:\n";
    }

    mensaje += "El servicio " + servicioIncidentado.getServicio().getNombre()
        + " del establecimiento " + servicioIncidentado.getEstablecimiento().getNombre()
        + " de la entidad " + servicioIncidentado.getEntidad().getNombre()
        + "\n\n"
    ;

    if (tipoMensaje == TipoMensaje.APERTURA_CIERRE) {
      if (servicioIncidentado.isIncidentado()) {
        mensaje += "\n\nHa sido INCIDENTADO.\n\nMotivo del Incidente: " + incidente.getDescripcion()
            + "\n\nFecha del incidente: " + fechaCreacionString;
      } else {
        mensaje += "\n\nHa sido SOLUCIONADO.\nEl problema era: " + incidente.getDescripcion()
            + "\nFecha de la solución: " + fechaResolucionString;
      }
      mensaje += "\n\nPara mas informacion, ingrese a: ";
    }

    if (tipoMensaje == TipoMensaje.REVISION) {

      mensaje +=
          "\n\nFecha del incidente: " + fechaCreacionString
              + "\n\nSi dicho Incidente se encuentra en funcionamiento, por favor notificarlo en: ";
    }

    // TODO -> en el momento de deployearlo, cambiar el localhsot por el link correcto
    //mensaje += "http://localhost:8080/incidentes?id=" + Integer.toString(incidente.getId());
    mensaje += "https://grupo-2-dds-2023.onrender.com/incidentes";
    mensaje += "\n\nGracias por su colaboracion.";

    return mensaje;
  }

}
