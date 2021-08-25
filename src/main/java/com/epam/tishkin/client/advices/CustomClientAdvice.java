package com.epam.tishkin.client.advices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

@ControllerAdvice
public class CustomClientAdvice {
    final static Logger logger = LogManager.getLogger(CustomClientAdvice.class);

    @ExceptionHandler({HttpClientErrorException.class, HttpServerErrorException.class})
    public void handleHttpStatusCodeExceptions(HttpStatusCodeException e) {
        if (e.getStatusCode().equals(HttpStatus.FORBIDDEN) || e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
            logger.error("Access denied");
            return;
        }
        logger.error("Ops.... something is wrong " + e.getStatusCode());
    }
}

