package cl.tuxpan.pruebaingreso.repositories;

import cl.tuxpan.pruebaingreso.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {
  Optional<UsuarioModel> findByName(String name);
}
