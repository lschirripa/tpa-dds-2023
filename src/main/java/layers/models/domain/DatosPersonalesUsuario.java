package layers.models.domain;

import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class DatosPersonalesUsuario {
  private String nombre;
  private String apellido;
  private String correo;
  private String telefono;

  public DatosPersonalesUsuario(String nombre, String apellido, String correo, String telefono) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.correo = correo;
    this.telefono = telefono;
  }

  public DatosPersonalesUsuario() {
  }
}
