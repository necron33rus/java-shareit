package ru.practicum.shareit.booking.model;

import org.apache.commons.lang3.StringUtils;
import ru.practicum.shareit.exception.BadRequestException;

import java.util.stream.Stream;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingState parseState(String state) {
        if (state == null || StringUtils.isBlank(state)) {
            return BookingState.ALL;
        }
        if (Stream.of(BookingState.values()).anyMatch(s -> s.name().equals(state))) {
            return BookingState.valueOf(state);
        } else {
            throw new BadRequestException("Unknown state: " + state);
        }
    }
}
