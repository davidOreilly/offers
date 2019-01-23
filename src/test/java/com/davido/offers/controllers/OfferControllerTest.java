package com.davido.offers.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.davido.offers.controller.OfferController;
import com.davido.offers.model.Offer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferController offerController;

    @Test
    public void retrieveAllOffersReturnsMultipleOffersWithExpectedValues() throws Exception {
        LocalDateTime expiryDate = LocalDateTime.of(2019, Month.JANUARY, 29, 10, 15, 59);
        Offer offerOne = createOffer("1357", "Test offer", "open", expiryDate);
        Offer offerTwo = createOffer("2468", "Another test offer", "expired", LocalDateTime.now());

        List<Offer> allOffers = new ArrayList<>();
        allOffers.add(offerOne);
        allOffers.add(offerTwo);

        given(offerController.retrieveAllOffers()).willReturn(allOffers);

        mockMvc.perform(get("/offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(offerOne.getId())))
                .andExpect(jsonPath("$[0].description", is(offerOne.getDescription())))
                .andExpect(jsonPath("$[0].status", is(offerOne.getStatus())))
                .andExpect(jsonPath("$[0].expiryDate", is(offerOne.getExpiryDate().toString())))
                .andExpect(jsonPath("$[1].id", is(offerTwo.getId())))
                .andExpect(jsonPath("$[1].description", is(offerTwo.getDescription())))
                .andExpect(jsonPath("$[1].status", is(offerTwo.getStatus())))
                .andExpect(jsonPath("$[1].expiryDate", is(offerTwo.getExpiryDate().toString())));
    }

    @Test
    public void retrieveOfferReturnsOfferWithExpectedValues() throws Exception {
        LocalDateTime expiryDate = LocalDateTime.of(2019, Month.MARCH, 20, 12, 30, 59);
        Offer offer = createOffer("12345", "Yet another offer", "open", expiryDate) ;

        given(offerController.retrieveOffer(offer.getId())).willReturn(offer);

        mockMvc.perform(get("/offers/" + offer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(offer.getId())))
                .andExpect(jsonPath("description", is(offer.getDescription())))
                .andExpect(jsonPath("status", is(offer.getStatus())))
                .andExpect(jsonPath("expiryDate", is(offer.getExpiryDate().toString())));
    }

    private Offer createOffer(String id, String description, String status, LocalDateTime expiryDate) {
        Offer offer = new Offer();

        offer.setId(id);
        offer.setDescription(description);
        offer.setStatus(status);
        offer.setExpiryDate(expiryDate);

        return offer;
    }
}
