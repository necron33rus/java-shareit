package ru.practicum.shareit.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BookingModelTest {
    @Test
    void testEquals_Symmetric() {
        var booking = Booking.builder()
                .id(1L)
                .item(Item.builder().id(1L).build())
                .build();
        var booking2 = Booking.builder()
                .id(2L)
                .item(Item.builder().id(2L).build())
                .build();
        assertFalse(booking.equals(booking2) && booking2.equals(booking));
        assertEquals(booking.hashCode(), booking2.hashCode());
    }
}
