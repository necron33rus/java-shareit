package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

@Controller
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {
    private static final String CUSTOM_USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemDto itemDto,
                                         @RequestHeader(CUSTOM_USER_ID_HEADER) long userId) {
        return itemClient.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody ItemDto itemDto,
                                         @PathVariable long itemId,
                                         @RequestHeader(CUSTOM_USER_ID_HEADER) long userId) {
        return itemClient.update(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(@PathVariable long itemId,
                                           @RequestHeader(CUSTOM_USER_ID_HEADER) long userId) {
        return itemClient.findById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByUserId(@RequestHeader(CUSTOM_USER_ID_HEADER) long userId) {
        return itemClient.findAllByUserId(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findByText(@RequestParam String text) {
        return itemClient.findByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Long itemId,
                                             @RequestHeader(CUSTOM_USER_ID_HEADER) Long userId,
                                             @RequestBody @Valid CommentDto commentDto) {
        return itemClient.addComment(itemId, userId, commentDto);
    }
}
