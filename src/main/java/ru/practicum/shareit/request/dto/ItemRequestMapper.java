package ru.practicum.shareit.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collections;
import java.util.List;

@Component
public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemDto> items) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(items == null ? Collections.emptyList() : items)
                .build();
    }

    public static ItemRequest toItemRequest(ItemRequestDto dto) {
        return ItemRequest.builder()
                .id(dto.getId())
                .description(dto.getDescription())
                .created(dto.getCreated())
                .build();
    }
}
