package net.mporter.grouch.holiday.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import net.mporter.grouch.holiday.repository.HolidayRepository;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableDynamoDBRepositories(basePackageClasses = HolidayRepository.class)
@Profile("local")
public class LocalDynamoDBConfig {

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AwsClientBuilder.EndpointConfiguration endpointConfiguration
                = new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "");
          return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration).build();
    }
}
