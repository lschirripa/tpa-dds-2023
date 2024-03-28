package externalservices.apiservicio1.servicio1.entities.entidadesadaptadas;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComunidadAdaptada {
  public String id;
  public String name;
  public String lastTimeMerged;
  public int degreeOfConfidence;
  public List<UsuarioAdaptado> members;
  public List<ServicioAdaptado> interestingServices;
  public List<EstablecimientoAdaptado> interestingEstablishments;

  public ComunidadAdaptada() {
  }
}
