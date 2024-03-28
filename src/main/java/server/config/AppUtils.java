package server.config;

import io.javalin.http.Context;
import java.util.Objects;
import layers.models.domain.Usuario;
import utils.enums.Rol;

public class AppUtils {
  public static String getUserRole(Context ctx) {
    return ctx.sessionAttribute("userRole");
  }

  public static Usuario getUserData(Context ctx) {
    Usuario usuario = new Usuario();
    usuario.setId(ctx.sessionAttribute("userId"));
    usuario.setUserName(ctx.sessionAttribute("userName"));
    //usuario.setDatosPersonales(ctx.sessionAttribute("datosPersonales"));
    return usuario;
  }

  public static void setUserData(Context ctx, Usuario usuario) {
    ctx.sessionAttribute("userId", usuario.getId());
    ctx.sessionAttribute("userRole", usuario.getRole().getNombre());
    ctx.sessionAttribute("userName", usuario.getUserName());
    ctx.sessionAttribute("correo", usuario.getDatosPersonales().getCorreo());
    ctx.sessionAttribute("nombre", usuario.getDatosPersonales().getNombre());
    ctx.sessionAttribute("apellido", usuario.getDatosPersonales().getApellido());
  }

  public static boolean isUserInRole(Context ctx, Rol role) {
    return Objects.equals(ctx.sessionAttribute("userRole"), role.getNombre());
  }

  public static boolean isUserLoggedIn(Context ctx) {
    return ctx.sessionAttribute("userId") != null;
  }


}
