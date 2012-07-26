package org.jboss.pressgangccms.restserver;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;

import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.pressgangccms.rest.v1.collections.base.BaseRestCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgangccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgangccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgangccms.restserver.utils.SkynetExceptionUtilities;
import org.jboss.pressgangccms.utils.common.ExceptionUtilities;
import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.InternalServerErrorException;

/**
 * Defines a factory that can create REST entity objects from JPA entities, and
 * update JPA entities from REST entities
 *
 * @param <T> The REST object type
 * @param <U> The database object type
 * @param <V> The REST object collection type
 */
abstract class RESTDataObjectFactory<T extends RESTBaseEntityV1<T, V>, U, V extends BaseRestCollectionV1<T, V>>
{
	final Class<U> databaseClass;

	RESTDataObjectFactory(final Class<U> databaseClass)
	{
		this.databaseClass = databaseClass;
	}
	
	/**
	 * @param entity
	 *            The source database entity with the data that needs to be
	 *            copied into the REST entity
	 * @param baseUrl
	 *            The REST url that was used to access this REST entity
	 * @param dataType
	 *            The type of the returned data (XML or JSON)
	 * @param expand
	 *            The expansion JSON string, which will be converted into a
	 *            ExpandDataTrunk
	 * @return A new REST entity populated with the values in a database entity
	 */
	T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType, final String expand) throws InvalidParameterException
	{
		return this.createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, null);
	}

	/**
	 * @param entity
	 *            The source database entity with the data that needs to be
	 *            copied into the REST entity
	 * @param baseUrl
	 *            The REST url that was used to access this REST entity
	 * @param dataType
	 *            The type of the returned data (XML or JSON)
	 * @param expand
	 *            The expansion JSON string, which will be converted into a
	 *            ExpandDataTrunk
	 * @param revision
	 * 			  If the entity is a revision
	 * @return A new REST entity populated with the values in a database entity
	 */
	T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType, final String expand, final Number revision) throws InvalidParameterException
	{
		return this.createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, revision, null);
	}
	
	/**
	 * @param entity
	 *            The source database entity with the data that needs to be
	 *            copied into the REST entity
	 * @param baseUrl
	 *            The REST url that was used to access this REST entity
	 * @param dataType
	 *            The type of the returned data (XML or JSON)
	 * @param expand
	 *            The expansion JSON string, which will be converted into a
	 *            ExpandDataTrunk
	 * @param revision
	 * 			  If the entity is a revision
	 * @return A new REST entity populated with the values in a database entity
	 */
	T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType, final String expand, final Number revision, final EntityManager entityManager) throws InvalidParameterException
	{
		if (expand != null && !expand.isEmpty())
		{
			try
			{
				/*
				 * convert the expand string from JSON to an instance of
				 * ExpandDataTrunk
				 */
				final ObjectMapper mapper = new ObjectMapper();
				final ExpandDataTrunk expandDataTrunk = mapper.readValue(expand, ExpandDataTrunk.class);

				return this.createRESTEntityFromDBEntity(entity, baseUrl, dataType, expandDataTrunk, revision, true, entityManager);
			}
			catch (final Exception ex)
			{
				ExceptionUtilities.handleException(ex);
			}
		}

		return this.createRESTEntityFromDBEntity(entity, baseUrl, dataType, new ExpandDataTrunk(), revision, true, entityManager);
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
	T createRESTEntityFromDBPK(final Object primaryKey, final String baseUrl, final String dataType, final String expand, final Number revision) throws InvalidParameterException
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
			SkynetExceptionUtilities.handleException(ex, false, "Probably an error saving the entity");

			try
			{
				transactionManager.rollback();
			}
			catch (final Exception ex2)
			{
				SkynetExceptionUtilities.handleException(ex2, false, "There was an error rolling back the transaction");
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
	T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand) throws InvalidParameterException
	{
		return createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, null, true);
	}
	
	/**
	 * @return A new REST entity populated with the values in a database entity
	 */
	T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision) throws InvalidParameterException
	{
		return createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, revision, true);
	}

	/**
	 * @return A new REST entity populated with the values in a database entity
	 */
	T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) throws InvalidParameterException
	{
		return createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, revision, expandParentReferences, null);
	}
	
	/**
	 * @return A new REST entity populated with the values in a database entity
	 */
	abstract T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager) throws InvalidParameterException;

	/**
	 * Populates the values of a database entity from a REST entity
	 * 
	 * @param entityManager
	 * @param entity
	 *            The database entity
	 * @param dataObject
	 *            The REST entity
	 */
	abstract void syncDBEntityWithRESTEntity(final EntityManager entityManager, final U entity, final T dataObject) throws InvalidParameterException;

	/**
	 * Creates, populates and returns a new database entity from a REST entity
	 * 
	 * @param entityManager
	 * @param dataObject
	 *            The REST entity used to populate the database entity's values
	 * @return A new database entity with the values supplied from the
	 *         dataObject
	 */
	U createDBEntityFromRESTEntity(final EntityManager entityManager, final T dataObject) throws InvalidParameterException
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
