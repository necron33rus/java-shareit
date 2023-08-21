package ru.practicum.shareit.booking.model;

import org.apache.commons.lang3.StringUtils;
import ru.practicum.shareit.exception.BadRequestException;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingState parseState(String state) {
        BookingState bookingState;
        if (StringUtils.isBlank(state)) {
            bookingState = BookingState.ALL;
        } else {
            try {
                bookingState = BookingState.valueOf(state);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Unknown state: " + state);
            }
        }
        return bookingState;
    }
}
