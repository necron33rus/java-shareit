package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private static final String CUSTOM_USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto create(@RequestBody @Valid ItemDto itemDto, @RequestHeader(CUSTOM_USER_ID_HEADER) long userId) {
        log.info("ItemController: POST method: create item {}", itemDto);
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable long itemId,
                          @RequestHeader(CUSTOM_USER_ID_HEADER) long userId) {
        log.info("ItemController: PATCH method: update item with id {} by item = {}", itemId, itemDto);
        return itemService.update(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable long itemId, @RequestHeader(CUSTOM_USER_ID_HEADER) long userId) {
        log.info("ItemController: GET method: get item with id={}", itemId);
        return itemService.findById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> findAllByUserId(@RequestHeader(CUSTOM_USER_ID_HEADER) long userId) {
        log.info("ItemController: GET method: find all items for user with id={}", userId);
        return itemService.findAllByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findByText(@RequestParam(required = false, name = "text") String text) {
        log.info("ItemController: GET method: find items by text={}", text);
        return itemService.searchByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId,
                                 @RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestBody @Valid CommentDto commentDto) {
        log.info("ItemController: POST method: added comment from user with id={} for item with id={}", userId, itemId);
        return itemService.addComment(itemId, userId, commentDto);
    }
}
