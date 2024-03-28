package layers.models.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ubicacion_geografica")
@Setter
@Getter
public class UbicacionGeografica {
  @Column(name = "latitud")
  public double lat;
  @Column(name = "longitud")
  public double lon;
  @Id
  @GeneratedValue
  private Integer id;

  public UbicacionGeografica(double latitud, double longitud) {
    this.lat = latitud;
    this.lon = longitud;
  }

  public UbicacionGeografica() {
  }
}
