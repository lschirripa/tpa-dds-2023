package externalservices.mensajeria;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import javax.mail.util.ByteArrayDataSource;
import layers.models.domain.Incidente;
import layers.models.domain.Usuario;
import org.apache.pdfbox.pdmodel.PDDocument;

public class JavaxMailSender implements EmailSender {


  @Override
  public void sendEmail(Usuario usuario, Incidente incidente, String mensaje) {
    // Datos de configuración para el servidor de correo
    String host = "smtp.gmail.com"; // Reemplaza esto con el servidor SMTP que estés usando
    int port = 587; // Reemplaza esto con el puerto del servidor SMTP
    String username = "notificacionesdds8@gmail.com";//"franciscomaver.fm@gmail.com";// // Reemplaza esto con tu dirección de correo
    String password = "vabd exkm sofz dacp";//"xxtkjzhuqjhtalri";// // Reemplaza esto con tu contraseña

    // Configuración de las propiedades del correo
    Properties props = new Properties();
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", port);
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");

    // Configuración de protocolos y cifrados
    props.put("mail.smtp.ssl.protocols", "TLSv1.2");
    props.put("mail.smtp.ssl.ciphersuites", "TLS_RSA_WITH_AES_128_CBC_SHA");

    // Autenticación para el servidor de correo
    Authenticator authenticator = new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    };

    // Crear una sesión de correo con la autenticación
    Session session = Session.getInstance(props, authenticator);

    try {
      // Crear el mensaje de correo
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));
      message.setRecipient(Message.RecipientType.TO,
          new InternetAddress(usuario.getDatosPersonales().getCorreo()));

      message.setSubject("Notificación de incidente");

      message.setText("Estimadx " + usuario.getDatosPersonales().getNombre() + ",\n\n"
          + mensaje + "\n\n");

      // Enviar el mensaje
      Transport.send(message);
      System.out.println("Correo enviado con éxito.");
    } catch (MessagingException e) {
      e.printStackTrace();
      System.out.println("Error al enviar el correo: " + e.getMessage());
    }
  }

  public void sendPDFEmail(PDDocument document, String correo) {

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try {
      document.save(byteArrayOutputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    byte[] pdfBytes = byteArrayOutputStream.toByteArray();


    String host = "smtp.gmail.com";
    int port = 587;
    String username = "notificacionesdds8@gmail.com";//"franciscomaver.fm@gmail.com";//seapruebacon8
    String password = "vabd exkm sofz dacp";//"xxtkjzhuqjhtalri";//

    Properties props = new Properties();
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", port);
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");

    Authenticator authenticator = new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    };

    Session session = Session.getInstance(props, authenticator);

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));
      message.setRecipient(Message.RecipientType.TO,
          new InternetAddress(correo));

      message.setSubject("REPORTE DE INCIDENTES");

      // Create the multipart message
      Multipart multipart = new MimeMultipart();
      MimeBodyPart attachmentPart = new MimeBodyPart();

      DataSource source = new ByteArrayDataSource(pdfBytes, "application/pdf");

      attachmentPart.setDataHandler(new DataHandler(source));

      attachmentPart.setFileName("attachment.pdf");
//      attachmentPart.setContent(pdfBytes, "application/pdf");

      multipart.addBodyPart(attachmentPart);

      MimeBodyPart textPart = new MimeBodyPart();
      textPart.setText("REPORTE DE INCIDENTES A LA FECHA DE " + LocalDate.now());
      multipart.addBodyPart(textPart);

      message.setContent(multipart);

      Transport.send(message);

      try {
        document.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

      System.out.println("Correo PDF enviado con éxito.");
    } catch (MessagingException e) {
      e.printStackTrace();
      System.out.println("Error al enviar el correo: " + e.getMessage());
    }

  }
}
