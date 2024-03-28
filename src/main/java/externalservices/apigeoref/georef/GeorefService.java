package externalservices.apigeoref.georef;
import externalservices.apigeoref.georef.entities.ListadoDeProvincias;
import externalservices.apigeoref.georef.entities.LocalizacionSchema;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeorefService {
  @GET("provincias")
  Call<ListadoDeProvincias> provincias();

  @GET("provincias")
  Call<ListadoDeProvincias> provincias(@Query("campos") String campos);

  @GET("ubicacion")
  Call<LocalizacionSchema> localizacion(@Query("lat") double latitud, @Query("lon") double longitud);
}