package layers.models.domain;

import excepciones.AgregarSucursalException;
import excepciones.EliminarSucursalException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("Organizacion")
@Getter
@Setter
public class Organizacion extends Entidad {
  @OneToMany
  @JoinColumn(name = "organizacion_id", referencedColumnName = "id")
  private List<Sucursal> sucursales = new ArrayList<>();

  public Organizacion(String nombre, UbicacionGeografica ubicacionActual,
                      ArrayList<Sucursal> sucursales) throws IOException {
    super(nombre, ubicacionActual);
    this.sucursales = sucursales;
  }

  public Organizacion() {
  }

  public void agregarSucursal(Sucursal sucursal) {
    if (this.sucursales.contains(sucursal)) {
      throw new AgregarSucursalException("Esta sucursal ya pertenece a la organizacion");
    } else {
      this.sucursales.add(sucursal);
    }
  }

  public void borrarSucursal(Sucursal sucursal) {
    if (!this.sucursales.contains(sucursal)) {
      throw new EliminarSucursalException("No se puede borrar sucursales que no esten en la org.");
    } else {
      this.sucursales.remove(sucursal);
    }
  }

}
