package com.davido.offers.repository;

import com.davido.offers.model.Offer;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for persistence for {@link Offer}
 */
@Repository
public class OfferRepository {

    JdbcTemplate jdbcTemplate;

    @Inject
    public OfferRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Offer findById(String id) {
        return jdbcTemplate.queryForObject("select * from offer where id=?", new Object[] {id},
                new BeanPropertyRowMapper<Offer>(Offer.class));
    }

    public List<Offer> findAllOffers() {
        return jdbcTemplate.query("select * from offer", new OfferRowMapper());
    }

    public void createOffer(String id, String description, LocalDateTime expiryDate) {
        jdbcTemplate.update("insert into offer (id, description, expiry_date) values (?,?,?)",
                new Object[] {
                        id, description, expiryDate
                });
    }

    public void expireOffer(String id) {
        jdbcTemplate.update("update offer set expiry_date=GETDATE() where id=?",
                new Object[] {
                        id
                });
    }

    class OfferRowMapper implements RowMapper<Offer> {
        @Override
        public Offer mapRow(ResultSet resultSet, int rownNumber) throws SQLException {
            Offer offer = new Offer();
            offer.setId(resultSet.getString("id"));
            offer.setDescription(resultSet.getString("description"));
            offer.setExpiryDate(resultSet.getObject(3, LocalDateTime.class));
            return offer;
        }
    }
}
