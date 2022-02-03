package org.rozenberg.craftshop.exception;

public class DaoException extends Exception{
    public DaoException() {
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Exception cause) {
        super(message, cause);
    }

    public DaoException(Exception cause) {
        super(cause);
    }
}
