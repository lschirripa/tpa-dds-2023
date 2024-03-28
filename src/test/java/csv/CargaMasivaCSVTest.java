//package csv;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import layers.models.domain.Organismo;
//import layers.models.domain.Prestadora;
//import utils.enums.CsvFile;
//import utils.ReadCsv;
//
//public class CargaMasivaCSVTest {
//  private ReadCsv readCsv;
//
//  @BeforeEach
//  public void init() {
//    readCsv = new ReadCsv(file);
//  }
//
//  @Test
//  @DisplayName("Se instancian multiples organismos de control a traves del csv")
//  public void testInstanciarOrganismoDeControl() {
//
//    readCsv.leerCsv(CsvFile.ORGANISMO);
//    String nameExpected = "Contraloria General de la Republica";
//    String descriptionExpected = "Fondo de Bienestar Social de la Contraloria General de la Republica";
//
//    Organismo organismo1 = readCsv.getOrganismosDeControl().get(0).getOrganismo();
//
//    assertTrue(organismo1.getNombre().contains(nameExpected));
//    assertTrue(organismo1.getDescripcion().contains(descriptionExpected));
//    Assertions.assertEquals(7, readCsv.getOrganismosDeControl().size());
//  }
//
//  @Test
//  @DisplayName("Se instancian multiples organismos de control a traves del csv")
//  public void testInstanciarPrestadoraDeServicio() {
//
//    readCsv.leerCsv(CsvFile.PRESTADORA);
//    String nameExpected = "personal de mantenimiento";
//    String descriptionExpected = "mantenimiento de computadoras";
//
//    Prestadora prestadora1 = readCsv.getPrestadorasDeServicios().get(0).getPrestadora();
//
//    assertTrue(prestadora1.getNombre().contains(nameExpected));
//    assertTrue(prestadora1.getDescripcion().contains(descriptionExpected));
//    Assertions.assertEquals(4, readCsv.getPrestadorasDeServicios().size());
//  }
//
//}
