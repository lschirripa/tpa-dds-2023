package layers.services;

import excepciones.UsuarioInexistenteException;
import externalservices.UsuarioScheduler;
import java.time.LocalTime;
import java.util.List;
import layers.models.domain.*;
import layers.models.repositories.UsuarioRepository;
import org.quartz.SchedulerException;
import utils.enums.TipoUsuario;

public class UsuarioService {
  public static UsuarioRepository usuarioRepository;
  private static UsuarioService instance = null;
  private static ServicioAsociadoService servicioAsociadoService;
  private UsuarioScheduler usuarioScheduler;

  public UsuarioService(UsuarioRepository usuarioRepository,
                        ServicioAsociadoService servicioAsociadoService) {
    UsuarioService.usuarioRepository = usuarioRepository;
    UsuarioService.servicioAsociadoService = servicioAsociadoService;
    this.usuarioScheduler = new UsuarioScheduler();
  }

  public static UsuarioService getInstance() {
    if (instance == null) {
      instance = new UsuarioService(usuarioRepository, servicioAsociadoService);
    }
    return instance;
  }

  public Usuario buscarYValidarUsuario(int usuarioId) {
    Usuario usuario = usuarioRepository.getById(usuarioId, Usuario.class);
    if (usuario == null) {
      throw new UsuarioInexistenteException("Usuario no encontrado");
    }
    return usuario;
  }

  public void addServicioPorEntidadYEstablecimiento(int usuarioId,
                                                    Entidad entidad,
                                                    Establecimiento establecimiento,
                                                    TipoUsuario tipoUsuario) {
    Usuario usuario = buscarYValidarUsuario(usuarioId);
    establecimiento.getServicios().forEach(servicio -> {
      ServicioAsociado servicioAsociado = servicioAsociadoService.findBy(entidad, establecimiento, servicio);
      this.addServicioAsociado(usuarioId, servicioAsociado.getId(), tipoUsuario);
    });
    usuarioRepository.update(usuario);
  }

  public void addHorarioNotificacionUsuario(int usuarioId,
                                            LocalTime horario) throws SchedulerException {
    Usuario usuario = this.buscarYValidarUsuario(usuarioId);
    usuario.getHorariosNotificacion().add(horario);

    //genero un nuevo job para el horario de notificacion
    this.usuarioScheduler.scheduleNotificaciones(usuario, horario);
    this.update(usuario);
  }

  public boolean debeSerNotificadoEl(Usuario miembro, Incidente incidente) {
    System.out.println("El usuario " + miembro.getUserName() + " tiene los siguientes "
        + "servicios asociados: ");
    miembro.getServiciosAsociados().forEach(servicioAsociado -> {
      System.out.println(servicioAsociado.getServicio().getNombre());
    });
    return miembro.getServiciosAsociados().contains(incidente.getServicioIncidentado());
  }

  public void addNotificacionIncidentePendienteA(Usuario usuario, Incidente incidente) {
    usuario.addNotificacionIncidentesPendientes(incidente);
    System.out.println("Se agrego el incidente " + incidente.getDescripcion() + " a la lista de "
        + "incidentes pendientes de " + usuario.getUserName());
    this.update(usuario);
    System.out.println("Se actualizo el usuario " + usuario.getUserName());
  }

  public void removeAllNotificacionIncidentePendiente(Usuario usuario,
                                                      List<Incidente> incidentesANotificar) {
    usuario.removeAllNotificacionIncidentesPendientes(incidentesANotificar);
    this.update(usuario);
  }

  public void save(Usuario usuario) {
    usuarioRepository.save(usuario);
  }

  public void update(Usuario usuario) {
    usuarioRepository.update(usuario);
  }

  public void addServicioAsociado(int usuarioId, int servicioAsociadoId, TipoUsuario tipoUsuario) {
    Usuario usuario = buscarYValidarUsuario(usuarioId);
    ServicioAsociado servicioAsociado = servicioAsociadoService
        .buscarYValidarServicioAsociado(servicioAsociadoId);

    usuario.addServicioAsociado(servicioAsociado);
    usuarioRepository.update(usuario);

    System.out.println("Agregando un servicio asociado a un usuario");

    setearTipoDeUsuarioEnServicio(usuario, servicioAsociado, tipoUsuario);
  }

  public void setearTipoDeUsuarioEnServicio(Usuario usuario,
                                            ServicioAsociado servicioAsociado,
                                            TipoUsuario tipoUsuario) {
    if (tipoUsuario == TipoUsuario.AFECTADO) {
      servicioAsociado.addServicioUsuario(new ServicioUsuario(servicioAsociado, usuario, true, false));
      System.out.println("Se agrego el usuario " + usuario.getUserName() + " como afectado al "
          + "servicio " + servicioAsociado.getServicio().getNombre());
    } else {
      servicioAsociado.addServicioUsuario(new ServicioUsuario(servicioAsociado, usuario, false, true));
      System.out.println("Se agrego el usuario " + usuario.getUserName() + " como observador al "
          + "servicio " + servicioAsociado.getServicio().getNombre());
    }
    servicioAsociadoService.update(servicioAsociado);
  }

  public void removeServicioAsociado(int usuarioId, int servicioAsociadoId,
                                     TipoUsuario tipoUsuario) {
    Usuario usuario = buscarYValidarUsuario(usuarioId);
    ServicioAsociado servicioAsociado = servicioAsociadoService
        .buscarYValidarServicioAsociado(servicioAsociadoId);


    //setearTipoDeUsuarioEnServicio(usuario, servicioAsociado, tipoUsuario);
    usuario.removeServicioAsociado(servicioAsociado);
    usuarioRepository.update(usuario);

  }

  public void cambiarEstadoDeUsuarioAServicio(int servicioAsociadoId, int usuarioId) {
    Usuario usuario = buscarYValidarUsuario(usuarioId);
    ServicioAsociado servicioAsociado = servicioAsociadoService
        .buscarYValidarServicioAsociado(servicioAsociadoId);

    servicioAsociado.getServicioUsuarios().stream()
        .filter(servicioUsuario -> servicioUsuario.getUsuario().equals(usuario))
        .forEach(servicioUsuario -> {
          servicioUsuario.setAfectado(!servicioUsuario.isAfectado());
          servicioUsuario.setObservador(!servicioUsuario.isObservador());
          servicioAsociadoService.updateServicioUsuario(servicioUsuario);
        });

    servicioAsociadoService.update(servicioAsociado);
  }

  public List<Usuario> getAll() {
    return usuarioRepository.getAll(Usuario.class);
  }

  public void removeHorarioNotificacionUsuario(int id, LocalTime horarioNotificacion) {
    Usuario usuario = buscarYValidarUsuario(id);
    usuario.getHorariosNotificacion().remove(horarioNotificacion);
    this.update(usuario);
  }

  public Usuario buscarYValidarElUsuario(int id) {
    Usuario usuario = usuarioRepository.getUsuarioById(id, Usuario.class);
    if (usuario == null) {
      throw new UsuarioInexistenteException("Usuario no encontrado");
    }
    return usuario;
  }
}
