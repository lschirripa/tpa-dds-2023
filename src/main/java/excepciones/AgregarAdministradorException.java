package excepciones;

public class AgregarAdministradorException extends RuntimeException {
  public AgregarAdministradorException(String error) {
    super((error));
  }
}
