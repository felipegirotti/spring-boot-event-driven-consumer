package com.drz.search.config;

import com.drz.search.persistence.listener.PlaceListenerSQSImpl;
import com.drz.search.persistence.repository.SearchRepository;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "place.listener")
@Data
public class PlaceListenerSQSConfig {

    private String topicName;

    private String topicDeleteName;

    @Bean
    public PlaceListenerSQSImpl placeListenerSQS(SearchRepository searchRepository) {
        return new PlaceListenerSQSImpl(searchRepository, topicName, topicDeleteName);
    }
}
