package net.mporter.grouch.holiday.cucumber;

import com.amazonaws.DefaultRequest;
import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.http.AmazonHttpClient;
import com.amazonaws.http.ExecutionContext;
import com.amazonaws.http.HttpMethodName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.mporter.grouch.holiday.cucumber.error.HolidayNotFoundException;
import net.mporter.grouch.holiday.cucumber.handler.GetHolidayResponseHandler;
import net.mporter.grouch.holiday.cucumber.handler.ErrorResponseHandler;
import net.mporter.grouch.holiday.model.CreateHolidayRequest;
import net.mporter.grouch.holiday.model.GetHolidayResponse;
import net.mporter.grouch.holiday.model.Holiday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.net.URI;

import static org.junit.Assert.*;

public class StepDefs extends SpringCucumberContext {
    @Autowired
    AmazonHttpClient amazonHttpClient;

    @Autowired
    AWSCredentialsProvider awsCredentialsProvider;

    @Autowired
    AWS4Signer aws4Signer;

    @Value("${aws.endpoint}")
    private String endpoint;

    @Value("${aws.servicename}")
    private String serviceName;

    private Request request;
    private Response response;
    private Exception exception;
    private Holiday holiday;

    @Before
    public void setup() {
        holiday = new Holiday();
        holiday.setName("holiday");
        holiday.setRouteDelays("No Delays!");
    }

    @After
    public void teardown() {
        deleteHoliday();
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Given("^A valid request for a holiday$")
    public void validHolidayRequest() {
        request = new DefaultRequest<Void>(serviceName);
        request.setHttpMethod(HttpMethodName.GET);
        request.setEndpoint(URI.create(endpoint+"/v1/holidays/holiday"));
        signRequest(request);
    }

    @Given("^the holiday exists$")
    public void createHoliday() throws JsonProcessingException {
        Request<CreateHolidayRequest> request = new DefaultRequest<CreateHolidayRequest>(serviceName);
        request.setHttpMethod(HttpMethodName.POST);
        request.setEndpoint(URI.create(endpoint+"/v1/holidays"));
        request.getHeaders().put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        CreateHolidayRequest createHolidayRequest = new CreateHolidayRequest(holiday);


        byte[] content = objectMapper.writeValueAsBytes(createHolidayRequest);
        request.setContent(new ByteArrayInputStream(content));
        signRequest(request);

        amazonHttpClient
                .requestExecutionBuilder()
                .executionContext(new ExecutionContext(true))
                .errorResponseHandler(new ErrorResponseHandler())
                .request(request)
                .execute();
    }

    @When("^Request for a holiday$")
    public void requestHoliday() {
        try {
            response = amazonHttpClient
                    .requestExecutionBuilder()
                    .executionContext(new ExecutionContext(true))
                    .errorResponseHandler(new ErrorResponseHandler())
                    .request(request)
                    .execute(new GetHolidayResponseHandler());
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("^a holiday is not returned$")
    public void errorIsThrown() {
        assertNotNull(exception);
        assertTrue(exception instanceof HolidayNotFoundException);
    }

    @Then("^a holiday is returned$")
    public void validateHoliday() {
        GetHolidayResponse getHolidayResponse = (GetHolidayResponse) response.getAwsResponse();
        assertEquals(HttpStatus.OK.value(), response.getHttpResponse().getStatusCode());
        assertEquals(holiday, getHolidayResponse.getData());
    }

    private void signRequest(Request request) {
        aws4Signer.sign(request, awsCredentialsProvider.getCredentials());
    }

    private void deleteHoliday() {
        Request<Void> request = new DefaultRequest<>(serviceName);
        request.setHttpMethod(HttpMethodName.DELETE);
        request.setEndpoint(URI.create(endpoint+"/v1/holidays/holiday"));

        signRequest(request);

        amazonHttpClient
                .requestExecutionBuilder()
                .executionContext(new ExecutionContext(true))
                .errorResponseHandler(new ErrorResponseHandler())
                .request(request)
                .execute();
    }
}
