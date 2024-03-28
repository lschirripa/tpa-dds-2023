package externalservices.apiservicio1.servicio1.entities.entidadesadaptadas;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServicioAdaptado {
  public String id;
  public String name;

  public ServicioAdaptado(String id, String nombre) {
    this.id = id;
    this.name = nombre;
  }
}
