package net.mporter.grouch.holiday.cucumber.error;

import com.amazonaws.AmazonClientException;

public class HolidayAlreadyExistsException extends AmazonClientException {
    public HolidayAlreadyExistsException(String message) {
        super(String.format(message));
    }
}