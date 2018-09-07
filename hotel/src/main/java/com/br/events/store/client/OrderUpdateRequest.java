package com.br.events.store.client;

import java.util.List;

public class OrderUpdateRequest {

    public List<Offer> offers;

    public static class Offer {
        public String price;
        public String name;
    }

}
