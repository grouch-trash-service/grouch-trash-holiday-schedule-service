package net.mporter.grouch.holiday.controller;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        log.info("Creating holiday holiday={}", createHolidayRequest.getData());
        Holiday holiday = createHolidayRequest.getData();
        holidayService.create(holiday);
        URI location = uriComponentsBuilder.path("/{id}").buildAndExpand(holiday.getName()).toUri();
        log.info("Holiday created. holiday={}, location={}", holiday, location.toString());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{name}")
    public ResponseEntity<Void> updateHoliday(@RequestBody final UpdateHolidayRequest updateHolidayRequest,
                                              @PathVariable final String name) {
        log.info("Updating holiday. holiday={} name={}", updateHolidayRequest.getData(), name);
        holidayService.createOrUpdate(updateHolidayRequest.getData());
        log.info("Holiday updated. holiday={}", updateHolidayRequest.getData());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteHoliday(@PathVariable final String name) {
        log.info("Deleting holiday. holiday={}", name);
        Holiday holiday = new Holiday();
        holiday.setName(name);
        holidayService.delete(holiday);
        log.info("Holiday deleted. holiday={}", name);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{name}")
    public GetHolidayResponse getHoliday(@PathVariable final String name) {
        log.info("Getting holiday. holiday={}", name);
        Holiday holiday = holidayService.fetchHoliday(name);
        log.info("Got holiday. holiday={}", holiday);
        return new GetHolidayResponse(holiday);
    }

    @GetMapping
    public GetHolidaysResponse getHolidays() {
        log.info("Getting holidays.");
        List<Holiday> holidays = holidayService.fetchAllHolidays();
        log.info("Got holidays. holidays={}", holidays);
        return new GetHolidaysResponse(holidays);
    }
}
