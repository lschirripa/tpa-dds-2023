package layers.models.domain;

import excepciones.AgregarEntidadException;
import excepciones.EliminarEntidadException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "prestadora_de_servicio")
@Getter
@Setter
public class PrestadoraDeServicio implements IEntidadControlada {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Embedded
  private Prestadora prestadora;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "prestadora_id")
  private List<Entidad> entidades = new ArrayList<>();
  @OneToOne(cascade = CascadeType.ALL)
  private Usuario usuarioDesignado;
  @Column(name = "correo")
  private String correo;

  public PrestadoraDeServicio(Prestadora prestadora) {
    this.prestadora = prestadora;
  }

  public PrestadoraDeServicio() {
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
