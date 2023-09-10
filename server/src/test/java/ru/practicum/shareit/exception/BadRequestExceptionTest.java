package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BadRequestExceptionTest {

    @Test
    void testExceptionMessage() {
        String expectedMessage = "Bad request occurred";

        Exception exception = assertThrows(BadRequestException.class, () -> {
            throw new BadRequestException(expectedMessage);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }
}
