package net.mporter.grouch.holiday.cucumber.handler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.http.HttpResponseHandler;
import net.mporter.grouch.holiday.cucumber.error.HolidayNotFoundException;

public class ErrorResponseHandler implements HttpResponseHandler<AmazonClientException> {
    @Override
    public AmazonClientException handle(HttpResponse httpResponse) throws Exception {
        switch (httpResponse.getStatusCode()) {
            case 404:
                return new HolidayNotFoundException("Holiday not found");
            default:
                return new AmazonClientException("Error");
        }
    }

    @Override
    public boolean needsConnectionLeftOpen() {
        return false;
    }
}
