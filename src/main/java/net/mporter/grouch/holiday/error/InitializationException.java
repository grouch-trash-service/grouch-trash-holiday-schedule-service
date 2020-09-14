package net.mporter.grouch.holiday.error;

public class InitializationException extends RuntimeException {
    public InitializationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
