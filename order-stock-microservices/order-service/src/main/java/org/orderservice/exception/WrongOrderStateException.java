package org.orderservice.exception;

public class WrongOrderStateException extends RuntimeException {
    public WrongOrderStateException(String message) {
        super(message);
    }
}
