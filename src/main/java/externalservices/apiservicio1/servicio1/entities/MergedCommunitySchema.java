package externalservices.apiservicio1.servicio1.entities;

import externalservices.apiservicio1.servicio1.entities.entidadesadaptadas.ComunidadAdaptada;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MergedCommunitySchema {
  public List<ComunidadAdaptada> mergedCommunity;

  public MergedCommunitySchema(List<ComunidadAdaptada> mergedCommunity) {
    this.mergedCommunity = mergedCommunity;
  }
}
