package externalservices.apigeoref.georef.entities;

import java.util.ArrayList;
import layers.models.domain.UbicacionGeografica;

public class ListadoDeProvincias {
  public int cantidad;
  public int inicio;
  public int total;
  public UbicacionGeografica parametros;
  public ArrayList<ProvinciaEjemplo> provinciaEjemplos;
}
