package layers.controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import layers.models.domain.Comunidad;
import layers.models.domain.GradoDeConfianza;
import layers.models.domain.Incidente;
import layers.models.domain.Usuario;
import layers.services.ComunidadService;
import layers.services.IncidenteService;
import layers.services.UsuarioService;
import utils.ICrudViewsHandler;

public class ComunidadController implements ICrudViewsHandler {
  private final ComunidadService comunidadService;
  private final UsuarioService usuarioService;
  private final IncidenteService incidenteService;

  public ComunidadController(ComunidadService comunidadService, UsuarioService usuarioService, IncidenteService incidenteService) {
    this.comunidadService = comunidadService;
    this.usuarioService = usuarioService;
    this.incidenteService = incidenteService;
  }

  @Override
  public void index(Context context) {
    System.out.println("index");
    List<Comunidad> comunidades = this.comunidadService.getAll();

    Map<String, Object> model = new HashMap<>();
    model.put("comunidades", comunidades);

    context.render("comunidades/comunidades.hbs", model);
  }

  @Override
  public void show(Context context) {
    System.out.println("show");
    Comunidad comunidad = this.comunidadService.obtenerYValidarComunidad(Integer.parseInt(context.pathParam("id")));
    Map<String, Object> model = new HashMap<>();
    model.put("comunidad", comunidad);
    context.render("comunidades/comunidad.hbs", model);
  }

  @Override
  public void create(Context context) {
    System.out.println("create");
    Comunidad comunidad = new Comunidad();
    List<Usuario> miembrosDisponibles = this.usuarioService.getAll();

    Map<String, Object> model = new HashMap<>();
    model.put("comunidad", comunidad);
    model.put("miembrosDisponibles", miembrosDisponibles);

    context.render("comunidades/comunidad.hbs", model);
  }

  @Override
  public void save(Context context) {
    System.out.println("save");

    Comunidad comunidad = new Comunidad();
    this.asignarParametros(comunidad, context);
    this.comunidadService.save(comunidad);

    context.status(HttpStatus.CREATED);
    context.redirect("/comunidades");
  }

  @Override
  public void edit(Context context) {
    System.out.println("edit");

    Comunidad comunidad = this.comunidadService.obtenerYValidarComunidad(Integer.parseInt(context.pathParam("id")));
    System.out.println("Nombre: " + comunidad.getNombre());

    List<Usuario> miembrosDisponibles = this.usuarioService.getAll();
    List<Usuario> miembrosSeleccionados = comunidad.getMiembros();
    List<Usuario> miembrosNoSeleccionados = miembrosDisponibles.stream()
        .filter(miembro -> !miembrosSeleccionados.contains(miembro))
        .toList();

    Map<String, Object> model = new HashMap<>();
    model.put("comunidad", comunidad);
    model.put("miembrosDisponibles", miembrosNoSeleccionados);

    context.render("comunidades/comunidad.hbs", model);
  }

  @Override
  public void update(Context context) {
    System.out.println("update");
    Comunidad comunidad = this.comunidadService.obtenerYValidarComunidad(Integer.parseInt(context.pathParam("id")));
    this.actualizarParametros(comunidad, context);
    this.comunidadService.update(comunidad);

    context.redirect("/comunidades");
  }

  @Override
  public void delete(Context context) {
    System.out.println("delete");
    Comunidad comunidad = this.comunidadService.obtenerYValidarComunidad(Integer.parseInt(context.pathParam("id")));

    List<Incidente> incidentes = this.incidenteService.getAll();
    incidentes.forEach(incidente -> {
      if (incidente.getComunidades().contains(comunidad)) {
        incidente.getComunidades().remove(comunidad);
        this.incidenteService.update(incidente);
      }
    });

    this.comunidadService.delete(comunidad);
    context.redirect("/comunidades");
  }

  private void asignarParametros(Comunidad comunidad, Context context) {
    System.out.println("asignarParametros");
    if (!Objects.equals(context.formParam("nombre"), "")) {
      comunidad.setNombre(context.formParam("nombre"));
    }

    if (!Objects.equals(context.formParam("descripcion"), "")) {
      comunidad.setDescripcion(context.formParam("descripcion"));
    }

    List<Integer> id = context.formParams("miembros").stream().map(Integer::parseInt).toList();
    List<Usuario> miembros = id.stream().map(this.usuarioService::buscarYValidarUsuario).toList();
    comunidad.setMiembros(miembros);

  }

  private void actualizarParametros(Comunidad comunidad, Context context) {
    System.out.println("actualizar");

    if (!Objects.equals(context.formParam("nombre"), comunidad.getNombre())) {
      comunidad.setNombre(context.formParam("nombre"));
    }

    if (!Objects.equals(context.formParam("descripcion"), comunidad.getDescripcion())) {
      comunidad.setDescripcion(context.formParam("descripcion"));
    }

    //si me llega un miembro actual, lo elimino de la comunidad
    List<Usuario> miembrosActualesAEliminar = context.formParams("miembrosActuales")
        .stream()
        .map(Integer::parseInt)
        .map(this.usuarioService::buscarYValidarUsuario).toList();

    System.out.println("miembrosActualesAEliminar: ");
    miembrosActualesAEliminar.forEach(miembro -> System.out.println(miembro.getUserName()));

    miembrosActualesAEliminar.forEach(miembro -> {
      if (comunidad.getMiembros().contains(miembro)) {
        comunidad.removeMiembro(miembro);
      }
    });

    //comparo los miembros y actualizo
    List<Usuario> miembrosActualizados = context.formParams("miembros")
        .stream()
        .map(Integer::parseInt)
        .map(this.usuarioService::buscarYValidarUsuario).toList();

    //por cada miembro, si no esta en la comunidad lo agrego
    miembrosActualizados.forEach(miembro -> {
      if (!comunidad.getMiembros().contains(miembro)) {
        comunidad.addMiembro(miembro);
      }
    });

  }

}