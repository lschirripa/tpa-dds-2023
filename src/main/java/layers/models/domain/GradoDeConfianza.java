package layers.models.domain;

public enum GradoDeConfianza {
  NO_CONFIABLE("No Confiable"),
  CON_RESERVAS("Con Reservas"),
  CONFIABLE_NIVEL_1("Confiable Nivel 1"),
  CONFIABLE_NIVEL_2("Confiable Nivel 2");

  private final String descripcion;

  GradoDeConfianza(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public static GradoDeConfianza fromString(String texto) {
    for (GradoDeConfianza grado : GradoDeConfianza.values()) {
      if (grado.descripcion.equalsIgnoreCase(texto)) {
        return grado;
      }
    }
    throw new IllegalArgumentException("No se pudo encontrar un GradoDeConfianza para la descripción: " + texto);
  }
}
