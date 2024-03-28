package layers.controllers;

import io.javalin.http.Context;

public class InitController {
  public InitController() {
  }

  public void init(Context context) {
    context.render("init.hbs");
  }
}
