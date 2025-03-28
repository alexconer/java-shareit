package ru.practicum.shareit.helper;

import java.util.Map;

public class CollectionHelper {
    /**
     * Возвращает max идентификатор элемента коллекции
     */
    public static long getNextId(Map<Long, ?> collection) {
        long currentMaxId = collection.keySet().stream().max(Long::compareTo).orElse(0L);
        return currentMaxId + 1;
    }
}
