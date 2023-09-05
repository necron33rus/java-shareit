package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    private static final String CUSTOM_USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto create(@RequestHeader(CUSTOM_USER_ID_HEADER) long userId,
                                 @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.create(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> findAllByUser(@RequestHeader(CUSTOM_USER_ID_HEADER) long userId) {
        return itemRequestService.getListRequestsByUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllByParams(@RequestHeader(CUSTOM_USER_ID_HEADER) long userId,
                                                               @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                               @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemRequestService.findAllByParams(userId, PageRequest.of(from, size, Sort.by("created").descending()));
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findById(@RequestHeader(CUSTOM_USER_ID_HEADER) long userId,
                                                   @PathVariable("requestId") long requestId) {
        return itemRequestService.findById(requestId, userId);
    }
}
