package com.eleks.academy.pharmagator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Component
public class MaterializedViewRefresher {
    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void refresh(){
        this.entityManager.createNativeQuery("REFRESH MATERIALIZED VIEW advanced_search_view;").executeUpdate();
    }
}
