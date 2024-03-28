package persistence;

import db.DatabaseSeeder;
import java.util.List;
import layers.models.domain.Comunidad;
import layers.models.domain.ServicioUsuario;
import layers.models.repositories.ComunidadRepository;
import layers.models.repositories.ServicioUsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ObtenerComunidadesActivasTest {
  private final ComunidadRepository comunidadRepository = new ComunidadRepository();
  private final DatabaseSeeder databaseSeeder = new DatabaseSeeder();

  @Test
  public void recuperarServicioUsuario() {
    System.out.println("inicializando base de datos...");
    try {
      databaseSeeder.seed();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error al cargar datos de prueba en la db. Verifique la conexion");
    }
    System.out.println("base de datos inicializada");

    Comunidad comunidad = comunidadRepository.getById(1, Comunidad.class);
    comunidad.setActiva(false);
    comunidadRepository.update(comunidad);

    List<Comunidad> comunidadRepositories = comunidadRepository.getAll(Comunidad.class);
    List<Comunidad> comunidadActivas = comunidadRepository.getAllActivas();
    Assertions.assertEquals(2, comunidadRepositories.size());
    Assertions.assertEquals(1, comunidadActivas.size());

  }


}
