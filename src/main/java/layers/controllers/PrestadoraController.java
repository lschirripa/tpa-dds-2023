package layers.controllers;

import io.javalin.http.Context;
import layers.models.domain.Prestadora;
import layers.models.domain.PrestadoraDeServicio;
import layers.models.repositories.PrestadoraDeServicioRepository;
import utils.ICrudViewsHandler;

public class PrestadoraController implements ICrudViewsHandler {

  private PrestadoraDeServicioRepository prestadoraDeServicioRepository;

  public PrestadoraController(PrestadoraDeServicioRepository prestadoraDeServicioRepository) {
    this.prestadoraDeServicioRepository = prestadoraDeServicioRepository;
  }


  public void save_prestadora_from_csv(String nombre, String descripcion) {
    Prestadora prestadora  = new Prestadora(nombre, descripcion);
    PrestadoraDeServicio prestadoraDeServicio = new PrestadoraDeServicio(prestadora);
    prestadoraDeServicioRepository.save(prestadoraDeServicio);
  }

@Override
  public void index(Context context) {

  }

  @Override
  public void show(Context context) {

  }

  @Override
  public void create(Context context) {

  }

  @Override
  public void save(Context context) {

  }

  @Override
  public void edit(Context context) {

  }

  @Override
  public void update(Context context) {

  }

  @Override
  public void delete(Context context) {

  }

}
