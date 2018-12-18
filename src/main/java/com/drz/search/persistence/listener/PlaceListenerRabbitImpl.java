package com.drz.search.persistence.listener;

import com.drz.search.dto.place.PlaceDTO;
import com.drz.search.persistence.repository.SearchRepository;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

public class PlaceListenerRabbitImpl {

    private SearchRepository searchRepository;

    private String routingKeySave;

    private String routingKeyDelete;

    public PlaceListenerRabbitImpl(SearchRepository searchRepository, String routingKeySave, String routingKeyDelete) {
        this.searchRepository = searchRepository;
        this.routingKeySave = routingKeySave;
        this.routingKeyDelete = routingKeyDelete;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${place.listener.queue-name}", durable = "true"),
            exchange = @Exchange(value = "${place.listener.topic-exchange-name}", ignoreDeclarationExceptions = "true",  type = ExchangeTypes.TOPIC)
    ))
    public void receive(@Header("amqp_receivedRoutingKey") String routingKey, @Payload PlaceDTO placeDTO) {
        System.out.println("routingKey:" + routingKey + " --- " + this.routingKeySave);
        System.out.println("OBJ:" + placeDTO);

        if (routingKeySave.equals(routingKey)) {
            searchRepository.save(placeDTO);
            return;
        }

        if (routingKeyDelete.equals(routingKey)) {
            searchRepository.delete(placeDTO.getId());
        }
    }
}
