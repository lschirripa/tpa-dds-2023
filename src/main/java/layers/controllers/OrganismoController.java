package layers.controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import java.util.Objects;
import layers.models.domain.Organismo;
import layers.models.domain.OrganismoDeControl;
import layers.models.repositories.OrganismoDeControlRepository;
import utils.ICrudViewsHandler;

public class OrganismoController implements ICrudViewsHandler {

  private OrganismoDeControlRepository organismoDeControlRepository;

  public OrganismoController(OrganismoDeControlRepository organismoDeControlRepository) {
    this.organismoDeControlRepository = organismoDeControlRepository;
  }


  public void save_organismo_from_csv(String nombre, String descripcion) {
    Organismo organismo  = new Organismo(nombre, descripcion);
    OrganismoDeControl organismoDeControl = new OrganismoDeControl(organismo);
    organismoDeControlRepository.save(organismoDeControl);
  }

  @Override
  public void index(Context context) {

  }

  @Override
  public void show(Context context) {

  }

  @Override
  public void create(Context context) {
//    OrganismoDeControl organismoDeControl = new OrganismoDeControl();
//    this.asignarParametros(servicio, context);
//    this.repositorioDeServicios.guardar(servicio);
//    context.status(HttpStatus.CREATED);
//    context.redirect("/servicios");
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
  private void asignarParametros(OrganismoDeControl organismoDeControl, Context context) {

  }
}

