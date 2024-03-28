package externalservices.apiservicio2.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioOutput {
  @SerializedName("id")
  private int id;

  @SerializedName("puntaje_inicial")
  private Double puntajeInicial;

  @SerializedName("puntaje_final")
  private Double puntajeFinal;

  @SerializedName("nivel_de_confianza")
  private String nivelDeConfianza;

  @SerializedName("recomendacion")
  private String recomendacion;

  // Constructor, getters y setters
}