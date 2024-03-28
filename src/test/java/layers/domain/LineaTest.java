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

public class LineaTest {
  private Linea linea;
  private Estacion estacion1;
  private Estacion estacion2;
  private Servicio servicio;

  @BeforeEach
  void setUp() throws IOException {
    servicio = new Servicio("Banio", "de onvre");
    ArrayList<Servicio> servicios = new ArrayList<>();
    servicios.add(servicio);
    estacion1 = new Estacion("Estacion1", servicios, new UbicacionGeografica(22, 23));
    estacion2 = new Estacion("Estacion2", servicios, new UbicacionGeografica(22, 33));
    ArrayList<Estacion> estaciones = new ArrayList<>();
    estaciones.add(estacion1);
    estaciones.add(estacion2);
    linea = new Linea("linea1", estaciones, new UbicacionGeografica(22, 23), estacion1, estacion2);
  }

  @Test
  @DisplayName("no puedo agregar una estacion que ya pertenece a la linea")
  void agregarEstacion() {
    Assertions.assertThrows(
        Exception.class,
        () -> {
          linea.agregarEstacion(estacion1);
        });
    Assertions.assertEquals(2, linea.getEstaciones().size());
  }

  @Test
  @DisplayName("no puedo borrar una estacion que no pertenece a la linea")
  void borrarEstacion() throws IOException {
    Estacion estacion3 = new Estacion("Estacion3", new ArrayList<>(), new UbicacionGeografica(22, 23));
    Assertions.assertThrows(
        Exception.class,
        () -> {
          linea.borrarEstacion(estacion3);
        });
    Assertions.assertEquals(2, linea.getEstaciones().size());
  }

  @Test
  @DisplayName("puedo agregar una estacion que no pertenece a la linea")
  void agregarEstacion2() throws IOException {
    Estacion estacion3 = new Estacion("Estacion3", new ArrayList<>(), new UbicacionGeografica(22, 23));
    linea.agregarEstacion(estacion3);
    Assertions.assertEquals(3, linea.getEstaciones().size());
  }

  @Test
  @DisplayName("puedo borrar una estacion que pertenece a la linea")
  void borrarEstacion2() throws IOException {
    Estacion estacion3 = new Estacion("Estacion3", new ArrayList<>(), new UbicacionGeografica(22, 23));
    linea.agregarEstacion(estacion3);
    linea.borrarEstacion(estacion3);
    Assertions.assertEquals(2, linea.getEstaciones().size());
  }

  public static class OrganismoDeControlTest {
    private Organizacion organizacion;
    private OrganismoDeControl organismoDeControl;
    private GeorefApiService georefApiService;
    private ObtencionDeLocalizacion obtencionDeLocalizacion;
    private UbicacionGeografica ubicacionGeografica;
    private UbicacionGeografica ubicacionGeografica2;
    private Servicio servicio;
    private Servicio servicio2;
    private ArrayList<Servicio> servicios = new ArrayList<>();
    private Sucursal sucursal1;
    private Sucursal sucursal2;
    private ArrayList<Sucursal> sucursales = new ArrayList<>();

    @BeforeEach
    public void init() throws IOException {
      organismoDeControl = new OrganismoDeControl(new Organismo("Santander", "Banco central"));

      this.georefApiService = Mockito.mock();
      this.obtencionDeLocalizacion = Mockito.mock();

      this.ubicacionGeografica = new UbicacionGeografica(-27.2741, -66.7529);
      this.ubicacionGeografica2 = new UbicacionGeografica(-28.2, -67.5);

      this.servicio = new Servicio("Banio", "hombres");
      this.servicios.add(servicio);

      this.sucursal1 = new Sucursal("Santander Caballito", servicios, ubicacionGeografica);
      this.sucursal2 = new Sucursal("Santander Devoto", servicios, ubicacionGeografica2);
      this.sucursales.add(sucursal1);
      this.sucursales.add(sucursal2);

      organizacion = new Organizacion("Santander Rio", ubicacionGeografica, sucursales);
    }

    @Test
    @DisplayName("Se agrega una entidad al organismo de control y se tiene 1 entidad en total")
    public void agregarEntidadAOrgTest() {
      organismoDeControl.addEntidad(organizacion);
      Assertions.assertEquals(1, organismoDeControl.getEntidades().size());
    }

    @Test
    @DisplayName("No se puede agregar una entidad que ya ha sido agregada previamente")
    public void noPermitirAgregarEntidadYaAgregadaTest() {
      organismoDeControl.addEntidad(organizacion);

      Exception exception = assertThrows(AgregarEntidadException.class, () -> {
        organismoDeControl.addEntidad(organizacion);
      });

      String expectedMessage = "No se puede agregar una entidad contenida";
      String actualMessage = exception.getMessage();

      Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("No se puede quitar una entidad de la lista si esta no esta se encuentra")
    public void noQuitarEntidadSiNoSeEncuentraTest() {
      Exception exception = assertThrows(EliminarEntidadException.class, () -> {
        organismoDeControl.borrarEntidad(organizacion);
      });

      String expectedMessage = "No se puede quitar una entidad que no esta incluida";
      String actualMessage = exception.getMessage();

      Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

  }
}
