package com.drz.search.infrastructure.listener;

import com.drz.search.dto.client.ClientDTO;
import com.drz.search.persistence.repository.SearchRepository;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

public class ClientListenerRabbitImpl implements ClientListener {

    private SearchRepository searchRepository;

    private String routingKeySave;

    private String routingKeyDelete;

    public ClientListenerRabbitImpl(SearchRepository searchRepository, String routingKeySave, String routingKeyDelete) {
        this.searchRepository = searchRepository;
        this.routingKeySave = routingKeySave;
        this.routingKeyDelete = routingKeyDelete;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${client.listener.queue-name}", durable = "true"),
            exchange = @Exchange(value = "${client.listener.topic-exchange-name}", ignoreDeclarationExceptions = "true",  type = ExchangeTypes.TOPIC)
    ))
    public void receive(@Header("amqp_receivedRoutingKey") String routingKey, @Payload ClientDTO clientDTO) {

        if (routingKeySave.equals(routingKey)) {
            searchRepository.save(clientDTO);
            return;
        }

        if (routingKeyDelete.equals(routingKey)) {
            searchRepository.deletePlace(clientDTO.getId());
        }
    }
}
