package excepciones;

public class IncidenteInexistenteException extends RuntimeException {
  public IncidenteInexistenteException(String message) {
    super(message);
  }
}
