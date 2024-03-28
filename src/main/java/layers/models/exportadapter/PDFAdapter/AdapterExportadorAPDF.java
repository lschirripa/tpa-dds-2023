package layers.models.exportadapter.PDFAdapter;

// esta interfaz se usa para poder adaptar los distintos tipos de exportadores a PDF

import layers.models.exportadapter.Exportable;
import org.apache.pdfbox.pdmodel.PDDocument;

public interface AdapterExportadorAPDF {
  public PDDocument exportar(Exportable exportable, String nombreDeArchivo);

}