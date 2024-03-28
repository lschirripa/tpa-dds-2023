package utils;

import excepciones.ReadFileAsStringException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadTextAsString {
  public static String readFileAsString(String fileName) {
    try {
      return Files
          .readString(Paths.get(ClassLoader
              .getSystemResource(fileName).toURI()));
    } catch (Exception error) {
      throw new ReadFileAsStringException(
          "No se pudo leer el archivo " + fileName + " como String."
      );
    }
  }
}
