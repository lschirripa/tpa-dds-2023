package layers.models.repositories;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityTransaction;
import layers.models.domain.Comunidad;
import layers.models.domain.ServicioAsociado;
import layers.models.domain.ServicioUsuario;
import lombok.Getter;

@Getter
public class ServicioAsociadoRepository extends BaseRepository<ServicioAsociado> {
  public ServicioAsociado findBy(Integer entidadId, Integer establecimientoId, Integer servicioId) {
    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    try {
      ServicioAsociado servicioAsociado = entityManager()
          .createQuery("SELECT s FROM ServicioAsociado s WHERE s.entidad.id = :entidadId AND s.establecimiento.id = :establecimientoId AND s.servicio.id = :servicioId", ServicioAsociado.class)
          .setParameter("entidadId", entidadId)
          .setParameter("establecimientoId", establecimientoId)
          .setParameter("servicioId", servicioId)
          .getSingleResult();

      tx.commit();
      return servicioAsociado;
    } catch (Exception e) {
      if (tx.isActive()) {
        tx.rollback();
      }
      throw e;
    }
  }

}
