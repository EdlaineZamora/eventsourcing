package com.br.events.store.event;

import br.net.eventstore.EventStore;
import br.net.eventstore.EventStoreBuilder;
import br.net.eventstore.EventStream;
import br.net.eventstore.model.Event;
import br.net.eventstore.provider.RedisProvider;
import br.net.eventstore.publisher.RedisPublisher;
import com.br.events.store.endpoint.response.OrderResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static org.apache.logging.log4j.util.Strings.EMPTY;

@Service
public class EventService {

    private static final String AGGREGATION_NAME = "orders";
    private static final String REDIS_URL = "redis://127.0.0.1:6379";
    private EventStore eventStore;

    @PostConstruct
    public void init() {
        RedisProvider redisProvider = new RedisProvider(REDIS_URL);
        RedisPublisher publisher = new RedisPublisher(REDIS_URL);

        this.eventStore = new EventStoreBuilder()
                .setProvider(redisProvider)
                .setPublisher(publisher)
                .createEventStore();
    }

    public void writeEventFor(OrderResponse orderResponse) {
        String eventPayload = createEventPayloadFor(orderResponse);

        Event event = new Event(eventPayload);

        EventStream ordersEventStream = eventStore.getEventStream(AGGREGATION_NAME, orderResponse.id);
        ordersEventStream.addEvent(event);

        System.out.println("Event Created - " + event.getPayload());
    }

    private String createEventPayloadFor(OrderResponse orderResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(orderResponse);
        } catch (JsonProcessingException jsonProcessingException) {
            return EMPTY;
        }
    }

    public OrderResponse createOrderFromEventStore(String orderId) {
        EventStream ordersEventStream = eventStore.getEventStream(AGGREGATION_NAME, orderId);

        Event event = ordersEventStream.getEvents()
                .findFirst()
                .get();

        OrderResponse orderResponse = new OrderResponse();

        try {
            ObjectMapper mapper = new ObjectMapper();
            orderResponse = mapper.readValue(event.getPayload(), OrderResponse.class);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            return orderResponse;
        }
    }

}
