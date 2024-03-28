package persistence;

import db.DatabaseSeeder;
import java.util.List;
import layers.models.domain.ServicioUsuario;
import layers.models.repositories.ServicioUsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RecuperarServicioUsuarioTest {
  private final ServicioUsuarioRepository servicioUsuarioRepository = new ServicioUsuarioRepository();
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

    List<ServicioUsuario> serviciosUsuario = servicioUsuarioRepository.getAll(ServicioUsuario.class);

    Assertions.assertEquals(3, serviciosUsuario.size());

    ServicioUsuario servicioUsuario = servicioUsuarioRepository.findByUsuarioAndServicioAsociado(1, 1);
    Assertions.assertEquals(1, servicioUsuario.getId());

  }


}
