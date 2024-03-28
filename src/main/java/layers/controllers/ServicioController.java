package layers.controllers;


import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import layers.models.domain.Servicio;
import layers.services.ServicioService;
import utils.ICrudViewsHandler;

public class ServicioController implements ICrudViewsHandler {
  private ServicioService servicioService;

  public ServicioController(ServicioService servicioService) {
    this.servicioService = servicioService;
  }

  @Override
  public void index(Context context) {
    System.out.println("index");
    Map<String, Object> model = new HashMap<>();
    List<Servicio> servicios = this.servicioService.getAll();
    model.put("servicios", servicios);
    context.render("servicios/servicios.hbs", model);
  }

  @Override
  public void show(Context context) {
    System.out.println("show");
    Servicio servicio = this.servicioService.buscarYValidarServicio(Integer.parseInt(context.pathParam("id")));
    Map<String, Object> model = new HashMap<>();
    model.put("servicio", servicio);
    context.render("servicios/servicio.hbs", model);
  }

  @Override
  public void create(Context context) {
    System.out.println("create");
    Servicio servicio = new Servicio();
    servicio.setNombre(context.formParam("nombre"));
    servicio.setDescripcion(context.formParam("descripcion"));

    Map<String, Object> model = new HashMap<>();
    model.put("servicio", servicio);
    context.render("servicios/servicio.hbs", model);
  }

  @Override
  public void save(Context context) {
    System.out.println("save");
    Servicio servicio = new Servicio();
    this.asignarParametros(servicio, context);
    this.servicioService.save(servicio);
    context.status(HttpStatus.CREATED);
    context.redirect("/servicios");
  }

  @Override
  public void edit(Context context) {
    System.out.println("edit");
    Servicio servicio = this.servicioService.buscarYValidarServicio(Integer.parseInt(context.pathParam("id")));
    System.out.println(servicio.getNombre());
    Map<String, Object> model = new HashMap<>();
    model.put("servicio", servicio);
    context.render("servicios/servicio.hbs", model);
  }

  @Override
  public void update(Context context) {
    System.out.println("update");
    Servicio servicio = this.servicioService.buscarYValidarServicio(Integer.parseInt(context.pathParam("id")));
    this.asignarParametros(servicio, context);
    this.servicioService.update(servicio);
    context.redirect("/servicios");
  }

  @Override
  public void delete(Context context) {
    System.out.println("delete");
    Servicio servicio = this.servicioService.buscarYValidarServicio(Integer.parseInt(context.pathParam("id")));
    this.servicioService.delete(servicio);
    context.redirect("/servicios");
  }

  private void asignarParametros(Servicio servicio, Context context) {
    if (!Objects.equals(context.formParam("nombre"), "")) {
      servicio.setNombre(context.formParam("nombre"));
    }
    if (!Objects.equals(context.formParam("descripcion"), "")) {
      servicio.setDescripcion(context.formParam("descripcion"));
    }
  }

}