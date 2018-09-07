package com.br.events.store.endpoint.request;

import com.br.events.store.endpoint.response.Offer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRequest {

    public String type;
    public String checkinDate;
    public String checkoutDate;
    public List<Offer> offers;

}
