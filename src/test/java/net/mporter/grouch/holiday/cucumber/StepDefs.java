package net.mporter.grouch.holiday.cucumber;

import com.amazonaws.DefaultRequest;
import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.http.AmazonHttpClient;
import com.amazonaws.http.ExecutionContext;
import com.amazonaws.http.HttpMethodName;
import com.amazonaws.http.HttpResponseHandler;
import com.amazonaws.http.response.AwsResponseHandlerAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import net.mporter.grouch.holiday.cucumber.error.HolidayAlreadyExistsException;
import net.mporter.grouch.holiday.cucumber.error.HolidayNotFoundException;
import net.mporter.grouch.holiday.cucumber.handler.GetHolidayResponseHandler;
import net.mporter.grouch.holiday.cucumber.handler.ErrorResponseHandler;
import net.mporter.grouch.holiday.cucumber.handler.GetHolidaysResponseHandler;
import net.mporter.grouch.holiday.model.CreateHolidayRequest;
import net.mporter.grouch.holiday.model.GetHolidayResponse;
import net.mporter.grouch.holiday.model.GetHolidaysResponse;
import net.mporter.grouch.holiday.model.Holiday;
import net.mporter.grouch.holiday.model.UpdateHolidayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.net.URI;

import static org.junit.Assert.*;

@Slf4j
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

    @Given("^a valid request for a holiday$")
    public void validHolidayRequest() {
        request = new DefaultRequest<Void>(serviceName);
        request.setHttpMethod(HttpMethodName.GET);
        request.setEndpoint(URI.create(endpoint+"/v1/holidays/holiday"));
        signRequest(request);
    }

    @Given("^the holiday exists$")
    public void createHoliday() throws JsonProcessingException {
        Request<CreateHolidayRequest> request = newCreateRequest();

        amazonHttpClient
                .requestExecutionBuilder()
                .executionContext(new ExecutionContext(true))
                .errorResponseHandler(new ErrorResponseHandler())
                .request(request)
                .execute();
    }

    @Given("^a valid request for all holidays$")
    public void validGetHolidaysRequest() {
        request = new DefaultRequest<Void>(serviceName);
        request.setHttpMethod(HttpMethodName.GET);
        request.setEndpoint(URI.create(endpoint+"/v1/holidays"));
        signRequest(request);
    }

    @Given("^a valid request to create a new holiday$")
    public void validCreateRequest() throws JsonProcessingException {
        request = newCreateRequest();
    }

    @Given("^a valid request to update a holiday$")
    public void validUpdateRequest() throws JsonProcessingException {
        request = new DefaultRequest<>(serviceName);
        request.setHttpMethod(HttpMethodName.PUT);
        request.setEndpoint(URI.create(endpoint+"/v1/holidays/"+holiday.getName()));
        request.getHeaders().put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        holiday.setRouteDelays("There are now delays!");
        UpdateHolidayRequest updateHolidayRequest = new UpdateHolidayRequest(holiday);


        byte[] content = objectMapper.writeValueAsBytes(updateHolidayRequest);
        request.setContent(new ByteArrayInputStream(content));
        signRequest(request);
    }

    @When("^a user requests for a holiday$")
    public void requestHoliday() {
        submitRequest(new GetHolidayResponseHandler());
    }

    @When("^a user requests for all holidays$")
    public void requestGetAllHolidays() throws JsonProcessingException {
        createHoliday();
        submitRequest(new GetHolidaysResponseHandler());
    }

    @When("^a user requests to create a holiday$")
    public void requestCreateHoliday() {
        submitRequest(null);
    }

    @When("^a user requests to update a holiday$")
    public void requestUpdateHoliday() {
        submitRequest(null);
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

    @Then("^all holidays are returned$")
    public void validateAllHoldiays() {
        GetHolidaysResponse getHolidaysResponse = (GetHolidaysResponse) response.getAwsResponse();
        assertEquals(HttpStatus.OK.value(), response.getHttpResponse().getStatusCode());
        assertTrue(getHolidaysResponse.getData().contains(holiday));
    }

    @Then("^the holiday is created$")
    public void validateHolidayIsCreated() {
        int createdStatus = HttpStatus.CREATED.value();
        int updatedStatus = HttpStatus.OK.value();
        assertTrue(
            response.getHttpResponse().getStatusCode() == createdStatus ||
                    response.getHttpResponse().getStatusCode() == updatedStatus
        );

        validHolidayRequest();
        requestHoliday();
        validateHoliday();
    }

    @Then("^the holiday is not created$")
    public void validateHolidayNotCreated() {
        assertNotNull(exception);
        assertTrue(exception instanceof HolidayAlreadyExistsException);
    }

    @Then("^the holiday is updated$")
    public void validateHolidayUpdated(){
        assertEquals(HttpStatus.OK.value(), response.getHttpResponse().getStatusCode());
        validHolidayRequest();
        requestHoliday();
        validateHoliday();
    }

    private void submitRequest(HttpResponseHandler httpResponseHandler) {
        try {
            response = amazonHttpClient
                    .requestExecutionBuilder()
                    .executionContext(new ExecutionContext(true))
                    .errorResponseHandler(new ErrorResponseHandler())
                    .request(request)
                    .execute(httpResponseHandler);
        } catch (Exception e) {
            log.warn("Error thrown.", e);
            exception = e;
        }
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

    private Request<CreateHolidayRequest> newCreateRequest() throws JsonProcessingException {
        Request<CreateHolidayRequest> request = new DefaultRequest<>(serviceName);
        request.setHttpMethod(HttpMethodName.POST);
        request.setEndpoint(URI.create(endpoint+"/v1/holidays"));
        request.getHeaders().put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        CreateHolidayRequest createHolidayRequest = new CreateHolidayRequest(holiday);


        byte[] content = objectMapper.writeValueAsBytes(createHolidayRequest);
        request.setContent(new ByteArrayInputStream(content));
        signRequest(request);
        return request;
    }
}
