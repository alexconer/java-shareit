package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.helper.CollectionHelper;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Collection<Item> findAllByUserId(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .toList();
    }

    @Override
    public Optional<Item> findById(Long userId, Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Item create(Long userId, Item item) {
        item.setId(CollectionHelper.getNextId(items));
        item.setOwner(userId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Long userId, Long itemId, Item item) {
        items.put(itemId, item);
        return item;
    }

    @Override
    public Collection<Item> search(Long userId, String searchText) {

        if (searchText.isEmpty() || searchText.isBlank()) {
            return List.of();
        }

        return items.values().stream()
                .filter(item -> item.getDescription().toLowerCase().contains(searchText.toLowerCase()) || item.getName().toLowerCase().contains(searchText.toLowerCase()))
                .filter(Item::getAvailable)
                .toList();
    }
}
