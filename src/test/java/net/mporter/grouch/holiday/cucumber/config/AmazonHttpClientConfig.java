package net.mporter.grouch.holiday.cucumber.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.http.AmazonHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonHttpClientConfig {
    @Bean
    AmazonHttpClient amazonHttpClient() {
        return new AmazonHttpClient(new ClientConfiguration());
    }
}
