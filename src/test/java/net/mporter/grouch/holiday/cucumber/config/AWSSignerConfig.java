package net.mporter.grouch.holiday.cucumber.config;

import com.amazonaws.auth.AWS4Signer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSSignerConfig {
    @Value("${aws.region}")
    private String region;

    @Value("${aws.servicename}")
    private String serviceName;

    @Bean
    AWS4Signer aws4Signer() {
        AWS4Signer aws4Signer = new AWS4Signer();
        aws4Signer.setRegionName(region);
        aws4Signer.setServiceName(serviceName);
        return aws4Signer;
    }
}
