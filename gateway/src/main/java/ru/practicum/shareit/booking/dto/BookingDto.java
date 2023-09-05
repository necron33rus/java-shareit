package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;

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


    private BookingStatus status;


    private BookingState state;
}
