package layers.models.domain;

import excepciones.AgregarIncidenteException;
import excepciones.AgregarMiembroException;
import excepciones.EliminarMiembroException;
import excepciones.IncidenteInexistenteException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "servicio_asociado")
@Getter
@Setter
public class ServicioAsociado {
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  private int id;
  @ManyToOne
  @JoinColumn(name = "entidad_id", referencedColumnName = "id")
  private Entidad entidad;
  @ManyToOne
  @JoinColumn(name = "establecimiento_id", referencedColumnName = "id")
  private Establecimiento establecimiento;
  @ManyToOne
  @JoinColumn(name = "servicio_id", referencedColumnName = "id")
  private Servicio servicio;
  @Column(name = "incidentado", columnDefinition = "boolean default false")
  private boolean incidentado = false;
  @ManyToMany
  @JoinTable(
      name = "historico_incidentes_por_serv_asociado",
      joinColumns = @JoinColumn(name = "serv_asociado_id"),
      inverseJoinColumns = @JoinColumn(name = "incidente_id"))
  private List<Incidente> historicoDeIncidentes = new ArrayList<>();
  @OneToMany(mappedBy = "servicioAsociado", cascade = CascadeType.ALL)
  private List<ServicioUsuario> servicioUsuarios;

  public ServicioAsociado(Entidad entidad, Establecimiento establecimiento, Servicio servicio) {
    this.entidad = entidad;
    this.establecimiento = establecimiento;
    this.servicio = servicio;
    servicioUsuarios = new ArrayList<>();
  }

  public ServicioAsociado() {
  }

  public void addHistoricoIncidente(Incidente incidente) {
    if (this.historicoDeIncidentes.contains(incidente)) {
      throw new AgregarIncidenteException("Incidente ya se encuentra en la lista de historicos");
    } else {
      this.historicoDeIncidentes.add(incidente);
    }
  }

  public void removeHistoricoIncidente(Incidente incidente) {
    if (!this.historicoDeIncidentes.contains(incidente)) {
      throw new IncidenteInexistenteException("Incidente no encontrado en la lista de historicos");
    } else {
      this.historicoDeIncidentes.remove(incidente);
    }
  }

  public void addServicioUsuario(ServicioUsuario servicioUsuario) {
    if (servicioContieneAUsuario(servicioUsuario.getUsuario())) {
      throw new
          AgregarMiembroException("El usuario ya esta en la lista de usuarios afectados");
    } else {
      this.servicioUsuarios.add(servicioUsuario);
    }
  }

  public void removeServicioUsuario(ServicioUsuario servicioUsuario) {
    if (!servicioContieneAUsuario(servicioUsuario.getUsuario())) {
      throw new
          EliminarMiembroException("El usuario no esta en la lista de usuarios afectados");
    } else {
      this.servicioUsuarios.remove(servicioUsuario);
    }
  }

  private boolean servicioContieneAUsuario(Usuario usuario) {
    // que no exista el par servicio-usuario
    return this.servicioUsuarios.stream().anyMatch(su -> su.getUsuario().equals(usuario));

  }

  public int cantidadDeIncidentes() {
    return this.historicoDeIncidentes.size();
  }

}
