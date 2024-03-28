package utils.passwordvalidator;

import utils.passwordvalidator.validacionesclave.ValidacionClaveListaNegra;
import utils.passwordvalidator.validacionesclave.ValidacionLongitudClave;
import java.util.ArrayList;
import java.util.List;

public class ValidadorRegistro {
  List<ValidacionClave> validadores = new ArrayList<>();

  public ValidadorRegistro() {
    validadores.add(new ValidacionLongitudClave());
    //validadores.add(new ValidacionClaveListaNegra());
  }

  public void validarPassword(String password) {
    validadores.forEach(v -> v.validarClave(password));
  }
}
