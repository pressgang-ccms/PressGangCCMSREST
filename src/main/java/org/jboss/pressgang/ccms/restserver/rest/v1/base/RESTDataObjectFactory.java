package org.jboss.pressgang.ccms.restserver.rest.v1.base;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;

import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.utils.common.ExceptionUtilities;
import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Defines a factory that can create REST entity objects from JPA entities, and
 * update JPA entities from REST entities
 *
 * @param <T> The REST object type
 * @param <U> The database object type
 * @param <V> The REST object collection type
 */
public abstract class RESTDataObjectFactory<T extends RESTBaseEntityV1<T, V, W>, U, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>>
{
    private static final Logger log = LoggerFactory.getLogger(RESTDataObjectFactory.class);
    
	final Class<U> databaseClass;

	public RESTDataObjectFactory(final Class<U> databaseClass)
	{
		this.databaseClass = databaseClass;
	}

	/**
	 * @param primaryKey
	 *            The id of the database entity to use as the source for the REST entity
	 * @param baseUrl
	 *            The REST url that was used to access this REST entity
	 * @param dataType
	 *            The type of the returned data (XML or JSON)
	 * @param expand
	 *            The expansion JSON string, which will be converted into a
	 *            ExpandDataTrunk
	 * @return A new REST entity populated with the values in a database entity
	 */
	public T createRESTEntityFromDBPK(final Object primaryKey, final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision) throws InvalidParameterException
	{
		TransactionManager transactionManager = null;
		EntityManager entityManager = null;

		try
		{
			final InitialContext initCtx = new InitialContext();

			final EntityManagerFactory entityManagerFactory = (EntityManagerFactory) initCtx.lookup("java:jboss/EntityManagerFactory");
			if (entityManagerFactory == null)
				throw new InternalServerErrorException("Could not find the EntityManagerFactory");

			transactionManager = (TransactionManager) initCtx.lookup("java:jboss/TransactionManager");
			if (transactionManager == null)
				throw new InternalServerErrorException("Could not find the TransactionManager");

			assert transactionManager != null : "transactionManager should not be null";
			assert entityManagerFactory != null : "entityManagerFactory should not be null";

			transactionManager.begin();

			entityManager = entityManagerFactory.createEntityManager();
			if (entityManager == null)
				throw new InternalServerErrorException("Could not create an EntityManager");

			assert entityManager != null : "entityManager should not be null";

			final U entity = entityManager.find(databaseClass, primaryKey);

			if (entity == null)
				throw new BadRequestException("No entity was found with the id " + primaryKey);
			
			return createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, revision);
		}
		catch (final Exception ex)
		{
			log.error("Probably an error saving the entity", ex);

			try
			{
				transactionManager.rollback();
			}
			catch (final Exception ex2)
			{
				log.error("There was an error rolling back the transaction", ex2);
			}

			throw new InternalServerErrorException("There was an error retrieving the entity");
		}
		finally
		{
			if (entityManager != null)
				entityManager.close();
		}
	}

	/**
	 * @return A new REST entity populated with the values in a database entity
	 */
	public T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand)
	{
		return createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, null, true);
	}
	
	/**
	 * @return A new REST entity populated with the values in a database entity
	 */
	public T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision)
	{
		return createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, revision, true);
	}

	/**
	 * @return A new REST entity populated with the values in a database entity
	 */
	public T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences)
	{
		return createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, revision, expandParentReferences, null);
	}
	
	/**
	 * @return A new REST entity populated with the values in a database entity
	 */
	public  abstract T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager);

	/**
	 * Populates the values of a database entity from a REST entity
	 * 
	 * @param entityManager
	 * @param entity
	 *            The database entity
	 * @param dataObject
	 *            The REST entity
	 */
	public  abstract void syncDBEntityWithRESTEntity(final EntityManager entityManager, final U entity, final T dataObject) throws InvalidParameterException;

	/**
	 * Creates, populates and returns a new database entity from a REST entity
	 * 
	 * @param entityManager
	 * @param dataObject
	 *            The REST entity used to populate the database entity's values
	 * @return A new database entity with the values supplied from the
	 *         dataObject
	 */
	public U createDBEntityFromRESTEntity(final EntityManager entityManager, final T dataObject) throws InvalidParameterException
	{
		try
		{
			final U entity = databaseClass.newInstance();
			this.syncDBEntityWithRESTEntity(entityManager, entity, dataObject);
			return entity;
		}
		catch (final Exception ex)
		{
			ExceptionUtilities.handleException(ex);
		}

		return null;
	}
}
