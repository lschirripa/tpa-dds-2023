package adapters.exportAdapter;

import java.util.LinkedHashMap;
import layers.models.exportadapter.Exportable;
import layers.models.exportadapter.PDFAdapter.AdapterApachePDFBox;
import layers.models.domain.Entidad;
import layers.models.domain.Organizacion;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AdapterApachePDFBoxTest {

  private AdapterApachePDFBox pdfAdapter;
  private Exportable testExportable;

  private Entidad entidad1;
  private Entidad entidad2;


  @BeforeEach
  public void setUp() throws IOException {
    pdfAdapter = new AdapterApachePDFBox();

    this.entidad1 = new Organizacion("Entidad 1", null, null);
    this.entidad2 = new Organizacion("Entidad 2", null, null);
    testExportable = new Exportable() {
      @Override
      public String getNombre() {
        return "Test Data";
      }

      @Override
      public LinkedHashMap<Entidad, Double> exportarDatos() {
        LinkedHashMap<Entidad, Double> data = new LinkedHashMap<>();
        data.put(entidad1, 10.0);
        data.put(entidad2, 5.0);
        return data;
      }
    };
  }

  @Test
  public void testExportar() {
    PDDocument filePath = pdfAdapter.exportar(testExportable, "Test Data");
    File file = new File("Test Data");
    assertTrue(file.exists());
    assertTrue(file.isFile());
    assertTrue(pdfContainsText(pdfAdapter.rutaCompletaDelArchivo("Test Data"), "Test Data"));
    file.delete(); // Limpiar después de la prueba
  }

  @Test
  public void testRutaCompletaDelArchivo() {
    String ruta = pdfAdapter.rutaCompletaDelArchivo("testFile.pdf");
    assertNotNull(ruta);
    assertTrue(ruta.endsWith("testFile.pdf"));
  }

  public boolean pdfContainsText(String pdfFile, String searchText) {
    try {
      PDDocument document = PDDocument.load(new File(pdfFile));
      PDFTextStripper textStripper = new PDFTextStripper();
      String text = textStripper.getText(document);
      document.close();

      return text.contains(searchText);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }
}
