package com.br.events.store.endpoint;

import com.br.events.store.endpoint.request.OrderRequest;
import com.br.events.store.endpoint.response.OrderResponse;
import com.br.events.store.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "orders")
public class OrderEndpoint {

    @Autowired
    private EventService eventService;

    @RequestMapping(method = RequestMethod.POST)
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = new OrderResponse();

        createOrderResponse(orderRequest, orderResponse);

        eventService.writeEventFor(orderResponse);

        return orderResponse;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{orderId}")
    public OrderResponse getOrder(@PathVariable String orderId) {
        return eventService.createOrderFromEventStore(orderId);
    }

    private void createOrderResponse(@RequestBody OrderRequest orderRequest, OrderResponse orderResponse) {
        orderResponse.id = UUID.randomUUID().toString();
        orderResponse.type = orderRequest.type;
        orderResponse.checkinDate = orderRequest.checkinDate;
        orderResponse.checkoutDate = orderRequest.checkoutDate;
    }

}
