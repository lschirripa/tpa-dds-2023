package utils;

import com.opencsv.CSVReader;
import io.javalin.http.UploadedFile;
import java.io.InputStreamReader;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import layers.controllers.FactoryController;
import layers.controllers.OrganismoController;
import layers.controllers.PrestadoraController;
import layers.models.domain.OrganismoDeControl;
import layers.models.domain.PrestadoraDeServicio;
import lombok.Getter;
import utils.enums.CsvFile;

@Getter
public class ReadCsv {
  private UploadedFile file;
  private ArrayList<OrganismoDeControl> organismosDeControl = new ArrayList<>();
  private ArrayList<PrestadoraDeServicio> prestadorasDeServicios = new ArrayList<>();

  public ReadCsv(UploadedFile file) {
    this.file = file;
  }

  public String leerCsv() throws Exception {
    String result;
    if (this.file != null && this.file.size() > 0) {
      try (CSVReader reader = new CSVReader(new InputStreamReader(file.content()))) {
        String[] nextLine;
        reader.readNext();
        while ((nextLine = reader.readNext()) != null) {
          System.out.println(nextLine[0]);

          CsvFile myEnum = CsvFile.valueOf(nextLine[0].toUpperCase());

          if (myEnum.equals(CsvFile.PRESTADORA)) {
            System.out.println("es prestadora");
            PrestadoraController prestadoraController = (PrestadoraController) FactoryController.controller("Prestadora");
            prestadoraController.save_prestadora_from_csv(nextLine[1], nextLine[2]);
          } else if (myEnum.equals(CsvFile.ORGANISMO)) {
            System.out.println("es organismo");
            OrganismoController organismoController = (OrganismoController) FactoryController.controller("Organismo");
            organismoController.save_organismo_from_csv(nextLine[1], nextLine[2]);

          }
        }
        result = "¡Archivo subido exitosamente!";
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(e);
      } catch (Exception e) {
        e.printStackTrace();
        result = "Error al procesar el archivo";
      }
    } else {
      throw new NoSuchFileException("No se envio ningun archivo");
    }
    return result;
  }
}

