package apiServices.servicio2;

import externalservices.CalculadorDeConfianza;
import externalservices.apiservicio2.entities.ResponseData;
import java.io.IOException;
import org.junit.jupiter.api.*;
import persistence.PersistenciaIntegralTest;
import retrofit2.Response;

public class CalculadorConfianzaTest {
  CalculadorDeConfianza calculadorDeConfianza = new CalculadorDeConfianza();
  Response<ResponseData> response;

  @BeforeEach
  public void setUp() throws IOException {
    PersistenciaIntegralTest persistenciaIntegralTest = new PersistenciaIntegralTest();
    persistenciaIntegralTest.context();
  }

  @Test
  public void calcularConfianzaTest() throws IOException {
    response = calculadorDeConfianza.calcularConfianza();
    Assertions.assertNotNull(200, String.valueOf(response.code()));
  }

}
