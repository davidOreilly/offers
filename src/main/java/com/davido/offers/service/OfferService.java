package com.davido.offers.service;

import com.davido.offers.model.Offer;
import com.davido.offers.repository.OfferRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service to handle communication with database layer
 */
@Component
public class OfferService {

    private static final String EXPIRED_OFFER_STATUS = "expired";
    private static final String OPEN_OFFER_STATUS = "open";
    private final OfferRepository offerRepository;

    @Inject
    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public Offer retrieveOffer(String id) {
        Offer offer = offerRepository.findById(id);
        determineExpirationStatus(offer);

        return offer;
    }

    public List<Offer> retrieveAllOffers() {
        List<Offer> offers = offerRepository.findAllOffers();

        for(Offer offer: offers) {
            determineExpirationStatus(offer);
        }

        return offers;
    }

    public void createOffer(String id, String description, LocalDateTime expiryDate) {
        offerRepository.createOffer(id, description, expiryDate);
    }

    public void expireOffer(String id) {
        offerRepository.expireOffer(id);
    }

    private void determineExpirationStatus(Offer offer) {
        if (offer.getExpiryDate().isBefore(LocalDateTime.now())) {
            offer.setStatus(EXPIRED_OFFER_STATUS);
        } else {
            offer.setStatus(OPEN_OFFER_STATUS);
        }
    }
}
