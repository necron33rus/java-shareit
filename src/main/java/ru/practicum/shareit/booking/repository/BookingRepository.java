package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerId(Long userId);

    List<Booking> findAllByItemOwnerId(long itemOwnerId);

    List<Booking> findAllByItemIdAndBookerId(long itemId, long bookerId);

    List<Booking> findAllByItemId(long itemId);
}
