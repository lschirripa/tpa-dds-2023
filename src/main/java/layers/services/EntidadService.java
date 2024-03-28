package layers.services;

import excepciones.UsuarioInexistenteException;
import java.util.List;
import layers.models.domain.Entidad;
import layers.models.repositories.EntidadRepository;
import layers.models.repositories.LocalizacionRepository;

public class EntidadService {
  private EntidadRepository entidadRepository;
  private LocalizacionRepository localizacionRepository;

  public EntidadService(EntidadRepository entidadRepository,
                        LocalizacionRepository localizacionRepository) {
    this.entidadRepository = entidadRepository;
    this.localizacionRepository = localizacionRepository;
  }

  public Entidad buscarYValidarEntidad(int entidadId) {
    Entidad entidad = entidadRepository.getById(entidadId, Entidad.class);
    if (entidad == null) {
      throw new UsuarioInexistenteException("Entidad no encontrada");
    }
    return entidad;
  }

  public void save(Entidad entidad) {
    localizacionRepository.getOrCreate(entidad.getLocalizacion());
    entidadRepository.save(entidad);
  }

  public List<Entidad> getAll() {
    return entidadRepository.getAll(Entidad.class);
  }


}
