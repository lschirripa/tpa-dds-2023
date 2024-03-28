package excepciones;

public class ComunidadInexistenteException extends RuntimeException {
  public ComunidadInexistenteException(String error) {
    super((error));
  }
}
