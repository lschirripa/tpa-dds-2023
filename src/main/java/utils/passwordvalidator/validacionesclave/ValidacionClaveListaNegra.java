package utils.passwordvalidator.validacionesclave;

import utils.passwordvalidator.ValidacionClave;
import excepciones.UsuarioInvalidoException;
import utils.ReadTextAsString;

public class ValidacionClaveListaNegra implements ValidacionClave {
  @Override
  public void validarClave(String clave) {
    System.out.println("Validando que la clave no este en la lista negra");
    try {
      if (ReadTextAsString.readFileAsString(
          "../resources/public/resources/top_10000_worst_passwords.txt").contains(clave)) {
        System.out.println("Clave esta en la lista negra");
        throw new UsuarioInvalidoException(
            "Eligio una de las contraseñas mas usadas. Por favor ingrese otra");
      }
      System.out.println("Clave no esta en la lista negra");
    } catch (Exception e) {
      System.out.println("Error al leer el archivo de la lista negra");
      System.out.println(e.getMessage());
    }
  }
}
