package com.br.events.store.endpoint;

import com.br.events.store.endpoint.request.OrderRequest;
import com.br.events.store.endpoint.response.OrderResponse;
import com.br.events.store.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.apache.logging.log4j.util.Strings.EMPTY;

@RestController
@RequestMapping(value = "orders")
public class OrderEndpoint {

    @Autowired
    private EventService eventService;

    @RequestMapping(method = RequestMethod.POST)
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = createOrderResponse(orderRequest, EMPTY);

        eventService.writeEventFor(orderResponse);

        return orderResponse;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{orderId}")
    public OrderResponse getOrder(@PathVariable String orderId) {
        return eventService.createOrderFromEventStore(orderId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{orderId}")
    public OrderResponse updateOrder(@PathVariable String orderId, @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = createOrderResponse(orderRequest, orderId);

        eventService.writeEventFor(orderResponse);

        return orderResponse;
    }

    private OrderResponse createOrderResponse(OrderRequest orderRequest, String orderId) {
        OrderResponse orderResponse = new OrderResponse();

        orderResponse.id = orderId.isEmpty() ? UUID.randomUUID().toString() : orderId;
        orderResponse.type = orderRequest.type;
        orderResponse.checkinDate = orderRequest.checkinDate;
        orderResponse.checkoutDate = orderRequest.checkoutDate;
        orderResponse.offers = orderRequest.offers;

        return orderResponse;
    }

}
