package excepciones;

public class EliminarEntidadException extends RuntimeException {
  public EliminarEntidadException(String error) {
    super(error);
  }
}