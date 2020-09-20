package net.mporter.grouch.holiday.cucumber.handler;

import com.amazonaws.http.HttpResponse;
import com.amazonaws.http.HttpResponseHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.mporter.grouch.holiday.model.GetHolidayResponse;

public class GetHolidayResponseHandler implements HttpResponseHandler<GetHolidayResponse> {
    @Override
    public GetHolidayResponse handle(HttpResponse httpResponse) throws Exception {
        if(httpResponse.getStatusCode() != 200) {
            return null;
        } else {
            return new ObjectMapper().readValue(httpResponse.getContent(), GetHolidayResponse.class);
        }
    }

    @Override
    public boolean needsConnectionLeftOpen() {
        return false;
    }
}
