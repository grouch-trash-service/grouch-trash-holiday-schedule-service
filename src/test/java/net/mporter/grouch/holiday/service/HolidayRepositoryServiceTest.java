package net.mporter.grouch.holiday.service;

import net.mporter.grouch.holiday.error.HolidayAlreadyExistException;
import net.mporter.grouch.holiday.model.Holiday;
import net.mporter.grouch.holiday.repository.HolidayRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HolidayRepositoryServiceTest {

    @Mock
    private HolidayRepository holidayRepository;

    private HolidayService holidayService;
    private String holidayName = "Christmas";

    @Before
    public void setup() {
        holidayService = new HolidayRepositoryService(holidayRepository);
    }

    @Test
    @DisplayName("Should fetch a holiday by name")
    public void testFetchHoliday() {
        Holiday expectedHoliday = expectedHoliday(holidayName);
        doReturn(Optional.of(expectedHoliday))
                .when(holidayRepository).findById(holidayName);

        Holiday holiday = holidayService.fetchHoliday(holidayName);

        assertEquals(expectedHoliday, holiday);
    }

    @Test
    @DisplayName("Should fetch all holidays")
    public void testFetchAllHolidays() {
        Holiday expectedHoliday = expectedHoliday(holidayName);
        List<Holiday> expectedHolidays = Collections.singletonList(expectedHoliday);
        doReturn(expectedHolidays).when(holidayRepository).findAll();

        List<Holiday> holidays = holidayService.fetchAllHolidays();

        assertEquals(expectedHolidays.size(), holidays.size());
        assertEquals(expectedHoliday, holidays.get(0));
    }

    @Test
    @DisplayName("Should create or update holiday")
    public void testCreateOrUpdate() {
        Holiday holiday = new Holiday();
        holidayService.createOrUpdate(holiday);

        verify(holidayRepository).save(holiday);
    }

    @Test
    @DisplayName("should create new holiday")
    public void testCreateNewHoliday() {
        Holiday holiday = new Holiday();

        holiday.setName(holidayName);
        doReturn(false).when(holidayRepository)
                .existsById(holiday.getName());

        holidayService.create(holiday);

        verify(holidayRepository).save(holiday);
    }

    @Test(expected = HolidayAlreadyExistException.class)
    @DisplayName("should throw an error if holiday already exists when trying to create")
    public void testCreateWhenHolidayExists() {
        Holiday holiday = new Holiday();
        holiday.setName(holidayName);
        doReturn(true).when(holidayRepository)
                .existsById(holiday.getName());

        holidayService.create(holiday);
    }

    @Test
    @DisplayName("should delete a holiday")
    public void testDeleteHoliday() {
        Holiday holiday = new Holiday();
        holidayService.delete(holiday);

        verify(holidayRepository).delete(holiday);
    }

    private Holiday expectedHoliday(String holidayName) {
        Holiday expectedHoliday = new Holiday();
        expectedHoliday.setName(holidayName);
        expectedHoliday.setRouteDelays("Delayed 1 day");
        return expectedHoliday;
    }
}