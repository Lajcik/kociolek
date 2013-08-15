package org.lajcik.kociolek.dao;

import org.lajcik.kociolek.domain.Item;

/**
 * User: sienkom
 */
public interface Dao<T> {

    T get(Long id);

    void save(T object);
}
