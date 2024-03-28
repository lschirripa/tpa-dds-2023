package externalservices.apigeoref;

import externalservices.apigeoref.georef.GeorefApiService;
import externalservices.apigeoref.georef.entities.LocalizacionSchema;
import java.io.IOException;
import layers.models.domain.Localizacion;
import layers.models.domain.UbicacionGeografica;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtencionDeLocalizacion {

  private static ObtencionDeLocalizacion instance = null;
  private GeorefApiService georefApiService;

  public ObtencionDeLocalizacion() {
    this.georefApiService = GeorefApiService.getInstancia();
  }

  public static ObtencionDeLocalizacion getInstance() {
    if (instance == null) {
      instance = new ObtencionDeLocalizacion();
    }
    return instance;
  }

  public Localizacion getLocalizacionByUbicacion(UbicacionGeografica ubicacionGeografica) throws IOException {
    LocalizacionSchema localizacion = this.georefApiService.ubicacionALocalizacion(ubicacionGeografica.getLat(), ubicacionGeografica.getLon());

    if (localizacion.ubicacion.provincia.getNombre() == null
        || localizacion.ubicacion.departamento.getNombre() == null
        || localizacion.ubicacion.municipio.getNombre() == null) {
      return null;
    }
    return new Localizacion(
        localizacion.ubicacion.provincia.getNombre(),
        localizacion.ubicacion.departamento.getNombre(),
        localizacion.ubicacion.municipio.getNombre()
    );
  }
}