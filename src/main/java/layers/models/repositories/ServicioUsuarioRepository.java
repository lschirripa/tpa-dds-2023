package layers.models.repositories;

import java.util.List;
import javax.persistence.EntityTransaction;
import layers.models.domain.ServicioUsuario;

public class ServicioUsuarioRepository extends BaseRepository<ServicioUsuario> {
  public ServicioUsuario findByUsuarioAndServicioAsociado(int userId, int servicioId) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    try {
      List<ServicioUsuario> resultList = entityManager()
          .createQuery("SELECT s FROM ServicioUsuario s WHERE s.usuario.id = :userId AND s.servicioAsociado.id = :servicioId", ServicioUsuario.class)
          .setParameter("userId", userId)
          .setParameter("servicioId", servicioId)
          .getResultList();

      tx.commit();

      return resultList.isEmpty() ? null : resultList.get(0);
    } catch (Exception e) {
      if (tx.isActive()) {
        tx.rollback();
      }
      throw e;
    }
  }

}
