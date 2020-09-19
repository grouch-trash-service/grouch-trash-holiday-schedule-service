package net.mporter.grouch.holiday.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class HolidayAlreadyExistException extends IllegalStateException {
    public HolidayAlreadyExistException(final String message) {
        super(message);
    }
}
