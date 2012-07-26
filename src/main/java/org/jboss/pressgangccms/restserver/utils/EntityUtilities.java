package org.jboss.pressgangccms.restserver.utils;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;
import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.jboss.pressgangccms.restserver.constants.Constants;
import org.jboss.pressgangccms.restserver.entities.BlobConstants;
import org.jboss.pressgangccms.restserver.entities.Category;
import org.jboss.pressgangccms.restserver.entities.Filter;
import org.jboss.pressgangccms.restserver.entities.FilterCategory;
import org.jboss.pressgangccms.restserver.entities.FilterField;
import org.jboss.pressgangccms.restserver.entities.FilterLocale;
import org.jboss.pressgangccms.restserver.entities.FilterTag;
import org.jboss.pressgangccms.restserver.entities.IntegerConstants;
import org.jboss.pressgangccms.restserver.entities.Project;
import org.jboss.pressgangccms.restserver.entities.StringConstants;
import org.jboss.pressgangccms.restserver.entities.Tag;
import org.jboss.pressgangccms.restserver.entities.Topic;
import org.jboss.pressgangccms.restserver.entities.TopicToBugzillaBug;
import org.jboss.pressgangccms.restserver.entities.TopicToPropertyTag;
import org.jboss.pressgangccms.restserver.entities.TranslatedTopicData;
import org.jboss.pressgangccms.restserver.filter.TopicFilter;
import org.jboss.pressgangccms.utils.common.CollectionUtilities;
import org.joda.time.DateTime;

import com.redhat.contentspec.processor.ContentSpecParser;



/**
 * This class provides a number of static methods for easy and safe lookup of
 * database entities.
 * @author Matthew Casperson
 */
public class EntityUtilities
{
	@PersistenceContext static EntityManager entityManager;
	
	public static byte[] loadBlobConstant(final EntityManager entityManager, final Integer id)
	{
		if (id == null)
			return null;

		final BlobConstants constant = entityManager.find(BlobConstants.class, id);

		if (constant == null)
		{
			System.out.println("Expected to find a record in the BlobConstants table with an ID of " + id);
			return null;
		}

		return constant.getConstantValue();
	}

	public static Integer loadIntegerConstant(final EntityManager entityManager, final Integer id)
	{
		if (id == null)
			return null;

		final IntegerConstants constant = entityManager.find(IntegerConstants.class, id);

		if (constant == null)
		{
			System.out.println("Expected to find a record in the IntegerConstants table with an ID of " + id);
			return null;
		}

		return constant.getConstantValue();
	}

	public static String loadStringConstant(final EntityManager entityManager, final Integer id)
	{
		if (id == null)
			return null;

		final StringConstants constant = entityManager.find(StringConstants.class, id);

		if (constant == null)
		{
			System.out.println("Expected to find a record in the StringConstants table with an ID of " + id);
			return null;
		}

		return constant.getConstantValue();
	}
	
	/**
	 * @return A comma separated list of topic ids that have been included in a
	 *         content spec
	 * @throws Exception
	 */
	public static String getTopicsInContentSpec(final Integer contentSpecTopicID) throws Exception
	{
		try
		{
			final Topic contentSpec = entityManager.find(Topic.class, contentSpecTopicID);

			if (contentSpec == null)
				return Constants.NULL_TOPIC_ID;

			final ContentSpecParser csp = new ContentSpecParser("http://localhost:8080/TopicIndex/");
			if (csp.parse(contentSpec.getTopicXML()))
			{
				final List<Integer> topicIds = csp.getReferencedTopicIds();
				if (topicIds.size() == 0)
					return Constants.NULL_TOPIC_ID;

				return CollectionUtilities.toSeperatedString(topicIds);
			}
			else
			{
				return Constants.NULL_TOPIC_ID;
			}
		}
		catch (final Exception ex)
		{
			SkynetExceptionUtilities.handleException(ex, true, "An invalid Topic ID was stored for a Content Spec in the database, or the topic was not a valid content spec");
			return Constants.NULL_TOPIC_ID;
		}
	}
	
	public static <E> String getEditedEntitiesString(final Class<E> type, final String pkColumnName, final DateTime startDate, final DateTime endDate)
	{
		final List<Integer> ids = getEditedEntities(type, pkColumnName, startDate, endDate);
		if (ids != null && ids.size() != 0)
			return CollectionUtilities.toSeperatedString(ids);
		return Constants.NULL_TOPIC_ID;
	}
	
	@SuppressWarnings("unchecked")
	public static <E> List<Integer> getEditedEntities(final Class<E> type, final String pkColumnName, final DateTime startDate, final DateTime endDate)
	{
		if (startDate == null && endDate == null)
			return null;

		final AuditReader reader = AuditReaderFactory.get(entityManager);

		final AuditQuery query = reader.createQuery().forRevisionsOfEntity(type, true, false).addOrder(AuditEntity.revisionProperty("timestamp").asc()).addProjection(AuditEntity.property("originalId." + pkColumnName).distinct());

		if (startDate != null)
			query.add(AuditEntity.revisionProperty("timestamp").ge(startDate.toDate().getTime()));

		if (endDate != null)
			query.add(AuditEntity.revisionProperty("timestamp").le(endDate.toDate().getTime()));

		final List<Integer> entityyIds = query.getResultList();

		return entityyIds;
	}
	
	public static String getIncomingRelationshipsTo(final Integer topicId)
	{
		final List<Integer> ids = getIncomingRelatedTopicIDs(topicId);
		if (ids != null && ids.size() != 0)
			return CollectionUtilities.toSeperatedString(ids);
		return Constants.NULL_TOPIC_ID;
	}
	
	public static List<Integer> getIncomingRelatedTopicIDs(final Integer topicRelatedFrom)
	{
		try
		{
			if (topicRelatedFrom != null)
			{
				final Topic topic = entityManager.find(Topic.class, topicRelatedFrom);
				return topic.getIncomingRelatedTopicIDs();
			}
		}
		catch (final Exception ex)
		{
			SkynetExceptionUtilities.handleException(ex, false, topicRelatedFrom + " is probably not a valid Topic ID");
		}

		return null;
	}
	
	public static String getOutgoingRelationshipsFrom(final Integer topicId)
	{
		final List<Integer> ids = getOutgoingRelatedTopicIDs(topicId);
		if (ids != null && ids.size() != 0)
			return CollectionUtilities.toSeperatedString(ids);
		return Constants.NULL_TOPIC_ID;
	}
	
	public static List<Integer> getOutgoingRelatedTopicIDs(final Integer topicRelatedTo)
	{
		try
		{
			if (topicRelatedTo != null)
			{
				final Topic topic = entityManager.find(Topic.class, topicRelatedTo);
				return topic.getRelatedTopicIDs();
			}
		}
		catch (final Exception ex)
		{
			SkynetExceptionUtilities.handleException(ex, false, topicRelatedTo + " is probably not a valid Topic ID");
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Integer> getTextSearchTopicMatch(final String phrase)
	{	
		final List<Integer> retValue = new ArrayList<Integer>();

		try
		{
			// get the Hibernate session from the EntityManager
			final Session session = (Session) entityManager.getDelegate();
			// get a Hibernate full text session. we use the Hibernate version,
			// instead of the JPA version,
			// because we can use the Hibernate versions to do projections
			final FullTextSession fullTextSession = Search.getFullTextSession(session);
			// create a query parser
			final QueryParser parser = new QueryParser(Version.LUCENE_31, "TopicSearchText", fullTextSession.getSearchFactory().getAnalyzer(Topic.class));
			// parse the query string
			final org.apache.lucene.search.Query query = parser.parse(phrase);

			// build a lucene query
			/*
			 * final org.apache.lucene.search.Query query = qb .keyword()
			 * .onFields("TopicSearchText") .matching(phrase) .createQuery();
			 */

			// build a hibernate query
			final org.hibernate.search.FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Topic.class);
			// set the projection to return the id's of any topic's that match
			// the query
			hibQuery.setProjection("topicId");
			// get the results. because we setup a projection, there is no trip
			// to the database
			final List<Object[]> results = hibQuery.list();
			// extract the data into the List<Integer>
			for (final Object[] projection : results)
			{
				final Integer id = (Integer) projection[0];
				retValue.add(id);
			}
		}
		catch (final Exception ex)
		{
			SkynetExceptionUtilities.handleException(ex, false, "Probably an error using Lucene");
		}
		
		/*
		 * an empty list will be interpreted as no restriction as opposed to
		 * return none. so add a non existent topic id so no matches are
		 * made
		 */
		if (retValue.size() == 0)
			retValue.add(-1);

		return retValue;
	}
	
	/**
	 * @return A comma separated list of topic ids that have open bugzilla bugs
	 *         assigned to them
	 */
	public static String getTopicsWithOpenBugsString()
	{
		final List<Integer> topics = getTopicsWithOpenBugs();
		if (topics.size() == 0)
			return Constants.NULL_TOPIC_ID;

		return CollectionUtilities.toSeperatedString(topics);
	}
	
	/**
	 * @return A list of topic ids that have open bugzilla bugs assigned to them
	 */
	@SuppressWarnings("unchecked")
	public static List<Integer> getTopicsWithOpenBugs()
	{
		final List<TopicToBugzillaBug> results = entityManager.createQuery(TopicToBugzillaBug.SELECT_ALL_QUERY + " WHERE topicToBugzillaBug.bugzillaBug.bugzillaBugOpen = true").getResultList();
		final List<Integer> retValue = new ArrayList<Integer>();
		for (final TopicToBugzillaBug map : results)
			retValue.add(map.getTopic().getTopicId());
		return retValue;
	}
	
	/**
	 * @return A comma separated list of topic ids that have bugzilla bugs
	 *         assigned to them
	 */
	public static String getTopicsWithBugsString()
	{
		final List<Integer> topics = getTopicsWithBugs();
		if (topics.size() == 0)
			return Constants.NULL_TOPIC_ID;

		return CollectionUtilities.toSeperatedString(topics);
	}
	
	/**
	 * @return A list of topic ids that have bugzilla bugs assigned to them
	 */
	@SuppressWarnings("unchecked")
	public static List<Integer> getTopicsWithBugs()
	{
		final List<TopicToBugzillaBug> results = entityManager.createQuery(TopicToBugzillaBug.SELECT_ALL_QUERY).getResultList();
		final List<Integer> retValue = new ArrayList<Integer>();
		for (final TopicToBugzillaBug map : results)
			if (!retValue.contains(map.getTopic().getTopicId()))
				retValue.add(map.getTopic().getTopicId());
		return retValue;
	}
	
	@SuppressWarnings("unchecked")
	public static String getTopicsWithPropertyTag(final Integer propertyTagIdInt, final String propertyTagValue)
	{
		final List<TopicToPropertyTag> mappings = entityManager.createQuery(TopicToPropertyTag.SELECT_ALL_QUERY + " WHERE topicToPropertyTag.propertyTag.propertyTagId = " + propertyTagIdInt + " AND topicToPropertyTag.value = '" + propertyTagValue + "'").getResultList();
		if (mappings.size() == 0)
			return Constants.NULL_TOPIC_ID;

		final StringBuilder retValue = new StringBuilder();
		for (final TopicToPropertyTag mapping : mappings)
		{
			if (retValue.length() != 0)
				retValue.append(",");
			retValue.append(mapping.getTopic().getTopicId());
		}

		return retValue.toString();
	}
	
	public static String getLatestTranslatedTopicsString()
	{
		final List<Integer> topics = getLatestTranslatedTopics();
		if (topics.size() == 0)
			return Constants.NULL_TOPIC_ID;

		return CollectionUtilities.toSeperatedString(topics);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Integer> getLatestTranslatedTopics()
	{
		String query = TranslatedTopicData.SELECT_ALL_QUERY;
		query += " where translatedTopicData.translatedTopic.topicRevision = (Select MAX(B.translatedTopic.topicRevision) FROM TranslatedTopicData B WHERE translatedTopicData.translatedTopic.topicId = B.translatedTopic.topicId AND B.translationLocale = translatedTopicData.translationLocale GROUP BY B.translatedTopic.topicId)";
		final List<TranslatedTopicData> results = entityManager.createQuery(query).getResultList();
		final List<Integer> retValue = new ArrayList<Integer>();
		for (final TranslatedTopicData topic : results)
			if (!retValue.contains(topic.getTranslatedTopicDataId()))
				retValue.add(topic.getTranslatedTopicDataId());
		return retValue;
	}
	
	public static String getLatestCompletedTranslatedTopicsString()
	{
		final List<Integer> topics = getLatestCompletedTranslatedTopics();
		if (topics.size() == 0)
			return Constants.NULL_TOPIC_ID;

		return CollectionUtilities.toSeperatedString(topics);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Integer> getLatestCompletedTranslatedTopics()
	{
		String query = TranslatedTopicData.SELECT_ALL_QUERY;
		query += " where translatedTopicData.translatedTopic.topicRevision = (Select MAX(B.translatedTopic.topicRevision) FROM TranslatedTopicData B WHERE translatedTopicData.translatedTopic.topicId = B.translatedTopic.topicId AND B.translationLocale = translatedTopicData.translationLocale AND B.translationPercentage >= 100 GROUP BY B.translatedTopic.topicId)";
		final List<TranslatedTopicData> results = entityManager.createQuery(query).getResultList();
		final List<Integer> retValue = new ArrayList<Integer>();
		for (final TranslatedTopicData topic : results)
			if (!retValue.contains(topic.getTranslatedTopicDataId()))
				retValue.add(topic.getTranslatedTopicDataId());
		return retValue;
	}
	
	public static Filter populateFilter(final MultivaluedMap<String, String> paramMap, final String filterName, final String tagPrefix, final String groupTagPrefix, final String categoryInternalPrefix, final String categoryExternalPrefix, final String localePrefix)
	{
		final Map<String, String> newParamMap = new HashMap<String, String>();
		for (final String key : paramMap.keySet())
		{
			try
			{
				newParamMap.put(key, URLDecoder.decode(paramMap.getFirst(key), "UTF-8"));
			}
			catch (final Exception ex)
			{
				SkynetExceptionUtilities.handleException(ex, true, "The URL query parameter " + key + " with value " + paramMap.getFirst(key) + " could not be URLDecoded");
			}
		}
		return populateFilter(newParamMap, filterName, tagPrefix, groupTagPrefix, categoryInternalPrefix, categoryExternalPrefix, localePrefix);

	}
	
	/**
	 * This function takes the url parameters and uses them to populate a Filter
	 * object
	 */
	public static Filter populateFilter(final Map<String, String> paramMap, final String filterName, final String tagPrefix, final String groupTagPrefix, final String categoryInternalPrefix, final String categoryExternalPrefix, final String localePrefix)
	{
		// attempt to get the filter id from the url
		Integer filterId = null;
		if (paramMap.containsKey(filterName))
		{
			final String filterQueryParam = paramMap.get(filterName);

			try
			{
				filterId = Integer.parseInt(filterQueryParam);
			}
			catch (final Exception ex)
			{
				// filter value was not an integer
				filterId = null;

				SkynetExceptionUtilities.handleException(ex, true, "The filter ID URL query parameter was not an integer. Got " + filterQueryParam + ". Probably a malformed URL.");
			}
		}

		Filter filter = null;

		/* First attempt to populate the filter from a filterID variable */
		if (filterId != null)
		{
			filter = entityManager.find(Filter.class, filterId);
		}

		/* If that fails, use the other URL params */
		if (filter == null)
		{
			filter = new Filter();

			for (final String key : paramMap.keySet())
			{
				final boolean tagVar = key.startsWith(tagPrefix);
				final boolean groupTagVar = key.startsWith(groupTagPrefix);
				final boolean catIntVar = key.startsWith(categoryInternalPrefix);
				final boolean catExtVar = key.startsWith(categoryExternalPrefix);
				final boolean localeVar = key.matches("^" + localePrefix + "\\d*$");
				final String state = paramMap.get(key);

				// add the filter category states
				if (catIntVar || catExtVar)
				{
					/*
					 * get the category and project id data from the variable
					 * name
					 */
					final String catProjDetails = catIntVar ? key.replaceFirst(categoryInternalPrefix, "") : key.replaceFirst(categoryExternalPrefix, "");
					// split the category and project id out of the data
					final String[] catProjID = catProjDetails.split("-");

					/*
					 * some validity checks. make sure we have one or two
					 * strings after the split.
					 */
					if (catProjID.length != 1 && catProjID.length != 2)
						continue;

					// try to get the category and project ids
					Integer catID = null;
					Integer projID = null;
					try
					{
						catID = Integer.parseInt(catProjID[0]);

						/*
						 * if the array has just one element, we have only
						 * specified the category. in this case the project is
						 * the common project
						 */
						if (catProjID.length == 2)
							projID = Integer.parseInt(catProjID[1]);
					}
					catch (final Exception ex)
					{
						SkynetExceptionUtilities.handleException(ex, true, "Was expecting an integer. Got " + catProjID[0] + ". Probably a malformed URL.");
						continue;
					}

					// at this point we have found a url variable that
					// contains a catgeory and project id

					final Category category = entityManager.find(Category.class, catID);
					final Project project = projID != null ? entityManager.find(Project.class, projID) : null;

					Integer dbState;

					if (catIntVar)
					{
						if (state.equals(Constants.AND_LOGIC))
							dbState = Constants.CATEGORY_INTERNAL_AND_STATE;
						else
							dbState = Constants.CATEGORY_INTERNAL_OR_STATE;
					}
					else
					{
						if (state.equals(Constants.AND_LOGIC))
							dbState = Constants.CATEGORY_EXTERNAL_AND_STATE;
						else
							dbState = Constants.CATEGORY_EXTERNAL_OR_STATE;
					}

					final FilterCategory filterCategory = new FilterCategory();
					filterCategory.setFilter(filter);
					filterCategory.setProject(project);
					filterCategory.setCategory(category);
					filterCategory.setCategoryState(dbState);

					filter.getFilterCategories().add(filterCategory);
				}

				// add the filter tag states
				else if (tagVar)
				{
					try
					{
						final Integer tagId = Integer.parseInt(key.replaceFirst(tagPrefix, ""));
						final Integer intState = Integer.parseInt(state);

						// get the Tag object that the tag id represents
						final Tag tag = entityManager.getReference(Tag.class, tagId);

						if (tag != null)
						{
							final FilterTag filterTag = new FilterTag();
							filterTag.setTag(tag);
							filterTag.setTagState(intState);
							filterTag.setFilter(filter);
							filter.getFilterTags().add(filterTag);
						}
					}
					catch (final Exception ex)
					{
						SkynetExceptionUtilities.handleException(ex, true, "Probably an invalid tag query pramater. Parameter: " + key + " Value: " + state);
					}
				}

				else if (groupTagVar)
				{
					final Integer tagId = Integer.parseInt(key.replaceFirst(groupTagPrefix, ""));
					// final Integer intState = Integer.parseInt(state);

					// get the Tag object that the tag id represents
					final Tag tag = entityManager.getReference(Tag.class, tagId);

					if (tag != null)
					{
						final FilterTag filterTag = new FilterTag();
						filterTag.setTag(tag);
						filterTag.setTagState(Constants.GROUP_TAG_STATE);
						filterTag.setFilter(filter);
						filter.getFilterTags().add(filterTag);
					}
				}
				else if (localeVar)
				{
					try
					{
						final String localeName = state.replaceAll("\\d", "");
						final Integer intState = Integer.parseInt(state.replaceAll("[^\\d]", ""));

						final FilterLocale filterLocale = new FilterLocale();
						filterLocale.setLocaleName(localeName);
						filterLocale.setLocaleState(intState);
						filterLocale.setFilter(filter);
						filter.getFilterLocales().add(filterLocale);
					}
					catch (final Exception ex)
					{
						SkynetExceptionUtilities.handleException(ex, true, "Probably an invalid locale query pramater. Parameter: " + key + " Value: " + state);
					}
				}

				// add the filter field states
				else
				{
					if (TopicFilter.hasFilterName(key))
					{
						final FilterField filterField = new FilterField();
						filterField.setFilter(filter);
						filterField.setField(key);
						filterField.setValue(state);
						filterField.setDescription(TopicFilter.getFilterDesc(key));
						filter.getFilterFields().add(filterField);
					}
				}

			}

		}

		return filter;
	}
}
