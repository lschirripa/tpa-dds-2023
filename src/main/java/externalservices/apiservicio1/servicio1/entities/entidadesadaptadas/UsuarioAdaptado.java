package externalservices.apiservicio1.servicio1.entities.entidadesadaptadas;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioAdaptado {
  public String id;
  public String name;

  public UsuarioAdaptado(String id, String userName) {
    this.id = id;
    this.name = userName;
  }
}
