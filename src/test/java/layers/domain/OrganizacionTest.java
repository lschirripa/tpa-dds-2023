package layers.domain;

import java.io.IOException;
import java.util.ArrayList;
import layers.models.domain.Organizacion;
import layers.models.domain.Servicio;
import layers.models.domain.Sucursal;
import layers.models.domain.UbicacionGeografica;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrganizacionTest {
  private Organizacion organizacion;
  private Sucursal sucursal1;
  private Sucursal sucursal2;
  private Servicio servicio;

  @BeforeEach
  void setUp() throws IOException {
    servicio = new Servicio("Banio", "de onvre");
    ArrayList<Servicio> servicios = new ArrayList<>();
    servicios.add(servicio);
    sucursal1 = new Sucursal("Sucursal1", servicios, new UbicacionGeografica(22, 23));
    sucursal2 = new Sucursal("Sucursal2", servicios, new UbicacionGeografica(22, 33));
    ArrayList<Sucursal> sucursales = new ArrayList<>();
    sucursales.add(sucursal1);
    sucursales.add(sucursal2);
    organizacion = new Organizacion("linea1", new UbicacionGeografica(22, 23), sucursales);
  }

  @Test
  @DisplayName("no puedo agregar una estacion que ya pertenece a la linea")
  void agregarEstacion() {
    Assertions.assertThrows(
        Exception.class,
        () -> {
          organizacion.agregarSucursal(sucursal1);
        });
    Assertions.assertEquals(2, organizacion.getSucursales().size());
  }

  @Test
  @DisplayName("no puedo borrar una estacion que no pertenece a la linea")
  void borrarEstacion() throws IOException {
    Sucursal sucursal3 = new Sucursal("Sucursal3", new ArrayList<>(), new UbicacionGeografica(22, 23));
    Assertions.assertThrows(
        Exception.class,
        () -> {
          organizacion.borrarSucursal(sucursal3);
        });
    Assertions.assertEquals(2, organizacion.getSucursales().size());
  }

  @Test
  @DisplayName("puedo agregar una estacion que no pertenece a la linea")
  void agregarEstacion2() throws IOException {
    Sucursal sucursal3 = new Sucursal("Sucursal3", new ArrayList<>(), new UbicacionGeografica(22, 23));
    organizacion.agregarSucursal(sucursal3);
    Assertions.assertEquals(3, organizacion.getSucursales().size());
  }

  @Test
  @DisplayName("puedo borrar una estacion que pertenece a la linea")
  void borrarEstacion2() throws IOException {
    Sucursal sucursal3 = new Sucursal("Sucursal3", new ArrayList<>(), new UbicacionGeografica(22, 23));
    organizacion.agregarSucursal(sucursal3);
    organizacion.borrarSucursal(sucursal3);
    Assertions.assertEquals(2, organizacion.getSucursales().size());
  }
}

