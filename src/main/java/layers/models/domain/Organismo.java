package layers.models.domain;

import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Organismo {
  private String nombre;
  private String descripcion;

  public Organismo(String nombre, String descripcion) {
    this.nombre = nombre;
    this.descripcion = descripcion;
  }

  public Organismo() {
  }
}
