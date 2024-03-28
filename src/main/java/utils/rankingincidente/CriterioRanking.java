package utils.rankingincidente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import layers.models.exportadapter.Exportable;
import layers.models.domain.Entidad;
import layers.models.domain.IEntidadControlada;
import layers.models.domain.Incidente;
import lombok.Getter;

public abstract class CriterioRanking implements Exportable {

  @Getter
  protected String nombre;
  protected LinkedHashMap<Entidad, Double> datos;

  public CriterioRanking(String nombre) {
    this.nombre = nombre;
  }

  public LinkedHashMap<Entidad, Double> exportarDatos() {
    return this.datos;
  }

  public abstract LinkedHashMap<Entidad, Double> generarRankingDe(IEntidadControlada controlador);

  public List<Incidente> filterIncidentesSemanales(List<Incidente> incidentes) {
    //como este metodo se va a llamar el lunes a las 00, directamente busco 7 dias atras
    LocalDate hoy = LocalDate.now();
    LocalDate sieteDiasAtras = hoy.minusDays(7);
    LocalDateTime sieteDiasAtrasInicio = sieteDiasAtras.atStartOfDay();

    List<Incidente> incidentesSemanales = new ArrayList<>();

    for (Incidente incidente : incidentes) {
      LocalDateTime fechaCreacion = incidente.getFechaCreacion();

      if (incidente.getFechaResolucion() != null
          && fechaCreacion.isAfter(sieteDiasAtrasInicio)) {
        incidentesSemanales.add(incidente);
      }
    }

    return incidentesSemanales;
  }

  public LinkedHashMap<Entidad, Double> ordenarEntidadesDescendente(LinkedHashMap<Entidad,
      Double> originalMap) {
    List<Map.Entry<Entidad, Double>> list = new ArrayList<>(originalMap.entrySet());

    list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

    LinkedHashMap<Entidad, Double> sortedMap = new LinkedHashMap<>();
    for (Map.Entry<Entidad, Double> entry : list) {
      sortedMap.put(entry.getKey(), entry.getValue());
    }

    return sortedMap;
  }
}
