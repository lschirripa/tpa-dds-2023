package apiServices.georef;

import externalservices.apigeoref.ObtencionDeLocalizacion;
import layers.models.domain.Localizacion;
import layers.models.domain.UbicacionGeografica;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ObtenerLocalizacionSinMockTest {
  private ObtencionDeLocalizacion obtencionDeLocalizacion;
  private UbicacionGeografica ubicacionGeografica;
  private Localizacion localizacion;

  @BeforeEach
  public void init() {
    obtencionDeLocalizacion = ObtencionDeLocalizacion.getInstance();
    ubicacionGeografica = new UbicacionGeografica(-26.8753965086829, -54.6516966230371);

  }

  @Test
  @DisplayName("Dado una ubicacion se obtiene una localizacion")
  public void testDadoUnaUbicacionDevuelveLocalizacion() throws Exception {
    localizacion = obtencionDeLocalizacion.getLocalizacionByUbicacion(ubicacionGeografica);

    Assertions.assertEquals("Misiones", localizacion.getProvincia());
    Assertions.assertEquals("Montecarlo", localizacion.getDepartamento());
    Assertions.assertEquals("Caraguatay", localizacion.getMunicipio());
  }
}
