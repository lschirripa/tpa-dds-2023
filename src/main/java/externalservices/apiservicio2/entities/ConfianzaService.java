package externalservices.apiservicio2.entities;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ConfianzaService {

  @POST("comunidad/usuarios")
  Call<ResponseData> calcularConfianza(@Body BodyRequest bodyRequest);
}
