package testServices;

import java.io.IOException;
import java.time.LocalDateTime;
import layers.models.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import externalservices.mensajeria.JavaxMailSender;
import utils.enums.Rol;


public class MailTest {


  @Test
  @DisplayName("se envia 1 mail")
  public void MethodMailTest() throws IOException {

    JavaxMailSender javaxMailSender = new JavaxMailSender();

    DatosPersonalesUsuario datosFran = new DatosPersonalesUsuario("fran", "mav", "franciscomaver.fm@gmail.com", "1138718498");
    DatosPersonalesUsuario datosJuan = new DatosPersonalesUsuario("juan", "cobas", "cobasjuanignacio@gmail.com", "1136430993");
    DatosPersonalesUsuario datosLucho = new DatosPersonalesUsuario("Lucho", "Schirri", "lucho.schirripa@gmail.com", "1155239730");

    Usuario fran = new Usuario("franmav", "contradificil22", datosFran, null, Rol.USUARIO);
    Usuario juani = new Usuario("juancai", "contradificil22", datosJuan, null, Rol.USUARIO);
    Usuario lucho = new Usuario("luchokpo", "contradificil123", datosLucho, null, Rol.USUARIO);

    ServicioAsociado servicioAsociado = new ServicioAsociado(new Organizacion("organizacion1", new UbicacionGeografica(-26.8753965086829, -54.6516966230371), null), new Sucursal("sucursal1", null, new UbicacionGeografica(-26.8753965086829, -54.6516966230371)), new Servicio("servicio1", "descripcion1"));
    Incidente incidente = new Incidente(servicioAsociado, "descripcionServicio", LocalDateTime.now());

    javaxMailSender.sendEmail(fran, incidente, "por favor revisar el incidente en: https://grupo-2-dds-2023.onrender.com/incidentes");
    javaxMailSender.sendEmail(juani, incidente, "por favor revisar el incidente en: https://grupo-2-dds-2023.onrender.com/incidentes");
    javaxMailSender.sendEmail(lucho, incidente, "por favor revisar el incidente en: https://grupo-2-dds-2023.onrender.com/incidentes");
  }
}



