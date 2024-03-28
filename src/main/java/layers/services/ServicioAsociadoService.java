package layers.services;

import excepciones.ServicioAsociadoInexistenteException;
import java.util.List;
import layers.models.domain.*;
import layers.models.repositories.ServicioAsociadoRepository;

public class ServicioAsociadoService {
  public ServicioAsociadoRepository servicioAsociadoRepository;
  private ServicioService servicioService;

  public ServicioAsociadoService(ServicioAsociadoRepository servicioAsociadoRepository, ServicioService servicioService) {
    this.servicioAsociadoRepository = servicioAsociadoRepository;
    this.servicioService = servicioService;
  }

  public ServicioAsociado buscarYValidarServicioAsociado(int servicioIncidentadoId) {
    ServicioAsociado servicioAsociado = servicioAsociadoRepository.getById(servicioIncidentadoId, ServicioAsociado.class);
    if (servicioAsociado == null) {
      throw new ServicioAsociadoInexistenteException("ServicioAsociado no encontrado");
    }
    return servicioAsociado;
  }

  public void save(ServicioAsociado servicioAsociado) {
    servicioAsociadoRepository.save(servicioAsociado);
  }

  public void update(ServicioAsociado servicioAsociado) {
    servicioAsociadoRepository.update(servicioAsociado);
  }

  public void saveServicio(Servicio servicio) {
    servicioService.save(servicio);
  }

  public void updateServicio(Servicio servicio) {
    servicioService.update(servicio);
  }

  public List<ServicioAsociado> getAll() {
    return servicioAsociadoRepository.getAll(ServicioAsociado.class);
  }

  public void liberarServicio(ServicioAsociado servicioIncidentado) {
    servicioIncidentado.setIncidentado(false);
    this.servicioAsociadoRepository.update(servicioIncidentado);
  }

  public void incidentarServicio(ServicioAsociado servicioAsociado) {
    servicioAsociado.setIncidentado(true);
    this.servicioAsociadoRepository.update(servicioAsociado);
  }

  public void addHistoricoIncidente(ServicioAsociado servicioAsociado, Incidente incidente) {
    servicioAsociado.getHistoricoDeIncidentes().remove(incidente);
    servicioAsociado.getHistoricoDeIncidentes().add(incidente);
  }

  public void removeHistoricoIncidente(ServicioAsociado servicioAsociado, Incidente incidente) {
    servicioAsociado.getHistoricoDeIncidentes().remove(incidente);
  }

  public void updateServicioUsuario(ServicioUsuario servicioUsuario) {
    servicioService.updateServicioUsuario(servicioUsuario);
  }

  public void delete(ServicioAsociado servicioAsociado) {
    servicioAsociadoRepository.delete(servicioAsociado);
  }

  public ServicioAsociado findBy(Entidad entidad, Establecimiento establecimiento, Servicio servicio) {
    return servicioAsociadoRepository
        .findBy(entidad.getId(), establecimiento.getId(), servicio.getId());
  }
}
