package layers.models.domain;

import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios_interesados_servicio")
@Getter
@Setter
public class ServicioUsuario {
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  private Integer id;
  @ManyToOne
  @JoinColumn(name = "servicio_asociado_id", referencedColumnName = "id")
  private ServicioAsociado servicioAsociado;
  @ManyToOne
  @JoinColumn(name = "usuario_id", referencedColumnName = "id")
  private Usuario usuario;
  @Column(name = "flag_afectado")
  private boolean afectado;
  @Column(name = "flag_observador")
  private boolean observador;

  public ServicioUsuario() {
  }

  public ServicioUsuario(ServicioAsociado servicioAsociado, Usuario usuario, boolean esAfectado, boolean esObservador) {
    this.servicioAsociado = servicioAsociado;
    this.usuario = usuario;
    this.afectado = esAfectado;
    this.observador = esObservador;
  }
}
