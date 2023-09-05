package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;

@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private static final String CUSTOM_USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid BookingDto bookingDto,
                                         @RequestHeader(CUSTOM_USER_ID_HEADER) long userId) {
        return bookingClient.create(bookingDto, userId);
    }

    @ResponseBody
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object>  updateStatus(@RequestHeader(CUSTOM_USER_ID_HEADER) long userId,
                                   @PathVariable Long bookingId,
                                   @RequestParam Boolean approved) {
        return bookingClient.updateStatus(userId, bookingId, approved);
    }

    @ResponseBody
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object>  findById(@PathVariable Long bookingId,
                               @RequestHeader(CUSTOM_USER_ID_HEADER) long userId) {
        return bookingClient.findById(bookingId, userId);
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<Object> findAllForBooker(@RequestHeader(CUSTOM_USER_ID_HEADER) long userId,
                                             @RequestParam(required = false, defaultValue = "ALL") String state,
                                             @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                             @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return bookingClient.findByBookerAndState(userId, state, from, size);
    }

    @ResponseBody
    @GetMapping("/owner")
    public ResponseEntity<Object> findAllItemsForOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(required = false, defaultValue = "ALL") String state,
                                                 @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                 @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return bookingClient.findByOwnerAndState(userId, state, from, size);
    }
}
