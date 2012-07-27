package org.jboss.pressgangccms.restserver.factories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.pressgangccms.rest.v1.collections.RESTPropertyTagCollectionV1;
import org.jboss.pressgangccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgangccms.rest.v1.collections.RESTTopicSourceUrlCollectionV1;
import org.jboss.pressgangccms.rest.v1.collections.RESTTranslatedTopicCollectionV1;
import org.jboss.pressgangccms.rest.v1.collections.RESTTranslatedTopicStringCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTPropertyTagV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTTopicSourceUrlV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTTranslatedTopicStringV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTTranslatedTopicV1;
import org.jboss.pressgangccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgangccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgangccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgangccms.restserver.BaseRESTv1;
import org.jboss.pressgangccms.restserver.entities.Tag;
import org.jboss.pressgangccms.restserver.entities.TopicSourceUrl;
import org.jboss.pressgangccms.restserver.entities.TopicToPropertyTag;
import org.jboss.pressgangccms.restserver.entities.TranslatedTopic;
import org.jboss.pressgangccms.restserver.entities.TranslatedTopicData;
import org.jboss.pressgangccms.restserver.entities.TranslatedTopicString;
import org.jboss.pressgangccms.restserver.factories.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgangccms.restserver.factories.base.RESTDataObjectFactory;
import org.jboss.pressgangccms.utils.common.DocBookUtilities;

public class TranslatedTopicV1Factory extends RESTDataObjectFactory<RESTTranslatedTopicV1, TranslatedTopicData, RESTTranslatedTopicCollectionV1>
{
	public TranslatedTopicV1Factory()
	{
		super(TranslatedTopicData.class);
	}

	@Override
	public RESTTranslatedTopicV1 createRESTEntityFromDBEntity(final TranslatedTopicData entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager) throws InvalidParameterException
	{
		assert entity != null : "Parameter topic can not be null";
		assert baseUrl != null : "Parameter baseUrl can not be null";
		assert expand != null : "Parameter expand can not be null";

		final RESTTranslatedTopicV1 retValue = new RESTTranslatedTopicV1();

		/* Set the expansion options */
		final List<String> expandOptions = new ArrayList<String>();
		expandOptions.add(RESTTranslatedTopicV1.TRANSLATEDTOPICSTRING_NAME);
		expandOptions.add(RESTTranslatedTopicV1.INCOMING_NAME);
		expandOptions.add(RESTTranslatedTopicV1.OUTGOING_NAME);
		expandOptions.add(RESTTranslatedTopicV1.ALL_LATEST_INCOMING_NAME);
		expandOptions.add(RESTTranslatedTopicV1.ALL_LATEST_OUTGOING_NAME);
		expandOptions.add(RESTTranslatedTopicV1.TAGS_NAME);
		expandOptions.add(RESTTranslatedTopicV1.SOURCE_URLS_NAME);
		expandOptions.add(RESTTranslatedTopicV1.PROPERTIES_NAME);

		if (revision == null)
			expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

		retValue.setExpand(expandOptions);

		/* Set the simple values */
		retValue.setId(entity.getTranslatedTopicDataId());
		retValue.setTranslatedTopicId(entity.getTranslatedTopic().getId());
		retValue.setTopicId(entity.getTranslatedTopic().getTopicId());
		retValue.setTopicRevision(entity.getTranslatedTopic().getTopicRevision());

		/*
		 * Get the title from the XML or if the XML is null then use the original topics title.
		 */
		String title = DocBookUtilities.findTitle(entity.getTranslatedXml());
		if (title == null)
			title = entity.getTranslatedTopic().getEnversTopic(entityManager).getTopicTitle();

		/*
		 * Append the locale to the title if its a dummy translation to show that it is missing the related translated topic
		 */
		if (entity.getId() < 0)
			title = "[" + entity.getTranslationLocale() + "] " + title;
		retValue.setTitle(title);

		retValue.setXml(entity.getTranslatedXml());
		retValue.setXmlErrors(entity.getTranslatedXmlErrors());
		retValue.setHtml(entity.getTranslatedXmlRendered());
		retValue.setLocale(entity.getTranslationLocale());
		retValue.setTranslationPercentage(entity.getTranslationPercentage());

		/* Set the object references */
		if (expandParentReferences)
		{
			retValue.setTopic(new TopicV1Factory().createRESTEntityFromDBEntity(entity.getTranslatedTopic().getEnversTopic(entityManager), baseUrl, dataType, expand.contains(RESTTranslatedTopicV1.TOPIC_NAME), entity.getTranslatedTopic().getTopicRevision(), true, entityManager));
			retValue.getTopic().setRevision(entity.getTranslatedTopic().getTopicRevision());
		}

		if (revision == null)
		{
			retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTTranslatedTopicV1, TranslatedTopicData, RESTTranslatedTopicCollectionV1>().create(RESTTranslatedTopicCollectionV1.class, new TranslatedTopicV1Factory(), entity, entity.getRevisions(entityManager), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
					entityManager));
		}

		/* Set the collections */
		retValue.setTranslatedTopicStrings_OTM(new RESTDataObjectCollectionFactory<RESTTranslatedTopicStringV1, TranslatedTopicString, RESTTranslatedTopicStringCollectionV1>().create(RESTTranslatedTopicStringCollectionV1.class, new TranslatedTopicStringV1Factory(), entity.getTranslatedTopicDataStringsArray(),
				RESTTranslatedTopicV1.TRANSLATEDTOPICSTRING_NAME, dataType, expand, baseUrl, false, /* don't set the reference to this entity on the children */
				entityManager));
		retValue.setTags(new RESTDataObjectCollectionFactory<RESTTagV1, Tag, RESTTagCollectionV1>().create(RESTTagCollectionV1.class, new TagV1Factory(), entity.getTranslatedTopic().getEnversTopic(entityManager).getTags(), BaseRESTv1.TAGS_EXPANSION_NAME, dataType, expand, baseUrl, entityManager));
		retValue.setOutgoingTranslatedRelationships(new RESTDataObjectCollectionFactory<RESTTranslatedTopicV1, TranslatedTopicData, RESTTranslatedTopicCollectionV1>().create(RESTTranslatedTopicCollectionV1.class, new TranslatedTopicV1Factory(), entity.getOutgoingRelatedTranslatedTopicData(entityManager),
				RESTTranslatedTopicV1.OUTGOING_NAME, dataType, expand, baseUrl, true, entityManager));
		retValue.setIncomingTranslatedRelationships(new RESTDataObjectCollectionFactory<RESTTranslatedTopicV1, TranslatedTopicData, RESTTranslatedTopicCollectionV1>().create(RESTTranslatedTopicCollectionV1.class, new TranslatedTopicV1Factory(), entity.getIncomingRelatedTranslatedTopicData(entityManager),
				RESTTranslatedTopicV1.INCOMING_NAME, dataType, expand, baseUrl, true, entityManager));
		retValue.setOutgoingRelationships(new RESTDataObjectCollectionFactory<RESTTranslatedTopicV1, TranslatedTopicData, RESTTranslatedTopicCollectionV1>().create(RESTTranslatedTopicCollectionV1.class, new TranslatedTopicV1Factory(), entity.getOutgoingDummyFilledRelatedTranslatedTopicDatas(entityManager),
				RESTTranslatedTopicV1.ALL_LATEST_OUTGOING_NAME, dataType, expand, baseUrl, true, entityManager));
		retValue.setIncomingRelationships(new RESTDataObjectCollectionFactory<RESTTranslatedTopicV1, TranslatedTopicData, RESTTranslatedTopicCollectionV1>().create(RESTTranslatedTopicCollectionV1.class, new TranslatedTopicV1Factory(), entity.getIncomingDummyFilledRelatedTranslatedTopicDatas(entityManager),
				RESTTranslatedTopicV1.ALL_LATEST_INCOMING_NAME, dataType, expand, baseUrl, true, entityManager));
		retValue.setSourceUrls_OTM(new RESTDataObjectCollectionFactory<RESTTopicSourceUrlV1, TopicSourceUrl, RESTTopicSourceUrlCollectionV1>().create(RESTTopicSourceUrlCollectionV1.class, new TopicSourceUrlV1Factory(), entity.getTranslatedTopic().getEnversTopic(entityManager).getTopicSourceUrls(),
				BaseRESTv1.SOURCE_URLS_EXPANSION_NAME, dataType, expand, baseUrl, false, entityManager));
		retValue.setProperties(new RESTDataObjectCollectionFactory<RESTPropertyTagV1, TopicToPropertyTag, RESTPropertyTagCollectionV1>().create(RESTPropertyTagCollectionV1.class, new TopicPropertyTagV1Factory(), entity.getTranslatedTopic().getEnversTopic(entityManager).getTopicToPropertyTagsArray(),
				BaseRESTv1.PROPERTIES_EXPANSION_NAME, dataType, expand, baseUrl, entityManager));

		retValue.setLinks(baseUrl, BaseRESTv1.TRANSLATEDTOPIC_URL_NAME, dataType, retValue.getId());

		return retValue;
	}

	@Override
	public
	void syncDBEntityWithRESTEntity(EntityManager entityManager, TranslatedTopicData entity, RESTTranslatedTopicV1 dataObject) throws InvalidParameterException
	{
		/*
		 * Since this factory is the rare case where two entities are combined into one. Check if it has a parent, if not then check if one exists that matches
		 * otherwise create one. If one exists then update it.
		 */
		TranslatedTopic translatedTopic = entity.getTranslatedTopic();
		if (translatedTopic == null)
		{
			try
			{
				final Query query = entityManager.createQuery(TranslatedTopic.SELECT_ALL_QUERY + " WHERE translatedTopic.topicId=" + dataObject.getTopicId() + " AND translatedTopic.topicRevision=" + dataObject.getTopicRevision());
				translatedTopic = (TranslatedTopic) query.getSingleResult();
			}
			catch (Exception e)
			{
				translatedTopic = new TranslatedTopic();

				/* populate the new translated topic */
				if (dataObject.hasParameterSet(RESTTranslatedTopicV1.TOPICID_NAME))
					translatedTopic.setTopicId(dataObject.getTopicId());
				if (dataObject.hasParameterSet(RESTTranslatedTopicV1.TOPICREVISION_NAME))
					translatedTopic.setTopicRevision(dataObject.getTopicRevision());
			}
		}

		entity.setTranslatedTopic(translatedTopic);

		if (dataObject.hasParameterSet(RESTTranslatedTopicV1.HTML_UPDATED))
			entity.setTranslatedXmlRenderedUpdated(dataObject.getHtmlUpdated());
		if (dataObject.hasParameterSet(RESTTranslatedTopicV1.XML_ERRORS_NAME))
			entity.setTranslatedXmlErrors(dataObject.getXmlErrors());
		if (dataObject.hasParameterSet(RESTTranslatedTopicV1.XML_NAME))
			entity.setTranslatedXml(dataObject.getXml());
		if (dataObject.hasParameterSet(RESTTranslatedTopicV1.HTML_NAME))
			entity.setTranslatedXmlRendered(dataObject.getHtml());
		if (dataObject.hasParameterSet(RESTTranslatedTopicV1.LOCALE_NAME))
			entity.setTranslationLocale(dataObject.getLocale());
		if (dataObject.hasParameterSet(RESTTranslatedTopicV1.TRANSLATIONPERCENTAGE_NAME))
			entity.setTranslationPercentage(dataObject.getTranslationPercentage());

		translatedTopic.getTranslatedTopicDataEntities().add(entity);

		/* Save the changes done to the translated topic */
		entityManager.persist(translatedTopic);

		// entityManager.persist(entity);

		/* One To Many - Add will create a child entity */
		if (dataObject.hasParameterSet(RESTTranslatedTopicV1.TRANSLATEDTOPICSTRING_NAME) && dataObject.getTranslatedTopicStrings_OTM() != null && dataObject.getTranslatedTopicStrings_OTM().getItems() != null)
		{
			/* remove any items first */
			for (final RESTTranslatedTopicStringV1 restEntity : dataObject.getTranslatedTopicStrings_OTM().getItems())
			{
				if (restEntity.getRemoveItem())
				{
					final TranslatedTopicString dbEntity = entityManager.find(TranslatedTopicString.class, restEntity.getId());
					if (dbEntity == null)
						throw new InvalidParameterException("No TranslatedTopicString entity was found with the primary key " + restEntity.getId());
					entity.getTranslatedTopicStrings().remove(dbEntity);
				}
			}

			/* add any items second */
			for (final RESTTranslatedTopicStringV1 restEntity : dataObject.getTranslatedTopicStrings_OTM().getItems())
			{
				if (restEntity.getAddItem())
				{
					final TranslatedTopicString dbEntity = new TranslatedTopicString();
					dbEntity.setTranslatedTopicData(entity);
					new TranslatedTopicStringV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
					entity.getTranslatedTopicStrings().add(dbEntity);
				}
			}
		}
	}
}
