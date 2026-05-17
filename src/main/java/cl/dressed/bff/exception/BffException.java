package cl.dressed.bff.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BffException extends RuntimeException {

    private final HttpStatus status;

    public BffException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}