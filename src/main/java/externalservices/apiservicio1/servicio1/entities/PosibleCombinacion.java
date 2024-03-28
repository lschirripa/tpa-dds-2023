package externalservices.apiservicio1.servicio1.entities;

import externalservices.apiservicio1.servicio1.entities.entidadesadaptadas.ComunidadAdaptada;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PosibleCombinacion {
  ComunidadAdaptada community1;
  ComunidadAdaptada community2;
}
