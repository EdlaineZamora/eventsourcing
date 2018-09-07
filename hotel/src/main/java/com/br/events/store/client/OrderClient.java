package com.br.events.store.client;

import com.br.events.store.offer.Offer;
import com.br.events.store.offer.Offers;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class OrderClient {

    private static final String STORE_URL = "http://localhost:8080/store/orders/";
    private RestTemplate restTemplate = new RestTemplate();

    public void updateWithOffers(Offers offers, String orderId) {
        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();
        orderUpdateRequest.offers = createOffersRequest(offers);

        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<OrderUpdateRequest> orderUpdateRequestHttpEntity = new HttpEntity<>(orderUpdateRequest, httpHeaders);
        restTemplate.exchange(STORE_URL.concat(orderId), HttpMethod.PUT, orderUpdateRequestHttpEntity,
                String.class);
    }

    private List<OrderUpdateRequest.Offer> createOffersRequest(Offers offers) {
        return offers.stream()
                .map(this::createOfferRequest)
                .collect(toList());
    }

    private OrderUpdateRequest.Offer createOfferRequest(Offer offer) {
        OrderUpdateRequest.Offer offerRequest = new OrderUpdateRequest.Offer();

        offerRequest.price = offer.price;
        offerRequest.name = offer.name;

        return offerRequest;
    }

}
