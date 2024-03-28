package layers.controllers.AuthHandlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import utils.enums.Rol;

public class AdminMiddleware implements Handler {

  public AdminMiddleware() {
  }

  @Override
  public void handle(Context context) throws Exception {
    if (context.sessionAttribute("userId") == null) {
      context.redirect("/init");
    } else {
      System.out.println("Rol: " + context.sessionAttribute("userRole"));
      if (Objects.equals(context.sessionAttribute("userRole"), Rol.ADMINISTRADOR.getNombre())) {
        context.render("admin/admin.hbs");
      } else {
        context.status(403); // Código de acceso denegado
        context.result("Acceso denegado: Debes ser administrador");
      }
    }
  }
}
