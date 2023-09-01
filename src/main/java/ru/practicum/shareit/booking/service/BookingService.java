package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.utils.EntityUtils;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.EntityUtils.stateBy;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EntityUtils entityUtils;

    @Transactional
    public BookingDto create(BookingDto bookingDto, long userId) {
        var user = entityUtils.getUserIfExists(userId);
        var item = entityUtils.getItemIfExists(bookingDto.getItemId());

        if (BooleanUtils.isFalse(item.getAvailable())) {
            throw new NotAvailableException("Вещь с ID = " + item.getId() + " не доступна для бронирования");
        }

        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new BadRequestException("Не указана дата бронирования");
        }

        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) || bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new BadRequestException("Дата начала бронирования вещи позже или равно дате окончания заказа");
        }

        if (userId == item.getOwner().getId()) {
            throw new NotFoundException("Владелец не может забронировать свою собственную вещь");
        }

        return BookingMapper.toBookingDto(bookingRepository.save(BookingMapper.toBooking(bookingDto, user, item)));
    }

    @Transactional
    public BookingDto updateStatus(long userId, long bookingId, Boolean isApproved) {
        var booking = entityUtils.getBookingIfExists(bookingId);
        var item = entityUtils.getItemIfExists(booking.getItem().getId());

        if (booking.getStatus() == BookingStatus.APPROVED && Boolean.TRUE.equals(isApproved)) {
            throw new BadRequestException("Бронирование уже подтверждено владельцем");
        }

        if (item.getOwner().getId() == userId) {
            booking.setStatus(BooleanUtils.isTrue(isApproved) ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        } else {
            throw new NotFoundException("Пользователь с ID = " + userId + " не является владельцем вещи с ID" + item.getId());
        }

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Transactional(readOnly = true)
    public BookingDto findById(long bookingId, long userId) {
        var booking = entityUtils.getBookingIfExists(bookingId);

        if (!Objects.equals(booking.getBooker().getId(), userId) && !Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new NotFoundException("Букинг с ID = " + bookingId + " не доступен для просмотра");
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingDto> findByBookerAndState(long userId, String state, int from, int size) {
        entityUtils.getUserIfExists(userId);
        return pagination(from, size, findAllByState(bookingRepository.findAllByBookerId(userId), state));
    }

    @Transactional(readOnly = true)
    public List<BookingDto> findByOwnerAndState(long userId, String state, int from, int size) {
        entityUtils.getUserIfExists(userId);
        return pagination(from, size, findAllByState(bookingRepository.findAllByItemOwnerId(userId), state));
    }

    private List<BookingDto> findAllByState(List<Booking> bookings, String state) {
        return bookings.stream()
                .filter(stateBy(EntityUtils.parseState(state)))
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private List<BookingDto> pagination(int from, int size, List<BookingDto> list) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Bad params from or size for request");
        }
        return list.stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }
}
