package layers.models.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "localizacion")
@Getter
@Setter
public class Localizacion {
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  private int id;
  @Column(name = "provincia")
  private String provincia;
  @Column(name = "departamento")
  private String departamento;
  @Column(name = "municipio")
  private String municipio;

  public Localizacion(String provincia, String departamento, String municipio) {
    this.provincia = provincia;
    this.departamento = departamento;
    this.municipio = municipio;
  }

  public Localizacion() {
  }
}
