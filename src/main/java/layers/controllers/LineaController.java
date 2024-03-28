package layers.controllers;

import io.javalin.http.Context;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import layers.models.domain.*;
import layers.models.repositories.EntidadRepository;
import layers.models.repositories.LocalizacionRepository;
import layers.models.repositories.UbicacionGeograficaRepository;
import layers.services.EstablecimientoService;
import utils.ICrudViewsHandler;

public class LineaController implements ICrudViewsHandler {
  private EstablecimientoService establecimientoService;
  private UbicacionGeograficaRepository ubicacionGeograficaRepository;
  private LocalizacionRepository localizacionRepository;
  private EntidadRepository entidadRepository;

  public LineaController(EstablecimientoService establecimientoService, UbicacionGeograficaRepository ubicacionGeograficaRepository, LocalizacionRepository localizacionRepository, EntidadRepository entidadRepository) {
    this.establecimientoService = establecimientoService;
    this.ubicacionGeograficaRepository = ubicacionGeograficaRepository;
    this.localizacionRepository = localizacionRepository;
    this.entidadRepository = entidadRepository;
  }


  @Override
  public void index(Context context) {
    System.out.println("index");
    List<Entidad> lineas = this.entidadRepository.getAllLineas(Entidad.class);

    Map<String, Object> model = new HashMap<>();
    model.put("lineas", lineas);

    context.render("entidades/lineas.hbs", model);
  }

  @Override
  public void show(Context context) {

  }

  @Override
  public void create(Context context) {
    List<String> estacionesNombres = new ArrayList<>();
    List<Estacion> estaciones = this.establecimientoService.getAllEstaciones();
    for (Estacion estacion : estaciones) {
      estacionesNombres.add(estacion.getNombre());
    }
    Map<String, Object> model = new HashMap<>();
    model.put("estacionesDisponibles", estacionesNombres);

    context.render("entidades/linea.hbs", model);
  }

  @Override
  public void save(Context context) throws IOException {
    String nombre = context.formParam("nombre");
    String latitud = context.formParam("latitud");
    double lat_num = Double.parseDouble(latitud);
    String longitud = context.formParam("longitud");
    double long_num = Double.parseDouble(longitud);
    String origen = context.formParam("estacionorigen");
    String destino = context.formParam("estaciondestino");

    ArrayList<Estacion> estaciones = new ArrayList<Estacion>();
    List<String> estacionesSeleccionadas = context.formParams("estaciones");

    for (String nombreEstacion : estacionesSeleccionadas) {
      Estacion estacion = this.establecimientoService.getEstacionByNombre(nombreEstacion);
      estaciones.add(estacion);
    }

    Estacion estacionOrigen = this.establecimientoService.getEstacionByNombre(origen);
    Estacion estacionDestino = this.establecimientoService.getEstacionByNombre(destino);

    UbicacionGeografica ubicacionGeografica = new UbicacionGeografica(lat_num, long_num);
    ubicacionGeograficaRepository.save(ubicacionGeografica);

    Linea linea = new Linea(nombre, estaciones, ubicacionGeografica, estacionOrigen, estacionDestino);
    Localizacion localizacion = linea.getLocalizacion();
    //si la localizacion es nula, no la guardo
    if (localizacion != null) {
      localizacionRepository.save(localizacion);
    }
    entidadRepository.save(linea);
    context.redirect("/lineas");
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
