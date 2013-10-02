package org.jboss.pressgang.ccms.server.utils;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceUnit;

public class ResourceProducer {

    @PersistenceUnit
    protected EntityManagerFactory entityManagerFactory;

    protected EntityManager em;

    @Produces
    @RequestScoped
    public EntityManager createEntityManager() {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.setFlushMode(FlushModeType.AUTO);
        return entityManager;
    }

    public void dispose(@Disposes EntityManager entityManager) {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
