package net.mporter.grouch.holiday.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Supplier;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HolidayDoesNotExistException extends RuntimeException implements Supplier<RuntimeException> {
    public HolidayDoesNotExistException(final String message) {
        super(message);
    }

    @Override
    public HolidayDoesNotExistException get() {
        return this;
    }
}
