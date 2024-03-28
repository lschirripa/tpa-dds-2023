package layers.models.domain;

import excepciones.AgregarMiembroException;
import excepciones.EliminarMiembroException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "servicio")
@Getter
@Setter
public class Servicio {
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "nombre")
  private String nombre;
  @Column(name = "descripcion", columnDefinition = "text")
  private String descripcion;

  public Servicio(String nombre, String descripcion) {
    this.nombre = nombre;
    this.descripcion = descripcion;
  }

  public Servicio() {
    this.id = null;
  }

}
