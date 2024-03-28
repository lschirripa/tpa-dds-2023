package externalservices.apigeoref.georef.entities;

import layers.models.domain.UbicacionGeografica;

public class LocalizacionSchema {
  public UbicacionGeografica parametros;
  public Ubicacion ubicacion;

  public LocalizacionSchema(UbicacionGeografica parametros, Ubicacion ubicacion) {
    this.parametros = parametros;
    this.ubicacion = ubicacion;
  }
}

