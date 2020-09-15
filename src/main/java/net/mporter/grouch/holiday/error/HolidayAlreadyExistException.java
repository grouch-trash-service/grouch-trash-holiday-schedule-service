package net.mporter.grouch.holiday.error;

public class HolidayAlreadyExistException extends IllegalStateException {
    public HolidayAlreadyExistException(final String message) {
        super(message);
    }
}
