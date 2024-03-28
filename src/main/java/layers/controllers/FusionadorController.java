package layers.controllers;

import externalservices.FusionadorDeComunidades;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import layers.models.domain.Comunidad;
import layers.models.domain.Entidad;
import utils.ICrudViewsHandler;

public class FusionadorController {
  private FusionadorDeComunidades fusionadorDeComunidades;

  public FusionadorController(FusionadorDeComunidades fusionadorDeComunidades) {
    this.fusionadorDeComunidades = fusionadorDeComunidades;
  }

  public void index(Context context) {
    List<Comunidad> comunidades = this.fusionadorDeComunidades
        .getComunidadRepository().getAll(Comunidad.class);
    Map<String, Object> model = new HashMap<>();

    model.put("comunidades", comunidades);

    context.render("fusionador_de_comu/fusionador.hbs", model);
  }

  public void create(Context context) throws Exception {
    System.out.println("crear");
    Map<String, Object> model = new HashMap<>();

    Comunidad comunidad1 = new Comunidad();
    Comunidad comunidad2 = new Comunidad();

    if (!Objects.equals(context.formParam("comunidad1"), "")) {
      comunidad1 = this.fusionadorDeComunidades.getComunidadRepository()
          .find(Comunidad.class, Integer.parseInt(context.formParam("comunidad1")));
    }

    if (!Objects.equals(context.formParam("comunidad2"), "")) {
      comunidad2 = this.fusionadorDeComunidades.getComunidadRepository()
          .find(Comunidad.class, Integer.parseInt(context.formParam("comunidad2")));
    }

    Comunidad comunidadFusionada = this.fusionadorDeComunidades
        .fusionDeComunidades(comunidad1, comunidad2);
    
    comunidadFusionada
        .setDescripcion("Fusion de " + comunidad1.getNombre() + " y " + comunidad2.getNombre());

    fusionadorDeComunidades.getComunidadRepository().save(comunidadFusionada);

    model.put("resultadoFusion", comunidadFusionada);

    context.render("fusionador_de_comu/fusionador.hbs", model);
  }


}
