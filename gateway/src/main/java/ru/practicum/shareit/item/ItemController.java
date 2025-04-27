package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    private static final String USER_HEADER_NAME = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAllItemsByUserId(@RequestHeader(USER_HEADER_NAME) Long userId) {
        log.info("Получен запрос на получение всех вещей пользователя с id: {}", userId);
        return itemClient.getAllItemsByUserId(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@RequestHeader(USER_HEADER_NAME) Long userId, @PathVariable Long id) {
        log.info("Получен запрос на получение вещи с id: {}", id);
        return itemClient.getItemById(userId, id);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(USER_HEADER_NAME) Long userId, @RequestBody @Valid ItemDto itemDto) {
        log.info("Получен запрос на добавление вещи: {}", itemDto);
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_HEADER_NAME) Long userId, @PathVariable Long id, @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на обновление вещи с id: {}, данные вещи: {}", id, itemDto);
        return itemClient.updateItem(userId, id, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(USER_HEADER_NAME) Long userId, @RequestParam String text) {
        log.info("Получен запрос на поиск вещей по тексту: {}", text);
        return itemClient.searchItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(USER_HEADER_NAME) Long userId, @PathVariable Long itemId, @RequestBody @Valid CommentDto commentDto) {
        log.info("Получен запрос на добавление комментария: {}", commentDto);
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
