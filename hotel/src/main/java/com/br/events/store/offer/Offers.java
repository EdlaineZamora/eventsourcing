package com.br.events.store.offer;

import java.util.List;
import java.util.stream.Stream;

public class Offers {

    private final List<Offer> offers;

    public Offers(List<Offer> offers) {
        this.offers = offers;
    }

    public Stream<Offer> stream() {
        return this.offers.stream();
    }
}
