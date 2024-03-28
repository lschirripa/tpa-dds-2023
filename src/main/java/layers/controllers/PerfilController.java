package layers.controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import java.time.LocalTime;
import java.util.*;
import layers.models.domain.*;
import layers.models.repositories.EntidadRepository;
import layers.models.repositories.EstablecimientoRepository;
import layers.models.repositories.ServicioUsuarioRepository;
import layers.services.ComunidadService;
import layers.services.ServicioAsociadoService;
import layers.services.UsuarioService;
import org.jetbrains.annotations.NotNull;
import org.quartz.SchedulerException;
import server.config.AppUtils;
import utils.enums.FormaNotificacion;
import utils.enums.ModoNotificacion;
import utils.enums.TipoUsuario;

public class PerfilController {
  private final UsuarioService usuarioService;
  private final ComunidadService comunidadService;
  private final ServicioAsociadoService servicioAsociadoService;
  private final ServicioUsuarioRepository servicioUsuarioRepository;
  private final EntidadRepository entidadRepository;
  private final EstablecimientoRepository establecimientoRepository;


  public PerfilController(UsuarioService usuarioService, ComunidadService comunidadService, ServicioAsociadoService servicioAsociadoService, ServicioUsuarioRepository servicioUsuarioRepository, EntidadRepository entidadRepository, EstablecimientoRepository establecimientoRepository) {
    this.usuarioService = usuarioService;
    this.comunidadService = comunidadService;
    this.servicioAsociadoService = servicioAsociadoService;
    this.servicioUsuarioRepository = servicioUsuarioRepository;
    this.entidadRepository = entidadRepository;
    this.establecimientoRepository = establecimientoRepository;
  }

  public void index(Context context) {
    System.out.println("index");

    //Usuario usuario = usuarioService.buscarYValidarUsuario(1);

    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = usuarioService.buscarYValidarElUsuario(usuarioSesion.getId());

    Map<String, Object> model = new HashMap<>();
    model.put("usuario", usuario);

    context.render("perfil/perfil.hbs", model);
  }

  public void edit(Context context) {
    System.out.println("edit");

    //Usuario usuario = this.usuarioService.buscarYValidarUsuario(1);
    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioSesion.getId());
    System.out.println("Nombre: " + usuario.getUserName());

    //le tendriamos que pasar los posibles modos y formas de noti?
    Map<String, Object> model = new HashMap<>();
    model.put("usuario", usuario);

    // Crear una lista de modos de notificación sin duplicados
    Set<String> modosNotificacionSet = new LinkedHashSet<>();
    modosNotificacionSet.add(usuario.getModoNotificacion().name());
    modosNotificacionSet.add(ModoNotificacion.EMAIL.name());
    modosNotificacionSet.add(ModoNotificacion.WHATSAPP.name());

    List<String> modosNotificacion = new ArrayList<>(modosNotificacionSet);
    model.put("modoNotificacion", modosNotificacion);

    // Crear una lista de modos de notificación sin duplicados
    Set<String> formasNotificacionSet = new LinkedHashSet<>();
    formasNotificacionSet.add(usuario.getFormaNotificacion().name());
    formasNotificacionSet.add(FormaNotificacion.NOW.name());
    formasNotificacionSet.add(FormaNotificacion.LATER.name());

    List<String> formasNotificacion = new ArrayList<>(formasNotificacionSet);
    model.put("formaNotificacion", formasNotificacion);

    context.render("perfil/editarPerfil.hbs", model);
  }

  public void update(Context context) {
    System.out.println("update");
    //Usuario usuario = this.usuarioService.buscarYValidarUsuario(1);
    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioSesion.getId());

    this.actualizarParametros(usuario, context);
    this.usuarioService.update(usuario);

    context.redirect("/perfil");
  }

  private void actualizarParametros(Usuario usuario, Context context) {
    System.out.println("asignarParametros");

    if (!Objects.equals(context.formParam("userName"), "")) {
      usuario.setUserName(context.formParam("userName"));
    }

    if (!Objects.equals(context.formParam("nombre"), "")) {
      usuario.getDatosPersonales().setNombre(context.formParam("nombre"));
    }

    if (!Objects.equals(context.formParam("apellido"), "")) {
      usuario.getDatosPersonales().setApellido(context.formParam("apellido"));
    }

    if (!Objects.equals(context.formParam("correo"), "")) {
      usuario.getDatosPersonales().setCorreo(context.formParam("correo"));
    }

    if (!Objects.equals(context.formParam("telefono"), "")) {
      usuario.getDatosPersonales().setTelefono(context.formParam("telefono"));
    }

    if (!Objects.equals(context.formParam("modoNotificacion"), "")) {
      usuario.setModoNotificacion(ModoNotificacion.valueOf(context.formParam("modoNotificacion")));
    }

    if (!Objects.equals(context.formParam("formaNotificacion"), "")) {
      usuario.setFormaNotificacion(FormaNotificacion.valueOf(context.formParam("formaNotificacion")));
    }
  }

  public void addComunidad(@NotNull Context context) {
    System.out.println("AddComunidad");
    Map<String, Object> model = new HashMap<>();

    //Usuario usuario = this.usuarioService.buscarYValidarUsuario(1);
    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioSesion.getId());

    List<Comunidad> comunidadesActuales = usuario.getComunidades();
    List<Comunidad> comunidadesTotalesActivas = comunidadService.getAllActivas();
    comunidadesTotalesActivas.removeAll(comunidadesActuales);

    model.put("comunidades", comunidadesTotalesActivas);
    context.render("perfil/agregarComunidad.hbs", model);
  }

  public void saveComunidad(Context context) {
    System.out.println("saveComunidad");
    //Usuario usuario = this.usuarioService.buscarYValidarUsuario(1);
    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioSesion.getId());

    List<Integer> id = context.formParams("comunidades").stream().map(Integer::parseInt).toList();
    List<Comunidad> comunidades = id.stream().map(this.comunidadService::obtenerYValidarComunidad).toList();
    usuario.getComunidades().addAll(comunidades);

    this.usuarioService.update(usuario);

    comunidades.forEach(comunidad -> comunidad.getMiembros().add(usuario));
    comunidades.forEach(this.comunidadService::update);

    context.status(HttpStatus.OK);
    context.redirect("/perfil");
  }

  public void addServicio(@NotNull Context context) {
    System.out.println("AddServicioAsociado");
    Map<String, Object> model = new HashMap<>();

    //Usuario usuario = this.usuarioService.buscarYValidarUsuario(1);
    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioSesion.getId());
    List<ServicioAsociado> servicioAsociados = usuario.getServiciosAsociados();
    List<ServicioAsociado> servicioAsociadosTotales = servicioAsociadoService.getAll();
    servicioAsociadosTotales.removeAll(servicioAsociados);

    List<String> tiposDeUsuarios = new ArrayList<>();
    tiposDeUsuarios.add(TipoUsuario.AFECTADO.name());
    tiposDeUsuarios.add(TipoUsuario.OBSERVADOR.name());

    model.put("tiposDeUsuarios", tiposDeUsuarios);
    model.put("serviciosAsociados", servicioAsociadosTotales);
    context.render("perfil/agregarServicioAsociado.hbs", model);
  }

  public void saveServicio(Context context) {
    System.out.println("saveServicioAsociado");

    //Usuario usuario = this.usuarioService.buscarYValidarUsuario(1);
    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioSesion.getId());

    List<Integer> id = context.formParams("serviciosAsociados").stream().map(Integer::parseInt).toList();
    List<ServicioAsociado> serviciosAsociados = id.stream().map(this.servicioAsociadoService::buscarYValidarServicioAsociado).toList();

    String tipoUsuario = context.formParam("tipoUsuario");
    TipoUsuario tipoUsuarioEnum = TipoUsuario.valueOf(tipoUsuario);

    serviciosAsociados.forEach(servicioAsociado -> usuarioService
        .addServicioAsociado(usuario.getId(), servicioAsociado.getId(), tipoUsuarioEnum));

    System.out.println("EN SERVICIO: Ahora el usuario tiene los siguientes servivios asociados: ");
    usuario.getServiciosAsociados().forEach(servicioAsociado -> System.out.println(servicioAsociado.getServicio().getNombre()));

    context.status(HttpStatus.OK);
    context.redirect("/perfil");
  }

  public void addHorarioNotificacion(@NotNull Context context) {
    System.out.println("addHorario");
    Map<String, Object> model = new HashMap<>();

    //TODO -> Recuperar el usuario real
    //Usuario usuario = this.usuarioService.buscarYValidarUsuario(1);

    context.render("perfil/agregarHorario.hbs", model);
  }

  public void saveHorarioNotificacion(Context context) throws SchedulerException {
    System.out.println("saveHorario");
    //Usuario usuario = this.usuarioService.buscarYValidarUsuario(1);
    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioSesion.getId());

    //(@RequestParam("nuevoHorarioNotificacion") LocalTime nuevoHorario)
    LocalTime horarioNotificacion = LocalTime.parse(context.formParam("horarioNotificacion"));
    System.out.println(horarioNotificacion);

    this.usuarioService.addHorarioNotificacionUsuario(usuario.getId(), horarioNotificacion);
    this.usuarioService.update(usuario);

    context.status(HttpStatus.OK);
    context.redirect("/perfil");
  }


  public void deleteComunidad(Context context) {
    System.out.println("deleteComunidad");

    //Usuario usuario = this.usuarioService.buscarYValidarUsuario(1);
    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioSesion.getId());
    Comunidad comunidad = this.comunidadService.obtenerYValidarComunidad(Integer.parseInt(context.pathParam("id")));

    usuario.removeComunidad(comunidad);
    this.usuarioService.update(usuario);

    comunidad.removeMiembro(usuario);
    this.comunidadService.update(comunidad);

    context.status(HttpStatus.OK);
    context.redirect("/perfil");
  }


  public void deleteServicio(@NotNull Context context) {
    System.out.println("deleteServicioAsociado");

    //Usuario usuario = this.usuarioService.buscarYValidarUsuario(1);
    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioSesion.getId());

    ServicioAsociado servicioAsociado = this.servicioAsociadoService.buscarYValidarServicioAsociado(Integer.parseInt(context.pathParam("id")));
    ServicioUsuario servicioUsuario = servicioUsuarioRepository.findByUsuarioAndServicioAsociado(usuario.getId(), servicioAsociado.getId());

    System.out.println("Encontramos el servicio Usuario con:");
    System.out.println("Usuario: " + servicioUsuario.getUsuario().getId());
    System.out.println("Servicio Asociado: " + servicioUsuario.getServicioAsociado().getId());
    if (servicioUsuario.isObservador()) {
      System.out.println("Es observador");
    } else if (servicioUsuario.isAfectado()) {
      System.out.println("Es afectado");
    }


    TipoUsuario tipoUsuario = null;
    if (servicioUsuario.isAfectado()) {
      tipoUsuario = TipoUsuario.AFECTADO;
    } else if (servicioUsuario.isObservador()) {
      tipoUsuario = TipoUsuario.OBSERVADOR;
    }

    this.servicioUsuarioRepository.delete(servicioUsuario);
    this.usuarioService.removeServicioAsociado(usuario.getId(), servicioAsociado.getId(), tipoUsuario);

    context.status(HttpStatus.OK);
    context.redirect("/perfil");
  }

  public void deleteHorario(@NotNull Context context) {
    System.out.println("deleteHorario");

    //Usuario usuario = this.usuarioService.buscarYValidarUsuario(1);
    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioSesion.getId());
    LocalTime horarioNotificacion = LocalTime.parse(context.formParam("horarioNotificacion"));
    System.out.println("se elimina el siguiente horario: " + horarioNotificacion);

    this.usuarioService.removeHorarioNotificacionUsuario(usuario.getId(), horarioNotificacion);

    context.status(HttpStatus.OK);
    context.redirect("/perfil");
  }

  public void addServicioMultiple(@NotNull Context context) {
    System.out.println("Agregando multiples servicios dado un establecimiento");
    Map<String, Object> model = new HashMap<>();

    //Usuario usuario = this.usuarioService.buscarYValidarUsuario(1);
    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioSesion.getId());
    List<Entidad> entidades = entidadRepository.getAll(Entidad.class);
    List<Establecimiento> establecimientos = establecimientoRepository.getAll(Establecimiento.class);

    model.put("entidades", entidades);
    model.put("establecimientos", establecimientos);

    List<String> tiposDeUsuarios = new ArrayList<>();
    tiposDeUsuarios.add(TipoUsuario.AFECTADO.name());
    tiposDeUsuarios.add(TipoUsuario.OBSERVADOR.name());

    model.put("tiposDeUsuarios", tiposDeUsuarios);

    context.render("perfil/agregarMultiplesServicios.hbs", model);
  }

  public void saveServicioMultiple(Context context) {
    System.out.println("saveServicioAsociadoMultiple");

    //Usuario usuario = this.usuarioService.buscarYValidarUsuario(1);
    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioSesion.getId());


    int idEntidad = (Integer.parseInt(context.formParam("entidad")));
    int idEstablecimiento = Integer.parseInt(context.formParam("establecimiento"));

    Entidad entidad = this.entidadRepository.getById(idEntidad, Entidad.class);
    Establecimiento establecimiento = this.establecimientoRepository.getById(idEstablecimiento, Establecimiento.class);

    String tipoUsuario = context.formParam("tipoUsuario");
    TipoUsuario tipoUsuarioEnum = TipoUsuario.valueOf(tipoUsuario);

    usuarioService.addServicioPorEntidadYEstablecimiento(usuario.getId(), entidad, establecimiento, tipoUsuarioEnum);

    System.out.println("EN SERVICIO: Ahora el usuario tiene los siguientes servivios asociados: ");
    usuario.getServiciosAsociados().forEach(servicioAsociado -> System.out.println(servicioAsociado.getServicio().getNombre()));

    context.status(HttpStatus.OK);
    context.redirect("/perfil");
  }
}
