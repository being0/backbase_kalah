package com.bakcbase.kalah.service;

import com.bakcbase.kalah.service.model.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/12/2020
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class KalahGameNotFoundException extends BusinessException {

    public KalahGameNotFoundException() {
    }

    public KalahGameNotFoundException(String message) {
        super(message);
    }

    public KalahGameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
