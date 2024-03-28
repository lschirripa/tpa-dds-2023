package externalservices.apiservicio1.servicio1.entities.entidadesadaptadas;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstablecimientoAdaptado {
  public String id;
  public String name;

  public EstablecimientoAdaptado(String id, String nombre) {
    this.id = id;
    this.name = nombre;
  }
}
