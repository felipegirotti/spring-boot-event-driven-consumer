package com.drz.search.config;

import com.drz.search.infrastructure.listener.ClientListenerRabbitImpl;
import com.drz.search.persistence.repository.SearchRepository;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "client.listener")
@Data
public class ClientListenerConfig {

    private String topicName;

    private String topicDeleteName;

    @Bean
    public ClientListenerRabbitImpl clientListener(SearchRepository searchRepository) {
        return new ClientListenerRabbitImpl(searchRepository, topicName, topicDeleteName);
    }
}
