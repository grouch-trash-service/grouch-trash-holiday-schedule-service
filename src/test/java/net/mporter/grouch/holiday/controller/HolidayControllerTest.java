package net.mporter.grouch.holiday.controller;

import net.mporter.grouch.holiday.model.*;
import net.mporter.grouch.holiday.service.HolidayService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HolidayControllerTest {

    @Mock
    UriComponentsBuilder uriComponentsBuilder;

    @Mock
    UriComponents uriComponents;

    @Mock
    HolidayService holidayService;

    private Holiday holiday;

    @Before
    public void setup() {
        holiday = new Holiday();
        holiday.setName("christmas");
        holiday.setRouteDelays("No Delays!");
    }

    @Test
    @DisplayName("should create holiday")
    public void testCreateHoliday() throws URISyntaxException {
        HolidayController holidayController = new HolidayController(holidayService);
        CreateHolidayRequest createHolidayRequest = new CreateHolidayRequest(holiday);
        URI uri = new URI("http://localhost:8080/holidays/"+holiday.getName());
        mockUriComponentsBuilderToReturn(uri);

        ResponseEntity<Void> response = holidayController.createHoliday(createHolidayRequest, uriComponentsBuilder);

        ResponseEntity<Void> expectedResponse = ResponseEntity.created(uri).build();
        assertEquals(expectedResponse, response);

        verify(holidayService).create(holiday);
    }

    @Test
    @DisplayName("should update holiday")
    public void testUpdateHoliday() {
        HolidayController holidayController = new HolidayController(holidayService);
        UpdateHolidayRequest updateHolidayRequest = new UpdateHolidayRequest(holiday);

        ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();

        ResponseEntity<Void> response = holidayController.updateHoliday(updateHolidayRequest, holiday.getName());

        assertEquals(expectedResponse, response);
        verify(holidayService).createOrUpdate(holiday);
    }

    @Test
    @DisplayName("should delete holiday")
    public void testDeleteHoliday() {
        HolidayController holidayController = new HolidayController(holidayService);

        ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();
        ResponseEntity<Void> response = holidayController.deleteHoliday(holiday.getName());

        assertEquals(expectedResponse, response);
        Holiday deletedHoliday = new Holiday();
        deletedHoliday.setName(holiday.getName());
        verify(holidayService).delete(deletedHoliday);
    }

    @Test
    @DisplayName("should get holiday")
    public void testGetHoliday() {
        doReturn(holiday).when(holidayService).fetchHoliday(holiday.getName());
        HolidayController holidayController = new HolidayController(holidayService);
        GetHolidayResponse getHolidayResponse = holidayController.getHoliday(holiday.getName());

        assertEquals(holiday, getHolidayResponse.getData());
    }

    @Test
    @DisplayName("should get all holidays")
    public void testGetHolidays() {
        List<Holiday> holidays = Collections.singletonList(holiday);
        doReturn(holidays).when(holidayService).fetchAllHolidays();
        HolidayController holidayController = new HolidayController(holidayService);
        GetHolidaysResponse getHolidayResponse = holidayController.getHolidays();

        assertEquals(holidays, getHolidayResponse.getData());
    }



    private void mockUriComponentsBuilderToReturn(URI uri) {
        doReturn(uriComponentsBuilder).when(uriComponentsBuilder).path("/{id}");
        doReturn(uriComponents).when(uriComponentsBuilder).buildAndExpand("christmas");
        doReturn(uri).when(uriComponents).toUri();
    }

}