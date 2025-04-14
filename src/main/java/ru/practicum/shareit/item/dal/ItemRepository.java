package ru.practicum.shareit.item.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Collection<Item> findAllByOwner(Long ownerId);

    @Query("select i from Item i where i.available = true and (upper(i.name) like upper(%?1%) or i.description like upper(%?1%))")
    Collection<Item> search(String searchText);
}
