package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String, String> handleBaseValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errResponse = new HashMap<>();

        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        log.error("Ошибка валидации: {} ", errors);

        errResponse.put("error", errors.toString());
        return errResponse;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({DuplicatedDataException.class, ValidationException.class})
    public Map<String, String> handleInternalErrorException(RuntimeException ex) {
        log.error("Ошибка: {} ", ex.getMessage());
        return createErrorResponse(ex);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public Map<String, String> handleAccessDeniedException(RuntimeException ex) {
        log.error("Ошибка: {} ", ex.getMessage());
        return createErrorResponse(ex);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public Map<String, String> handleNotFoundException(RuntimeException ex) {
        log.error("Ошибка: {} ", ex.getMessage());
        return createErrorResponse(ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Throwable.class})
    public Map<String, String> handleInternalErrorException(Throwable ex) {
        log.error("Ошибка: {} ", ex.getMessage());
        return createErrorResponse(new Exception("Произошла непредвиденная ошибка"));
    }

    private Map<String, String> createErrorResponse(Exception e) {
        Map<String, String> errResponse = new HashMap<>();
        errResponse.put("error", e.getMessage());
        return errResponse;
    }
}
