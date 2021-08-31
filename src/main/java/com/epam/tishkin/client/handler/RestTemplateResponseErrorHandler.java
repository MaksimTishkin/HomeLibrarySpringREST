package com.epam.tishkin.client.handler;

import com.epam.tishkin.client.exception.CustomResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

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
        } else {
            message= "Ops... something is wrong " + httpResponse.getStatusText();
        }
        throw new CustomResponseException(httpResponse.getStatusCode() + " - " + message);
    }
}
