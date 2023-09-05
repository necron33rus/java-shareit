package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private static final String CUSTOM_USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(CUSTOM_USER_ID_HEADER) long userId,
                                         @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return itemRequestClient.create(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByUser(@RequestHeader(CUSTOM_USER_ID_HEADER) long userId) {
        return itemRequestClient.getListRequestsByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllByParams(@RequestHeader(CUSTOM_USER_ID_HEADER) long userId,
                                                  @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemRequestClient.findAllByParams(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@RequestHeader(CUSTOM_USER_ID_HEADER) long userId,
                                           @PathVariable("requestId") long requestId) {
        return itemRequestClient.findById(requestId, userId);
    }
}
