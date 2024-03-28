package server.Auth;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class AuthorizationHandler implements Handler {

  @Override
  public void handle(Context context) throws Exception {
    if (context.path().equals("/reguladoras/carga-masiva")) {

      if (!context.sessionAttribute("userRole").equals(Role.ADMIN)) {

        context.redirect("/init"); //TODO Redirect to a 403 unauthorized page
      }
    }
  }
}
