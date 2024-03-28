package externalservices.apigeoref.georef.entities;

import layers.models.domain.Departamento;
import layers.models.domain.Municipio;
import layers.models.domain.Provincia;

public class Ubicacion {

  public Departamento departamento;
  public Municipio municipio;
  public Provincia provincia;

  public Ubicacion(Departamento departamento, Municipio municipio, Provincia provincia) {
    this.departamento = departamento;
    this.municipio = municipio;
    this.provincia = provincia;
  }
}
