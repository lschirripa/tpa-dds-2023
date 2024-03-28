package password;

import java.util.ArrayList;
import layers.models.domain.DatosPersonalesUsuario;
import layers.models.domain.Usuario;
import excepciones.UsuarioInvalidoException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import layers.models.domain.UbicacionGeografica;

public class ValidatePassTest {
  @Test
  @DisplayName("Si la contrasenia es mayor a 8 caracts y no esta en la lista, el registro es exitoso")
  public void testRegistroValido() {
    Assertions.assertEquals(1, cantidadUsuariosRegistrados());
  }

  private int cantidadUsuariosRegistrados() {
    Usuario usuario1 = new Usuario("jucobas", "contracorrecta", new DatosPersonalesUsuario("Juan", "Cobas", "jco@frba.com", null), new UbicacionGeografica(22.22, 33.33), null);
    ArrayList<Usuario> usuariosRegistrados = new ArrayList<>();
    usuariosRegistrados.add(usuario1);
    return usuariosRegistrados.size();
  }

  @Test
  @DisplayName("No se permiten claves que se encuentren en la lista negra")
  public void testLongitudClaveInvalidaListaNegra() {
    UsuarioInvalidoException usuarioInvalidoException = Assertions.assertThrows(UsuarioInvalidoException.class, this::usuarioListaNegra);
    Assertions.assertEquals("Eligio una de las contraseñas mas usadas. Por favor ingrese otra", usuarioInvalidoException.getMessage());
  }

  private void usuarioListaNegra() {
    Usuario usuario1 = new Usuario("jucobas", "1234", new DatosPersonalesUsuario("Juan", "Cobas", "jco@frba.com", null), new UbicacionGeografica(22.22, 33.33), null);
  }

  @Test
  @DisplayName("No se permiten claves menores a 8 caracteres")
  public void testLongitudClaveInvalidaLongitud() {
    UsuarioInvalidoException usuarioInvalidoException = Assertions.assertThrows(UsuarioInvalidoException.class, this::usuarioLongitudInvalida);
    Assertions.assertEquals("La contraseña debe tener una longitudad minima de ocho caracteres", usuarioInvalidoException.getMessage());
  }

  private void usuarioLongitudInvalida() {
    Usuario usuario1 = new Usuario("jucobas", "zas88#", new DatosPersonalesUsuario("Juan", "Cobas", "jco@frba.com", null), new UbicacionGeografica(22.22, 33.33), null);
  }
}
