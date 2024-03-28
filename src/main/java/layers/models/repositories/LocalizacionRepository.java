package layers.models.repositories;

import java.util.List;
import layers.models.domain.Localizacion;

public class LocalizacionRepository extends BaseRepository<Localizacion> {

  public void getOrCreate(Localizacion localizacion) {
    List<Localizacion> localizaciones = getAll(Localizacion.class);

    if (localizaciones.isEmpty()) {
      System.out.println("No hay localizaciones, agrego la primera");
      save(localizacion);
    } else {
      System.out.println("Hay localizaciones, busco si existe la que quiero agregar");
      Localizacion localizacionEncontrada = localizaciones.stream()
          .filter(l -> l.getProvincia().equals(localizacion.getProvincia())
              && l.getDepartamento().equals(localizacion.getDepartamento())
              && l.getMunicipio().equals(localizacion.getMunicipio()))
          .findFirst()
          .orElse(null);

      if (localizacionEncontrada == null) {
        System.out.println("No existe, la agrego");
        save(localizacion);
      } else {
        System.out.println("Existe, actualizo el id");
        localizacion.setId(localizacionEncontrada.getId());
      }
    }
  }
}
