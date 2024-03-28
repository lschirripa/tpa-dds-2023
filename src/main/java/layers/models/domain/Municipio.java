package layers.models.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Municipio implements LocalizacionTipo {
  public String nombre;

  public Municipio(String nombre) {
    this.nombre = nombre;
  }

  public Municipio() {
  }
}
