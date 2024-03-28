package utils;

import layers.models.domain.UbicacionGeografica;

public class CalculadorDistancia {

  public static final double RADIO_TIERRA_KM = 6371.0; // Radio de la Tierra en kilómetros

  public double calcularDistancia(UbicacionGeografica ubicacion1, UbicacionGeografica ubicacion2) {
    double latitudRad1 = Math.toRadians(ubicacion1.getLat());
    double latitudRad2 = Math.toRadians(ubicacion2.getLat());
    double diferenciaLatitudRad = Math.toRadians(ubicacion2.getLat() - ubicacion1.getLat());
    double diferenciaLongitudRad = Math.toRadians(ubicacion2.getLon() - ubicacion1.getLon());

    double a = Math.sin(diferenciaLatitudRad / 2) * Math.sin(diferenciaLatitudRad / 2)
        + Math.cos(latitudRad1) * Math.cos(latitudRad2)
        * Math.sin(diferenciaLongitudRad / 2) * Math.sin(diferenciaLongitudRad / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return RADIO_TIERRA_KM * c;
  }
}
