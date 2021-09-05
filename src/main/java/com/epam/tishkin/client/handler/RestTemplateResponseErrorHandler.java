package com.epam.tishkin.client.handler;

import com.epam.tishkin.client.exception.CustomResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR ||
                httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        String message;
        if (httpResponse.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
            message = "You need to login";
        } else if (httpResponse.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
            message = "You don't have enough rights";
        } else if (httpResponse.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            message = new BufferedReader(new InputStreamReader(httpResponse.getBody()))
                    .lines().collect(Collectors.joining("\n\r"));
        } else {
            message= "Ops... something is wrong ";
        }
        throw new CustomResponseException(httpResponse.getStatusCode() + " - " + message);
    }
}
