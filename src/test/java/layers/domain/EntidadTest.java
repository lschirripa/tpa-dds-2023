package layers.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import externalservices.apigeoref.ObtencionDeLocalizacion;
import externalservices.apigeoref.georef.GeorefApiService;
import excepciones.*;
import java.io.IOException;
import java.util.ArrayList;
import layers.models.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EntidadTest {
  private Linea linea;
  private Organizacion organizacion;
  private UbicacionGeografica ubicacionGeografica;
  private UbicacionGeografica ubicacionGeografica2;
  private ArrayList<Sucursal> sucursales = new ArrayList<>();
  private ArrayList<Estacion> estaciones = new ArrayList<>();
  private Sucursal sucursal1;
  private Sucursal sucursal2;
  private Estacion estacion1;
  private Estacion estacion2;
  private Servicio servicio;
  private ArrayList<Servicio> servicios = new ArrayList<>();
  private GeorefApiService georefApiService;
  private ObtencionDeLocalizacion obtencionDeLocalizacion;

  @BeforeEach
  public void init() throws IOException {

    this.georefApiService = Mockito.mock();
    this.obtencionDeLocalizacion = Mockito.mock();

    this.ubicacionGeografica = new UbicacionGeografica(-27.2741, -66.7529);
    this.ubicacionGeografica2 = new UbicacionGeografica(-28.2, -67.5);

    this.servicio = new Servicio("Banio", "hombres");
    this.servicios.add(servicio);

    this.estacion1 = new Estacion("Diaz Velez", servicios, ubicacionGeografica);
    this.estacion2 = new Estacion("San Martin", servicios, ubicacionGeografica2);
    this.estaciones.add(estacion1);
    this.estaciones.add(estacion2);

    this.sucursal1 = new Sucursal("Santander Caballito", servicios, ubicacionGeografica);
    this.sucursal2 = new Sucursal("Santander Devoto", servicios, ubicacionGeografica2);
    this.sucursales.add(sucursal1);
    this.sucursales.add(sucursal2);
  }

  @Test
  @DisplayName("Al agregar 1 establecimiento a la entidad, tengo un establecimiento mas")
  public void testAgregarEstacion() throws IOException {
    //Provincia provincia = new Provincia("Buenos Aires");

    //Mockito.when(obtencionDeLocalizacion.getLocalizacionByUbicacionFromApi(ubicacionGeografica, NivelDeLocalizacion.PROVINCIA))
    //    .thenReturn(provincia);

    //test para linea
    linea = new Linea("105", estaciones, ubicacionGeografica, estacion1, estacion2);
    Estacion estacion3 = new Estacion("Centro", servicios, ubicacionGeografica);
    this.linea.agregarEstacion(estacion3);

    //test para organizacion
    organizacion = new Organizacion("Stantander Rio", ubicacionGeografica, sucursales);
    Sucursal sucursal3 = new Sucursal("Santander Palermo", servicios, ubicacionGeografica);
    this.organizacion.agregarSucursal(sucursal3);


    Assertions.assertEquals(3, linea.getEstaciones().size());
    Assertions.assertEquals(3, organizacion.getSucursales().size());

  }

  @Test
  @DisplayName("No se puede agregar un Establecimiento que ya esta en una Entidad")
  public void testAgregarEstablecimiento() throws IOException {
    //Provincia provincia = new Provincia("Buenos Aires");

    //Mockito.when(obtencionDeLocalizacion.getLocalizacionByUbicacionFromApi(ubicacionGeografica, NivelDeLocalizacion.PROVINCIA))
    //    .thenReturn(provincia);

    //test para Linea
    linea = new Linea("105", estaciones, ubicacionGeografica, estacion1, estacion2);

    Exception exception = assertThrows(AgregarEstacionException.class, () -> {
      linea.agregarEstacion(estacion1);
    });

    String expectedMessage = "Esta estacion ya se pertenece a la linea";
    String actualMessage = exception.getMessage();

    //test para Organizacion
    organizacion = new Organizacion("Santander Rio", ubicacionGeografica, sucursales);

    Exception exception2 = assertThrows(AgregarSucursalException.class, () -> {
      organizacion.agregarSucursal(sucursal1);
    });

    String expectedMessage2 = "Esta sucursal ya pertenece a la organizacion";
    String actualMessage2 = exception2.getMessage();


    assertTrue(actualMessage.contains(expectedMessage));
    assertTrue(actualMessage2.contains(expectedMessage2));
  }

  @Test
  @DisplayName("No se puede quitar un Establecimiento que no esta en una Entidad")
  public void testQuitarEstablecimiento() throws IOException {
    //Provincia provincia = new Provincia("Buenos Aires");

    //Mockito.when(obtencionDeLocalizacion.getLocalizacionByUbicacionFromApi(ubicacionGeografica, NivelDeLocalizacion.PROVINCIA))
    //    .thenReturn(provincia);

    //test para Linea
    linea = new Linea("105", estaciones, ubicacionGeografica, estacion1, estacion2);
    Estacion estacion3 = new Estacion("Centro", servicios, ubicacionGeografica);

    Exception exception = assertThrows(EliminarEstacionException.class, () -> {
      linea.borrarEstacion(estacion3);
    });

    String expectedMessage = "Esta estacion no pertenece a la linea";
    String actualMessage = exception.getMessage();

    //test para Organizacion
    organizacion = new Organizacion("Santander Rio", ubicacionGeografica, sucursales);
    Sucursal sucursal3 = new Sucursal("Santander Palermo", servicios, ubicacionGeografica);

    Exception exception2 = assertThrows(EliminarSucursalException.class, () -> {
      organizacion.borrarSucursal(sucursal3);
    });

    String expectedMessage2 = "No se puede borrar sucursales que no esten en la org.";
    String actualMessage2 = exception2.getMessage();


    assertTrue(actualMessage.contains(expectedMessage));
    assertTrue(actualMessage2.contains(expectedMessage2));
  }
}
