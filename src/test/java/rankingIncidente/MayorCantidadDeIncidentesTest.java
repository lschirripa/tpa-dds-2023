package rankingIncidente;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import layers.models.domain.Entidad;
import layers.models.domain.Incidente;
import layers.models.domain.Organizacion;
import layers.models.domain.Prestadora;
import layers.models.domain.PrestadoraDeServicio;
import layers.models.domain.ServicioAsociado;
import layers.models.repositories.ServicioAsociadoRepository;
import layers.models.repositories.ServicioRepository;
import layers.services.ServicioAsociadoService;
import layers.services.ServicioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import utils.rankingincidente.MayorCantidadDeIncidentes;

public class MayorCantidadDeIncidentesTest {
  private MayorCantidadDeIncidentes mayorCantidadDeIncidentes;
  private ServicioAsociadoRepository servicioAsociadoRepository;
  private ServicioAsociadoService servicioAsociadoService;

  private Prestadora prestadora;
  private PrestadoraDeServicio prestadoraDeServicio;
  private Entidad entidad, entidad2;
  private ServicioAsociado servicioAsociado, servicioAsociado2, servicioAsociado3;
  private Incidente incidente, incidente2, incidente3;
  private ServicioService servicioService;
  private ServicioRepository servicioRepository;

  @BeforeEach
  public void setUp() throws IOException {
    this.servicioAsociadoRepository = new ServicioAsociadoRepository();
    this.servicioService = new ServicioService(servicioRepository, null);
    this.servicioAsociadoService = new ServicioAsociadoService(servicioAsociadoRepository, servicioService);
    this.mayorCantidadDeIncidentes = new MayorCantidadDeIncidentes("mayorCantidadDeIncidentes");
    this.prestadora = new Prestadora("Prestadora 1", null);
    this.prestadoraDeServicio = new PrestadoraDeServicio(prestadora);

    this.entidad = new Organizacion();
    this.entidad.setNombre("Entidad 1");
    this.entidad2 = new Organizacion();
    this.entidad2.setNombre("Entidad 2");
    this.prestadoraDeServicio.addEntidad(entidad);
    this.prestadoraDeServicio.addEntidad(entidad2);
    this.servicioAsociado = new ServicioAsociado(entidad, null, null);
    this.servicioAsociado2 = new ServicioAsociado(entidad, null, null);
    this.servicioAsociado3 = new ServicioAsociado(entidad2, null, null);
    this.incidente = new Incidente(servicioAsociado, null, LocalDateTime.now().minusDays(1));
    incidente.setFechaResolucion(LocalDateTime.now());
    this.incidente2 = new Incidente(servicioAsociado2, null, LocalDateTime.now().minusDays(2));
    incidente2.setFechaResolucion(LocalDateTime.now());
    this.incidente3 = new Incidente(servicioAsociado3, null, LocalDateTime.now().minusDays(1));
    incidente3.setFechaResolucion(LocalDateTime.now());

    servicioAsociadoService.addHistoricoIncidente(servicioAsociado, incidente);
    servicioAsociadoService.addHistoricoIncidente(servicioAsociado2, incidente2);
    servicioAsociadoService.addHistoricoIncidente(servicioAsociado3, incidente3);
    servicioAsociadoService.save(servicioAsociado);
    servicioAsociadoService.save(servicioAsociado2);
    servicioAsociadoService.save(servicioAsociado3);
  }

  @Test
  @DisplayName("comprobar elementos del hashmap generados por las 2 entidades de la misma prestadora")
  public void testValidarElementosHashmap() {
    LinkedHashMap<Entidad, Double> hashmapGenerado = mayorCantidadDeIncidentes.generarRankingDe(this.prestadoraDeServicio);
    assert hashmapGenerado.size() == 2;
  }

  @Test
  @DisplayName("comprobar elementos del hashmap generados por la unica entidad de la misma prestadora")
  public void testValidarElementoHashmap() {
    this.prestadoraDeServicio.borrarEntidad(entidad2);

    LinkedHashMap<Entidad, Double> hashmapGenerado = mayorCantidadDeIncidentes.generarRankingDe(this.prestadoraDeServicio);
    assert hashmapGenerado.size() == 1;
  }

  @Test
  @DisplayName("Quedarme con los dos incidentes semanales, uno queda fuera por filtrado")
  public void test_filtrado_semanal() {
    // cambio la fecha de creacion de incidente3 para que quede fuera del filtro
    incidente3.setFechaCreacion(LocalDateTime.now().minusDays(100));
    servicioAsociadoService.addHistoricoIncidente(servicioAsociado3, incidente3);
    servicioAsociadoService.save(servicioAsociado3);
    MayorCantidadDeIncidentes mayorCantidadDeIncidentes = new MayorCantidadDeIncidentes("mayorCantidadDeIncidentes");
    LinkedHashMap<Entidad, Double> hashmapGenerado = mayorCantidadDeIncidentes.generarRankingDe(prestadoraDeServicio);
    List<Double> result = new ArrayList<>();
    result.add(2.0);
    result.add(0.0);

    assertEquals(hashmapGenerado.values().stream().toList(), result);

  }

  @Test
  @DisplayName("se levanta un incidente sobre una prestacion que ya tiene un incidente abierto el mismo dia, se filtra el segundo")
  public void test_filtrado_diario() {
    // agrego un nuevo incidente4 que se abre el mismo dia que el incidente3 (sobre el mismo servicioAsociado3)
    Incidente incidente4 = new Incidente(servicioAsociado3, null, LocalDateTime.now().minusDays(1));
    incidente4.setFechaResolucion(LocalDateTime.now());

    servicioAsociadoService.addHistoricoIncidente(servicioAsociado3, incidente4);
    servicioAsociadoService.save(servicioAsociado3);
    MayorCantidadDeIncidentes mayorCantidadDeIncidentes = new MayorCantidadDeIncidentes("mayorCantidadDeIncidentes");

    LinkedHashMap<Entidad, Double> hashmapGenerado = mayorCantidadDeIncidentes.generarRankingDe(prestadoraDeServicio);
    List<Double> result = new ArrayList<>();
    result.add(2.0);
    result.add(1.0);

    assertEquals(hashmapGenerado.values().stream().toList(), result);


//    for (Entidad entidad : hashmapGenerado.keySet()) {
//      System.out.println(entidad.getNombre());
//    }
//    System.out.println(hashmapGenerado.values());
//    System.out.println(incidente3.getFechaCreacion());
//
  }

  @Test
  @DisplayName("se levantan 4 incidentes sobre una prestacion que ya tiene un incidente abierto el mismo dia, se filtra el segundo")
  public void test_filtrado_diario_muchos_incidentes() {
    Incidente incidente4 = new Incidente(servicioAsociado3, null, LocalDateTime.now().minusDays(1));
    incidente4.setFechaResolucion(LocalDateTime.now());

    Incidente incidente5 = new Incidente(servicioAsociado3, null, LocalDateTime.now().minusDays(1));
    incidente4.setFechaResolucion(LocalDateTime.now());

    Incidente incidente6 = new Incidente(servicioAsociado3, null, LocalDateTime.now().minusDays(1));
    incidente4.setFechaResolucion(LocalDateTime.now());

    servicioAsociadoService.addHistoricoIncidente(servicioAsociado3, incidente4);
    servicioAsociadoService.addHistoricoIncidente(servicioAsociado3, incidente5);
    servicioAsociadoService.addHistoricoIncidente(servicioAsociado3, incidente6);
    servicioAsociadoService.save(servicioAsociado3);
    MayorCantidadDeIncidentes mayorCantidadDeIncidentes = new MayorCantidadDeIncidentes("mayorCantidadDeIncidentes");

    LinkedHashMap<Entidad, Double> hashmapGenerado = mayorCantidadDeIncidentes.generarRankingDe(prestadoraDeServicio);
    List<Double> result = new ArrayList<>();
    result.add(2.0);
    result.add(1.0);

    assertEquals(hashmapGenerado.values().stream().toList(), result);

  }

}
