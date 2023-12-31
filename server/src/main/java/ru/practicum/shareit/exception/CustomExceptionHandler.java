package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler({AlreadyExistException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseError conflictHandle(Exception exception) {
        log.error(exception.getMessage());
        return new ResponseError(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError notFoundHandle(Exception exception) {
        log.error(exception.getMessage());
        return new ResponseError(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserOwnershipException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseError notUserOwnership(Exception exception) {
        log.error(exception.getMessage());
        return new ResponseError(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({NotAvailableException.class,
            BadRequestException.class,
            MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError badRequestHandle(Exception exception) {
        log.error(exception.getMessage());
        return new ResponseError(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError internalServerErrorHandler(Throwable exception) {
        log.error(Arrays.toString(exception.getStackTrace()));
        return new ResponseError(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
