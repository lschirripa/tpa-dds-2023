package layers.models.repositories;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import layers.models.domain.Incidente;
import lombok.Getter;

@Getter
public class IncidenteRepository extends BaseRepository<Incidente> {

  public List<Incidente> getResolvedIncidentes() {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    try {
      List resultList = entityManager()
          .createQuery("from Incidente where fechaResolucion is not null")
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

  public List<Incidente> getIncidentesByUsuarioId(int usuarioId) {
    EntityManager entityManager = entityManager();
    entityManager.getTransaction().begin();

    try {
      String hql = "SELECT DISTINCT i FROM Incidente i "
          + "JOIN i.comunidades c "
          + "JOIN c.miembros u "
          + "WHERE u.id = :usuarioId";

      Query query = entityManager.createQuery(hql);
      query.setParameter("usuarioId", usuarioId);

      @SuppressWarnings("unchecked")
      List<Incidente> resultList = query.getResultList();

      entityManager.getTransaction().commit();
      return resultList;
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      throw e;
    } finally {
      entityManager.close(); // Asegúrate de cerrar el EntityManager después de usarlo
    }
  }

}
