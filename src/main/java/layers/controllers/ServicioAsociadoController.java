package layers.controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import layers.models.domain.*;
import layers.services.EntidadService;
import layers.services.EstablecimientoService;
import layers.services.ServicioAsociadoService;
import layers.services.ServicioService;
import utils.ICrudViewsHandler;

public class ServicioAsociadoController implements ICrudViewsHandler {
  private EstablecimientoService establecimientoService;
  private EntidadService entidadService;
  private ServicioService servicioService;
  private ServicioAsociadoService servicioAsociadoService;

  public ServicioAsociadoController(EstablecimientoService establecimientoService, EntidadService entidadService, ServicioService servicioService, ServicioAsociadoService servicioAsociadoService) {
    this.establecimientoService = establecimientoService;
    this.entidadService = entidadService;
    this.servicioService = servicioService;
    this.servicioAsociadoService = servicioAsociadoService;
  }

  @Override
  public void index(Context context) {
    System.out.println("index");
    List<ServicioAsociado> serviciosAsociados = this.servicioAsociadoService.getAll();

    // Recuperar los parámetros del filtro desde la solicitud
    String entidadId = context.queryParam("entidad");
    String establecimientoId = context.queryParam("establecimiento");
    String servicioId = context.queryParam("servicio");

    System.out.println("entidadId: " + entidadId);
    System.out.println("establecimientoId: " + establecimientoId);
    System.out.println("servicioId: " + servicioId);

    // Filtrar los servicios asociados según los parámetros de filtro
    if (entidadId != null && !entidadId.equals("")) {
      System.out.println("entidadId != null");
      System.out.println("entidadId: " + entidadId);
      serviciosAsociados = serviciosAsociados.stream()
          .filter(sa -> sa.getEntidad().getId() == Integer.parseInt(entidadId))
          .collect(Collectors.toList());
    }

    if (establecimientoId != null && !establecimientoId.equals("")) {
      System.out.println("establecimientoId != null");
      System.out.println("establecimientoId: " + establecimientoId);
      serviciosAsociados = serviciosAsociados.stream()
          .filter(sa -> sa.getEstablecimiento().getId() == Integer.parseInt(establecimientoId))
          .collect(Collectors.toList());
    }

    if (servicioId != null && !servicioId.equals("")) {
      System.out.println("servicioId != null");
      System.out.println("servicioId: " + servicioId);
      serviciosAsociados = serviciosAsociados.stream()
          .filter(sa -> sa.getServicio().getId() == Integer.parseInt(servicioId))
          .collect(Collectors.toList());
    }

    // Ahora, necesitas cargar las listas de entidades, establecimientos y servicios disponibles
    List<Entidad> entidadesDisponibles = entidadService.getAll();
    List<Establecimiento> establecimientosDisponibles = establecimientoService.getAll();
    List<Servicio> serviciosDisponibles = servicioService.getAll();

    Map<String, Object> model = new HashMap<>();
    model.put("serviciosAsociados", serviciosAsociados);
    model.put("entidadesDisponibles", entidadesDisponibles);
    model.put("establecimientosDisponibles", establecimientosDisponibles);
    model.put("serviciosDisponibles", serviciosDisponibles);

    context.render("servicios_asociados/serviciosAsociados.hbs", model);
  }

  @Override
  public void show(Context context) {
    System.out.println("show");
    ServicioAsociado servicioAsociado = this.servicioAsociadoService.buscarYValidarServicioAsociado(Integer.parseInt(context.pathParam("id")));
    Map<String, Object> model = new HashMap<>();
    model.put("servicioAsociado", servicioAsociado);
    context.render("servicios_asociados/serviciosAsociados.hbs", model);
  }

  @Override
  public void create(Context context) {
    System.out.println("create");
    ServicioAsociado servicioAsociado = new ServicioAsociado();

    List<Entidad> entidades = this.entidadService.getAll();
    List<Establecimiento> establecimientos = this.establecimientoService.getAll();
    List<Servicio> servicios = this.servicioService.getAll();

    Map<String, Object> model = new HashMap<>();
    model.put("servicioAsociado", servicioAsociado);
    model.put("entidades", entidades);
    model.put("establecimientos", establecimientos);
    model.put("servicios", servicios);

    context.render("servicios_asociados/servicioAsociado.hbs", model);
  }

  @Override
  public void save(Context context) {
    System.out.println("save");

    ServicioAsociado servicioAsociado = new ServicioAsociado();
    this.asignarParametros(servicioAsociado, context);
    this.servicioAsociadoService.save(servicioAsociado);

    context.status(HttpStatus.CREATED);
    context.redirect("/serviciosasociados");
  }

  @Override
  public void edit(Context context) {
    System.out.println("edit");

    ServicioAsociado servicioAsociado = this.servicioAsociadoService.buscarYValidarServicioAsociado(Integer.parseInt(context.pathParam("id")));
    System.out.println("Servicio: " + servicioAsociado.getServicio().getNombre());
    System.out.println("Establecimiento: " + servicioAsociado.getEstablecimiento().getNombre());
    System.out.println("Entidad: " + servicioAsociado.getEntidad().getNombre());


    List<Entidad> entidades = this.entidadService.getAll();
    List<Establecimiento> establecimientos = this.establecimientoService.getAll();
    List<Servicio> servicios = this.servicioService.getAll();

    Map<String, Object> model = new HashMap<>();
    model.put("servicioAsociado", servicioAsociado);
    model.put("entidades", entidades);
    model.put("establecimientos", establecimientos);
    model.put("servicios", servicios);

    context.render("servicios_asociados/servicioAsociado.hbs", model);
  }

  @Override
  public void update(Context context) {
    System.out.println("update");
    ServicioAsociado servicioAsociado = this.servicioAsociadoService.buscarYValidarServicioAsociado(Integer.parseInt(context.pathParam("id")));
    this.asignarParametros(servicioAsociado, context);
    this.servicioAsociadoService.update(servicioAsociado);

    context.redirect("/serviciosasociados");
  }

  @Override
  public void delete(Context context) {
    System.out.println("delete");

    ServicioAsociado servicioAsociado = this.servicioAsociadoService.buscarYValidarServicioAsociado(Integer.parseInt(context.pathParam("id")));
    this.servicioAsociadoService.delete(servicioAsociado);
    context.redirect("/serviciosasociados");
  }

  private void asignarParametros(ServicioAsociado servicioAsociado, Context context) {


    System.out.println("asignarParametros");

    if (!Objects.equals(context.formParam("entidad"), "")) {
      Entidad entidad = this.entidadService.buscarYValidarEntidad(Integer.parseInt(context.formParam("entidad")));
      servicioAsociado.setEntidad(entidad);
    }

    if (!Objects.equals(context.formParam("establecimiento"), "")) {
      Establecimiento establecimiento = this.establecimientoService.buscarYValidarEstablecimiento(Integer.parseInt(context.formParam("establecimiento")));
      servicioAsociado.setEstablecimiento(establecimiento);
    }

    if (!Objects.equals(context.formParam("servicio"), "")) {
      Servicio servicio = this.servicioService.buscarYValidarServicio(Integer.parseInt(context.formParam("servicio")));
      servicioAsociado.setServicio(servicio);
    }

  }


  private void actualizarParametros(ServicioAsociado servicioAsociado, Context context) {
    System.out.println("actualizar");
    if (!Objects.equals(context.formParam("entidad"), "")) {
      Entidad entidad = this.entidadService.buscarYValidarEntidad(Integer.parseInt(context.formParam("entidad")));
      servicioAsociado.setEntidad(entidad);
    }

    if (!Objects.equals(context.formParam("establecimiento"), "")) {
      Establecimiento establecimiento = this.establecimientoService.buscarYValidarEstablecimiento(Integer.parseInt(context.formParam("establecimiento")));
      servicioAsociado.setEstablecimiento(establecimiento);
    }

    if (!Objects.equals(context.formParam("servicio"), "")) {
      Servicio servicio = this.servicioService.buscarYValidarServicio(Integer.parseInt(context.formParam("servicio")));
      servicioAsociado.setServicio(servicio);
    }
  }

}
