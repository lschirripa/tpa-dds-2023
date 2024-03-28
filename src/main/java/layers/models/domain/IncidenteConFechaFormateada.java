package layers.models.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidenteConFechaFormateada {
  private Incidente incidente;
  private String fechaCreacion;
  private String fechaResolucion;

  public IncidenteConFechaFormateada() {

  }
}