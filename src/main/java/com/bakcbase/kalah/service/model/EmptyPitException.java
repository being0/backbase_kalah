package com.bakcbase.kalah.service.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/11/2020
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class EmptyPitException extends BusinessException {

    public EmptyPitException() {
    }

    public EmptyPitException(String message) {
        super(message);
    }

    public EmptyPitException(String message, Throwable cause) {
        super(message, cause);
    }
}
