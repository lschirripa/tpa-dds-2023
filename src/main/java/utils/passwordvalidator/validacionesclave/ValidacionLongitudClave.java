package utils.passwordvalidator.validacionesclave;

import utils.passwordvalidator.ValidacionClave;
import excepciones.UsuarioInvalidoException;

public class ValidacionLongitudClave implements ValidacionClave {
  private static final int LARGO_CLAVE_MINIMO = 8;

  @Override
  public void validarClave(String clave) {
    System.out.println("Validando que la clave tenga una longitud minima de 8 caracteres");
    if ((clave.length() < LARGO_CLAVE_MINIMO)) {
      throw new UsuarioInvalidoException(
          "La contraseña debe tener una longitudad minima de ocho caracteres");
    }
    System.out.println("Clave tiene una longitud minima de 8 caracteres");
  }
}
