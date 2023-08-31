package ru.practicum.shareit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.EntityUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private EntityUtils entityUtils;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void createWithItemNotAvailable() {
        var item = Item.builder()
                .available(false)
                .build();
        when(entityUtils.getItemIfExists(anyLong())).thenReturn(item);
        var exception = assertThrows(NotAvailableException.class,
                () -> bookingService.create(BookingDto.builder().itemId(1L).build(), 1L));
        assertEquals("Вещь с ID = null не доступна для бронирования", exception.getMessage());
        verify(entityUtils, times(1)).getUserIfExists(anyLong());
        verify(entityUtils, times(1)).getItemIfExists(anyLong());
    }

    @Test
    void createWithStartNull() {
        var bookingDto = BookingDto.builder()
                .itemId(1L)
                .end(LocalDateTime.now())
                .build();
        when(entityUtils.getItemIfExists(anyLong())).thenReturn(Item.builder().available(true).build());
        var exception = assertThrows(BadRequestException.class, () -> bookingService.create(bookingDto, 1L));
        assertEquals("Не указана дата бронирования", exception.getMessage());
        verify(entityUtils, times(1)).getUserIfExists(anyLong());
        verify(entityUtils, times(1)).getItemIfExists(anyLong());
    }

    @Test
    void createWithEndNull() {
        var bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now())
                .build();
        when(entityUtils.getItemIfExists(anyLong())).thenReturn(Item.builder().available(true).build());
        var exception = assertThrows(BadRequestException.class, () -> bookingService.create(bookingDto, 1L));
        assertEquals("Не указана дата бронирования", exception.getMessage());
        verify(entityUtils, times(1)).getUserIfExists(anyLong());
        verify(entityUtils, times(1)).getItemIfExists(anyLong());
    }

    @Test
    void createWithStartAfterEnd() {
        var bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2020, 2, 1, 1, 1))
                .end(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        when(entityUtils.getItemIfExists(anyLong())).thenReturn(Item.builder().available(true).build());
        var exception = assertThrows(BadRequestException.class, () -> bookingService.create(bookingDto, 1L));
        assertEquals("Дата начала бронирования вещи позже или равно дате окончания заказа", exception.getMessage());
        verify(entityUtils, times(1)).getUserIfExists(anyLong());
        verify(entityUtils, times(1)).getItemIfExists(anyLong());
    }

    @Test
    void createWithStartEqualsEnd() {
        var bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2020, 1, 1, 1, 1))
                .end(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        when(entityUtils.getItemIfExists(anyLong())).thenReturn(Item.builder().available(true).build());
        var exception = assertThrows(BadRequestException.class, () -> bookingService.create(bookingDto, 1L));
        assertEquals("Дата начала бронирования вещи позже или равно дате окончания заказа", exception.getMessage());
        verify(entityUtils, times(1)).getUserIfExists(anyLong());
        verify(entityUtils, times(1)).getItemIfExists(anyLong());
    }

    @Test
    void createByItemOwner() {
        var bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .build();
        var item = Item.builder()
                .available(true)
                .owner(User.builder().id(1L).build())
                .build();
        when(entityUtils.getItemIfExists(anyLong())).thenReturn(item);
        var exception = assertThrows(NotFoundException.class, () -> bookingService.create(bookingDto, 1L));
        assertEquals("Владелец не может забронировать свою собственную вещь", exception.getMessage());
        verify(entityUtils, times(1)).getUserIfExists(anyLong());
        verify(entityUtils, times(1)).getItemIfExists(anyLong());
    }

    @Test
    void create() {
        var item = Item.builder()
                .available(true)
                .owner(User.builder().id(1L).build())
                .build();
        var user = User.builder().id(2L).build();
        var booking = Booking.builder()
                .item(item)
                .booker(user)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .build();
        when(entityUtils.getUserIfExists(anyLong())).thenReturn(user);
        when(entityUtils.getItemIfExists(anyLong())).thenReturn(item);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        var bookingDto = BookingMapper.toBookingDto(booking);
        bookingDto.setItemId(1L);
        var resultBookingDto = bookingService.create(bookingDto, 2L);
        booking.setId(1L);
        resultBookingDto.setId(1L);
        assertEquals(BookingMapper.toBookingDto(booking), resultBookingDto);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void updateStatusAlreadyApproved() {
        var booking = Booking.builder()
                .booker(User.builder().id(1L).build())
                .status(BookingStatus.APPROVED)
                .item(Item.builder().id(1L).build())
                .start(LocalDateTime.of(2020, 1, 1, 1, 1))
                .end(LocalDateTime.of(2020, 1, 2, 1, 1))
                .build();
        when(entityUtils.getBookingIfExists(anyLong())).thenReturn(booking);
        var exception = assertThrows(BadRequestException.class, () -> bookingService.updateStatus(1L, 1L, true));
        assertEquals("Бронирование уже подтверждено владельцем", exception.getMessage());
        verify(entityUtils, times(1)).getBookingIfExists(anyLong());
    }

    @Test
    void updateStatusWithApproveByOnlyItemOwner() {
        var item = Item.builder()
                .id(1L)
                .owner(User.builder().id(1L).build())
                .build();
        var booking = Booking.builder()
                .status(BookingStatus.REJECTED)
                .item(item)
                .build();
        when(entityUtils.getBookingIfExists(anyLong())).thenReturn(booking);
        when(entityUtils.getItemIfExists(anyLong())).thenReturn(item);
        var exception = assertThrows(NotFoundException.class, () -> bookingService.updateStatus(2L, 1L, false));
        assertEquals("Пользователь с ID = 2 не является владельцем вещи с ID1", exception.getMessage());
        verify(entityUtils, times(1)).getBookingIfExists(anyLong());
        verify(entityUtils, times(1)).getItemIfExists(anyLong());
    }

    @Test
    void updateStatus() {
        var item = Item.builder()
                .id(1L)
                .owner(User.builder().id(1L).build())
                .build();
        var booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2020, 1, 1, 1, 1))
                .end(LocalDateTime.of(2020, 2, 1, 1, 1))
                .status(BookingStatus.REJECTED)
                .item(item)
                .booker(User.builder().id(2L).build())
                .build();
        when(entityUtils.getBookingIfExists(anyLong())).thenReturn(booking);
        when(entityUtils.getItemIfExists(anyLong())).thenReturn(item);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        var actualDto = bookingService.updateStatus(1L, 1L, true);
        assertEquals(BookingStatus.APPROVED, actualDto.getStatus());
        verify(entityUtils, times(1)).getBookingIfExists(anyLong());
        verify(entityUtils, times(1)).getItemIfExists(anyLong());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void findByIdNotAvailableForView() {
        var booking = Booking.builder()
                .booker(User.builder().id(1L).build())
                .item(Item.builder().owner(User.builder().id(2L).build()).build())
                .build();
        when(entityUtils.getBookingIfExists(anyLong())).thenReturn(booking);
        var exception = assertThrows(NotFoundException.class, () -> bookingService.findById(1L, 3L));
        assertEquals("Букинг с ID = 1 не доступен для просмотра", exception.getMessage());
        verify(entityUtils, times(1)).getBookingIfExists(anyLong());
    }

    @Test
    void findById() {
        var booking = Booking.builder()
                .booker(User.builder().id(1L).build())
                .item(Item.builder().owner(User.builder().id(2L).build()).build())
                .start(LocalDateTime.of(2020, 1, 1, 1, 1))
                .end(LocalDateTime.of(2020, 2, 1, 1, 1))
                .status(BookingStatus.APPROVED)
                .build();
        when(entityUtils.getBookingIfExists(anyLong())).thenReturn(booking);
        assertEquals(BookingMapper.toBookingDto(booking), bookingService.findById(1L, 1L));
        verify(entityUtils, times(1)).getBookingIfExists(anyLong());
    }
}
