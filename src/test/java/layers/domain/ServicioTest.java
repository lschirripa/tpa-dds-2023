package layers.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import excepciones.AgregarEntidadException;
import excepciones.EliminarEntidadException;
import externalservices.apigeoref.ObtencionDeLocalizacion;
import externalservices.apigeoref.georef.GeorefApiService;
import java.io.IOException;
import java.util.ArrayList;
import layers.models.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ServicioTest {

  private Servicio servicio;
  private Usuario usuario;
  private Usuario usuario2;

  @BeforeEach
  public void init() {
    this.servicio = new Servicio("Servicio1", "Descripcion");
    this.usuario = new Usuario("Usuario1", "carpool1515", null, null, null);
    this.usuario2 = new Usuario("Usuario2", "carpool1515", null, null, null);

  }
/*
  @Test
  @DisplayName("Si un usuario es afectado para un servicio, lo ingreso a la lista de usuarios afectados")
  public void testAgregarUsuarioAfectado() {
    this.servicio.addUsuarioAfectado(this.usuario);

    Assertions.assertEquals(1, this.servicio.getUsuariosAfectados().size());
    Assertions.assertTrue(this.servicio.getUsuariosAfectados().contains(this.usuario));
    Assertions.assertEquals(0, this.servicio.getUsuariosObservadores().size());
  }

  @Test
  @DisplayName("Si un usuario es observador para un servicio, lo ingreso a la lista de usuarios observadores")
  public void testAgregarUsuarioObservador() {
    this.servicio.addUsuarioObservador(this.usuario);

    Assertions.assertEquals(1, this.servicio.getUsuariosObservadores().size());
    Assertions.assertTrue(this.servicio.getUsuariosObservadores().contains(this.usuario));
    Assertions.assertEquals(0, this.servicio.getUsuariosAfectados().size());
  }

  @Test
  @DisplayName("no puedo agregar a un usuario que ya se encuentra en la lista de usuarios afectados u observadores")
  public void agregarUsuariosRepetidos() {
    this.servicio.addUsuarioAfectado(this.usuario);
    Assertions.assertThrows(Exception.class, () -> {
      this.servicio.addUsuarioAfectado(this.usuario);
    });
    Assertions.assertEquals(1, this.servicio.getUsuariosAfectados().size());
    Assertions.assertTrue(this.servicio.getUsuariosAfectados().contains(this.usuario));

    this.servicio.addUsuarioObservador(this.usuario2);
    Assertions.assertThrows(Exception.class, () -> {
      this.servicio.addUsuarioObservador(this.usuario2);
    });

    Assertions.assertEquals(1, this.servicio.getUsuariosObservadores().size());
    Assertions.assertTrue(this.servicio.getUsuariosObservadores().contains(this.usuario2));
  }

  @Test
  @DisplayName("Si un usuario es afectado para un servicio, no puede ser observador y viceversa")
  public void testAgregarUsuarioAfectadoYObservador() {
    this.servicio.addUsuarioAfectado(this.usuario);
    Assertions.assertThrows(Exception.class, () -> {
      this.servicio.addUsuarioObservador(this.usuario);
    });
    Assertions.assertEquals(1, this.servicio.getUsuariosAfectados().size());
    Assertions.assertTrue(this.servicio.getUsuariosAfectados().contains(this.usuario));
    Assertions.assertEquals(0, this.servicio.getUsuariosObservadores().size());

    this.servicio.addUsuarioObservador(this.usuario2);
    Assertions.assertThrows(Exception.class, () -> {
      this.servicio.addUsuarioAfectado(this.usuario2);
    });
    Assertions.assertEquals(1, this.servicio.getUsuariosObservadores().size());
    Assertions.assertTrue(this.servicio.getUsuariosObservadores().contains(this.usuario2));
  }


 */

  public static class PrestadoraDeServicioTest {
    private Linea linea;
    private PrestadoraDeServicio prestadoraDeServicio;
    private GeorefApiService georefApiService;
    private ObtencionDeLocalizacion obtencionDeLocalizacion;
    private UbicacionGeografica ubicacionGeografica;
    private UbicacionGeografica ubicacionGeografica2;
    private Servicio servicio;
    private ArrayList<Servicio> servicios = new ArrayList<>();
    private Estacion estacion;
    private ArrayList<Estacion> estaciones = new ArrayList<>();

    @BeforeEach
    public void init() throws IOException {
      prestadoraDeServicio = new PrestadoraDeServicio(new Prestadora("CNRT", "Transportes"));

      this.georefApiService = Mockito.mock();
      this.obtencionDeLocalizacion = Mockito.mock();

      this.ubicacionGeografica = new UbicacionGeografica(-27.2741, -66.7529);
      this.ubicacionGeografica2 = new UbicacionGeografica(-28.2, -67.5);

      this.servicio = new Servicio("Banio", "hombres");
      this.servicios.add(servicio);

      this.estacion = new Estacion("Primera Junta", servicios, ubicacionGeografica);
      this.estaciones.add(estacion);

      linea = new Linea("Subte A", estaciones, ubicacionGeografica, estacion, estacion);
    }

    @Test
    @DisplayName("Se agrega una entidad al organismo de control y se tiene 1 entidad en total")
    public void agregarEntidadAOrgTest() {
      prestadoraDeServicio.addEntidad(linea);
      Assertions.assertEquals(1, prestadoraDeServicio.getEntidades().size());
    }

    @Test
    @DisplayName("No se puede agregar una entidad que ya ha sido agregada previamente")
    public void noPermitirAgregarEntidadYaAgregadaTest() {
      prestadoraDeServicio.addEntidad(linea);

      Exception exception = assertThrows(AgregarEntidadException.class, () -> {
        prestadoraDeServicio.addEntidad(linea);
      });

      String expectedMessage = "No se puede agregar una entidad contenida";
      String actualMessage = exception.getMessage();

      Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("No se puede quitar una entidad de la lista si esta no esta se encuentra")
    public void noQuitarEntidadSiNoSeEncuentraTest() {
      Exception exception = assertThrows(EliminarEntidadException.class, () -> {
        prestadoraDeServicio.borrarEntidad(linea);
      });

      String expectedMessage = "No se puede quitar una entidad que no esta incluida";
      String actualMessage = exception.getMessage();

      Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
  }
}
