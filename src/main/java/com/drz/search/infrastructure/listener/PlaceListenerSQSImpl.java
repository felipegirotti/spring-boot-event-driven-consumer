package com.drz.search.infrastructure.listener;

import com.drz.search.dto.place.PlaceDTO;
import com.drz.search.dto.place.PlaceSQSMessage;
import com.drz.search.persistence.repository.SearchRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;

import java.io.IOException;

@EnableSqs
public class PlaceListenerSQSImpl implements PlaceListener {

    private SearchRepository searchRepository;

    private String routingKeySave;

    private String routingKeyDelete;

    private ObjectMapper objectMapper = new ObjectMapper();

    public PlaceListenerSQSImpl(SearchRepository searchRepository, String routingKeySave, String routingKeyDelete) {
        this.searchRepository = searchRepository;
        this.routingKeySave = routingKeySave;
        this.routingKeyDelete = routingKeyDelete;
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    @SqsListener("${sqs.queue-name}")
    public void receive(String message) {
        try {
            PlaceSQSMessage placeSQSMessage = objectMapper.reader().forType(PlaceSQSMessage.class).readValue(message);
            PlaceDTO placeDTO = objectMapper.reader().forType(PlaceDTO.class).readValue(placeSQSMessage.getMessage());
            if (routingKeySave.equals(placeSQSMessage.getSubject())) {
                searchRepository.save(placeDTO);
                return;
            }

            if (routingKeyDelete.equals(placeSQSMessage.getSubject())) {
                searchRepository.delete(placeDTO.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
