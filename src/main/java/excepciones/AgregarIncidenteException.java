package excepciones;

public class AgregarIncidenteException extends RuntimeException {
  public AgregarIncidenteException(String error) {
    super((error));
  }
}
