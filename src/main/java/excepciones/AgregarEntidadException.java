package excepciones;

public class AgregarEntidadException extends RuntimeException {
  public AgregarEntidadException(String error) {
    super(error);
  }
}