package externalservices.mensajeria;

import layers.models.domain.Incidente;
import layers.models.domain.Usuario;
import org.apache.pdfbox.pdmodel.PDDocument;

public interface EmailSender {
  void sendEmail(Usuario usuario, Incidente incidente, String mensaje);

  void sendPDFEmail(PDDocument document, String correo);

}
