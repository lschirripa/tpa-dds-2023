package layers.models.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Departamento implements LocalizacionTipo {
  public String nombre;
  private int id;


  public Departamento(String nombre) {
    this.nombre = nombre;
  }

  public Departamento() {
  }
}
