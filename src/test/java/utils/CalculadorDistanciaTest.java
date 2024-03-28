package utils;

import layers.models.domain.UbicacionGeografica;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CalculadorDistanciaTest {

  @Test
  @DisplayName("La distancia entre dos puntos concretos es la esperada (877.4633259175431 km)")
  public void calcularDistanciaTest() {
    CalculadorDistancia calculadorDistancia = new CalculadorDistancia();
    UbicacionGeografica ubi1 = new UbicacionGeografica(52.5200, 13.4050);
    UbicacionGeografica ubi2 = new UbicacionGeografica(48.8566, 2.3522);

    Assertions.assertEquals(877.4633259175431, calculadorDistancia.calcularDistancia(ubi1, ubi2));
  }
}
