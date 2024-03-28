package layers.models.repositories;

import java.util.List;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import layers.models.domain.Establecimiento;
import layers.models.domain.Estacion;
import layers.models.domain.Linea;
import layers.models.domain.Sucursal;

public class EstablecimientoRepository extends BaseRepository<Establecimiento> {
  public Estacion getEstacionById(int id, Class<Estacion> estacionClass) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    Estacion estacion = entityManager().find(estacionClass, id);

    tx.commit();
    return estacion;
  }

  public Sucursal getSucursalById(int id, Class<Sucursal> sucursalClass) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    Sucursal linea = entityManager().find(sucursalClass, id);

    tx.commit();
    return linea;
  }

  public List<Estacion> getAllEstaciones(Class<Estacion> estacionClass) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    try {
      List<Estacion> resultList = entityManager()
          .createQuery("from " + estacionClass.getName())
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

  public List<Sucursal> getAllSucursales(Class<Sucursal> sucursalClass) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    try {
      List<Sucursal> resultList = entityManager()
          .createQuery("from " + sucursalClass.getName())
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

  public List<Sucursal> getSucursalesSinOrganizacion(Class<Sucursal> sucursalClass) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    try {
      List<Sucursal> resultList = entityManager()
          .createQuery("from " + sucursalClass.getName() + " where organizacion_id is null")
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


  public Sucursal getSucursalByNombre(String nombre, Class<Sucursal> sucursalClass) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    try {
      TypedQuery<Sucursal> query = entityManager()
          .createQuery("SELECT s FROM " + sucursalClass.getName() + " s WHERE s.nombre = :nombre", sucursalClass)
          .setParameter("nombre", nombre);

      Sucursal sucursal = query.getSingleResult();

      tx.commit();
      return sucursal;
    } catch (NoResultException e) {
      // Manejar la excepción si no se encuentra ninguna sucursal con ese nombre
      tx.rollback();
      return null; // O puedes lanzar una excepción personalizada
    } catch (Exception e) {
      if (tx.isActive()) {
        tx.rollback();
      }
      throw e;
    }
  }

  public Estacion getEstacionByNombre(String nombre, Class<Estacion> estacionClass) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    try {
      TypedQuery<Estacion> query = entityManager()
          .createQuery("SELECT e FROM " + estacionClass.getName() + " e WHERE e.nombre = :nombre", estacionClass)
          .setParameter("nombre", nombre);

      Estacion estacion = query.getSingleResult();

      tx.commit();
      return estacion;
    } catch (NoResultException e) {
      // Manejar la excepción si no se encuentra ninguna estación con ese nombre
      tx.rollback();
      return null; // O puedes lanzar una excepción personalizada
    } catch (Exception e) {
      if (tx.isActive()) {
        tx.rollback();
      }
      throw e;
    }
  }


}
