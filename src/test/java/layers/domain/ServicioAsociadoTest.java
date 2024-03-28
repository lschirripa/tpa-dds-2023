package layers.domain;

import java.time.LocalDateTime;
import layers.models.domain.Incidente;
import layers.models.domain.Servicio;
import layers.models.domain.ServicioAsociado;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ServicioAsociadoTest {
  private ServicioAsociado servicioAsociado;
  private Incidente incidente1;
  private Incidente incidente2;

  @BeforeEach
  void setUp() {
    this.servicioAsociado = new ServicioAsociado(null, null, new Servicio("serv", "serv"));
    this.incidente1 = new Incidente(servicioAsociado, "se rompio", LocalDateTime.now());
    this.incidente2 = new Incidente(servicioAsociado, "se rompio otro", LocalDateTime.now());
  }

  @Test
  @DisplayName("ServicioAsociado tiene un historico de incidentes")
  void testGuardarIncidentesEnHistorico() {
    this.servicioAsociado.addHistoricoIncidente(incidente1);
    this.servicioAsociado.addHistoricoIncidente(incidente2);

    Assertions.assertEquals(2, this.servicioAsociado.getHistoricoDeIncidentes().size());
  }

  @Test
  @DisplayName("no se puede guardar un incidente repetido en el historico")
  void testGuardarIncidenteRepetidoEnHistorico() {
    this.servicioAsociado.addHistoricoIncidente(incidente1);
    Assertions.assertThrows(RuntimeException.class, () -> {
      this.servicioAsociado.addHistoricoIncidente(incidente1);
    });
    Assertions.assertEquals(1, this.servicioAsociado.getHistoricoDeIncidentes().size());
  }

  @Test
  @DisplayName("no se puede eliminar un incidente que no esta en el historico")
  void testEliminarIncidenteInexistente() {
    this.servicioAsociado.addHistoricoIncidente(incidente1);
    Assertions.assertThrows(RuntimeException.class, () -> {
      this.servicioAsociado.removeHistoricoIncidente(incidente2);
    });
    Assertions.assertEquals(1, this.servicioAsociado.getHistoricoDeIncidentes().size());
  }

}
