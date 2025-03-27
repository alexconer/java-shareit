package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {
    Collection<Item> findAllByUserId(Long userId);
    Collection<Item> search(Long userId, String searchText);
    Optional<Item> findById(Long userId, Long itemId);
    Item create(Long userId, Item item);
    Item update(Long userId, Long itemId, Item item);
}
