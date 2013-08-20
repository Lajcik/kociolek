package org.lajcik.kociolek.domain;

import javax.persistence.*;

/**
 * @author lajcik
 */
@Entity
@Table(name = "ITEM")
public class Item {
    private long id;
    private String name;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "NAME", nullable = false, length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
