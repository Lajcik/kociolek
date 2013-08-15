package org.lajcik.kociolek.dao;

import org.hibernate.Query;
import org.lajcik.kociolek.domain.Rental;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: sienkom
 */
@Repository
public class RentalDaoImpl extends AbstractDao<Rental> implements RentalDao {
    public RentalDaoImpl() {
        super(Rental.class);
    }

    public Rental getByTicket(int ticketNumber) {
        Query query = getSession().createQuery("from Rental r where r.ticketNumber = :ticketNumber and r.rented = true");

        @SuppressWarnings("unchecked")
        List<Rental> results = query.list();

        return DataAccessUtils.uniqueResult(results);
    }
}
