package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {

    private static final String USER_HEADER_NAME = "X-Sharer-User-Id";

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader(USER_HEADER_NAME) Long userId, @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Получен запрос на создание запроса вещи.");
        return itemRequestService.addItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public Collection<ItemRequestWithItemsDto> getAllRequestsByUserId(@RequestHeader(USER_HEADER_NAME) Long userId) {
        log.info("Получен запрос на получение всех запросов.");
        return itemRequestService.getAllRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllRequests(@RequestHeader(USER_HEADER_NAME) Long userId) {
        log.info("Получен запрос на получение всех запросов.");
        return itemRequestService.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithItemsDto getRequestById(@RequestHeader(USER_HEADER_NAME) Long userId, @PathVariable Long requestId) {
        log.info("Получен запрос на получение запроса вещи с id: {}", requestId);
        return itemRequestService.getRequestById(userId, requestId);
    }
}
