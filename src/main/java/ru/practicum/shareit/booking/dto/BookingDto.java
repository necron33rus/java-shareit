package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private Long id;

    @Future(message = "Дата начала не может быть в прошлом")
    private LocalDateTime start;

    @Future(message = "Дата окончания не может быть в прошлом")

    private LocalDateTime end;

    private Long itemId;

    private Item item;

    private BookingStatus status;

    private User booker;

    private BookingState state;
}
