package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public Collection<ItemDto> getAllItemsByUserId(Long userId) {
        return itemStorage.findAllByUserId(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    public ItemDto getItemById(Long userId, Long itemId) {
        return ItemMapper.toItemDto(itemStorage.findById(userId, itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена")));
    }

    public ItemDto addItem(Long userId, ItemDto itemDto) {
        User user = userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return ItemMapper.toItemDto(itemStorage.create(userId, ItemMapper.toItemModel(itemDto)));
    }

    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        User user = userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item oldItem = itemStorage.findById(userId, itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (!user.getId().equals(oldItem.getOwner())) {
            throw new AccessDeniedException("Вещь не принадлежит пользователю");
        }

        if (itemDto.getName() != null) {
            oldItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            oldItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            oldItem.setAvailable(itemDto.getAvailable());
        }

        itemStorage.update(userId, itemId, oldItem);

        return ItemMapper.toItemDto(oldItem);
    }

}
