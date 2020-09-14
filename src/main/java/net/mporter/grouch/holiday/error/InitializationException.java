package net.mporter.grouch.holiday.error;

public class InitializationException extends RuntimeException{
    public InitializationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
