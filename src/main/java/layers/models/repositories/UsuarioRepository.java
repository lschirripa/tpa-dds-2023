package layers.models.repositories;

import javax.persistence.EntityTransaction;
import layers.models.domain.Usuario;
import lombok.Getter;

@Getter
public class UsuarioRepository extends BaseRepository<Usuario> {
  public Usuario getUsuarioById(int id, Class<Usuario> usuario) {

    entityManager().clear(); // Limpiar la caché después de la consulta

    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    Usuario entity = entityManager().find(usuario, id);

    tx.commit();
    return entity;
  }
}

