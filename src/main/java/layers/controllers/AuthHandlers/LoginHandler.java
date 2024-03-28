package layers.controllers.AuthHandlers;

import static server.fakeDB.users;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.HashMap;
import java.util.Map;
import layers.models.domain.Usuario;
import layers.models.repositories.UsuarioRepository;
import server.config.AppUtils;
import utils.PasswordHasher;

public class LoginHandler implements Handler {
  private UsuarioRepository usuarioRepository;

  public LoginHandler(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  public void handle(Context ctx) throws Exception {

    String username = ctx.formParam("username");
    String password = ctx.formParam("password");


    Usuario userBuscado = usuarioRepository.searchByColumn(Usuario.class, "username", username).get(0);
    String usernameBuscado = userBuscado.getUserName();
    String passwordBuscado = userBuscado.getPassword();


    if (username.equals(usernameBuscado) && PasswordHasher.checkPassword(password, passwordBuscado)) {

      AppUtils.setUserData(ctx, userBuscado);

      //boolean esAdministrador = "ADMINISTRADOR".equals(userBuscado.getRole().toString());
      //System.out.println("esAdministrador: " + esAdministrador);

      //Map<String, Object> model = new HashMap<>();
      //model.put("esAdministrador", esAdministrador); // Pasar el booleano a la plantilla
      //ctx.render("init.hbs", model);

      System.out.println("Login successful");
      System.out.println("Username logged: " + username);
      ctx.redirect("/init");


    } else {
      // Authentication failed, show error message
      ctx.result("Invalid credentials. Please try again.");
    }

  }

  public void handleView(Context ctx) throws Exception {
    if (AppUtils.isUserLoggedIn(ctx)) {
      ctx.redirect("/init");
    } else {
      ctx.render("auth/login.hbs");
    }
  }
}
