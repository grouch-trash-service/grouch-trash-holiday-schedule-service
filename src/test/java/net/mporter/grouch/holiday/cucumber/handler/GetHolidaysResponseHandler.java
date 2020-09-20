package net.mporter.grouch.holiday.cucumber.handler;

import com.amazonaws.http.HttpResponse;
import com.amazonaws.http.HttpResponseHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.mporter.grouch.holiday.model.GetHolidaysResponse;

public class GetHolidaysResponseHandler implements HttpResponseHandler<GetHolidaysResponse> {
    @Override
    public GetHolidaysResponse handle(HttpResponse httpResponse) throws Exception {
        return new ObjectMapper().readValue(httpResponse.getContent(), GetHolidaysResponse.class);
    }

    @Override
    public boolean needsConnectionLeftOpen() {
        return false;
    }
}
