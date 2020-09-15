package net.mporter.grouch.holiday.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Setter
@EqualsAndHashCode
@ToString
@DynamoDBTable(tableName = "Holiday")
public class Holiday {

    private String name;
    private String routeDelays;

    @DynamoDBHashKey
    public String getName() {
        return name;
    }

    @DynamoDBAttribute
    public String getRouteDelays() {
        return routeDelays;
    }
}
