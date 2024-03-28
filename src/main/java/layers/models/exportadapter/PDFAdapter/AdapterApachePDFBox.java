package layers.models.exportadapter.PDFAdapter;

// aca se adaptan las funciones de la libreria ApachePDFBox(mi servicio) a mis funciones, para que puedan ser usadas por el sistema

import com.twilio.rest.bulkexports.v1.Export;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import layers.models.exportadapter.Exportable;
import layers.models.domain.Entidad;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.IOException;
import java.util.Map;

public class AdapterApachePDFBox implements AdapterExportadorAPDF {

  public AdapterApachePDFBox() {
  }

  public PDDocument exportar(Exportable exportable, String nombreDeArchivo) {
    PDDocument doc = new PDDocument();
    PDPage myPage = new PDPage();
    doc.addPage(myPage);
    try {
      PDPageContentStream cont = new PDPageContentStream(doc, myPage);
      cont.beginText();
      cont.setFont(PDType1Font.HELVETICA, 23);
      cont.setLeading(29f);
      cont.newLineAtOffset(25, 700);

      agregarDatos(cont, exportable, exportable.exportarDatos(), nombreDeArchivo);

      cont.endText();
      cont.close();
      doc.save(rutaCompletaDelArchivo(nombreDeArchivo));
      doc.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return doc;
  }

  public PDDocument exportarPorMail(Exportable exportable, String nombreDeArchivo) {
    PDDocument doc = new PDDocument();
    PDPage myPage = new PDPage();
    doc.addPage(myPage);
    try {
      PDPageContentStream cont = new PDPageContentStream(doc, myPage);
      cont.beginText();
      cont.setFont(PDType1Font.HELVETICA, 23);
      cont.setLeading(29f);
      cont.newLineAtOffset(25, 700);

      agregarDatos(cont, exportable, exportable.exportarDatos(), nombreDeArchivo);

      cont.endText();
      cont.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return doc;
  }

  public String rutaCompletaDelArchivo(String nombreDeArchivo) {

    String path = System.getProperty("user.dir") + "\\src\\main\\resources\\public\\resources\\" + nombreDeArchivo + ".pdf";
    System.out.println(path);
    return path;
  }

  public void agregarDatos(PDPageContentStream pagina, Exportable exportable, LinkedHashMap<Entidad, Double> datos, String nombreDeArchivo) throws IOException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    System.out.printf("Estoy agregando datos");
    String formattedFecha = LocalDateTime.now().format(formatter);
    pagina.showText("Fecha: " + formattedFecha);
    pagina.newLine();
    System.out.printf("El nombre del exportable es:");
    System.out.printf(exportable.getNombre());
    if (exportable.getNombre() == "mayorPromedioDeCierre") {
      System.out.printf("Entre a mayorpromedio");
      pagina.showText("RANKING SEMANAL DE ENTIDADES");
      pagina.newLine();
      pagina.showText("Entidades con mayor promedio de tiempo de cierre");
      pagina.newLine();
      pagina.showText("de incidentes.");
      pagina.newLine();
      System.out.println("EL SIZE ES");
      System.out.println(datos.size());
      for (Map.Entry<Entidad, Double> entry : datos.entrySet()) {
        System.out.println("Entre al for");
        pagina.newLine();
        String valorCasteado = String.valueOf(entry.getValue().intValue());
        System.out.println("La entidad de cantidad es:");
        System.out.printf(entry.getKey().getNombre());
        System.out.println("El valor casteado de cantidad es:");
        System.out.printf(valorCasteado);
        String datosPorEntidad = String.join(" : ", entry.getKey().getNombre(), valorCasteado + " minutos");
        System.out.printf("EL RESULT ES");
        System.out.printf(datosPorEntidad);
        pagina.showText(datosPorEntidad);
      }
    } else if (exportable.getNombre() == "mayorCantidadDeIncidentes") {
      System.out.printf("Entre a mayorcantidadad");
      pagina.showText("RANKING SEMANAL DE ENTIDADES");
      pagina.newLine();
      pagina.showText("Entidades con mayor cantidad de incidentes reportados");
      pagina.newLine();
      pagina.showText("en la semana.");
      pagina.newLine();
      System.out.println("EL SIZE ES");
      System.out.println(datos.size());
      for (Map.Entry<Entidad, Double> entry : datos.entrySet()) {
        System.out.println("Entre al for");
        pagina.newLine();
        String valorCasteado = String.valueOf(entry.getValue().intValue());
        System.out.println("La entidad de cantidad es:");
        System.out.printf(entry.getKey().getNombre());
        System.out.println("El valor casteado de cantidad es:");
        System.out.printf(valorCasteado);
        String datosPorEntidad = String.join(" : ", entry.getKey().getNombre(), valorCasteado + " incidentes");
        System.out.printf("EL RESULT ES");
        System.out.printf(datosPorEntidad);
        pagina.showText(datosPorEntidad);
      }
    }
  }

}