package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public Collection<ItemDto> getAllItemsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return itemRepository.findAllByOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    public ItemDto getItemById(Long userId, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (!user.getId().equals(item.getOwner())) {
            throw new NotFoundException("Вещь не принадлежит пользователю");
        }
        return ItemMapper.toItemDto(item);
    }

    @Transactional
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = ItemMapper.toItemModel(itemDto);
        item.setOwner(user.getId());
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item oldItem = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
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

        itemRepository.save(oldItem);

        return ItemMapper.toItemDto(oldItem);
    }

    public Collection<ItemDto> searchItems(Long userId, String text) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return itemRepository.search(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }
}
