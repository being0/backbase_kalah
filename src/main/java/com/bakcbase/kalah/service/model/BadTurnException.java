package com.bakcbase.kalah.service.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Raised when it is not user turn.
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/11/2020
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class BadTurnException extends BusinessException {

    public BadTurnException() {
    }

    public BadTurnException(String message) {
        super(message);
    }

    public BadTurnException(String message, Throwable cause) {
        super(message, cause);
    }
}
