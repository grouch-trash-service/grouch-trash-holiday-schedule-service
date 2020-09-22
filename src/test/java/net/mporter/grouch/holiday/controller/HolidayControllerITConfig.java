package net.mporter.grouch.holiday.controller;

import net.mporter.grouch.holiday.service.HolidayService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

public class HolidayControllerITConfig {
    @Bean
    @Primary
    public HolidayService holidayService() {
        return mock(HolidayService.class);
    }
}
