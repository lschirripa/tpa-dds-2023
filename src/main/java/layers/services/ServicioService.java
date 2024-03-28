package layers.services;

import excepciones.ServicioAsociadoInexistenteException;
import java.util.List;
import layers.models.domain.Servicio;
import layers.models.domain.ServicioUsuario;
import layers.models.repositories.ServicioRepository;
import layers.models.repositories.ServicioUsuarioRepository;

public class ServicioService {
  private ServicioRepository servicioRepository;
  private ServicioUsuarioRepository servicioUsuarioRepository;

  public ServicioService(ServicioRepository servicioRepository,
                         ServicioUsuarioRepository servicioUsuarioRepository) {
    this.servicioRepository = servicioRepository;
    this.servicioUsuarioRepository = servicioUsuarioRepository;
  }

  public List<Servicio> getAll() {
    return servicioRepository.getAll(Servicio.class);
  }

  public Servicio buscarYValidarServicio(int servicioId) {
    Servicio servicio = servicioRepository.getById(servicioId, Servicio.class);
    if (servicio == null) {
      throw new ServicioAsociadoInexistenteException("Servicio no encontrado");
    }
    return servicio;
  }

  public List<Servicio> listarServicios() {
    return this.servicioRepository.getAll(Servicio.class);
  }

  public void delete(Servicio servicio) {
    this.servicioRepository.delete(servicio);
  }

  public void save(Servicio servicio) {
    this.servicioRepository.save(servicio);
  }

  public void update(Servicio servicio) {
    this.servicioRepository.update(servicio);
  }

  public void updateServicioUsuario(ServicioUsuario servicioUsuario) {
    this.servicioUsuarioRepository.update(servicioUsuario);
  }

  public Servicio getByNombre(String nombre) {
    return this.servicioRepository.getByNombre(nombre, Servicio.class);
  }
}
