package org.lajcik.kociolek.dao;

import org.lajcik.kociolek.domain.Item;
import org.springframework.stereotype.Repository;

/**
 * User: sienkom
 */
@Repository
public class ItemDaoImpl extends AbstractDao<Item> implements ItemDao {
    public ItemDaoImpl() {
        super(Item.class);
    }
}
