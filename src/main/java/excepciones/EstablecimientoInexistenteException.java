package excepciones;

public class EstablecimientoInexistenteException extends RuntimeException {
  public EstablecimientoInexistenteException(String error) {
    super((error));
  }
 
}
