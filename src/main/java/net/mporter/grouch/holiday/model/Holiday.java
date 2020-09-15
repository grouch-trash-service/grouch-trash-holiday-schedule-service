package net.mporter.grouch.holiday.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

@Data
@DynamoDBTable(tableName = "Holiday")
public class Holiday {

    @DynamoDBHashKey
    private String name;
    @DynamoDBAttribute
    private String routeDelays;
}
