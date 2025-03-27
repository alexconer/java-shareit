package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public Collection<ItemDto> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на получение всех вещей пользователя с id: {}", userId);
        return itemService.getAllItemsByUserId(userId);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        log.info("Получен запрос на получение вещи с id: {}", id);
        return itemService.getItemById(userId, id);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid ItemDto itemDto) {
        log.info("Получен запрос на добавление вещи: {}", itemDto);
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id, @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на обновление вещи с id: {}, данные вещи: {}", id, itemDto);
        return itemService.updateItem(userId, id, itemDto);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text) {
        log.info("Получен запрос на поиск вещей по тексту: {}", text);
        return itemService.searchItems(userId, text);
    }
}
