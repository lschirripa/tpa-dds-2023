package layers.models.domain;

import externalservices.apigeoref.ObtencionDeLocalizacion;
import java.io.IOException;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "entidad")
@DiscriminatorColumn(name = "tipo")
@Getter
@Setter
public abstract class Entidad {
  @Transient
  protected ObtencionDeLocalizacion obtencionDeLocalizacion = ObtencionDeLocalizacion.getInstance();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "nombre")
  private String nombre;
  @ManyToOne
  @JoinColumn(name = "ubicacion_actual_id", referencedColumnName = "id")
  private UbicacionGeografica ubicacionActual;
  @ManyToOne
  @JoinColumn(name = "localizacion_id", referencedColumnName = "id")
  private Localizacion localizacion;

  public Entidad(String nombre, UbicacionGeografica ubicacion) throws IOException {
    this.nombre = nombre;
    this.ubicacionActual = ubicacion;
    this.localizacion = asignarLocalizacion(ubicacion);
  }

  public Entidad() {
  }

  public void setUbicacionActual(UbicacionGeografica ubicacion) throws IOException {
    System.out.println("Ubicacion actual: " + ubicacion.lat + ", " + ubicacion.lon);
    this.ubicacionActual = ubicacion;
    this.localizacion = asignarLocalizacion(ubicacion);
  }

  public Localizacion asignarLocalizacion(UbicacionGeografica ubicacion)
      throws IOException {
    return obtencionDeLocalizacion
        .getLocalizacionByUbicacion(ubicacion);
  }
}
