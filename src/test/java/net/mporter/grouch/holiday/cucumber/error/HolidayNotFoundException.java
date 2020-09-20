package net.mporter.grouch.holiday.cucumber.error;

import com.amazonaws.AmazonClientException;

public class HolidayNotFoundException extends AmazonClientException {
    public HolidayNotFoundException(String message) {
        super(String.format(message));
    }
}
