package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item create(Item item);

    Item findById(Long id);

    void update(Item item);

    List<Item> findAll();
}
