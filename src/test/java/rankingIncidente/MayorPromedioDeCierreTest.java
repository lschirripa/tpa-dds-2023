package rankingIncidente;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import layers.models.domain.Entidad;
import layers.models.domain.Incidente;
import layers.models.domain.Organizacion;
import layers.models.domain.ServicioAsociado;
import layers.models.repositories.IncidenteRepository;
import layers.models.repositories.ServicioAsociadoRepository;
import layers.models.repositories.ServicioRepository;
import layers.models.repositories.UsuarioRepository;
import layers.services.IncidenteService;
import layers.services.ServicioAsociadoService;
import layers.services.ServicioService;
import layers.services.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.rankingincidente.MayorPromedioDeCierre;

public class MayorPromedioDeCierreTest {
  private MayorPromedioDeCierre mayorPromedioDeCierre;

  @BeforeEach
  public void setUp() {
    UsuarioRepository usuarioRepository = new UsuarioRepository();
    ServicioService servicioService = new ServicioService(new ServicioRepository(), null);
    ServicioAsociadoService servicioAsociadoService = new ServicioAsociadoService(new ServicioAsociadoRepository(), servicioService);
    UsuarioService usuarioService = new UsuarioService(usuarioRepository, servicioAsociadoService);
    IncidenteRepository incidenteRepository = new IncidenteRepository();
    IncidenteService incidenteService = new IncidenteService(incidenteRepository, usuarioService);
    mayorPromedioDeCierre = new MayorPromedioDeCierre("mayorPromedioDeCierre");

  }

  @Test
  @DisplayName("Quedarme con los incidentes semanales")
  public void testObtenerIncidentesSemanales() throws IOException {
    Entidad entidad = new Organizacion("Entidad 1", null, null);
    ServicioAsociado servicioAsociado = new ServicioAsociado(entidad, null, null);
    Incidente incidente1 = new Incidente(servicioAsociado, null, LocalDateTime.now().minusDays(2));
    incidente1.setFechaResolucion(LocalDateTime.now().minusDays(1));

    Entidad entidad2 = new Organizacion("Entidad 2", null, null);
    ServicioAsociado servicioAsociado2 = new ServicioAsociado(entidad2, null, null);
    Incidente incidente2 = new Incidente(servicioAsociado2, null, LocalDateTime.now().minusDays(8));
    incidente2.setFechaResolucion(LocalDateTime.now().minusDays(1));

    mayorPromedioDeCierre.getIncidenteService().save(incidente1);
    mayorPromedioDeCierre.getIncidenteService().save(incidente2);

    List<Incidente> incidentes = mayorPromedioDeCierre.getIncidenteService().getAll();
    List<Incidente> incidentesSemanales = mayorPromedioDeCierre.filterIncidentesSemanales(incidentes);

    Assertions.assertEquals(1, incidentesSemanales.size());
    Assertions.assertEquals(incidente1, incidentesSemanales.get(0));
  }

  @Test
  @DisplayName("El tiempo de cierre de un incidente es el que corresponde")
  public void testTiempoDeCierreDeIncidente() {
    Incidente incidente = new Incidente(null, null, LocalDateTime.now().minusHours(1));
    incidente.setFechaResolucion(LocalDateTime.now());
    Assertions.assertEquals(1, incidente.tiempoDeCierre().toHours());
  }

  @Test
  @DisplayName("Calcular el promedio de tiempo de cierre de una entidad")
  public void testCalcularPromedioPorEntidad() throws IOException {

    Entidad entidad1 = new Organizacion("Entidad 1", null, null);
    ServicioAsociado servicioAsociado = new ServicioAsociado(entidad1, null, null);
    Incidente incidente1 = new Incidente(servicioAsociado, null, LocalDateTime.now().minusHours(1));
    incidente1.setFechaResolucion(LocalDateTime.now());

    Entidad entidad2 = new Organizacion("Entidad 2", null, null);
    ServicioAsociado servicioAsociado2 = new ServicioAsociado(entidad2, null, null);
    Incidente incidente2 = new Incidente(servicioAsociado2, null, LocalDateTime.now().minusHours(2));
    incidente2.setFechaResolucion(LocalDateTime.now());

    List<Incidente> incidentes = new ArrayList<>(List.of(incidente1, incidente2));

    LinkedHashMap<Entidad, Double> promedioPorEntidad = mayorPromedioDeCierre.calcularPromedioPorEntidad(incidentes);

    //verifica que el mapa de promedios no esta vacio
    Assertions.assertFalse(promedioPorEntidad.isEmpty());

    //verifico el promedio de los tiempos
    Assertions.assertEquals(60, promedioPorEntidad.get(entidad1));
    Assertions.assertEquals(120, promedioPorEntidad.get(entidad2));


    //si ahora agrego un incidente con la entidad 1 que se resolvio en 5 horas,
    // el promedio de la entidad 1 deberias ser 180 minutos
    Incidente incidente3 = new Incidente(servicioAsociado, null, LocalDateTime.now().minusHours(5));
    incidente3.setFechaResolucion(LocalDateTime.now());
    incidentes.add(incidente3);

    promedioPorEntidad = mayorPromedioDeCierre.calcularPromedioPorEntidad(incidentes);

    Assertions.assertEquals(180, promedioPorEntidad.get(entidad1));
    Assertions.assertEquals(120, promedioPorEntidad.get(entidad2));

  }

  @Test
  @DisplayName("Dado un map de entidad-incidente, se ordena por mayor tiempo de cierre")
  public void testOrdenarIncidentesPorTiempoDeCierre() throws IOException {
    Entidad entidad1 = new Organizacion("Entidad 1", null, null);
    ServicioAsociado servicioAsociado = new ServicioAsociado(entidad1, null, null);
    Incidente incidente1 = new Incidente(servicioAsociado, null, LocalDateTime.now().minusHours(1));
    incidente1.setFechaResolucion(LocalDateTime.now());

    Entidad entidad2 = new Organizacion("Entidad 2", null, null);
    ServicioAsociado servicioAsociado2 = new ServicioAsociado(entidad2, null, null);
    Incidente incidente2 = new Incidente(servicioAsociado2, null, LocalDateTime.now().minusHours(2));
    incidente2.setFechaResolucion(LocalDateTime.now());

    List<Incidente> incidentes = new ArrayList<>(List.of(incidente1, incidente2));

    //verifico que la entidad con mayor promedio es la 2
    LinkedHashMap<Entidad, Double> promedioPorEntidad = mayorPromedioDeCierre.calcularPromedioPorEntidad(incidentes);
    LinkedHashMap<Entidad, Double> incidentesOrdenados = mayorPromedioDeCierre.ordenarEntidadesDescendente(promedioPorEntidad);

    Assertions.assertEquals(120, incidentesOrdenados.entrySet().iterator().next().getValue());
    Assertions.assertEquals(entidad2, incidentesOrdenados.entrySet().iterator().next().getKey());


    Incidente incidente3 = new Incidente(servicioAsociado, null, LocalDateTime.now().minusHours(5));
    incidente3.setFechaResolucion(LocalDateTime.now());

    incidentes.add(incidente3);

    promedioPorEntidad = mayorPromedioDeCierre.calcularPromedioPorEntidad(incidentes);

    //ordeno los incidentes por mayor tiempo de cierre
    incidentesOrdenados = mayorPromedioDeCierre.ordenarEntidadesDescendente(promedioPorEntidad);

    //verifico que la entidad con mayor promedio es la 1
    Assertions.assertEquals(180, incidentesOrdenados.entrySet().iterator().next().getValue());
    Assertions.assertEquals(entidad1, incidentesOrdenados.entrySet().iterator().next().getKey());


  }
}

