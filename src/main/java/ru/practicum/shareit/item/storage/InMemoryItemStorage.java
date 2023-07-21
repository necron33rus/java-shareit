package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryItemStorage implements ItemStorage{

    private final Map<Long, Item> storage = new HashMap<>();
    private Long counter = 0L;

    @Override
    public Item create(Item item) {
        if (item.getId() == null) {
            item.setId(++counter);
        }
        storage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item findById(Long id) {
        return storage.get(id);
    }

    @Override
    public void update(Item item) {
        storage.put(item.getId(), item);
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(storage.values());
    }
}
