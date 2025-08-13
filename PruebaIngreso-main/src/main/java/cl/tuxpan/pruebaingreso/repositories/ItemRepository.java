package cl.tuxpan.pruebaingreso.repositories;

import cl.tuxpan.pruebaingreso.models.ItemModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemModel, Integer> {}
