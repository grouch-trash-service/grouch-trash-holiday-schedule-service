package net.mporter.grouch.holiday.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import net.mporter.grouch.holiday.model.Holiday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class HolidayTableConfig {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    private static final long READ_CAPACITY_UNITS = 10L;
    private static final long WRITE_CAPACITY_UNITS = 10L;

    @PostConstruct
    public void createTables() {
        if (!isTable("Holiday")) {
            amazonDynamoDB.createTable(createTableRequest());
        }
    }

    private CreateTableRequest createTableRequest() {
        return dynamoDBMapper.generateCreateTableRequest(Holiday.class)
                .withProvisionedThroughput(
                        new ProvisionedThroughput(READ_CAPACITY_UNITS, WRITE_CAPACITY_UNITS)
                ).withTableName("Holiday");
    }
    private boolean isTable(final String table) {
        return amazonDynamoDB.listTables().getTableNames().contains(table);
    }
}
