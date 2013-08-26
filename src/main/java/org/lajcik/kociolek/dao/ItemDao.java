package org.lajcik.kociolek.dao;

import org.lajcik.kociolek.domain.Item;

import java.util.List;

/**
 * User: sienkom
 */
public interface ItemDao extends Dao<Item> {

    Item findItem(String itemName);

    List<String> getAllItemNames();
}
