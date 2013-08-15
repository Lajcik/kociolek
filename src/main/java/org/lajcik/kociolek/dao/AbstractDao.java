package org.lajcik.kociolek.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * User: sienkom
 */
public class AbstractDao<T> extends HibernateDaoSupport {

    private Class<T> clazz;

    public AbstractDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T get(Long id) {
        return clazz.cast(getSession().get(clazz, id));
    }

    public void save(T o) {
        getSession().save(o);
    }
}
