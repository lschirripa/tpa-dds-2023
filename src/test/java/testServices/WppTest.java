package testServices;

import externalservices.mensajeria.TwilioWhatsappSender;
import externalservices.mensajeria.WhatsappSender;
import java.io.IOException;
import layers.models.domain.DatosPersonalesUsuario;
import layers.models.domain.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.enums.Rol;

public class WppTest {
  @Test
  @DisplayName("se envia 1 wpp a juani")
  public void MethodMailTest() throws IOException {

    DatosPersonalesUsuario datosFran = new DatosPersonalesUsuario("fran", "mav", "franciscomaver.fm@gmail.com", "1138718498");
    DatosPersonalesUsuario datosJuan = new DatosPersonalesUsuario("juan", "cobas", "cobasjuanignaciofm@gmail.com", "1136430993");
    DatosPersonalesUsuario datosLucho = new DatosPersonalesUsuario("Lucho", "Schirri", "lucho@gmail.com", "1155239730");

    Usuario fran = new Usuario("franmav", "contradificil22", datosFran, null, Rol.USUARIO);
    Usuario juani = new Usuario("juancai", "contradificil22", datosJuan, null, Rol.USUARIO);
    Usuario lucho = new Usuario("luchokpo", "contradificil123", datosLucho, null, Rol.USUARIO);

    WhatsappSender twilioSender = new TwilioWhatsappSender();
    twilioSender.sendWhatsapp(fran, null, "Hola Fran");
    twilioSender.sendWhatsapp(juani, null, "Hola Juan");
    twilioSender.sendWhatsapp(lucho, null, "Hola Lucho");


  }
}
