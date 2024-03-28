package layers.controllers;

import io.javalin.http.Context;
import java.util.List;
import layers.models.domain.Comunidad;
import layers.models.domain.UbicacionGeografica;
import layers.models.domain.Usuario;
import layers.models.repositories.UbicacionGeograficaRepository;
import server.config.AppUtils;
import utils.SugerenciaDeRevision;

public class SugerenciaDeRevisionController {
  private SugerenciaDeRevision sugerenciaDeRevision;
  private UbicacionGeograficaRepository ubicacionGeograficaRepository = new UbicacionGeograficaRepository();
  public SugerenciaDeRevisionController(SugerenciaDeRevision sugerenciaDeRevision) {
    this.sugerenciaDeRevision = sugerenciaDeRevision;
  }

  public void index(io.javalin.http.Context context) throws Exception {
    System.out.println("index");

    java.util.Map<String, Object> model = new java.util.HashMap<>();
    // Renderiza la maqueta HTML con Handlebars
    context.render("sugerencia_chequeo/chequeo_incidente.hbs", model);
  }


  public void create(Context context) throws Exception {
    System.out.println("crear");
    java.util.Map<String, Object> model = new java.util.HashMap<>();

    //Usuario usuario = sugerenciaDeRevision.getUsuarioService().buscarYValidarUsuario(1);
    Usuario usuarioSesion = AppUtils.getUserData(context);
    Usuario usuario = sugerenciaDeRevision.getUsuarioService().buscarYValidarUsuario(usuarioSesion.getId());
    if (usuario.getUbicacionActual() == null) {
      usuario.setUbicacionActual(ubicacionGeograficaRepository.getById(1, UbicacionGeografica.class));
    }
    model.put("sugerencia", sugerenciaDeRevision.sugerirRevisionDe(usuario));

    context.render("sugerencia_chequeo/chequeo_incidente.hbs", model);
  }
}
