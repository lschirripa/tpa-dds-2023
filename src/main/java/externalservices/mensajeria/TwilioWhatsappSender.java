package externalservices.mensajeria;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import layers.models.domain.Incidente;
import layers.models.domain.Usuario;

public class TwilioWhatsappSender implements WhatsappSender {
  // Find your Account SID and Auth Token at twilio.com/console
  // and set the environment variables. See http://twil.io/secure

  // RECORDAR: hay que mandar un wpp: join <sandbox name> para volver a conectarse
  public static final String ACCOUNT_SID_JUANI = "AC50430ad2e68b75b5e52d816acf162ab2";
  public static final String ACCOUNT_SID_FRAN = "AC6b6ed83cd1e1c92c67733ee0d7d7781b";
  public static final String AUTH_TOKEN_FRAN = "b0e3d8d82e37c35342c185a5e3945f70";
  public static final String AUTH_TOKEN_JUANI = "ad9d7ff9f4cc1ffe7bee234f9cb23575";


  @Override
  public void sendWhatsapp(Usuario usuario, Incidente incidente, String mensaje) {

    Twilio.init(ACCOUNT_SID_FRAN, AUTH_TOKEN_FRAN);
    Message message = Message.creator(
            new com.twilio.type.PhoneNumber("whatsapp:+549" + usuario.getDatosPersonales().getTelefono()),
            new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
            "Estimadx " + usuario.getDatosPersonales().getNombre() + ",\n\n"
                + mensaje + "\n\n")
        .create();

    System.out.println("\nEnviando whatsapp....");
    System.out.println(message.getSid());
  }
}
