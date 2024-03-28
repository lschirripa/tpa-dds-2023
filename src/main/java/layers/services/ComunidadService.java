package layers.services;

import excepciones.*;
import java.util.List;
import layers.models.domain.Comunidad;
import layers.models.domain.Usuario;
import layers.models.repositories.ComunidadRepository;

public class ComunidadService {
  private final ComunidadRepository comunidadRepository;
  private final UsuarioService usuarioService;

  public ComunidadService(ComunidadRepository comunidadRepository,
                          UsuarioService usuarioService) {
    this.comunidadRepository = comunidadRepository;
    this.usuarioService = usuarioService;
  }

  public void agregarMiembroAComunidad(int comunidadId, int usuarioId) {
    Comunidad comunidad = obtenerYValidarComunidad(comunidadId);
    Usuario miembro = usuarioService.buscarYValidarUsuario(usuarioId);

    comunidad.addMiembro(miembro);
    comunidadRepository.save(comunidad);

  }

  public void borrarMiembro(int comunidadId, int usuarioId) {
    Comunidad comunidad = obtenerYValidarComunidad(comunidadId);
    Usuario miembro = usuarioService.buscarYValidarUsuario(usuarioId);

    comunidad.removeMiembro(miembro);
    comunidadRepository.save(comunidad);

  }

  public void agregarAdministradorAComunidad(int comunidadId, int usuarioId) {
    Comunidad comunidad = obtenerYValidarComunidad(comunidadId);
    Usuario admin = usuarioService.buscarYValidarUsuario(usuarioId);
    comunidad.addAdministrador(admin);
    comunidadRepository.save(comunidad);

  }

  public void borrarAdmin(int comunidadId, int usuarioId) {
    Comunidad comunidad = obtenerYValidarComunidad(comunidadId);
    Usuario admin = usuarioService.buscarYValidarUsuario(usuarioId);

    comunidad.removeAdministrador(admin);
    comunidadRepository.save(comunidad);

  }

  public Comunidad obtenerYValidarComunidad(int comunidadId) {
    Comunidad comunidad = this.comunidadRepository.getById(comunidadId, Comunidad.class);
    if (comunidad == null) {
      throw new ComunidadInexistenteException("No existe la comunidad");
    }
    return comunidad;
  }

  public void save(Comunidad comunidad) {
    comunidadRepository.save(comunidad);
  }

  public List<Comunidad> getAll() {
    return comunidadRepository.getAll(Comunidad.class);
  }

  public Comunidad getById(int i) {
    return comunidadRepository.getById(i, Comunidad.class);
  }

  public void update(Comunidad comunidad) {
    comunidadRepository.update(comunidad);
  }

  public void delete(Comunidad comunidad) {
    comunidadRepository.delete(comunidad);
  }

  public List<Comunidad> getAllActivas() {
    return this.comunidadRepository.getAllActivas();
  }
}
