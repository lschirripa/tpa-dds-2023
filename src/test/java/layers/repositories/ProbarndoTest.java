package layers.repositories;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import layers.models.domain.Usuario;
import layers.models.repositories.UsuarioRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProbarndoTest implements SimplePersistenceTest {

  private UsuarioRepository repositorioUsuarios;

  @Before
  public void init() {
    repositorioUsuarios = new UsuarioRepository();
    repositorioUsuarios.save(new Usuario("Carlos", "contradificil20", null, null, null));
    repositorioUsuarios.save(new Usuario("Juan", "contradificil209", null, null, null));
    repositorioUsuarios.save(new Usuario("Santi", "contradificil201", null, null, null));
  }

  public Usuario iniciarADani() {
    return new Usuario("Dani", "contradificil2010", null, null, null);
  }

  @Test
  public void contextUp() {
    assertNotNull(entityManager());
  }

  @Test
  public void contextUpWithTransaction() throws Exception {
    withTransaction(() -> {
    });
  }

  @Test
  public void testDeCargaInicial() {

    Usuario dani = iniciarADani();

    entityManager().persist(dani);
    assertEquals("Dani", dani.getUserName());
  }

  @Test
  public void deberiaEncontrarAlJugadorPorNombreCantidad() {
    Assert.assertEquals("Juan", repositorioUsuarios.getById(2, Usuario.class).getUserName());
  }

  @Test
  public void noDeberiaEncontrarUnJugadorSiNoExiste() {
    Assert.assertNull(repositorioUsuarios.getById(20, Usuario.class));
  }

  @Test
  public void meBorraUnUsuario() {
    Usuario usuario = repositorioUsuarios.getById(1, Usuario.class);
    repositorioUsuarios.delete(usuario);
    Assert.assertNull(repositorioUsuarios.getById(1, Usuario.class));
  }

  @Test
  public void modificarElUserDeUnUsuario() {
    Usuario usuario = repositorioUsuarios.getById(2, Usuario.class);
    usuario.setUserName("Carlos");
    repositorioUsuarios.update(usuario);
    Assert.assertEquals("Carlos", repositorioUsuarios.getById(2, Usuario.class).getUserName());
  }

}

