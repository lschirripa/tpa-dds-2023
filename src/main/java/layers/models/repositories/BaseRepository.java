package layers.models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public abstract class BaseRepository<T> implements WithSimplePersistenceUnit {

  public void save(T t) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();
    entityManager().persist(t);
    tx.commit();
    System.out.println("Saving " + t.getClass().getName());
  }

  public void delete(T t) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();
    entityManager().remove(t);
    tx.commit();
  }

  public void update(T t) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();
    entityManager().merge(t);
    tx.commit();
  }

  public List<T> getAll(Class<T> t) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    try {
      List<T> resultList = entityManager()
          .createQuery("from " + t.getName())
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

  public T getById(int id, Class<T> t) {

    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    T entity = entityManager().find(t, id);

    tx.commit();
    return entity;
  }

  public T getByNombre(String nombre, Class<T> t) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    CriteriaBuilder builder = entityManager().getCriteriaBuilder();
    CriteriaQuery<T> criteriaQuery = builder.createQuery(t);
    Root<T> root = criteriaQuery.from(t);

    // Crear una expresión para comparar el nombre
    Expression<String> nombreExpr = root.get("nombre");

    // Crear una predicado para buscar por nombre
    Predicate predicado = builder.equal(nombreExpr, nombre);

    criteriaQuery.where(predicado);

    T entity = entityManager().createQuery(criteriaQuery).getSingleResult();

    tx.commit();
    return entity;
  }

  public List<T> searchByColumn(Class<T> entityClass, String columnName, String searchTerm) {

    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    try {
      List<T> resultList = entityManager()
          .createQuery("from " + entityClass.getName() + " where " + columnName + " like :searchTerm")
          .setParameter("searchTerm", "%" + searchTerm + "%").getResultList();

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

