package externalservices.apiservicio1.servicio1;

import externalservices.apiservicio1.servicio1.entities.MergedCommunitySchema;
import externalservices.apiservicio1.servicio1.entities.PosibleCombinacion;
import externalservices.apiservicio1.servicio1.entities.PossibleMergesSchema;
import externalservices.apiservicio1.servicio1.entities.entidadesadaptadas.ComunidadAdaptada;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FusionService {

  @POST("fusion")
  Call<MergedCommunitySchema> fusionarComunidades(@Body PosibleCombinacion comunidades);

  @POST("recommendations")
  Call<PossibleMergesSchema> recomendacionDeFusion(@Body List<ComunidadAdaptada> comunidades);

}
