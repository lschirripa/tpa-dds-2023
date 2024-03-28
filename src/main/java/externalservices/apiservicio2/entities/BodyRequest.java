package externalservices.apiservicio2.entities;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BodyRequest {
  @SerializedName("usuarios")
  private List<UsuarioAdaptado> usuarios;

  @SerializedName("incidentes")
  private List<IncidenteAdaptado> incidentes;

  public BodyRequest (List<UsuarioAdaptado> usuarios, List<IncidenteAdaptado> incidentes) {
    this.usuarios = usuarios;
    this.incidentes = incidentes;
  }

}
