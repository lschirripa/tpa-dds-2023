package layers.controllers;

import io.javalin.http.Context;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import layers.models.domain.*;
import layers.models.repositories.EstablecimientoRepository;
import layers.models.repositories.LocalizacionRepository;
import layers.models.repositories.UbicacionGeograficaRepository;
import layers.services.ServicioService;
import utils.ICrudViewsHandler;

public class EstablecimientoController implements ICrudViewsHandler {
  private UbicacionGeograficaRepository ubicacionGeograficaRepository;
  private EstablecimientoRepository establecimientoRepository;
  private LocalizacionRepository localizacionRepository;
  private ServicioService servicioService;

  public EstablecimientoController(UbicacionGeograficaRepository ubicacionGeograficaRepository, EstablecimientoRepository establecimientoRepository, LocalizacionRepository localizacionRepository, ServicioService servicioService) {
    this.ubicacionGeograficaRepository = ubicacionGeograficaRepository;
    this.establecimientoRepository = establecimientoRepository;
    this.localizacionRepository = localizacionRepository;
    this.servicioService = servicioService;
  }


  @Override
  public void index(Context context) {
    System.out.println("index");
    List<EstablecimientoDTO> establecimientos = obtenerEstablecimientosDTO();

    for (EstablecimientoDTO establecimientoDTO : establecimientos) {
      System.out.println(establecimientoDTO.getEstablecimiento().getNombre());
      System.out.println(establecimientoDTO.getTipo());
    }

    Map<String, Object> model = new HashMap<>();
    model.put("establecimientos", establecimientos);

    context.render("establecimientos/establecimientos.hbs", model);

  }

  @Override
  public void show(Context context) {

  }

  @Override
  public void create(Context context) {
    List<String> servicios = new ArrayList<>();
    List<Servicio> serviciosDisponibles = this.servicioService.getAll();
    for (Servicio servicio : serviciosDisponibles) {
      servicios.add(servicio.getNombre());
    }
    Map<String, Object> model = new HashMap<>();
    model.put("serviciosDisponibles", servicios);

    context.render("establecimientos/establecimiento.hbs", model);
  }

  @SuppressWarnings("checkstyle:WhitespaceAround")
  @Override
  public void save(Context context) throws IOException {
    System.out.println("SAVE");
    List<Servicio> listaServicios = new ArrayList<Servicio>();
    String tipo = context.formParam("tipo");
    String nombre = context.formParam("nombre");
    String latitud = context.formParam("latitud");
    double lat_num = Double.parseDouble(latitud);
    String longitud = context.formParam("longitud");
    double long_num = Double.parseDouble(longitud);
    List<String> serviciosSeleccionados = context.formParams("servicios");
    System.out.println(serviciosSeleccionados);

    for (String nombreServicio : serviciosSeleccionados) {
      Servicio servicio = this.servicioService.getByNombre(nombreServicio);
      System.out.println("EL SERVICIO ES:");
      System.out.println(servicio.getNombre());
      listaServicios.add(servicio);
    }

    UbicacionGeografica ubicacionGeografica = new UbicacionGeografica(lat_num, long_num);
    ubicacionGeograficaRepository.save(ubicacionGeografica);

    if (Objects.equals(tipo, "Sucursal")) {
      Sucursal sucursal = new Sucursal(nombre, listaServicios, ubicacionGeografica);
      Localizacion localizacion = sucursal.getLocalizacion();
      if (localizacion != null) {
        localizacionRepository.save(localizacion);
      }
      establecimientoRepository.save(sucursal);
    } else if (Objects.equals(tipo, "Estacion")) {
      Estacion estacion = new Estacion(nombre, listaServicios, ubicacionGeografica);
      Localizacion localizacion = estacion.getLocalizacion();
      if (localizacion != null) {
        localizacionRepository.save(localizacion);
      }
      establecimientoRepository.save(estacion);
    }

    context.redirect("/establecimientos");

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

  public List<EstablecimientoDTO> obtenerEstablecimientosDTO() {
    List<Establecimiento> establecimientos = this.establecimientoRepository.getAll(Establecimiento.class);
    List<EstablecimientoDTO> establecimientosDTO = new ArrayList<>();

    for (Establecimiento establecimiento : establecimientos) {
      EstablecimientoDTO establecimientoDTO = new EstablecimientoDTO(establecimiento);
      establecimientoDTO.setEstablecimiento(establecimiento);
      establecimientosDTO.add(establecimientoDTO);
    }

    return establecimientosDTO;
  }

}
