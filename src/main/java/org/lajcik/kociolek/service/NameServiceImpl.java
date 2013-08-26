package org.lajcik.kociolek.service;

import org.apache.commons.lang.StringUtils;
import org.lajcik.kociolek.dao.ItemDao;
import org.lajcik.kociolek.domain.Item;
import org.lajcik.kociolek.domain.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * User: sienkom
 */
@Component
public class NameServiceImpl implements NameService, RentalListener {

    private Set<String> itemSet = Collections.synchronizedSet(new TreeSet<String>());

    @Autowired
    private RentalService rentalService;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Override
    public void itemRented(Rental rental) {
        for (Item item : rental.getRentedItems()) {
            String name = StringUtils.capitalize(item.getName().trim());
            itemSet.add(name);
        }
    }

    @Override
    public void itemReturned(Rental rental) {
        // noop
    }

    @Override
    public void itemsChanged(Rental before, Rental after) {
        itemRented(after);
    }

    @Override
    public List<String> getItemDictionary() {
        return new ArrayList<String>(itemSet);
    }

    @PostConstruct
    public void init() {
        rentalService.addListener(this);

        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                List<String> itemNames = itemDao.getAllItemNames();
                itemSet.addAll(itemNames);
            }
        });
    }
}