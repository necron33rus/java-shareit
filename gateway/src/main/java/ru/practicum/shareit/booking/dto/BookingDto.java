package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private Long itemId;
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime start;
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime end;
}