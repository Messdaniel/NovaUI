package me.messdaniel.novaui.exception;

public class MenuException extends RuntimeException {

    public MenuException(String message) {
        super(message);
    }

    public MenuException(String message, Exception cause) {
        super(message, cause);
    }
}
