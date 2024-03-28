package externalservices.apiservicio1.servicio1;

import com.google.gson.Gson;
import externalservices.apiservicio1.servicio1.entities.MergedCommunitySchema;
import externalservices.apiservicio1.servicio1.entities.PosibleCombinacion;
import externalservices.apiservicio1.servicio1.entities.PossibleMergesSchema;
import externalservices.apiservicio1.servicio1.entities.entidadesadaptadas.ComunidadAdaptada;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FusionApiService {
  private static final String URL = "https://service-01-merge-community-utn-production.up.railway.app/api/";
  private static FusionApiService instance = null;
  private Retrofit retrofit;

  private FusionApiService() {
    System.out.println("se crea el servicio de fusion");
    this.retrofit = new Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  public static FusionApiService getInstance() {
    if (instance == null) {
      instance = new FusionApiService();
    }
    return instance;
  }

  public PossibleMergesSchema postRecomendacionDeFusion(List<ComunidadAdaptada> comunidades)
      throws IOException {

    System.out.println("***********");
    System.out.println("Convirtiendo la comunidad a Json e imprimirlo");
    System.out.println("***********");

    Gson gson = new Gson();
    String json = gson.toJson(comunidades);
    System.out.println(json);

    FusionService fusionService = this.retrofit.create(FusionService.class);

    Call<PossibleMergesSchema> requestRecomendacionDeFusion = fusionService
        .recomendacionDeFusion(comunidades);

    Response<PossibleMergesSchema> response = requestRecomendacionDeFusion.execute();

    return response.body();
  }

  public MergedCommunitySchema postComunidadesFusionadas(PosibleCombinacion comunidadesAUnir)
      throws IOException {

    FusionService fusionService = this.retrofit.create(FusionService.class);
    System.out.println("Se realiza el llamado, usando el retrofit de fusion");
    Call<MergedCommunitySchema> requestComunidadesFusionadas = fusionService
        .fusionarComunidades(comunidadesAUnir);

    Response<MergedCommunitySchema> response = requestComunidadesFusionadas.execute();

    return response.body();
  }
}
