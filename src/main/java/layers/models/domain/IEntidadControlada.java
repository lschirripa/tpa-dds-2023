package layers.models.domain;

import java.util.List;

public interface IEntidadControlada {
  public void addEntidad(Entidad entidad);

  public void borrarEntidad(Entidad entidad);

  public List<Entidad> getEntidades();
}
