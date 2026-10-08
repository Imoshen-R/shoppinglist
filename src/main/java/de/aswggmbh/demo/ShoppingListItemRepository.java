package de.aswggmbh.demo;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ShoppingListItemRepository extends CrudRepository<ShoppingListItem, Long> {
    List<ShoppingListItem> findByName(String name);
}
