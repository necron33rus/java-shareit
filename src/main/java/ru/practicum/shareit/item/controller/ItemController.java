package ru.practicum.shareit.item.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto create(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("POST method: create item");
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable long itemId,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("PATCH method: update item with id{}", itemId);
        return itemService.update(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable long itemId) {
        log.info("GET method: get item by id={}", itemId);
        return itemService.findById(itemId);
    }

    @GetMapping
    public List<ItemDto> findAllByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GET method: find all items for user with id={}", userId);
        return itemService.findAllByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findByText(@RequestParam(required = false, name = "text") String text) {
        log.info("GET method: find item by text={}", text);
        return itemService.searchByText(text);
    }
}