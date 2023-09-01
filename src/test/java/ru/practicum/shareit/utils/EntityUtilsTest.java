package ru.practicum.shareit.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntityUtilsTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private EntityUtils entityUtils;

    @Test
    void getUserNotExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        var exception = assertThrows(NotFoundException.class, () -> entityUtils.getUserIfExists(1L));
        assertEquals("User with id = 1 not found", exception.getMessage());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void getUserExists() {
        var user = User.builder()
                .id(1L)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        assertEquals(User.builder().id(1L).build(), entityUtils.getUserIfExists(1L));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void getItemNotExists() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        var exception = assertThrows(NotFoundException.class, () -> entityUtils.getItemIfExists(1L));
        assertEquals("Item with id = 1 not found", exception.getMessage());
        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void getItemRequestNotExists() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());
        var exception = assertThrows(NotFoundException.class, () -> entityUtils.getItemRequestIfExists(1L));
        assertEquals("Request with id = 1 not exists", exception.getMessage());
        verify(itemRequestRepository, times(1)).findById(anyLong());
    }

    @Test
    void getItemRequestExists() {
        var itemRequest = ItemRequest.builder()
                .id(1L)
                        .requester(User.builder().id(1L).build())
                                .created(LocalDateTime.now())
                                        .build();
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        assertEquals(itemRequest, entityUtils.getItemRequestIfExists(1L));
        verify(itemRequestRepository, times(1)).findById(anyLong());
    }

    @Test
    void getItemExists() {
        var item = Item.builder()
                .id(1L)
                .build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        assertEquals(Item.builder().id(1L).build(), entityUtils.getItemIfExists(1L));
        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void getBookingNotExists() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());
        var exception = assertThrows(NotFoundException.class, () -> entityUtils.getBookingIfExists(1L));
        assertEquals("Booking with id = 1 not found", exception.getMessage());
        verify(bookingRepository, times(1)).findById(anyLong());
    }

    @Test
    void getBookingExists() {
        var booking = Booking.builder()
                .id(1L)
                .build();
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        assertEquals(Booking.builder().id(1L).build(), entityUtils.getBookingIfExists(1L));
        verify(bookingRepository, times(1)).findById(anyLong());
    }

    @Test
    void parseCorrectState() {
        assertEquals(BookingState.ALL, EntityUtils.parseState(null));
    }

    @Test
    void parseIncorrectState() {
        String newState = "New";
        var exception = assertThrows(BadRequestException.class, () -> EntityUtils.parseState(newState));
        assertEquals("Unknown state: New", exception.getMessage());
    }
}