package layers.models.domain;

import java.io.IOException;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("Estacion")
@Setter
@Getter
public class Estacion extends Establecimiento {

  public Estacion(String nombre, List<Servicio> servicios,
                  UbicacionGeografica ubicacionGeografica) throws IOException {
    super(nombre, servicios, ubicacionGeografica);
  }

  public Estacion() {
  }

  public void agregarPrestacionDeServicio(Servicio servicio) {
    super.agregarPrestacionDeServicio(servicio);
  }

  public void borrarPrestacionDeServicio(Servicio servicio) {
    super.borrarPrestacionDeServicio(servicio);
  }

  public Localizacion asignarLocalizacion(UbicacionGeografica ubicacion)
      throws IOException {
    return super.asignarLocalizacion(ubicacion);
  }

  @Override
  public String getTipoEstablecimiento() {
    return "Estación";
  }

  public void addServicio(Servicio servicio) {
    super.addServicio(servicio);
  }
}
