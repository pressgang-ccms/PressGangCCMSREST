package org.jboss.pressgang.ccms.restserver.webdav;

import org.jboss.pressgang.ccms.restserver.utils.JNDIUtilities;
import org.jboss.resteasy.spi.InternalServerErrorException;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

public class WebDavUtils {
    /**
     * The Factory used to create EntityManagers
     */
    @PersistenceUnit
    private static EntityManagerFactory entityManagerFactory;

    public static EntityManager getEntityManager(boolean joinTransaction) {
        if (entityManagerFactory == null) {
            try {
                entityManagerFactory = JNDIUtilities.lookupJBossEntityManagerFactory();
            } catch (NamingException e) {
                throw new InternalServerErrorException("Could not find the EntityManagerFactory");
            }
        }

        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        if (entityManager == null) throw new InternalServerErrorException("Could not create an EntityManager");

        if (joinTransaction) {
            entityManager.joinTransaction();
        }

        return entityManager;
    }

    private WebDavUtils() {

    }
}
