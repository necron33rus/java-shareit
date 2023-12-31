package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private static final String CUSTOM_USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestBody BookingDto bookingDto, @RequestHeader(CUSTOM_USER_ID_HEADER) long userId) {
        log.info("POST: create booking for user with id={}", userId);
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatus(@RequestHeader(CUSTOM_USER_ID_HEADER) long userId,
                                   @PathVariable Long bookingId,
                                   @RequestParam Boolean approved) {
        log.info("PATCH/id: update status of booking with id={} and user with id={}", bookingId, userId);
        return bookingService.updateStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@PathVariable Long bookingId,
                               @RequestHeader(CUSTOM_USER_ID_HEADER) long userId) {
        log.info("GET/id: find by id booking with id={}", bookingId);
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> findAllForBooker(@RequestHeader(CUSTOM_USER_ID_HEADER) long userId,
                                             @RequestParam(defaultValue = "ALL") String state,
                                             @RequestParam(value = "from", defaultValue = "0") Integer from,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("GET: find all bookings for booker with id={} and state: {}", userId, state);
        return bookingService.findByBookerAndState(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllItemsForOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(defaultValue = "ALL") String state,
                                                 @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("GET/owner: find all bookings for item's owner with id={} and state{}", userId, state);
        return bookingService.findByOwnerAndState(userId, state, from, size);
    }
}
