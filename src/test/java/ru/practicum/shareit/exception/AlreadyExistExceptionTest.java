package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlreadyExistExceptionTest {
    @Test
    void testExceptionMessage() {
        String expectedMessage = "Bad request occurred";

        Exception exception = assertThrows(AlreadyExistException.class, () -> {
            throw new AlreadyExistException(expectedMessage);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }
}
