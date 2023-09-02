package ru.practicum.shareit.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserModelTest {
    @Test
    void testEquals_Symmetric() {
        var user = User.builder()
                .id(1L)
                .build();
        var user2 = User.builder()
                .id(2L)
                .build();
        assertFalse(user.equals(user2) && user2.equals(user));
        assertEquals(user.hashCode(), user2.hashCode());
    }
}
