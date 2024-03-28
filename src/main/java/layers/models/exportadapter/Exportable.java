package layers.models.exportadapter;

import java.util.LinkedHashMap;
import layers.models.domain.Entidad;

// la interfaz exportable va a estar implementada por las clases que quieran exportar sus datos
public interface Exportable {
  public LinkedHashMap<Entidad, Double> exportarDatos();

  public String getNombre();

}