package ru.practicum.shareit.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ItemModelTest {
    @Test
    void testEquals_Symmetric() {
        var item = Item.builder()
                .id(1L)
                .name("item")
                .description("desc")
                .owner(User.builder().id(2L).build())
                .available(true)
                .build();
        var item2 = Item.builder()
                .id(2L)
                .name("item")
                .description("desc")
                .owner(User.builder().id(2L).build())
                .available(true)
                .build();
        assertFalse(item.equals(item2) && item2.equals(item));
        assertEquals(item.hashCode(), item2.hashCode());
    }
}
