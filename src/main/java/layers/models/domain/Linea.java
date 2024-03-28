package layers.models.domain;


import excepciones.AgregarEstacionException;
import excepciones.EliminarEstacionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("Linea")
@Setter
@Getter
public class Linea extends Entidad {
  @OneToMany
  @JoinColumn(name = "linea_id", referencedColumnName = "id")
  private List<Estacion> estaciones = new ArrayList<>();
  @ManyToOne
  @JoinColumn(name = "estacion_origen_id", referencedColumnName = "id")
  private Estacion estacionOrigen;
  @ManyToOne
  @JoinColumn(name = "estacion_destino_id", referencedColumnName = "id")
  private Estacion estacionDestino;

  public Linea(String nombre, ArrayList<Estacion> estaciones,
               UbicacionGeografica ubicacionSede,
               Estacion estacionOrigen, Estacion estacionDestino) throws IOException {
    super(nombre, ubicacionSede);
    this.estaciones = estaciones;
    this.estacionOrigen = estacionOrigen;
    this.estacionDestino = estacionDestino;
  }

  public Linea() {
  }

  public void agregarEstacion(Estacion estacion) {
    if (estaciones.contains(estacion)) {
      throw new AgregarEstacionException("Esta estacion ya se pertenece a la linea");
    } else {
      this.estaciones.add(estacion);
    }
  }

  public void borrarEstacion(Estacion estacion) {
    if (!estaciones.contains(estacion)) {
      throw new EliminarEstacionException("Esta estacion no pertenece a la linea");
    } else {
      this.estaciones.remove(estacion);
    }
  }
}
