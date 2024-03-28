package excepciones;

public class AgregarEstacionException extends RuntimeException {
  public AgregarEstacionException(String error) {
    super((error));
  }
}
