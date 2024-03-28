package layers.domain;

import excepciones.IncidenteInexistenteException;
import java.time.LocalDateTime;
import layers.models.domain.Incidente;
import layers.models.domain.Servicio;
import layers.models.domain.ServicioAsociado;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IncidenteTest {
  private Incidente incidente;
  private ServicioAsociado servicioIncidentado;
  private Servicio servicio;

  @BeforeEach
  public void init() {
    this.servicio = new Servicio("Servicio1", "Descripcion");
    this.servicioIncidentado = new ServicioAsociado(null, null, servicio);
    this.incidente = new Incidente(servicioIncidentado, "descripcion", LocalDateTime.now().minusHours(1));
  }

  @Test
  @DisplayName("Si un incidente no fue resuelto, no puedo obtener el tiempo de cierre")
  public void testTiempoDeCierre() {
    Assertions.assertThrows(IncidenteInexistenteException.class, () -> {
      this.incidente.tiempoDeCierre();
    });
  }

  @Test
  @DisplayName("La diferencia de tiempo entre la fecha de creacion y la fecha de resolucion es correcta")
  public void testTiempoDeCierreCorrecto() {
    this.incidente.setFechaResolucion(LocalDateTime.now());
    Assertions.assertEquals(1, this.incidente.tiempoDeCierre().toHours());

    Incidente incidente1 = new Incidente(servicioIncidentado, "descripcion", LocalDateTime.now().minusHours(4));
    incidente1.setFechaResolucion(LocalDateTime.now());
    Assertions.assertEquals(4, incidente1.tiempoDeCierre().toHours());
    Assertions.assertEquals(240, incidente1.tiempoDeCierre().toMinutes());


  }
}
