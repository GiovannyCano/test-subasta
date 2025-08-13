package cl.tuxpan.pruebaingreso.repositories;

import cl.tuxpan.pruebaingreso.models.ApuestaModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApuestaRepository extends JpaRepository<ApuestaModel, Integer> {
  Optional<ApuestaModel> findTopByItem_IdOrderByAmountDesc(Integer id);

  @Query("select coalesce(sum(a.amount), 0) from ApuestaModel a where a.usuario.id = :usuarioId")
  Integer totalApostadoPorUsuario(@Param("usuarioId") Integer usuarioId);

  @Modifying
  @Transactional
  @Query(value = """
    DELETE FROM subasta_apuesta a
    USING subasta_usuario u, subasta_item i
    WHERE a.apuesta_usuario_id = u.usuario_id
      AND a.apuesta_item_id   = i.item_id
      AND i.item_abierta      = TRUE
      AND u.usuario_nombre    ~ '[^[:alnum:] ]'  -- contiene algún carácter no alfanumérico ni espacio
    """, nativeQuery = true)
  int purgeApuestasCorruptas();
}
