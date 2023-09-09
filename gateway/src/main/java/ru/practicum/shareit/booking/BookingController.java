package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private static final String CUSTOM_USER_ID_HEADER = "X-Sharer-User-Id";

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid BookingDto bookingDto,
                                         @RequestHeader(CUSTOM_USER_ID_HEADER) long userId) {
        return bookingClient.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(@RequestHeader(CUSTOM_USER_ID_HEADER) long userId,
                                               @PathVariable long bookingId,
                                               @RequestParam Boolean approved) {
        return bookingClient.updateStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@PathVariable long bookingId,
                                           @RequestHeader(CUSTOM_USER_ID_HEADER) long userId) {
        return bookingClient.findById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllForBooker(@RequestHeader(CUSTOM_USER_ID_HEADER) long userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(value = "from", required = false, defaultValue = "0") int from,
                                                   @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return bookingClient.findALlForBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllItemForOwner(@RequestHeader(CUSTOM_USER_ID_HEADER) long userId,
                                                      @RequestParam(defaultValue = "ALL") String state,
                                                      @RequestParam(value = "from", required = false, defaultValue = "0") int from,
                                                      @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return bookingClient.findAllItemsForOwner(userId, state, from, size);
    }
}