package org.lajcik.kociolek.dao;

import org.hibernate.Query;
import org.lajcik.kociolek.domain.Item;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: sienkom
 */
@Repository
public class ItemDaoImpl extends AbstractDao<Item> implements ItemDao {
    public ItemDaoImpl() {
        super(Item.class);
    }

    @Override
    public Item findItem(String itemName) {
        Query query = getSession().createQuery("from Item i where lower(i.name) = :name");
        query.setParameter("name", itemName.trim().toLowerCase());

        @SuppressWarnings("unchecked")
        List<Item> list = query.list();

        return DataAccessUtils.singleResult(list);
    }
}
