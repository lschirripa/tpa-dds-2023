package excepciones;

public class AgregarServicioException extends RuntimeException {
  public AgregarServicioException(String error) {
    super((error));
  }
}
