package layers.models.domain;

import excepciones.AgregarIncidenteException;
import excepciones.ComunidadInexistenteException;
import excepciones.ServicioAsociadoInexistenteException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import utils.PasswordHasher;
import utils.enums.FormaNotificacion;
import utils.enums.ModoNotificacion;
import utils.enums.Rol;
import utils.enums.UserStatus;
import utils.passwordvalidator.ValidadorRegistro;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "username")
  private String userName;
  @Column(name = "password")
  private String password;
  @Embedded
  private DatosPersonalesUsuario datosPersonales;
  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinColumn(name = "ubicacion_id", referencedColumnName = "id")
  private UbicacionGeografica ubicacionActual;
  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  private Localizacion localizacion;
  @ManyToMany//(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  private List<ServicioAsociado> serviciosAsociados = new ArrayList<>();
  @ManyToMany(mappedBy = "miembros", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  private List<Comunidad> comunidades = new ArrayList<>();
  @Enumerated(EnumType.STRING)
  private FormaNotificacion formaNotificacion = FormaNotificacion.NOW;
  @Enumerated(EnumType.STRING)
  private ModoNotificacion modoNotificacion = ModoNotificacion.WHATSAPP;
  @ElementCollection
  @CollectionTable(name = "horarios_notificacion", joinColumns = @JoinColumn(name = "usuario_id"))
  @Column(name = "hora_notificacion")
  private List<LocalTime> horariosNotificacion = new ArrayList<>();
  @ManyToMany
  @JoinTable(name = "incidentes_pendientes_notificacion",
      joinColumns = @JoinColumn(name = "usuario_id"),
      inverseJoinColumns = @JoinColumn(name = "incidente_id"))
  private List<Incidente> notificacionIncidentesPendientes = new ArrayList<>();
  @Column(name = "puntos_de_confianza")
  private Double puntosDeConfianza;
  @Enumerated(EnumType.STRING)
  private GradoDeConfianza gradoDeConfianza;
  @Enumerated(EnumType.STRING)
  private UserStatus userStatus = UserStatus.ACTIVE;
  @Column(name = "last_login", columnDefinition = "timestamp")
  private LocalDateTime lastLogin;
  @Column(name = "created_at", columnDefinition = "timestamp")
  private LocalDateTime createdAt = LocalDateTime.now();
  @Enumerated(EnumType.STRING)
  private Rol role;

  public Usuario(String userName, String password, DatosPersonalesUsuario datosPersonales,
                 UbicacionGeografica ubicacionActual, Rol role) {
    new ValidadorRegistro().validarPassword(password);
    this.userName = userName;
    this.password = password;
    this.datosPersonales = datosPersonales;
    this.ubicacionActual = ubicacionActual;
    this.localizacion = null; //nulo hasta que el usuario asigna una localizacion de interes.
    this.puntosDeConfianza = 5.0;
    this.gradoDeConfianza = GradoDeConfianza.CONFIABLE_NIVEL_1;
    this.role = role;
  }

  public Usuario() {
    this.localizacion = null; //nulo hasta que el usuario asigna una localizacion de interes.
    this.puntosDeConfianza = 5.0;
    this.gradoDeConfianza = GradoDeConfianza.CONFIABLE_NIVEL_1;
    this.role = Rol.USUARIO;
  }

  public void setPassword(String password) {
    System.out.println("Validando clave..");
    new ValidadorRegistro().validarPassword(password);
    System.out.println("Clave valida");
    this.password = PasswordHasher.hashPassword(password);
    System.out.println("Clave hasheada");
  }

  public void setPasswordSinValidacion(String password) {
    this.password = PasswordHasher.hashPassword(password);
  }

  public void addServicioAsociado(ServicioAsociado servicioAsociado) {
    if (serviciosAsociados.contains(servicioAsociado)) {
      throw new ServicioAsociadoInexistenteException("El usuario ya esta asociado al servicio");
    } else {
      this.serviciosAsociados.add(servicioAsociado);
    }
  }

  public void removeServicioAsociado(ServicioAsociado servicioAsociado) {
    if (!serviciosAsociados.contains(servicioAsociado)) {
      throw new ServicioAsociadoInexistenteException("El usuario no esta asociado al servicio");
    } else {
      this.serviciosAsociados.remove(servicioAsociado);
    }
  }

  public void addComunidad(Comunidad comunidad) {
    if (comunidades.contains(comunidad)) {
      throw new ComunidadInexistenteException("El usuario ya esta asociado a la comunidad");
    } else {
      this.comunidades.add(comunidad);
    }
  }

  public void removeComunidad(Comunidad comunidad) {
    if (!comunidades.contains(comunidad)) {
      throw new ComunidadInexistenteException("El usuario no esta asociado a la comunidad");
    } else {
      this.comunidades.remove(comunidad);
    }
  }

  public void addNotificacionIncidentesPendientes(Incidente incidente) {
    if (notificacionIncidentesPendientes.contains(incidente)) {
      throw new AgregarIncidenteException("El incidente ya esta asociado al usuario");
    } else {
      this.notificacionIncidentesPendientes.add(incidente);
    }
  }

  public void removeAllNotificacionIncidentesPendientes(List<Incidente> incidentesANotificar) {
    this.notificacionIncidentesPendientes.removeAll(incidentesANotificar);
  }
}