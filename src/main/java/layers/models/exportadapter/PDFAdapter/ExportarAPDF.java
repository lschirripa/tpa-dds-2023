package layers.models.exportadapter.PDFAdapter;

import layers.models.exportadapter.Exportable;

// esta clase, sera llamada por alguien, y por parametro se le pasara un objeto de tipo EXPORTABLE
// (interfaz), que es el que contiene los datos a exportar
public class ExportarAPDF {
  private AdapterExportadorAPDF adapterExportadorAPDF;

  // se hardcodea que adaptador se va a usar porque solo tenemos uno por ahora (ApachePDFBox)
  public ExportarAPDF() {
    this.adapterExportadorAPDF = new AdapterApachePDFBox();
  }

  public void exportar(Exportable exportable, String nombreDeArchivo) {
    this.adapterExportadorAPDF.exportar(exportable, nombreDeArchivo);
  }

}
