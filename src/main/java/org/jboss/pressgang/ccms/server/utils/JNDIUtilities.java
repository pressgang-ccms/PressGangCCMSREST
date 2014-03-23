package org.jboss.pressgang.ccms.server.utils;

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

/**
 * A class that holds a set of utility methods for looking up objects from JNDI.
 *
 * @author lnewson
 */
public class JNDIUtilities {
    private static final String ENTITY_MANAGER_FACTORY_JNDI_NAME = "java:jboss/EntityManagerFactory";
    private static final String TRANSACTION_MANAGER_JNDI_NAME = "java:jboss/TransactionManager";
    private static final String USER_TRANSACTION_JNDI_NAME = "java:jboss/UserTransaction";
    private static final String BEAN_MANAGER_JNDI_NAME = "java:comp/BeanManager";

    /**
     * Lookup the named "java:jboss/EntityManagerFactory" EntityManagerFactory associated with the underlying Application
     * Server.
     *
     * @return The EntityManagerFactory object.
     * @throws NamingException Thrown if a name based error occurs looking up the EntityManagerFactory.
     */
    public static EntityManagerFactory lookupJBossEntityManagerFactory() throws NamingException {
        final InitialContext initCtx = new InitialContext();
        final EntityManagerFactory entityManagerFactory = (EntityManagerFactory) initCtx.lookup(ENTITY_MANAGER_FACTORY_JNDI_NAME);
        if (entityManagerFactory == null)
            throw new NamingException("Could not find the EntityManagerFactory at " + ENTITY_MANAGER_FACTORY_JNDI_NAME);

        return entityManagerFactory;
    }

    /**
     * A generic method provided to lookup any object bound to JNDI.
     *
     * @param clazz The class type of the expected return.
     * @param name  The JNDI name of the object. eg "java:comp/BeanManager"
     * @return The requested lookup object.
     * @throws NamingException Thrown if a name based error occurs looking up the Object.
     */
    public static <T> T lookup(final Class<T> clazz, final String name) throws NamingException {
        final InitialContext initCtx = new InitialContext();
        final T object = clazz.cast(initCtx.lookup(name));

        return object;
    }

    /**
     * Lookup the TransactionManager associated with the underlying Application Server.
     *
     * @return The TransactionManager object.
     * @throws NamingException Thrown if a name based error occurs looking up the TransactionManager.
     */
    public static final TransactionManager lookupJBossTransactionManager() throws NamingException {
        final InitialContext initCtx = new InitialContext();

        final TransactionManager transactionManager = (TransactionManager) initCtx.lookup(TRANSACTION_MANAGER_JNDI_NAME);
        if (transactionManager == null)
            throw new NamingException("Could not find the TransactionManager at " + TRANSACTION_MANAGER_JNDI_NAME);

        return transactionManager;
    }

    /**
     * Lookup the a UserTransaction managed with the underlying Application Server.
     *
     * @return The UserTransaction object.
     * @throws NamingException Thrown if a name based error occurs looking up the TransactionManager.
     */
    public static final UserTransaction lookupUserTransaction() throws NamingException {
        final InitialContext initCtx = new InitialContext();

        final UserTransaction userTransaction = (UserTransaction) initCtx.lookup(USER_TRANSACTION_JNDI_NAME);
        if (userTransaction == null)
            throw new NamingException("Could not find the UserTransaction at " + USER_TRANSACTION_JNDI_NAME);

        return userTransaction;
    }

    /**
     * Lookup the BeanManager associated with the underlying Application Server.
     *
     * @return The BeanManager object.
     * @throws NamingException Thrown if a name based error occurs looking up the Bean Manager.
     */
    public static BeanManager lookupBeanManager() throws NamingException {
        final InitialContext initCtx = new InitialContext();
        final BeanManager beanManager = (BeanManager) initCtx.lookup(BEAN_MANAGER_JNDI_NAME);
        if (beanManager == null)
            throw new NamingException("Could not find the BeanManager at " + BEAN_MANAGER_JNDI_NAME);

        return beanManager;
    }
}
