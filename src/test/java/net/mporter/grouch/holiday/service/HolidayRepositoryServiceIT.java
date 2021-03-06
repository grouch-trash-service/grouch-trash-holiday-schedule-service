package net.mporter.grouch.holiday.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import net.mporter.grouch.holiday.Application;
import net.mporter.grouch.holiday.error.HolidayDoesNotExistException;
import net.mporter.grouch.holiday.model.Holiday;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;

import javax.annotation.PostConstruct;

import java.io.File;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("local")
public class HolidayRepositoryServiceIT {

    @ClassRule
    public static DockerComposeContainer compose =
            new DockerComposeContainer(
                    new File("docker-compose.yaml"));

    @Autowired
    HolidayService holidayService;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    private Holiday holiday;

    @Before
    public void setup() {
        holiday = new Holiday();
        holiday.setName("Programming Day");
        holiday.setRouteDelays("No Delays!");

        holidayService.delete(holiday);
    }

    @After
    public void teardown() {
        holidayService.delete(holiday);
    }

    @Test
    public void testCRUD() {
       testCreate();
       testUpdate();
       testGetAll();
       testDelete();
    }

    private void testCreate() {
        holidayService.create(holiday);
        assertEquals(holiday, holidayService.fetchHoliday(holiday.getName()));
    }

    private void testUpdate() {
        String delay = "There is a delay now";
        holiday.setRouteDelays(delay);
        holidayService.createOrUpdate(holiday);
        assertEquals(holiday, holidayService.fetchHoliday(holiday.getName()));
    }

    private void testGetAll() {
        assertFalse(holidayService.fetchAllHolidays().isEmpty());
    }

    private void testDelete() {
        holidayService.delete(holiday);
        Throwable thrown = catchThrowable(() -> {
            holidayService.fetchHoliday(holiday.getName());
        });
        assertThat(thrown).isInstanceOf(HolidayDoesNotExistException.class).hasMessageContaining(holiday.getName());
    }

    @PostConstruct
    private void createHolidayTable() {
        if (!isTable("Holiday")) {
            amazonDynamoDB.createTable(createTableRequest());
        }
    }

    private CreateTableRequest createTableRequest() {
        return dynamoDBMapper.generateCreateTableRequest(Holiday.class)
                .withProvisionedThroughput(
                        new ProvisionedThroughput(5L, 5L)
                ).withTableName("Holiday");
    }
    private boolean isTable(final String table) {
        return amazonDynamoDB.listTables().getTableNames().contains(table);
    }
}