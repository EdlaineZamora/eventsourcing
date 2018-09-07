package com.br.events.store.offer;

import br.net.eventstore.EventStore;
import br.net.eventstore.EventStoreBuilder;
import br.net.eventstore.model.Message;
import br.net.eventstore.provider.RedisProvider;
import br.net.eventstore.publisher.RedisPublisher;
import com.br.events.store.client.OrderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static java.util.Arrays.asList;

@Component
public class OfferService {

    private static final String REDIS_URL = "redis://127.0.0.1:6379";
    private static final String AGGREGATION_NAME = "orders";
    private EventStore eventStore;

    @Autowired
    private OrderClient orderClient;

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

            Offers offers = generateOffersFor();

            if (isTypeHotel(message)) {
                orderClient.updateWithOffers(offers, message.getStreamId());
            }
        });
    }

    private boolean isTypeHotel(Message message) {
        return message.getEvent().getPayload().contains("\"type\":\"hotel\"");
    }

    private Offers generateOffersFor() {
        Offer offer = new Offer();
        offer.price = "1.000,00";
        offer.name = "Copacabana Palace";

        return new Offers(asList(offer));
    }

}
