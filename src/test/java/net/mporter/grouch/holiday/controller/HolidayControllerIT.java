package net.mporter.grouch.holiday.controller;

import net.mporter.grouch.holiday.Application;
import net.mporter.grouch.holiday.error.HolidayAlreadyExistException;
import net.mporter.grouch.holiday.model.CreateHolidayRequest;
import net.mporter.grouch.holiday.model.GetHolidayResponse;
import net.mporter.grouch.holiday.model.GetHolidaysResponse;
import net.mporter.grouch.holiday.model.Holiday;
import net.mporter.grouch.holiday.model.UpdateHolidayRequest;
import net.mporter.grouch.holiday.service.HolidayService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("local")
@Import(HolidayControllerITConfig.class)
public class HolidayControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HolidayService holidayService;

    private Holiday holiday;

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        holiday = new Holiday();
        holiday.setName("Christmas");
        holiday.setRouteDelays("No Delays");

        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetHolidays() throws Exception {
        List<Holiday> holidays = Collections.singletonList(holiday);
        doReturn(holidays).when(holidayService).fetchAllHolidays();
        MvcResult result = mockMvc.perform(get("/v1/holidays"))
                .andExpect(status().isOk()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        GetHolidaysResponse getHolidaysResponse = objectMapper.readValue(responseBody, GetHolidaysResponse.class);

        assertEquals(holidays, getHolidaysResponse.getData());
    }

    @Test
    public void testGetHoliday() throws Exception {
        doReturn(holiday).when(holidayService).fetchHoliday(holiday.getName());
        MvcResult result = mockMvc.perform(get("/v1/holidays/"+ holiday.getName()))
                .andExpect(status().isOk()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        GetHolidayResponse getHolidaysResponse = objectMapper.readValue(responseBody, GetHolidayResponse.class);

        assertEquals(holiday, getHolidaysResponse.getData());
    }

    @Test
    public void testCreateHoliday() throws Exception {
        CreateHolidayRequest createHolidayRequest = new CreateHolidayRequest(holiday);
        String request = objectMapper.writeValueAsString(createHolidayRequest);
        mockMvc.perform(post("/v1/holidays")
                .content(request)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(holidayService).create(holiday);
    }

    @Test
    public void testUpdateHoliday() throws Exception {
        UpdateHolidayRequest createHolidayRequest = new UpdateHolidayRequest(holiday);
        String request = objectMapper.writeValueAsString(createHolidayRequest);
        mockMvc.perform(put("/v1/holidays/"+holiday.getName())
                .content(request)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(holidayService).createOrUpdate(holiday);
    }

    @Test
    public void testDeleteHoliday() throws Exception {
        mockMvc.perform(delete("/v1/holidays/"+holiday.getName()))
                .andExpect(status().isOk());

        holiday.setRouteDelays(null);
        verify(holidayService).delete(holiday);
    }

    @Test
    public void testAlreadyExistError() throws Exception {
        CreateHolidayRequest createHolidayRequest = new CreateHolidayRequest(holiday);
        String request = objectMapper.writeValueAsString(createHolidayRequest);
        doThrow(HolidayAlreadyExistException.class).when(holidayService).create(holiday);
        mockMvc.perform(post("/v1/holidays")
                .content(request)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

    }
}
