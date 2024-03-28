package layers.controllers;

import externalservices.FusionadorDeComunidades;
import io.javalin.http.Context;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import layers.models.domain.*;
import layers.models.repositories.*;

public class RecomendadorController {
  private FusionadorDeComunidades fusionadorDeComunidades;

  public RecomendadorController(FusionadorDeComunidades fusionadorDeComunidades) {
    this.fusionadorDeComunidades = fusionadorDeComunidades;
  }

  public void index(io.javalin.http.Context context) throws Exception {
    System.out.println("index");

    java.util.Map<String, Object> model = new java.util.HashMap<>();
    // Renderiza la maqueta HTML con Handlebars
    context.render("fusionador_de_comu/recomendador.hbs", model);
  }


  public void create(Context context) throws Exception {
    System.out.println("crear");
    java.util.Map<String, Object> model = new java.util.HashMap<>();

    model.put("recomendaciones", fusionadorDeComunidades.recomendacionDeFusion());

    context.render("fusionador_de_comu/recomendador.hbs", model);
  }
}
