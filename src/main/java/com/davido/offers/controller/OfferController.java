package com.davido.offers.controller;

import com.davido.offers.model.Offer;

import javax.inject.Inject;

import com.davido.offers.service.OfferService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * REST interface to the Offer Service
 */
@RestController
public class OfferController {

    private final OfferService offerService;

    @Inject
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @RequestMapping(value = "/offers")
    public List<Offer> retrieveAllOffers() {
        return offerService.retrieveAllOffers();
    }

    @RequestMapping("/offers/{id}")
    public Offer retrieveOffer(@PathVariable(value="id") String id) {
        return offerService.retrieveOffer(id);
    }

    @RequestMapping("/createOffer")
    public void createOffer(@RequestParam(value="id") String id, @RequestParam(value="description") String description, @RequestParam(value="expiryDate") String expiryDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime formattedExpiryDate = LocalDateTime.parse(expiryDate, dateTimeFormatter);

        offerService.createOffer(id, description, formattedExpiryDate);
    }

    @RequestMapping("/expireOffer/{id}")
    public void expireOffer(@PathVariable(value="id") String id) {
        offerService.expireOffer(id);
    }

}
