package externalservices.apiservicio2.entities;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseData {
  @SerializedName("usuarios_output")
  private List<UsuarioOutput> usuariosOutput;

  @SerializedName("nivel_de_confianza")
  private Double nivelDeConfianza;

}
