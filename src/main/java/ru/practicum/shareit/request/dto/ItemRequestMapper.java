package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.model.ItemRequest;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequest toItemRequest(ItemRequestDto dto) {
        ItemRequest request = new ItemRequest();
        request.setDescription(dto.getDescription());
        return request;
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .build();
    }
}
