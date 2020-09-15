package net.mporter.grouch.holiday.service;

import net.mporter.grouch.holiday.model.Holiday;

import java.util.List;

public interface HolidayService {
    Holiday fetchHoliday(String holiday);
    List<Holiday> fetchAllHolidays();
    void createOrUpdate(Holiday holiday);
    void create(Holiday holiday);
    void delete(Holiday holiday);
}
