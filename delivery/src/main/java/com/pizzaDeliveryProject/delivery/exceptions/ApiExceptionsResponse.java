package com.pizzaDeliveryProject.delivery.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ApiExceptionsResponse extends RuntimeException {

    private final HttpStatus status;
    private final List<String> errors;

    public ApiExceptionsResponse(String message, HttpStatus status, List<String> errors) {
        super(message);
        this.status = status;
        this.errors = errors;
    }
}
