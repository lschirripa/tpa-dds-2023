package layers.domain;

import excepciones.ComunidadInexistenteException;
import layers.models.domain.Comunidad;
import layers.models.domain.DatosPersonalesUsuario;
import layers.models.domain.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UsuarioTest {
  private Usuario usuario;
  private DatosPersonalesUsuario datosPersonalesUsuario;
  private Comunidad comunidad1;
  private Comunidad comunidad2;


  @BeforeEach
  void setUp() {
    datosPersonalesUsuario = new DatosPersonalesUsuario("nombre", "apellido", "email", null);
    usuario = new Usuario("username", "crack2024jeje", datosPersonalesUsuario, null, null);
    comunidad1 = new Comunidad("comunidad1");
    comunidad2 = new Comunidad("comunidad2");
  }

  @Test
  @DisplayName("no puedo agregar a una comunidad a la que ya pertenezco")
  void agregarComunidadRepetidaTest() {
    usuario.addComunidad(comunidad1);
    Assertions.assertThrows(ComunidadInexistenteException.class, () -> usuario.addComunidad(comunidad1));
    Assertions.assertEquals(1, usuario.getComunidades().size());
  }

  @Test
  @DisplayName("puedo agregar una comunidad a la que no pertenezco")
  void agregarComunidadTest() {
    usuario.addComunidad(comunidad1);
    Assertions.assertEquals(1, usuario.getComunidades().size());
  }

  @Test
  @DisplayName("puedo remover una comunidad a la que pertenezco")
  void removerComunidadTest() {
    usuario.addComunidad(comunidad1);
    usuario.removeComunidad(comunidad1);
    Assertions.assertEquals(0, usuario.getComunidades().size());
  }

  @Test
  @DisplayName("no puedo remover una comunidad a la que no pertenezco")
  void removerComunidadInexistenteTest() {
    usuario.addComunidad(comunidad1);
    Assertions.assertThrows(ComunidadInexistenteException.class, () -> usuario.removeComunidad(comunidad2));
    Assertions.assertEquals(1, usuario.getComunidades().size());
  }
}
