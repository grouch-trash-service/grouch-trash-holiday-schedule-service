package net.mporter.grouch.holiday.service;

import net.mporter.grouch.holiday.error.HolidayAlreadyExistException;
import net.mporter.grouch.holiday.model.Holiday;
import net.mporter.grouch.holiday.repository.HolidayRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HolidayRepositoryService implements HolidayService {

    private final HolidayRepository holidayRepository;

    public HolidayRepositoryService(final HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    @Override
    public Holiday fetchHoliday(final String holiday) {
        return holidayRepository.findById(holiday).orElse(null);
    }

    @Override
    public List<Holiday> fetchAllHolidays() {
        List<Holiday> holidays = new ArrayList<>();
        holidayRepository.findAll().forEach(holidays::add);
        return holidays;
    }

    @Override
    public void createOrUpdate(final Holiday holiday) {
        holidayRepository.save(holiday);
    }

    @Override
    public void create(final Holiday holiday) {
        if (holidayRepository.existsById(holiday.getName())) {
            throw new HolidayAlreadyExistException(
                    String.format("Holiday '%s' already exists.", holiday.getName()));
        } else {
            holidayRepository.save(holiday);
        }
    }

    @Override
    public void delete(final Holiday holiday) {
        holidayRepository.delete(holiday);
    }
}
