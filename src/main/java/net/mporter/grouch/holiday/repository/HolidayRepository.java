package net.mporter.grouch.holiday.repository;

import net.mporter.grouch.holiday.model.Holiday;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface HolidayRepository extends CrudRepository<Holiday, String> {
}
