package layers.controllers.AuthHandlers;

import excepciones.UsuarioInvalidoException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import java.util.List;
import layers.models.domain.DatosPersonalesUsuario;
import layers.models.domain.Usuario;
import layers.models.repositories.RoleRepository;
import layers.models.repositories.UsuarioRepository;
import server.config.AppUtils;
import utils.PasswordHasher;
import utils.enums.Rol;

public class RegisterHandler implements Handler {

  private UsuarioRepository usuarioRepository;

  private RoleRepository roleRepository;


  public RegisterHandler(UsuarioRepository usuarioRepository, RoleRepository roleRepository) {
    this.usuarioRepository = usuarioRepository;
    this.roleRepository = roleRepository;
  }

  @Override
  public void handle(Context ctx) throws Exception {

    String username = ctx.formParam("username");
    String password = ctx.formParam("password");
    String nombre = ctx.formParam("nombre");
    String apellido = ctx.formParam("apellido");
    String telefono = ctx.formParam("telefono");
    String correo = ctx.formParam("correo");

    System.out.println("Verify if user or username exists");
    List<Usuario> usuarios = usuarioRepository.searchByColumn(Usuario.class, "username", username);


    if (usuarios.size() > 0) {

      ctx.result("User " + usuarios.get(0).getUserName() + " already exists");

    } else {
      try {
        System.out.println("Creating user");
        DatosPersonalesUsuario datosPersonalesUsuario = new DatosPersonalesUsuario(nombre, apellido, correo, telefono);
        System.out.println("Datos personales creados");
        //Usuario usuario = new Usuario(username, PasswordHasher.hashPassword(password), datosPersonalesUsuario, null, Rol.USUARIO);
        Usuario usuario = new Usuario();
        System.out.println("Usuario creado y seteando parametros");
        usuario.setUserName(username);
        System.out.println("Username seteado");
        //usuario.setPassword(PasswordHasher.hashPassword(password));
        usuario.setPassword(password);
        System.out.println("Password seteado");
        usuario.setDatosPersonales(datosPersonalesUsuario);
        usuario.setRole(Rol.USUARIO);
        System.out.println("Parametros seteados");

        System.out.println("Guardando usuario");
        usuarioRepository.save(usuario);
        System.out.println("Usuario guardado");

        System.out.println("Setting user data");
        AppUtils.setUserData(ctx, usuario);
        System.out.println("User data set");

        ctx.status(HttpStatus.CREATED);
        ctx.redirect("/init");
      } catch (UsuarioInvalidoException e) {
        ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
        ctx.result(e.getMessage());
      } catch (Exception e) {
        ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
        ctx.result("Error creating user");
      }
    }
  }

  public void handleView(Context ctx) throws Exception {
    if (AppUtils.isUserLoggedIn(ctx)) {
      ctx.redirect("/init");
    } else {
      ctx.render("auth/register.hbs");
    }
  }
}
