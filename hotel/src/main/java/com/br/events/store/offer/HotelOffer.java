package com.br.events.store.offer;

import br.net.eventstore.EventStore;
import br.net.eventstore.EventStoreBuilder;
import br.net.eventstore.provider.RedisProvider;
import br.net.eventstore.publisher.RedisPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class HotelOffer {

    private static final String REDIS_URL = "redis://127.0.0.1:6379";
    private static final String AGGREGATION_NAME = "orders";
    private EventStore eventStore;

    @PostConstruct
    public void init() {
        this.eventStore = new EventStoreBuilder()
                .setProvider(new RedisProvider(REDIS_URL))
                .setPublisher(new RedisPublisher(REDIS_URL))
                .createEventStore();

        listenerEvent();
    }

    private void listenerEvent() {
        eventStore.subscribe(AGGREGATION_NAME, message -> {
            System.out.println(message.getAggregation() + "/" + message.getStreamId());
            System.out.println(message.getEvent().getPayload());
        });
    }

}
