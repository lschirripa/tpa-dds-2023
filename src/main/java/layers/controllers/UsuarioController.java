package layers.controllers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import layers.models.domain.ServicioAsociado;
import layers.models.domain.Usuario;
import layers.services.EntidadService;
import layers.services.EstablecimientoService;
import layers.services.ServicioAsociadoService;
import layers.services.UsuarioService;
import org.quartz.SchedulerException;

public class UsuarioController {
  private final UsuarioService usuarioService;
  private final EntidadService entidadService;
  private final EstablecimientoService establecimientoService;
  private final ServicioAsociadoService servicioAsociadoService;

  public UsuarioController(UsuarioService usuarioService, EntidadService entidadService,
                           EstablecimientoService establecimientoService,
                           ServicioAsociadoService servicioAsociadoService) {
    this.usuarioService = usuarioService;
    this.entidadService = entidadService;
    this.establecimientoService = establecimientoService;
    this.servicioAsociadoService = servicioAsociadoService;
  }

  //agregar todos los servicios de una entidad/establecimiento,
  //public void addServicioPorEntidadYEstablecimiento(int usuarioId, int entidadId,
  //                                                  int establecimientoId) {

  //  this.usuarioService.addServicioPorEntidadYEstablecimiento(
  //      usuarioId,
  //      this.entidadService.buscarYValidarEntidad(entidadId),
  //      this.establecimientoService.buscarYValidarEstablecimiento(establecimientoId));
  //}

  public void addServicioPorProvincia(int usuarioId) {
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioId);

    String provinciaUsuario = usuario.getLocalizacion().getProvincia();

    List<ServicioAsociado> serviciosAsociadosDispo =
        getServiciosAsociadosDispoEnProv(provinciaUsuario);

    usuario.getServiciosAsociados().addAll(serviciosAsociadosDispo);
  }

  private List<ServicioAsociado> getServiciosAsociadosDispoEnProv(String nombreProvincia) {
    List<ServicioAsociado> servDispoDeEntidad = servicioAsociadoService.getAll().stream()
        .filter(servicioAsociado -> servicioAsociado.getEntidad().getLocalizacion()
            .getProvincia().equals(nombreProvincia)).toList();

    List<ServicioAsociado> servDispoDeEstablecimiento =
        servicioAsociadoService.getAll().stream()
            .filter(servicioAsociado -> servicioAsociado.getEstablecimiento().getLocalizacion()
                .getProvincia().equals(nombreProvincia))
            .toList();

    List<ServicioAsociado> servDispoDeEntidadYEstablecimiento = new ArrayList<>();
    servDispoDeEntidadYEstablecimiento.addAll(servDispoDeEntidad);
    servDispoDeEntidadYEstablecimiento.addAll(servDispoDeEstablecimiento);

    return servDispoDeEntidadYEstablecimiento;
  }

  public void addServicioPorDepartamento(int usuarioId) {
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioId);

    String deptoUsuario = usuario.getLocalizacion().getDepartamento();

    List<ServicioAsociado> serviciosAsociadosDispo =
        getServiciosAsociadosDispoEnDpto(deptoUsuario);

    usuario.getServiciosAsociados().addAll(serviciosAsociadosDispo);
  }

  private List<ServicioAsociado> getServiciosAsociadosDispoEnDpto(String nombreDepto) {
    List<ServicioAsociado> servDispoDeEntidad = servicioAsociadoService.getAll().stream()
        .filter(servicioAsociado -> servicioAsociado.getEntidad().getLocalizacion()
            .getDepartamento().equals(nombreDepto)).toList();

    List<ServicioAsociado> servDispoDeEstablecimiento =
        servicioAsociadoService.getAll().stream()
            .filter(servicioAsociado -> servicioAsociado.getEstablecimiento().getLocalizacion()
                .getDepartamento().equals(nombreDepto)).toList();

    List<ServicioAsociado> servDispoDeEntidadYEstablecimiento = new ArrayList<>();
    servDispoDeEntidadYEstablecimiento.addAll(servDispoDeEntidad);
    servDispoDeEntidadYEstablecimiento.addAll(servDispoDeEstablecimiento);

    return servDispoDeEntidadYEstablecimiento;
  }

  public void addServicioPorMunicipio(int usuarioId) {
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioId);

    String municipioUsuario = usuario.getLocalizacion().getMunicipio();

    List<ServicioAsociado> serviciosAsociadosDispo =
        getServiciosAsociadosDispoEnMunicipio(municipioUsuario);

    usuario.getServiciosAsociados().addAll(serviciosAsociadosDispo);
  }

  private List<ServicioAsociado> getServiciosAsociadosDispoEnMunicipio(String municipioUsuario) {
    List<ServicioAsociado> servDispoDeEntidad = servicioAsociadoService.getAll().stream()
        .filter(servicioAsociado -> servicioAsociado.getEntidad().getLocalizacion()
            .getMunicipio().equals(municipioUsuario)).toList();

    List<ServicioAsociado> servDispoDeEstablecimiento =
        servicioAsociadoService.getAll().stream()
            .filter(servicioAsociado -> servicioAsociado.getEstablecimiento().getLocalizacion()
                .getMunicipio().equals(municipioUsuario)).toList();

    List<ServicioAsociado> servDispoDeEntidadYEstablecimiento = new ArrayList<>();
    servDispoDeEntidadYEstablecimiento.addAll(servDispoDeEntidad);
    servDispoDeEntidadYEstablecimiento.addAll(servDispoDeEstablecimiento);

    return servDispoDeEntidadYEstablecimiento;
  }

  public void addHorarioNotificacionUsuario(int usuarioId,
                                            LocalTime horario) throws SchedulerException {
    usuarioService.addHorarioNotificacionUsuario(usuarioId, horario);
  }

}
