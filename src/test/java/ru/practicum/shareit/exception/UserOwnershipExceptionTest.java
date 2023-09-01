package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserOwnershipExceptionTest {
    @Test
    void testExceptionMessage() {
        String expectedMessage = "Bad request occurred";

        Exception exception = assertThrows(UserOwnershipException.class, () -> {
            throw new UserOwnershipException(expectedMessage);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }
}
