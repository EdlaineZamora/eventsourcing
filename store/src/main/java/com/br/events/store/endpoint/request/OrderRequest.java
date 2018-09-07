package com.br.events.store.endpoint.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRequest {

    public String type;
    public String checkinDate;
    public String checkoutDate;

}
