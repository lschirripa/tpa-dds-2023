package excepciones;

public class AgregarSucursalException extends RuntimeException {
  public AgregarSucursalException(String error) {
    super((error));
  }
}
