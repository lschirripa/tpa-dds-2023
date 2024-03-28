package layers.models.domain;

import java.io.IOException;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("Sucursal")
@Getter
@Setter
public class Sucursal extends Establecimiento {

  public Sucursal(String nombre, List<Servicio> servicios,
                  UbicacionGeografica ubicacionGeografica) throws IOException {
    super(nombre, servicios, ubicacionGeografica);
  }

  public Sucursal() {
  }

  public void agregarPrestacionDeServicio(Servicio servicio) {
    super.agregarPrestacionDeServicio(servicio);
  }

  public void borrarPrestacionDeServicio(Servicio servicio) {
    super.borrarPrestacionDeServicio(servicio);
  }

  public Localizacion asignarLocalizacion(UbicacionGeografica ubicacion) throws IOException {
    return super.asignarLocalizacion(ubicacion);
  }

  @Override
  public String getTipoEstablecimiento() {
    return "Sucursal";
  }
}
