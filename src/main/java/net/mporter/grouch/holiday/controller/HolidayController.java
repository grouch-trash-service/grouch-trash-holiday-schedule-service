package net.mporter.grouch.holiday.controller;



import net.mporter.grouch.holiday.model.CreateHolidayRequest;
import net.mporter.grouch.holiday.model.GetHolidayResponse;
import net.mporter.grouch.holiday.model.GetHolidaysResponse;
import net.mporter.grouch.holiday.model.Holiday;
import net.mporter.grouch.holiday.model.UpdateHolidayRequest;
import net.mporter.grouch.holiday.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/holidays")
public class HolidayController {

    private final HolidayService holidayService;

    @Autowired
    public HolidayController(final HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @PostMapping
    public ResponseEntity<Void> createHoliday(@RequestBody final CreateHolidayRequest createHolidayRequest,
                                              final UriComponentsBuilder uriComponentsBuilder) {

        Holiday holiday = createHolidayRequest.getData();
        holidayService.create(holiday);
        URI location = uriComponentsBuilder.path("/{id}").buildAndExpand(holiday.getName()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{name}")
    public ResponseEntity<Void> updateHoliday(@RequestBody final UpdateHolidayRequest updateHolidayRequest,
                                              @PathVariable final String name) {
        holidayService.createOrUpdate(updateHolidayRequest.getData());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteHoliday(@PathVariable final String name) {
        Holiday holiday = new Holiday();
        holiday.setName(name);
        holidayService.delete(holiday);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{name}")
    public GetHolidayResponse getHoliday(@PathVariable final String name) {
        Holiday holiday = holidayService.fetchHoliday(name);
        return new GetHolidayResponse(holiday);
    }

    @GetMapping
    public GetHolidaysResponse getHolidays() {
        List<Holiday> holidays = holidayService.fetchAllHolidays();
        return new GetHolidaysResponse(holidays);
    }
}
