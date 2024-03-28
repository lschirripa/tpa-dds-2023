package layers.controllers;

import io.javalin.http.Context;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import layers.models.domain.*;
import layers.models.repositories.EntidadRepository;
import layers.models.repositories.EstablecimientoRepository;
import layers.models.repositories.LocalizacionRepository;
import layers.models.repositories.UbicacionGeograficaRepository;
import layers.services.EntidadService;
import layers.services.EstablecimientoService;
import utils.ICrudViewsHandler;

public class OrganizacionController implements ICrudViewsHandler {
  private EstablecimientoService establecimientoService;
  private UbicacionGeograficaRepository ubicacionGeograficaRepository;
  private LocalizacionRepository localizacionRepository;
  private EntidadRepository entidadRepository;

  public OrganizacionController(EstablecimientoService establecimientoService, UbicacionGeograficaRepository ubicacionGeograficaRepository, LocalizacionRepository localizacionRepository, EntidadRepository entidadRepository) {
    this.establecimientoService = establecimientoService;
    this.ubicacionGeograficaRepository = ubicacionGeograficaRepository;
    this.localizacionRepository = localizacionRepository;
    this.entidadRepository = entidadRepository;
  }


  @Override
  public void index(Context context) {
    System.out.println("index");
    List<Entidad> organizaciones = this.entidadRepository.getAllOrganizaciones(Entidad.class);

    Map<String, Object> model = new HashMap<>();
    model.put("organizaciones", organizaciones);

    context.render("entidades/organizaciones.hbs", model);
  }

  @Override
  public void show(Context context) {

  }

  @Override
  public void create(Context context) {
    List<String> sucursalesNombres = new ArrayList<>();
    List<Sucursal> sucursales = this.establecimientoService.getSucursalesSinOrganizacion();
    for (Sucursal sucursal : sucursales) {
      sucursalesNombres.add(sucursal.getNombre());
    }
    Map<String, Object> model = new HashMap<>();
    model.put("sucursalesDisponibles", sucursalesNombres);

    context.render("entidades/organizacion.hbs", model);
  }

  @Override
  public void save(Context context) throws IOException {
    String nombre = context.formParam("nombre");
    String latitud = context.formParam("latitud");
    double lat_num = Double.parseDouble(latitud);
    String longitud = context.formParam("longitud");
    double long_num = Double.parseDouble(longitud);

    ArrayList<Sucursal> sucursales = new ArrayList<Sucursal>();
    List<String> sucursalesSeleccionadas = context.formParams("sucursales");

    for (String nombreSucursal : sucursalesSeleccionadas) {
      Sucursal sucursal = this.establecimientoService.getSucursalByNombre(nombreSucursal);
      sucursales.add(sucursal);
    }

    UbicacionGeografica ubicacionGeografica = new UbicacionGeografica(lat_num, long_num);
    ubicacionGeograficaRepository.save(ubicacionGeografica);

    Organizacion organizacion = new Organizacion(nombre, ubicacionGeografica, sucursales);
    Localizacion localizacion = organizacion.getLocalizacion();
    if (localizacion != null) {
      localizacionRepository.save(localizacion);
    }
    entidadRepository.save(organizacion);

    context.redirect("/organizaciones");
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
