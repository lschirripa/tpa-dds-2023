package externalservices.apiservicio2.entities;

import com.google.gson.Gson;
import externalservices.apiservicio1.servicio1.FusionApiService;
import externalservices.apiservicio1.servicio1.FusionService;
import externalservices.apiservicio1.servicio1.entities.MergedCommunitySchema;
import externalservices.apiservicio1.servicio1.entities.PosibleCombinacion;
import externalservices.apiservicio1.servicio1.entities.PossibleMergesSchema;
import externalservices.apiservicio1.servicio1.entities.entidadesadaptadas.ComunidadAdaptada;
import java.io.IOException;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfianzaApiService {
  private static final String URL = "http://localhost:8000/";
  private static ConfianzaApiService instance = null;
  private Retrofit retrofit;

  private ConfianzaApiService() {
    System.out.println("se crea el servicio de confianza");
    this.retrofit = new Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  public static ConfianzaApiService getInstance() {
    if (instance == null) {
      instance = new ConfianzaApiService();
    }
    return instance;
  }

  public Response<ResponseData> postConfianza(BodyRequest bodyRequest)
      throws IOException {


    Gson gson = new Gson();
    String json = gson.toJson(bodyRequest);

    System.out.println("******my body request*****");
    System.out.println(json);

    ConfianzaService confianzaService = this.retrofit.create(ConfianzaService.class);

    Call<ResponseData> requestConfianza = confianzaService
        .calcularConfianza(bodyRequest);

    Response<ResponseData> response = requestConfianza.execute();

    System.out.println("******my response*****");
    System.out.println(response.body().getUsuariosOutput().get(0).getRecomendacion());
    System.out.println(response.body().getNivelDeConfianza());

    return response;
  }

}
