package org.lajcik.kociolek.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: sienkom
 */
public class AbstractDao<T> {

    private Class<T> clazz;

    @Autowired
    private SessionFactory sessionFactory;

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

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
