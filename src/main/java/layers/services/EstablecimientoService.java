package layers.services;

import excepciones.EstablecimientoInexistenteException;
import java.util.List;
import layers.models.domain.Establecimiento;
import layers.models.domain.Estacion;
import layers.models.domain.Sucursal;
import layers.models.repositories.EstablecimientoRepository;
import layers.models.repositories.LocalizacionRepository;

public class EstablecimientoService {
  private EstablecimientoRepository establecimientoRepository;
  private LocalizacionRepository localizacionRepository;

  public EstablecimientoService(EstablecimientoRepository establecimientoRepository,
                                LocalizacionRepository localizacionRepository) {
    this.establecimientoRepository = establecimientoRepository;
    this.localizacionRepository = localizacionRepository;
  }

  public Establecimiento buscarYValidarEstablecimiento(int establecimientoId) {
    Establecimiento establecimiento = establecimientoRepository
        .getById(establecimientoId, Establecimiento.class);
    if (establecimiento == null) {
      throw new EstablecimientoInexistenteException("Establecimiento no encontrado");
    }
    return establecimiento;
  }

  public void save(Establecimiento establecimiento) {
    localizacionRepository.getOrCreate(establecimiento.getLocalizacion());
    establecimientoRepository.save(establecimiento);
  }

  public List<Establecimiento> getAll() {
    return establecimientoRepository.getAll(Establecimiento.class);
  }

  public List<Sucursal> getAllSucursales() {
    return establecimientoRepository.getAllSucursales(Sucursal.class);
  }

  public List<Estacion> getAllEstaciones() {
    return establecimientoRepository.getAllEstaciones(Estacion.class);
  }

  public Sucursal getSucursalByNombre(String nombre) {
    return establecimientoRepository.getSucursalByNombre(nombre, Sucursal.class);
  }

  public Estacion getEstacionByNombre(String nombre) {
    return establecimientoRepository.getEstacionByNombre(nombre, Estacion.class);
  }

  public List<Sucursal> getSucursalesSinOrganizacion() {
    return establecimientoRepository.getSucursalesSinOrganizacion(Sucursal.class);
  }

}
