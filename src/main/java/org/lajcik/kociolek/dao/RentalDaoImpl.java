package org.lajcik.kociolek.dao;

import org.hibernate.Query;
import org.lajcik.kociolek.domain.Rental;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
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
        Query query = getSession().createQuery("from Rental r where r.ticketNumber = :ticketNumber");
        query.setParameter("ticketNumber", ticketNumber);

        @SuppressWarnings("unchecked")
        List<Rental> results = query.list();

        return DataAccessUtils.uniqueResult(results);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Rental> getAllActiveRentals() {
        Query query = getSession().createQuery("from Rental r where r.ticketNumber is not null");

        return query.list();
    }

    public List<Object[]> getReportData(Date dateFrom, Date dateTo) {
        Query query = getSession().createQuery("select i.name, count(r), sum(r.minutesRented) " +
                "from Rental r join r.rentedItems i " +
                "where r.minutesRented is not null and r.rentDate >= :dateFrom and r.returnDate <= :dateTo " +
                "group by i.name ");
        query.setParameter("dateFrom", dateFrom);
        query.setParameter("dateTo", dateTo);
        //noinspection unchecked
        return query.list();
    }
}