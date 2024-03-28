package externalservices.apigeoref.georef;

import externalservices.apigeoref.georef.entities.LocalizacionSchema;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GeorefApiService {
  private static final String urlAPI = "https://apis.datos.gob.ar/georef/api/";
  private static GeorefApiService instancia = null;
  private Retrofit retrofit;

  private GeorefApiService() {
    this.retrofit = new Retrofit.Builder()
        .baseUrl(urlAPI)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  public static GeorefApiService getInstancia() {
    if (instancia == null) {
      instancia = new GeorefApiService();
    }
    return instancia;
  }

  public LocalizacionSchema ubicacionALocalizacion(double latitud,
                                                   double longitud) throws IOException {
    GeorefService georefService = this.retrofit.create(GeorefService.class);
    Call<LocalizacionSchema> requestLocalizacion = georefService.localizacion(latitud, longitud);
    Response<LocalizacionSchema> response = requestLocalizacion.execute();
    return response.body();
  }
}
