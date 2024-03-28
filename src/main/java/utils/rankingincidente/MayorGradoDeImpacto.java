package utils.rankingincidente;

import java.util.LinkedHashMap;
import layers.models.domain.Entidad;
import layers.models.domain.IEntidadControlada;

public class MayorGradoDeImpacto extends CriterioRanking {

  public MayorGradoDeImpacto(String nombre) {
    super(nombre);
  }

  @Override
  public LinkedHashMap<Entidad, Double> generarRankingDe(IEntidadControlada controlador) {
    return null;
  }
}
