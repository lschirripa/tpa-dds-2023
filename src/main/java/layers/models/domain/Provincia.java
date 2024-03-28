package layers.models.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Provincia implements LocalizacionTipo {
  public String nombre;

  public Provincia(String nombre) {
    this.nombre = nombre;
  }

  public Provincia() {
  }
}

