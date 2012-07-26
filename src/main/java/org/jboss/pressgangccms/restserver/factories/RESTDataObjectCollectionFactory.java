package org.jboss.pressgangccms.restserver.factories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.pressgangccms.rest.v1.collections.base.BaseRestCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgangccms.rest.v1.expansion.ExpandDataDetails;
import org.jboss.pressgangccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgangccms.restserver.entities.base.AuditedEntity;
import org.jboss.pressgangccms.restserver.utils.SkynetExceptionUtilities;
import org.jboss.resteasy.spi.BadRequestException;

/**
 * A factory used to create collections of REST entity objects
 *
 * @param <T> The type of REST entity to work with
 * @param <U> The type of database entity to work with
 * @param <V> The type of REST collection to work with
 */
public class RESTDataObjectCollectionFactory<T extends RESTBaseEntityV1<T, V>, U extends AuditedEntity<U>, V extends BaseRestCollectionV1<T, V>>
{
	V create(final Class<V> clazz, final RESTDataObjectFactory<T, U, V> dataObjectFactory, final List<U> entities, final String expandName, final String dataType, final EntityManager entityManager)
	{
		return create(clazz, dataObjectFactory, entities, expandName, dataType, "", null, entityManager);
	}

	public V create(final Class<V> clazz, final RESTDataObjectFactory<T, U, V> dataObjectFactory, final List<U> entities, final String expandName, final String dataType, final String expand, final String baseUrl, final EntityManager entityManager)
	{
		if (expand != null && !expand.isEmpty())
		{
			try
			{
				/*
				 * convert the expand string from JSON to an instance of ExpandDataTrunk
				 */
				final ObjectMapper mapper = new ObjectMapper();
				final ExpandDataTrunk expandDataTrunk = mapper.readValue(expand, ExpandDataTrunk.class);

				return this.create(clazz, dataObjectFactory, entities, expandName, dataType, expandDataTrunk, baseUrl, entityManager);
			}
			catch (final Exception ex)
			{
				throw new BadRequestException("The expand parameter was not a valid JSON representation of a ExpandDataTrunk class");
			}
		}
		else
		{
			return this.create(clazz, dataObjectFactory, entities, expandName, dataType, (ExpandDataTrunk) null, baseUrl, entityManager);
		}
	}

	public V create(final Class<V> clazz, final RESTDataObjectFactory<T, U, V> dataObjectFactory, final List<U> entities, final String expandName, final String dataType, final ExpandDataTrunk parentExpand, final String baseUrl, final EntityManager entityManager)
	{
		return create(clazz, dataObjectFactory, entities, null, null, null, expandName, dataType, parentExpand, baseUrl, true, entityManager);
	}
	
	V create(final Class<V> clazz, final RESTDataObjectFactory<T, U, V> dataObjectFactory, final List<U> entities, final String expandName, final String dataType, final ExpandDataTrunk parentExpand, final String baseUrl, final Number revision, final EntityManager entityManager)
	{
		return create(clazz, dataObjectFactory, entities, null, revision, null, expandName, dataType, parentExpand, baseUrl, true, entityManager);
	}

	V create(final Class<V> clazz, final RESTDataObjectFactory<T, U, V> dataObjectFactory, final U parent, final List<Number> revisions, final String expandName, final String dataType, final ExpandDataTrunk parentExpand, final String baseUrl, final EntityManager entityManager)
	{
		return create(clazz, dataObjectFactory, null, parent, null, revisions, expandName, dataType, parentExpand, baseUrl, true, entityManager);
	}
	
	V create(final Class<V> clazz, final RESTDataObjectFactory<T, U, V> dataObjectFactory, final U parent, final List<Number> revisions, final String expandName, final String dataType, final ExpandDataTrunk parentExpand, final String baseUrl, final Number revision, final EntityManager entityManager)
	{
		return create(clazz, dataObjectFactory, null, parent, revision, revisions, expandName, dataType, parentExpand, baseUrl, true, entityManager);
	}

	public V create(final Class<V> clazz, final RESTDataObjectFactory<T, U, V> dataObjectFactory, final List<U> entities, final String expandName, final String dataType, final ExpandDataTrunk parentExpand, final String baseUrl, final boolean expandParentReferences, final EntityManager entityManager)
	{
		return create(clazz, dataObjectFactory, entities, null, null, null, expandName, dataType, parentExpand, baseUrl, expandParentReferences, entityManager);
	}
	
	V create(final Class<V> clazz, final RESTDataObjectFactory<T, U, V> dataObjectFactory, final List<U> entities, final String expandName, final String dataType, final ExpandDataTrunk parentExpand, final String baseUrl, final Number revision, final boolean expandParentReferences, final EntityManager entityManager)
	{
		return create(clazz, dataObjectFactory, entities, null, revision, null, expandName, dataType, parentExpand, baseUrl, expandParentReferences, entityManager);
	}

	/**
	 * @param dataObjectFactory
	 *            The factory to convert the database entity to a REST entity
	 * @param entities
	 *            A collection of numbers mapped to database entities. If isRevsionMap is true, these numbers are envers revision numbers. If isRevsionMap is
	 *            false, these numbers have no meaning.
	 * @param parent
	 *            The parent from which to find previous versions
	 * @param revisions
	 *            A list of Envers revision numbers that we want to add to the collection
	 * @param isRevsionMap
	 *            true if the entities keyset are related to envers revision numbers. false if the entities keyset have no meaning.
	 * @param expandName
	 *            The name of the collection that we are working with
	 * @param dataType
	 *            The type of data that is returned through the REST interface
	 * @param parentExpand
	 *            The parent objects expansion details
	 * @param baseUrl
	 *            The base of the url that was used to access this collection
	 * @return a REST collection from a collection of database entities.
	 */
	V create(final Class<V> clazz, final RESTDataObjectFactory<T, U, V> dataObjectFactory, final List<U> entities, final U parent, final Number parentRevision, final List<Number> revisions, final String expandName, final String dataType, final ExpandDataTrunk parentExpand, final String baseUrl, final boolean expandParentReferences,
			final EntityManager entityManager)
	{
		V retValue = null;
		
		try
		{
			retValue = clazz.newInstance();
		}
		catch (final Exception ex)
		{
			return null;
		}

		/*
		 * either the entities collection needs to be set, or the revisions and parent
		 */
		if (!(entities != null || (revisions != null && parent != null)))
			return retValue;

		final boolean usingRevisions = entities == null;

		retValue.setExpand(expandName);

		final ExpandDataTrunk expand = parentExpand != null ? parentExpand.contains(expandName) : null;
		
		try
		{
			if (expand != null)
			{	
				if (expand.getTrunk().getName().equals(expandName))
				{
					assert baseUrl != null : "Parameter baseUrl can not be null if parameter expand is not null";
					
					final ExpandDataDetails indexes = expand.getTrunk();
					
					if (indexes.isShowSize() != null && indexes.isShowSize())
					{
						retValue.setSize(usingRevisions ? revisions.size() : entities.size());
					}
					
					int start = 0;
					if (indexes.getStart() != null)
					{
						final int startIndex = indexes.getStart();
						final int size = usingRevisions ? revisions.size() : entities.size();
						if (startIndex < 0)
						{
							start = Math.max(0, size + startIndex);
						}
						else
						{
							start = Math.min(startIndex, size - 1);
						}
					}

					int end = usingRevisions ? revisions.size() : entities.size();
					if (indexes.getEnd() != null)
					{
						final int endIndex = indexes.getEnd();
						final int size = usingRevisions ? revisions.size() : entities.size();
						if (endIndex < 0)
						{
							end = Math.max(0, size + endIndex + 1);
						}
						else
						{
							end = Math.min(endIndex, size);
						}
					}

					final int fixedStart = Math.min(start, end);
					final int fixedEnd = Math.max(start, end);

					retValue.setStartExpandIndex(fixedStart);
					retValue.setEndExpandIndex(fixedEnd);

					final List<T> restEntityArray = new ArrayList<T>();

					for (int i = fixedStart; i < fixedEnd; i++)
					{
						U dbEntity = null;

						Number revision = parentRevision;
						if (usingRevisions)
						{
							/*
							 * Looking up an Envers previous version is an expensive operation. So instead of getting a complete collection and only adding
							 * those we need to the REST collection (like we do with standard related entities in the database), when it comes to Envers we only
							 * retrieve the previous versions when they are specifically requested.
							 * 
							 * This means that we only have to request the list of revision numbers (supplied to us via the revisions parameter) instead of
							 * having to request every revision.
							 */
							final AuditedEntity<U> parentAuditedEntity = (AuditedEntity<U>) parent;
							revision = revisions.get(i);
							dbEntity = parentAuditedEntity.getRevision(revision);

						}
						else
						{
							dbEntity = entities.get(i);
						}

						if (dbEntity != null)
						{
							final T restEntity = dataObjectFactory.createRESTEntityFromDBEntity(dbEntity, baseUrl, dataType, expand, revision, expandParentReferences, entityManager);

							/*
							 * if the entities keyset relates to the revision numbers, copy that data across
							 */
							if (usingRevisions)
							{
								restEntity.setRevision(revisions.get(i).intValue());
							}

							restEntityArray.add(restEntity);
						}

					}

					retValue.setItems(restEntityArray);
				}
			}
		}
		catch (final Exception ex)
		{
			SkynetExceptionUtilities.handleException(ex, false, "An error creating or populating a BaseRestCollectionV1");
		}

		return retValue;
	}
}
