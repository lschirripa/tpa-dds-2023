package layers.controllers.AuthHandlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class AuthenticationHandler implements Handler {
  @Override
  public void handle(Context ctx) throws Exception {
    // Check if user is authenticated
    if (ctx.sessionAttribute("userId") == null && !ctx.path().equals("/") && !ctx.path().equals("/login") && !ctx.path().equals("/css/login.css") && !ctx.path().equals("/register") && !ctx.path().equals("/css/register.css") && !ctx.path().equals("/css/homepage.css") && !ctx.path().equals("/js/homepage.js") && !ctx.path().equals("/resources/image-1.jpg") && !ctx.path().equals("/resources/image-2.jpg") && !ctx.path().equals("/resources/image-3.jpg") && !ctx.path().equals("/resources/image-4.jpg") && !ctx.path().equals("/resources/image-5.jpg") && !ctx.path().equals("/resources/image-6.jpg") && !ctx.path().equals("/resources/top_10000_worst_passwords.txt")) {
      System.out.println("Redirecting to login page");
      ctx.redirect("/login");
    }
  }
}
