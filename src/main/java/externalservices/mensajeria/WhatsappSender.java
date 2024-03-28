package externalservices.mensajeria;

import layers.models.domain.Incidente;
import layers.models.domain.Usuario;

public interface WhatsappSender {
  void sendWhatsapp(Usuario usuario, Incidente incidente, String mensaje);

}
