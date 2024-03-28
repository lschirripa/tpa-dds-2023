package layers.models.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EstablecimientoDTO {
  private Establecimiento establecimiento;
  private String tipo;

  public EstablecimientoDTO(Establecimiento establecimiento) {
    this.establecimiento = establecimiento;
    this.tipo = determinarTipo(establecimiento);
  }

  // Getters y setters

  private String determinarTipo(Establecimiento establecimiento) {
    return establecimiento instanceof Estacion ? "Estación" : "Sucursal";
  }
}


