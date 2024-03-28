package layers.models.domain;

import excepciones.IncidenteInexistenteException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "incidente")
@Getter
@Setter
public class Incidente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @ManyToOne
  @JoinColumn(name = "servicio_asociado_id", referencedColumnName = "id")
  private ServicioAsociado servicioIncidentado;
  @Column(name = "descripcion", columnDefinition = "text")
  private String descripcion;
  @Column(name = "fecha_creacion", columnDefinition = "timestamp")
  private LocalDateTime fechaCreacion;
  @Column(name = "fecha_resolucion", columnDefinition = "timestamp")
  private LocalDateTime fechaResolucion = null;
  @ManyToMany
  private List<Comunidad> comunidades = new ArrayList<>();
  @ManyToOne
  private Usuario usuarioDeApertura = null;
  @ManyToOne
  private Usuario usuarioDeResolucion = null;

  public Incidente(ServicioAsociado servicioIncidentado, String descripcion,
                   LocalDateTime fechaCreacion) {
    this.servicioIncidentado = servicioIncidentado;
    this.descripcion = descripcion;
    this.fechaCreacion = fechaCreacion;
  }

  public Incidente() {
  }

  public Duration tiempoDeCierre() {
    if (this.fechaResolucion == null) {
      throw new IncidenteInexistenteException("El incidente no fue resuelto");
    } else {
      return Duration.between(this.fechaCreacion, this.fechaResolucion);
    }
  }

  public void addComunidad(Comunidad comunidad) {
    this.comunidades.add(comunidad);
  }
}