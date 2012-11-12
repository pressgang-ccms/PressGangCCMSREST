package org.jboss.pressgang.ccms.restserver.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;

public class JNDIUtilities {
    /**
     * Lookup the named "java:jboss/EntityManagerFactory" EntityManagerFactory associated with the underlying Application
     * Server.
     * 
     * @return The EntityManagerFactory object.
     * @throws NamingException Thrown if a name based error occurs looking up the EntityManagerFactory.
     */
    public static EntityManagerFactory lookupEntityManagerFactory() throws NamingException {
        final InitialContext initCtx = new InitialContext();
        final EntityManagerFactory entityManagerFactory = (EntityManagerFactory) initCtx
                .lookup("java:jboss/EntityManagerFactory");

        return entityManagerFactory;
    }

    /**
     * Lookup the TransactionManager associated with the underlying Application Server.
     * 
     * @return The TransactionManager object.
     * @throws NamingException Thrown if a name based error occurs looking up the TransactionManager.
     */
    public static final TransactionManager lookupTransactionManager() throws NamingException {
        final InitialContext initCtx = new InitialContext();

        final TransactionManager transactionManager = (TransactionManager) initCtx.lookup("java:jboss/TransactionManager");
        if (transactionManager == null)
            throw new NamingException("Could not find the TransactionManager");

        return transactionManager;
    }
}
