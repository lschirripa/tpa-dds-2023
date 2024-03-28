package excepciones;

public class AgregarMiembroException extends RuntimeException {
  public AgregarMiembroException(String error) {
    super(error);
  }
}
