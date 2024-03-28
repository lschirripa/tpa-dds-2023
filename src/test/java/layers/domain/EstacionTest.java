package layers.domain;

import java.io.IOException;
import java.util.ArrayList;
import layers.models.domain.Estacion;
import layers.models.domain.Servicio;
import layers.models.domain.UbicacionGeografica;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EstacionTest {
  private Estacion estacion;
  private Servicio servicio1;
  private Servicio servicio2;

  @BeforeEach
  void setUp() throws IOException {
    this.servicio1 = new Servicio("Estacion de Servicio", "Super");
    this.servicio2 = new Servicio("Estacion de Servicio", "Premium");
    ArrayList<Servicio> servicios = new ArrayList<>();
    servicios.add(servicio1);
    servicios.add(servicio2);
    this.estacion = new Estacion("YPF", servicios, new UbicacionGeografica(22, 23));
  }

  @Test
  @DisplayName("no puedo quitar un servicio de una entidad si esta no lo tiene")
  void eliminarServicioTest() {
    Servicio servicio3 = new Servicio("Estacion de Servicio", "Comun");

    Assertions.assertEquals(2, this.estacion.getServicios().size());
    Assertions.assertThrows(
        Exception.class,
        () -> this.estacion.borrarPrestacionDeServicio(servicio3)
    );
    Assertions.assertEquals(2, this.estacion.getServicios().size());
  }

  @Test
  @DisplayName("puedo quitar un servicio de una entidad si esta lo tiene")
  void eliminarServicioTest2() {
    Assertions.assertEquals(2, this.estacion.getServicios().size());
    Assertions.assertDoesNotThrow(
        () -> this.estacion.borrarPrestacionDeServicio(this.servicio1)
    );
    Assertions.assertEquals(1, this.estacion.getServicios().size());
  }

  @Test
  @DisplayName("no puedo agregar un servicio a una entidad si esta ya lo tiene")
  void agregarServicioTest() {
    Assertions.assertEquals(2, this.estacion.getServicios().size());
    Assertions.assertThrows(
        Exception.class,
        () -> this.estacion.agregarPrestacionDeServicio(this.servicio1)
    );
    Assertions.assertEquals(2, this.estacion.getServicios().size());
  }

  @Test
  @DisplayName("puedo agregar un servicio a una entidad si esta no lo tiene")
  void agregarServicioTest2() {
    Servicio servicio3 = new Servicio("Estacion de Servicio", "Comun");
    Assertions.assertEquals(2, this.estacion.getServicios().size());
    Assertions.assertDoesNotThrow(
        () -> this.estacion.agregarPrestacionDeServicio(servicio3)
    );
    Assertions.assertEquals(3, this.estacion.getServicios().size());
  }

}
