package layers.models.repositories;

import java.util.List;
import javax.persistence.EntityTransaction;
import layers.models.domain.Entidad;
import layers.models.domain.Linea;
import layers.models.domain.Organizacion;
import lombok.Getter;

@Getter
public class EntidadRepository extends BaseRepository<Entidad> {
  public Linea getLineaById(int id, Class<Linea> lineaClass) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    Linea linea = entityManager().find(lineaClass, id);

    tx.commit();
    return linea;
  }

  public Organizacion getOrganizacionById(int i, Class<Organizacion> organizacionClass) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    Organizacion organizacion = entityManager().find(organizacionClass, i);

    tx.commit();
    return organizacion;
  }

  public List<Entidad> getAllLineas(Class<Entidad> lineaClass) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    // Modifica la consulta para obtener solo las entidades de tipo Linea
    List<Entidad> lineas = entityManager()
        .createQuery("from Entidad where tipo = 'Linea'", Entidad.class)
        .getResultList();

    tx.commit();
    return lineas;
  }

  public List<Entidad> getAllOrganizaciones(Class<Entidad> organizacionClass) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    // Modifica la consulta para obtener solo las entidades de tipo Organizacion
    List<Entidad> organizaciones = entityManager()
        .createQuery("from Entidad where tipo = 'Organizacion'", Entidad.class)
        .getResultList();

    tx.commit();
    return organizaciones;
  }


}
