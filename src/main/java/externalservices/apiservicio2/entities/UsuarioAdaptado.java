package externalservices.apiservicio2.entities;

import com.google.gson.annotations.SerializedName;

public class UsuarioAdaptado {
  @SerializedName("id")
  private int id;

  @SerializedName("puntaje_inicial")
  private double puntajeInicial;

  public UsuarioAdaptado (int id, double puntajeInicial) {
    this.id = id;
    this.puntajeInicial = puntajeInicial;
  }

}
