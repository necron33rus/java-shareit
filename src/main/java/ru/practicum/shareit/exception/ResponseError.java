package ru.practicum.shareit.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
class ResponseError {

    private final String message;
    private final HttpStatus status;
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private final LocalDateTime time = LocalDateTime.now();
}