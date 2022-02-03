package org.rozenberg.craftshop.exception;

public class CustomPoolException extends Exception{
    public CustomPoolException() {
    }

    public CustomPoolException(String message) {
        super(message);
    }

    public CustomPoolException(String message, Exception cause) {
        super(message, cause);
    }

    public CustomPoolException(Exception cause) {
        super(cause);
    }
}
