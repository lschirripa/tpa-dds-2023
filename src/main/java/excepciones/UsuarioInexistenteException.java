package excepciones;

public class UsuarioInexistenteException extends RuntimeException {
  public UsuarioInexistenteException(String message) {
    super(message);
  }
}
