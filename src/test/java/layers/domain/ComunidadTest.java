package layers.domain;

import excepciones.AgregarAdministradorException;
import excepciones.AgregarMiembroException;
import excepciones.EliminarAdministradorException;
import excepciones.EliminarMiembroException;
import layers.models.domain.Comunidad;
import layers.models.domain.DatosPersonalesUsuario;
import layers.models.domain.UbicacionGeografica;
import layers.models.domain.Usuario;
import layers.models.repositories.ComunidadRepository;
import layers.models.repositories.ServicioAsociadoRepository;
import layers.models.repositories.ServicioRepository;
import layers.models.repositories.UsuarioRepository;
import layers.services.ComunidadService;
import layers.services.ServicioAsociadoService;
import layers.services.ServicioService;
import layers.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComunidadTest {
  private Comunidad comunidad;
  private ComunidadService comunidadService;


  @BeforeEach
  public void init() {
    UsuarioRepository usuarioRepository = new UsuarioRepository();
    ComunidadRepository comunidadRepository = new ComunidadRepository();
    ServicioService servicioService = new ServicioService(new ServicioRepository(), null);
    ServicioAsociadoService servicioAsociadoService = new ServicioAsociadoService(new ServicioAsociadoRepository(), servicioService);

    UsuarioService usuarioService = new UsuarioService(usuarioRepository, servicioAsociadoService);
    this.comunidadService = new ComunidadService(comunidadRepository, usuarioService);

    this.comunidad = new Comunidad("Comunidad");
    this.comunidad.setId(1);

    DatosPersonalesUsuario datosPersonalesUsuario1 = new DatosPersonalesUsuario("Oggi", "Junco", "oggi.junco@gmail.com", null);
    DatosPersonalesUsuario datosPersonalesUsuario2 = new DatosPersonalesUsuario("Nicolas", "Cage", "nico.cage@gmail.com", null);
    DatosPersonalesUsuario datosPersonalesUsuario3 = new DatosPersonalesUsuario("Ogro", "Fabbiani", "ogro.fabbiani@gmail.com", null);

    Usuario usuario1 = new Usuario("oggi_kpo", "qwerty123!@#", datosPersonalesUsuario1, new UbicacionGeografica(22.22, 33.33), null);
    usuario1.setId(1);
    Usuario usuario2 = new Usuario("nikCage", "yeahCage##", datosPersonalesUsuario2, new UbicacionGeografica(22.22, 33.33), null);
    usuario2.setId(2);
    Usuario usuario3 = new Usuario("ogrito", "carp9-12", datosPersonalesUsuario3, new UbicacionGeografica(22.22, 33.33), null);
    usuario3.setId(3);

    usuarioService.save(usuario1);
    usuarioService.save(usuario2);
    usuarioService.save(usuario3);

    this.comunidadService.save(comunidad);
  }

  @Test
  @DisplayName("Al guardar una comunidad, tengo 1 comunidad en mi repositorio")
  public void testGuardarComunidadd() {
    Assertions.assertEquals(1, comunidadService.getAll().size());
  }

  @Test
  @DisplayName("Al guardar una comunidad, si la busco en el repositorio, la encuentro")
  public void testGuardarComunidad() {
    Comunidad comunidad1 = comunidadService.getById(1);

    Assertions.assertEquals(comunidad, comunidad1);
  }

  @Test
  @DisplayName("Al agregar 1 miembro, la comunidad tiene un miembro más")
  public void testAgregarMiembro() {
    Comunidad comunidad1 = comunidadService.getById(1);

    Assertions.assertEquals(0, comunidad1.getMiembros().size());

    comunidadService.agregarMiembroAComunidad(1, 1);
    Assertions.assertEquals(1, comunidad1.getMiembros().size());

    comunidadService.agregarMiembroAComunidad(1, 2);
    Assertions.assertEquals(2, comunidad1.getMiembros().size());
  }

  @Test
  @DisplayName("No se puede agregar miembros que ya esten en la comunidad")
  public void testAgregarMiembroDuplicado() {
    comunidadService.agregarMiembroAComunidad(1, 1);

    Exception exception = assertThrows(AgregarMiembroException.class, () -> {
      comunidadService.agregarMiembroAComunidad(1, 1);
    });

    String expectedMessage = "Este miembro ya pertenece a la comunidad";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  @DisplayName("Al borrar 1 miembro, la comunidad tiene un miembro menos")
  public void testBorrarMiembro() {
    comunidadService.agregarMiembroAComunidad(1, 1);
    comunidadService.agregarMiembroAComunidad(1, 2);

    comunidadService.borrarMiembro(1, 2);
    Assertions.assertEquals(1, this.comunidad.getMiembros().size());
  }

  @Test
  @DisplayName("No se puede borrar miembros que no esten en la comunidad")
  public void testBorrarMiembroInexistente() {

    Exception exception = assertThrows(EliminarMiembroException.class, () -> {
      comunidadService.borrarMiembro(1, 2);
    });

    String expectedMessage = "No se puede borrar miembros que no esten en la comunidad";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  @DisplayName("Al agregar 1 admin, la comunidad tiene un admin asignado")
  public void testAgregarAdmin() {
    Assertions.assertEquals(0, this.comunidad.getAdministradoresDeComunidad().size());

    comunidadService.agregarAdministradorAComunidad(1, 1);
    Assertions.assertEquals(1, this.comunidad.getAdministradoresDeComunidad().size());
  }

  @Test
  @DisplayName("No se puede agregar un administrador que ya está asignado a esta comunidad")
  public void testAgregarAdminDuplicado() {
    comunidadService.agregarAdministradorAComunidad(1, 1);

    Exception exception = assertThrows(AgregarAdministradorException.class, () -> {
      comunidadService.agregarAdministradorAComunidad(1, 1);
    });

    String expectedMessage = "Este administrador ya se encuentra designado";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  @DisplayName("Al borrar admin, la comunidad no tiene admin")
  public void testBorrarAdmin() {
    comunidadService.agregarAdministradorAComunidad(1, 1);
    Assertions.assertEquals(1, this.comunidad.getAdministradoresDeComunidad().size());

    comunidadService.borrarAdmin(1, 1);
    Assertions.assertEquals(0, this.comunidad.getAdministradoresDeComunidad().size());
  }

  @Test
  @DisplayName("No se puede eliminar a un administrador que no está asignado a esta comunidad")
  public void testBorrarAdminInexistente() {
    //comunidadService.agregarAdministradorAComunidad(1, 1);

    Exception exception = assertThrows(EliminarAdministradorException.class, () -> {
      comunidadService.borrarAdmin(1, 2);
      ;
    });

    String expectedMessage = "No se puede eliminar un administrador que no pertenece a la comunidad";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}