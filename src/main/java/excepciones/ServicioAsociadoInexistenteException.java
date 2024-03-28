package excepciones;

public class ServicioAsociadoInexistenteException extends RuntimeException {
  public ServicioAsociadoInexistenteException(String message) {
    super(message);
  }
}
