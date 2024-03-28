package server.Auth;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.security.AccessManager;
import io.javalin.security.RouteRole;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class Auth implements AccessManager {
//  public static void accessManager(Handler handler, Context ctx, Set<? extends RouteRole> permittedRoles) throws Exception {
//    if (permittedRoles.contains(Role.ANYONE)) {
//      handler.handle(ctx);
//      return;
//    }
////    if (ctx.userRoles().stream().anyMatch(permittedRoles::contains)) {
////      handler.handle(ctx);
////      return;
////    }
////    ctx.header(Header.WWW_AUTHENTICATE, "Basic");
////    throw new UnauthorizedResponse();
////  }



  @Override
  public void manage(@NotNull Handler handler, @NotNull Context ctx, @NotNull Set<? extends RouteRole> permittedRoles) throws Exception {

    System.out.println("manage");
    System.out.println("ctx.path() = " + ctx.path());

    return;
//    if (permittedRoles.contains(Role.ANYONE)) {
//      handler.handle(ctx);
//    }

  }
}
