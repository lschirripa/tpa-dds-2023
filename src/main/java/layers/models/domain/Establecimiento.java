package layers.models.domain;

import excepciones.AgregarServicioException;
import excepciones.EliminarSucursalException;
import externalservices.apigeoref.ObtencionDeLocalizacion;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "establecimiento")
@DiscriminatorColumn(name = "tipo")
@Getter
@Setter
public abstract class Establecimiento {
  @Transient
  ObtencionDeLocalizacion obtencionDeLocalizacion = ObtencionDeLocalizacion.getInstance();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "nombre")
  private String nombre;
  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  private List<Servicio> servicios = new ArrayList<>();
  @ManyToOne(cascade = CascadeType.ALL)
  private UbicacionGeografica ubicacionGeografica;
  @ManyToOne
  @JoinColumn(name = "localizacion_id", referencedColumnName = "id")
  private Localizacion localizacion;

  public Establecimiento() {
  }

  public Establecimiento(String nombre,
                         List<Servicio> servicios,
                         UbicacionGeografica ubicacionGeografica) throws IOException {
    this.nombre = nombre;
    this.servicios = servicios;
    this.ubicacionGeografica = ubicacionGeografica;
    this.localizacion = asignarLocalizacion(ubicacionGeografica);
  }

  public void setUbicacionGeografica(UbicacionGeografica ubicacionGeografica) throws IOException {
    this.ubicacionGeografica = ubicacionGeografica;
    this.localizacion = asignarLocalizacion(ubicacionGeografica);
    System.out.println("Localizacion prov: " + this.localizacion.getProvincia());
    System.out.println("Localizacion dep: " + this.localizacion.getDepartamento());
    System.out.println("Localizacion mun: " + this.localizacion.getMunicipio());
  }

  public Localizacion asignarLocalizacion(UbicacionGeografica ubicacion)
      throws IOException {
    return this.obtencionDeLocalizacion
        .getLocalizacionByUbicacion(ubicacion);
  }

  public void agregarPrestacionDeServicio(Servicio servicio) {
    if (this.servicios.contains(servicio)) {
      throw new
          AgregarServicioException("Este servicio ya se encuentra prestado por el establecimiento");
    } else {
      this.servicios.add(servicio);
    }
  }

  public void borrarPrestacionDeServicio(Servicio servicio) {
    if (!this.servicios.contains(servicio)) {
      throw new
          EliminarSucursalException("Este establecimiento no cuenta con ese Servicio");
    } else {
      this.servicios.remove(servicio);
    }
  }

  public abstract String getTipoEstablecimiento();

  protected void addServicio(Servicio servicio) {
    this.servicios.add(servicio);
  }
}
