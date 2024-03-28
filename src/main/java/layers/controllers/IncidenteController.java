package layers.controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import layers.models.domain.*;
import layers.services.IncidenteService;
import layers.services.ServicioAsociadoService;
import layers.services.UsuarioService;
import server.config.AppUtils;
import utils.ICrudViewsHandler;

public class IncidenteController implements ICrudViewsHandler {

  private IncidenteService incidenteService;
  private ServicioAsociadoService servicioAsociadoService;
  private UsuarioService usuarioService;

  public IncidenteController(IncidenteService incidenteService,
                             ServicioAsociadoService servicioAsociadoService,
                             UsuarioService usuarioService) {
    this.incidenteService = incidenteService;
    this.servicioAsociadoService = servicioAsociadoService;
    this.usuarioService = usuarioService;
  }

  @Override
  public void index(Context context) { //listarIncidentes()
    System.out.println("index");
    String descripcion = context.queryParam("descripcion");
    String servicioIncidentadoId = context.queryParam("servicioIncidentado");
    String resueltoParam = context.queryParam("resuelto");

    // Parsea el valor de resuelto
    Boolean resuelto = resueltoParam != null ? Boolean.parseBoolean(resueltoParam) : null;

    //obtenemos el id del usuario que esta logueado
    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioSesion.getId());
    System.out.println("Nombre: " + usuario.getUserName());


    // Realiza la lógica de filtrado según los parámetros proporcionados
    List<Incidente> incidentesFiltrados = incidenteService
        .filtrarIncidentes(descripcion, servicioIncidentadoId, resuelto, usuarioSesion.getId());

    // Carga las listas de servicios incidentados disponibles y otros datos necesarios
    List<ServicioAsociado> serviciosIncidentadosDisponibles = this.servicioAsociadoService.getAll();
    // ... otras listas necesarias ...

    //aca creamos el IncidenteConFechaFormateada para pasarlo al modelo
    List<IncidenteConFechaFormateada> listaIncidentesFormateados = new ArrayList<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    for (Incidente incidente : incidentesFiltrados) {
      String fechaCreacion = incidente.getFechaCreacion().format(formatter);
      String fechaResolucion = "No esta Resuelto";
      if (incidente.getFechaResolucion() != null) {
        fechaResolucion = incidente.getFechaResolucion().format(formatter);
      }
      IncidenteConFechaFormateada incidenteConFechaFormateada = new IncidenteConFechaFormateada();
      incidenteConFechaFormateada.setIncidente(incidente);
      incidenteConFechaFormateada.setFechaCreacion(fechaCreacion);
      incidenteConFechaFormateada.setFechaResolucion(fechaResolucion);
      listaIncidentesFormateados.add(incidenteConFechaFormateada);
    }

    // Prepara el modelo con los incidentes filtrados y otros datos
    Map<String, Object> model = new HashMap<>();
    model.put("incidentesFormateados", listaIncidentesFormateados);
    model.put("serviciosIncidentadosDisponibles", serviciosIncidentadosDisponibles);
    // ... otros datos necesarios ...

    // Renderiza la plantilla con los resultados filtrados
    context.render("incidentes/incidentes.hbs", model);
  }

  @Override
  public void show(Context context) {
    System.out.println("show");
    Incidente incidente = this.incidenteService.buscarYValidarIncidente(Integer.parseInt(context.pathParam("id")));
    Map<String, Object> model = new HashMap<>();
    model.put("incidente", incidente);
    context.render("incidentes/incidente.hbs", model);
  }

  @Override
  public void create(Context context) {
    System.out.println("create");
    Incidente incidente = new Incidente();
    List<ServicioAsociado> servicioAsociados = this.servicioAsociadoService.getAll();

    Map<String, Object> model = new HashMap<>();
    model.put("incidente", incidente);
    model.put("serviciosAsociados", servicioAsociados);

    context.render("incidentes/incidente.hbs", model);

  }

  @Override
  public void save(Context context) {
    System.out.println("save");
    Incidente incidente = new Incidente();

    asignarParametros(context, incidente);

    this.incidenteService.save(incidente);

    //Usuario usuario = this.usuarioService.buscarYValidarUsuario(1);
    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioSesion.getId());
    System.out.println("EN INCIDENTE: Ahora el usuario" + usuario.getUserName() + "tiene los siguientes servivios asociados: ");
    usuario.getServiciosAsociados().forEach(servicioAsociado -> System.out.println(servicioAsociado.getServicio().getNombre()));

    // Notificamos a los miembros de las comunidades.
    incidenteService.notificarComunidades(incidente);

    context.status(HttpStatus.CREATED);
    context.redirect("/incidentes");
  }

  @Override
  public void edit(Context context) {
    // el editado de incidente sera cuando se resuelva el incidente
    Incidente incidente = this.incidenteService.buscarYValidarIncidente(Integer.parseInt(context.pathParam("id")));
    System.out.println("Descripcion: " + incidente.getDescripcion());

    context.status(HttpStatus.OK);
    context.redirect("/incidentes");

  }

  @Override
  public void update(Context context) {
    System.out.println("update");
    Incidente incidente = this.incidenteService.buscarYValidarIncidente(Integer.parseInt(context.pathParam("id")));

    this.actualizarParametros(context, incidente);
    this.incidenteService.update(incidente);

    incidenteService.notificarComunidades(incidente);

    context.redirect("/incidentes");
  }


  @Override
  public void delete(Context context) {
    System.out.println("delete");

    Incidente incidente = this.incidenteService.buscarYValidarIncidente(Integer.parseInt(context.pathParam("id")));
    this.incidenteService.delete(incidente);
    context.redirect("/incidentes");
  }


  private void asignarParametros(Context context, Incidente incidente) {
    if (!Objects.equals(context.formParam("Descripcion"), "")) {
      incidente.setDescripcion(context.formParam("Descripcion"));
    }

    int id = Integer.parseInt(context.formParam("serviciosAsociados"));
    ServicioAsociado servicioIncidentado = this.servicioAsociadoService
        .buscarYValidarServicioAsociado(id);

    this.servicioAsociadoService.incidentarServicio(servicioIncidentado);
    incidente.setServicioIncidentado(servicioIncidentado);

    //int idUsuario = 1;
    //Usuario usuario = this.usuarioService.buscarYValidarUsuario(idUsuario);
    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioSesion.getId());
    List<Comunidad> comunidades = new ArrayList<>(usuario.getComunidades());
    incidente.setComunidades(comunidades);

    ZoneId zonaHorariaArgentina = ZoneId.of("America/Argentina/Buenos_Aires");
    incidente.setFechaCreacion(LocalDateTime.now(zonaHorariaArgentina));
    incidente.setUsuarioDeApertura(usuario);
  }

  private void actualizarParametros(Context context, Incidente incidente) {
    System.out.println("Resolviendo el incidente...");
    this.servicioAsociadoService.liberarServicio(incidente.getServicioIncidentado());
    this.incidenteService.resolverIncidente(incidente);

    //Usuario usuario = this.usuarioService.buscarYValidarUsuario(1);
    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = usuarioService.buscarYValidarUsuario(usuarioSesion.getId());
    incidente.setUsuarioDeResolucion(usuario);
  }
}
