package layers.models.domain;

import excepciones.AgregarEntidadException;
import excepciones.EliminarEntidadException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "organismo_de_control")
@Getter
@Setter
public class OrganismoDeControl implements IEntidadControlada {
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  private int id;
  @Embedded
  private Organismo organismo;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "organismo_id")
  private List<Entidad> entidades = new ArrayList<>();
  @OneToOne(cascade = CascadeType.ALL)
  private Usuario usuarioDesignado;
  @Column(name = "correo")
  private String correo;


  public OrganismoDeControl(Organismo organismo) {
    this.organismo = organismo;
  }

  public OrganismoDeControl() {
  }

  public void addEntidad(Entidad entidad) {
    if (entidades.contains(entidad)) {
      throw new AgregarEntidadException("No se puede agregar una entidad contenida");
    } else {
      this.entidades.add(entidad);
    }
  }

  public void borrarEntidad(Entidad entidad) {
    if (!entidades.contains(entidad)) {
      throw new EliminarEntidadException("No se puede quitar una entidad que no esta incluida");
    } else {
      this.entidades.remove(entidad);
    }
  }
}
