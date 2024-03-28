package utils.rankingincidente;

import java.util.*;
import layers.models.domain.Entidad;
import layers.models.domain.IEntidadControlada;
import layers.models.domain.Incidente;
import layers.models.repositories.IncidenteRepository;
import layers.models.repositories.ServicioAsociadoRepository;
import layers.models.repositories.ServicioRepository;
import layers.models.repositories.ServicioUsuarioRepository;
import layers.models.repositories.UsuarioRepository;
import layers.services.IncidenteService;
import layers.services.ServicioAsociadoService;
import layers.services.ServicioService;
import layers.services.UsuarioService;
import lombok.Getter;

@Getter
public class MayorPromedioDeCierre extends CriterioRanking {

  private IncidenteService incidenteService = new IncidenteService(new IncidenteRepository(), new UsuarioService(new UsuarioRepository(), new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository()))));

  public MayorPromedioDeCierre(String nombre) {
    super(nombre);
  }


  @Override
  public LinkedHashMap<Entidad, Double> generarRankingDe(IEntidadControlada controlador) {
    List<Incidente> incidentes = incidenteService.getAll();
    System.out.println("El controlador tiene n entidades:");
    System.out.println(controlador.getEntidades().size());
    System.out.println("La cantidad de incidentes es");
    System.out.println(incidentes.size());
    //nos quedamos con los incidentes cerrados
    incidentes = filterIncidentesCerrados(incidentes);

    //List<Incidente> incidentesControlador = getIncidentesDe(controlador, incidentes);

    //return generarRanking(incidentesControlador);
    return generarRanking(incidentes);
  }

  private List<Incidente> filterIncidentesCerrados(List<Incidente> incidentes) {
    List<Incidente> incidentesCerrados = new ArrayList<>();

    for (Incidente incidente : incidentes) {

      if (incidente.getFechaResolucion() != null) {
        incidentesCerrados.add(incidente);
      }
    }

    return incidentesCerrados;
  }

  public LinkedHashMap<Entidad, Double> generarRanking(List<Incidente> incidentes) {
    System.out.println("Estoy generando el ranking de mayor PROMEDIO");
    System.out.println("La cantidad de incidentes cerrados es");
    System.out.println(incidentes.size());
    List<Incidente> incidentesSemanales = filterIncidentesSemanales(incidentes);

    LinkedHashMap<Entidad, Double> promedioPorEntidad
        = calcularPromedioPorEntidad(incidentesSemanales);

    LinkedHashMap<Entidad, Double> rankingSorted = ordenarEntidadesDescendente(promedioPorEntidad);
    this.datos = rankingSorted;

    return rankingSorted;
  }

  public List<Incidente> getIncidentesDe(IEntidadControlada controlador,
                                         List<Incidente> incidentes) {
    List<Incidente> incidentesControlador = new ArrayList<>();

    for (Entidad entidad : controlador.getEntidades()) {
      for (Incidente incidente : incidentes) {
        if (incidente.getServicioIncidentado().getEntidad().equals(entidad)) {
          incidentesControlador.add(incidente);
        }
      }
    }
    return incidentesControlador;
  }

  public LinkedHashMap<Entidad, Double> calcularPromedioPorEntidad(List<Incidente> incidentes) {
    LinkedHashMap<Entidad, Double> promedioPorEntidad = new LinkedHashMap<>();

    for (Incidente incidente : incidentes) {
      Entidad entidad = incidente.getServicioIncidentado().getEntidad();
      double tiempoCierre = incidente.tiempoDeCierre().toMinutes();

      promedioPorEntidad.merge(entidad, tiempoCierre, (oldValue, newValue) ->
          (oldValue + newValue) / 2.0); // Calcular el nuevo promedio
    }

    return promedioPorEntidad;
  }

}
