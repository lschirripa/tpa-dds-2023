package layers.models.domain;

import excepciones.AgregarAdministradorException;
import excepciones.AgregarMiembroException;
import excepciones.EliminarAdministradorException;
import excepciones.EliminarMiembroException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comunidad")
@Getter
@Setter
public class Comunidad {
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  private int id;
  @Column(name = "nombre")
  private String nombre;
  @Column(name = "descripcion")
  private String descripcion;
  @Column(name = "puntos_de_confianza")
  private Double puntosDeConfianza;
  @Column(name = "grado_de_confianza")
  private GradoDeConfianza gradoDeConfianza;
  @Column(name = "ultima_fecha_de_fusion", columnDefinition = "TIMESTAMP")
  private LocalDateTime ultimaFechaDeFusion;
  @ManyToMany
  @JoinTable(
      name = "comunidad_miembros",
      joinColumns = @JoinColumn(name = "comunidad_id"),
      inverseJoinColumns = @JoinColumn(name = "usuario_id"))
  private List<Usuario> miembros = new ArrayList<>();
  @ManyToMany
  @JoinTable(
      name = "comunidad_administradores",
      joinColumns = @JoinColumn(name = "comunidad_id"),
      inverseJoinColumns = @JoinColumn(name = "usuario_id"))
  private List<Usuario> administradoresDeComunidad = new ArrayList<>();
  @Column(name = "activa")
  private Boolean activa;

  public Comunidad(String nombre) {
    this.nombre = nombre;
    this.activa = true;
    this.ultimaFechaDeFusion = LocalDateTime.of(1999, 1, 1, 0, 0);
    this.setPuntosDeConfianza(5.0);
    this.setGradoDeConfianza(GradoDeConfianza.CONFIABLE_NIVEL_1);
  }

  public Comunidad() {
    this.activa = true;
    this.ultimaFechaDeFusion = LocalDateTime.of(1999, 1, 1, 0, 0);
    this.setPuntosDeConfianza(5.0);
    this.setGradoDeConfianza(GradoDeConfianza.CONFIABLE_NIVEL_1);
  }

  public void addMiembro(Usuario miembro) {
    if (this.miembros.contains(miembro)) {
      throw new AgregarMiembroException("Este miembro ya pertenece a la comunidad");
    } else {
      miembros.add(miembro);
    }
  }

  public void removeMiembro(Usuario miembro) {
    if (!this.miembros.contains(miembro)) {
      throw new EliminarMiembroException("Este miembro no pertenece a la comunidad");
    } else {
      miembros.remove(miembro);
    }
  }

  public void addAdministrador(Usuario admin) {
    if (this.administradoresDeComunidad.contains(admin)) {
      throw new AgregarAdministradorException("Este administrador ya se encuentra designado");
    } else {
      administradoresDeComunidad.add(admin);
    }
  }

  public void removeAdministrador(Usuario admin) {
    if (!this.administradoresDeComunidad.contains(admin)) {
      throw new EliminarAdministradorException("Este administrador no pertenece a la comunidad");
    } else {
      administradoresDeComunidad.remove(admin);
    }
  }

}
