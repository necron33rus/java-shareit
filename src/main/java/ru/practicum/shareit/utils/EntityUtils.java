package ru.practicum.shareit.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class EntityUtils {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    public User getUserIfExists(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id = " + userId + " not found")
        );
    }

    public Item getItemIfExists(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Item with id = " + itemId + " not found")
        );
    }

    public Booking getBookingIfExists(long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException("Booking with id = " + bookingId + " not found")
        );
    }

    private static final Map<BookingState, Predicate<Booking>> STATE_FILTER = new EnumMap<>(BookingState.class);

    static {
        STATE_FILTER.put(BookingState.ALL, b -> true);
        STATE_FILTER.put(BookingState.PAST, b -> b.getEnd().isBefore(LocalDateTime.now()));
        STATE_FILTER.put(BookingState.FUTURE, b -> b.getStart().isAfter(LocalDateTime.now()));
        STATE_FILTER.put(BookingState.CURRENT, b -> b.getStart().isBefore(LocalDateTime.now())
                && b.getEnd().isAfter(LocalDateTime.now()));
        STATE_FILTER.put(BookingState.WAITING, b -> b.getStatus().equals(BookingStatus.WAITING));
        STATE_FILTER.put(BookingState.REJECTED, b -> b.getStatus().equals(BookingStatus.REJECTED));
    }

    public static Predicate<Booking> stateBy(BookingState state) {
        return STATE_FILTER.getOrDefault(state, b -> {
            throw new BadRequestException("Unknown state: " + state);
        });
    }
}
