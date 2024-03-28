package apiServices.georef;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class ObtencionDeLocalizacionTest {

  public static void main(String[] args) {
    System.out.println("Hello world!");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    LocalDateTime time = LocalDateTime.now();
    System.out.println(time);
    System.out.println(time.toString());


  }
/*
  private ObtencionDeLocalizacion obtencionDeLocalizacion;
  private UbicacionGeografica ubicacionGeografica;
  private Ubicacion ubicacion;
  private LocalizacionSchema localizacion;
  private GeorefApiService georefApiService;
  private Departamento departamento;
  private Municipio municipio;
  private Provincia provincia;

  @BeforeEach
  public void init() {
    this.ubicacionGeografica = new UbicacionGeografica(-27.2741, -66.7529);
    this.georefApiService = Mockito.mock();
    this.obtencionDeLocalizacion = Mockito.mock(); //new ObtencionDeLocalizacion();
    this.departamento = new Departamento("villa fiorito");
    this.municipio = new Municipio("lanus");
    this.provincia = new Provincia("cordoba");
    this.ubicacion = new Ubicacion(this.departamento, this.municipio, this.provincia);
    this.localizacion = new LocalizacionSchema(ubicacionGeografica, ubicacion);

  }

  @Test
  @DisplayName("dado una ubicacion se pide solo la provincia")
  public void testDadoUnaUbicacionDevuelveProvincia() throws IOException {

//    Mockito.when(georefApiService.ubicacionALocalizacion(this.ubicacionGeografica.lat, this.ubicacionGeografica.lon))
//            .thenReturn(localizacion);

    Mockito.when(obtencionDeLocalizacion.getLocalizacionByUbicacionFromApi(this.ubicacionGeografica, NivelDeLocalizacion.PROVINCIA))
        .thenReturn(provincia);

    final LocalizacionTipo resultado = this.obtencionDeLocalizacion.getLocalizacionByUbicacionFromApi(ubicacionGeografica, NivelDeLocalizacion.PROVINCIA);

    Assertions.assertEquals(ubicacion.provincia.getNombre(), resultado.getNombre());

  }


  @Test
  @DisplayName("dado una ubicacion se pide solo el municipio")
  public void testDadoUnaUbicacionDevuelveMunicipio() throws IOException {

//    Mockito.when(georefApiService.ubicacionALocalizacion(this.ubicacionGeografica.lat, this.ubicacionGeografica.lon))
//        .thenReturn(localizacion);

    Mockito.when(obtencionDeLocalizacion.getLocalizacionByUbicacionFromApi(this.ubicacionGeografica, NivelDeLocalizacion.MUNICIPIO))
        .thenReturn(municipio);

    final LocalizacionTipo resultado = this.obtencionDeLocalizacion.getLocalizacionByUbicacionFromApi(ubicacionGeografica, NivelDeLocalizacion.MUNICIPIO);

    Assertions.assertEquals("lanus", resultado.getNombre());

  }


  @Test
  @DisplayName("dado una ubicacion se pide solo el departamento")
  public void testDadoUnaUbicacionDevuelveDepartamento() throws IOException {

//    Mockito.when(georefApiService.ubicacionALocalizacion(this.ubicacionGeografica.lat, this.ubicacionGeografica.lon))
//        .thenReturn(localizacion);

    Mockito.when(obtencionDeLocalizacion.getLocalizacionByUbicacionFromApi(this.ubicacionGeografica, NivelDeLocalizacion.DEPARTAMENTO))
        .thenReturn(departamento);

    final LocalizacionTipo resultado = this.obtencionDeLocalizacion.getLocalizacionByUbicacionFromApi(ubicacionGeografica, NivelDeLocalizacion.DEPARTAMENTO);

    Assertions.assertEquals("villa fiorito", resultado.getNombre());

  }


 */
}
