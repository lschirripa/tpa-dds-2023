package utils.rankingincidente;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import layers.models.domain.Entidad;
import layers.models.domain.IEntidadControlada;
import layers.models.domain.Incidente;
import layers.models.domain.ServicioAsociado;
import layers.models.repositories.ServicioAsociadoRepository;
import layers.models.repositories.ServicioRepository;
import layers.models.repositories.ServicioUsuarioRepository;
import layers.services.ServicioAsociadoService;
import layers.services.ServicioService;

public class MayorCantidadDeIncidentes extends CriterioRanking {

  private ServicioAsociadoService servicioAsociadoService = new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository()));

  public MayorCantidadDeIncidentes(String nombre) {
    super(nombre);
  }


  public List<ServicioAsociado> getServiciosAsociados() {
    return servicioAsociadoService.getAll();
  }

  @Override
  public LinkedHashMap<Entidad, Double> generarRankingDe(IEntidadControlada controlador) {
    System.out.println("Estoy generando el ranking de mayor cantidad");

    LinkedHashMap<Entidad, Double> mayorCantDeInc = generarHashMapDe(controlador);
    LinkedHashMap<Entidad, Double> hashmapOrdenado = ordenarEntidadesDescendente(mayorCantDeInc);
    this.datos = hashmapOrdenado;

    return hashmapOrdenado;
  }

  public int cantidadDeIncidentes(ServicioAsociado servicioAsociado) {
    List<Incidente> incidentes = servicioAsociado.getHistoricoDeIncidentes();
    List<Incidente> incidentesFiltrados = filtrarIncidentes(incidentes);
    return incidentesFiltrados.size();
  }

  private List<Incidente> filtrarIncidentes(List<Incidente> incidentes) {
    List<Incidente> primerFiltro = filterIncidentesSemanales(incidentes);
    List<Incidente> segundoFiltro = filterIncidentesRepetidosEnElDia(primerFiltro);
    return segundoFiltro;
  }


  public List<Incidente> filterIncidentesRepetidosEnElDia(List<Incidente> incidentes) {

    for (Incidente incidente : incidentes) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

      LocalDateTime incidentefechaCreacion = incidente.getFechaCreacion();
      String incidentefechaCreacionFormatted = incidentefechaCreacion.format(formatter);

      Iterator<Incidente> itr2 = incidentes.iterator();
      while (itr2.hasNext()) {
        Incidente incidente2 = itr2.next();
        LocalDateTime incidentefechaCreacion2 = incidente2.getFechaCreacion();
        String incidentefechaCreacionFormatted2 = incidentefechaCreacion2.format(formatter);
        if (incidentefechaCreacionFormatted.equals(incidentefechaCreacionFormatted2)
            && incidente != incidente2) {
          itr2.remove();
        }
      }

    }
    return incidentes;
  }

  public LinkedHashMap<Entidad, Double> generarHashMapDe(IEntidadControlada controlador) {
    LinkedHashMap<Entidad, Double> hashMap = new LinkedHashMap<>();
    List<ServicioAsociado> serviciosAsociados = getServiciosAsociados();

    //obtener los servicios asociados cuyas entidades coincidan con las entidades del controlador
    for (ServicioAsociado servicioAsociado : serviciosAsociados) {
      Entidad entidad = servicioAsociado.getEntidad();
      /*if (controlador.getEntidades().contains(entidad)) {*/
        if (servicioAsociado.cantidadDeIncidentes() != 0) {
          if (hashMap.containsKey(entidad)) {
            hashMap.put(entidad, hashMap.get(entidad) + cantidadDeIncidentes(servicioAsociado));
          } else {
            hashMap.put(entidad, (double) cantidadDeIncidentes(servicioAsociado));
          }

        }
      //}
    }
    return hashMap;
  }

}
