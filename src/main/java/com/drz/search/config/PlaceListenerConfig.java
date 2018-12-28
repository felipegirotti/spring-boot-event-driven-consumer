package com.drz.search.config;


import com.drz.search.infrastructure.listener.PlaceListenerRabbitImpl;
import com.drz.search.persistence.repository.SearchRepository;
import lombok.Data;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
@ConfigurationProperties(prefix = "place.listener")
@Data
public class PlaceListenerConfig extends SpringBootServletInitializer implements RabbitListenerConfigurer {

    private String topicExchangeName;

    private String topicName;

    private String topicDeleteName;

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }
    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConverter());
        return factory;
    }

    @Bean
    public PlaceListenerRabbitImpl placeListener(SearchRepository searchRepository) {
        return new PlaceListenerRabbitImpl(searchRepository, topicName, topicDeleteName);
    }


    @Override
    public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }
}
