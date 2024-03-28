package layers.models.repositories;

import java.util.List;
import javax.persistence.EntityTransaction;
import layers.models.domain.Comunidad;
import lombok.Getter;

@Getter
public class ComunidadRepository extends BaseRepository<Comunidad> {

  public List<Comunidad> getAllActivas() {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    try {
      List resultList = entityManager()
          .createQuery("from Comunidad where activa is true")
          .getResultList();

      tx.commit();
      return resultList;
    } catch (Exception e) {
      if (tx.isActive()) {
        tx.rollback();
      }
      throw e;
    }
  }
}
