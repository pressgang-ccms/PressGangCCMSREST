package org.jboss.pressgangccms.restserver.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.pressgangccms.restserver.constants.Constants;
import org.jboss.pressgangccms.restserver.entities.Topic;
import org.jboss.pressgangccms.restserver.entities.TranslatedTopicData;
import org.jboss.pressgangccms.restserver.utils.EntityUtilities;
import org.jboss.pressgangccms.restserver.utils.SkynetExceptionUtilities;
import org.jboss.pressgangccms.utils.common.CollectionUtilities;
import org.jboss.pressgangccms.utils.constants.CommonConstants;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Provides the query elements required by Filter.buildQuery() to get a list of
 * Topic elements
 */
public class TranslatedTopicDataFilterQueryBuilder implements FilterQueryBuilder
{
	private DateTime startEditDate;
	private DateTime endEditDate;
	private String startCreateDate;
	private String endCreateDate;
	private String filterFieldsLogic = "AND";
	private List<String> fields = new ArrayList<String>();

	@Override
	public String getSelectAllQuery()
	{
		return TranslatedTopicData.SELECT_ALL_QUERY;
	}

	@Override
	public String getMatchTagString(final Integer tagId)
	{
		return "EXISTS (SELECT 1 FROM TopicToTag topicToTag WHERE topicToTag.topic.topicId = translatedTopicData.translatedTopic.topicId AND topicToTag.tag.tagId = " + tagId + ") ";
	}

	@Override
	public String getNotMatchTagString(final Integer tagId)
	{
		return "NOT EXISTS (SELECT 1 FROM TopicToTag topicToTag WHERE topicToTag.topic.topicId = translatedTopicData.translatedTopic.topicId AND topicToTag.tag.tagId = " + tagId + ") ";
	}
	
	@Override
	public String getMatchingLocalString(final String locale)
	{
		if (locale == null)
			return "";
		
		final String entityName = "translatedTopicData";
		
		final String defaultLocale = System.getProperty(CommonConstants.DEFAULT_LOCALE_PROPERTY);
		
		if (defaultLocale != null && defaultLocale.toLowerCase().equals(locale.toLowerCase()))
			return "(" + entityName + ".translationLocale = '" + locale + "' OR " + entityName + ".translationLocale is null)";
		
		return entityName + ".translationLocale = '" + locale + "'";
	}
	
	@Override
	public String getNotMatchingLocalString(final String locale)
	{
		if (locale == null)
			return "";
		
		final String entityName = "translatedTopicData";
		
		final String defaultLocale = System.getProperty(CommonConstants.DEFAULT_LOCALE_PROPERTY);
		
		if (defaultLocale != null && defaultLocale.toLowerCase().equals(locale.toLowerCase()))
			return "(" + entityName + ".translationLocale != '" + locale + "' AND " + entityName + ".translationLocale is not null)";
		
		return entityName + ".translationLocale != '" + locale + "'";
	}

	@Override
	public String getFilterString()
	{
		if (startCreateDate != null || endCreateDate != null)
		{
			String thisRestriction = "";

			if (startCreateDate != null)
			{
				thisRestriction = "translatedTopicData.translatedTopic.topicId in (SELECT topic FROM Topic topic WHERE topic.topicTimeStamp >= '" + startCreateDate + "')";
			}

			if (endCreateDate != null)
			{
				if (startCreateDate != null)
					thisRestriction += " AND ";

				thisRestriction += "translatedTopicData.translatedTopic.topicId in (SELECT topic FROM Topic topic WHERE topic.topicTimeStamp <= '" + endCreateDate + "')";
			}

			fields.add(thisRestriction);

		}

		if (startEditDate != null || endEditDate != null)
		{
			final String editedTopics = EntityUtilities.getEditedEntitiesString(Topic.class, "topicId", startEditDate, endEditDate);
			fields.add("translatedTopicData.translatedTopic.topicId IN (" + editedTopics + ")");
		}

		String retValue = "";
		for (final String field : fields)
		{
			if (retValue.length() != 0)
				retValue += filterFieldsLogic + " ";

			retValue += field;
		}

		return retValue;
	}

	@Override
	public void processFilterString(final String fieldName, final String fieldValue)
	{
		if (fieldName.equals(Constants.TOPIC_LOGIC_FILTER_VAR))
		{
			filterFieldsLogic = fieldValue;
		}

		else if (fieldName.equals(Constants.TOPIC_IDS_FILTER_VAR))
		{
			if (fieldValue.trim().length() != 0 && fieldValue.matches("^[0-9,]+$"))
				fields.add("translatedTopicData.translatedTopic.topicId IN (" + fieldValue + ")");
		}
		
		else if (fieldName.equals(Constants.TOPIC_IDS_NOT_FILTER_VAR))
		{
			if (fieldValue.trim().length() != 0 && fieldValue.matches("^[0-9,]+$"))
				fields.add("translatedTopicData.translatedTopic.topicId NOT IN (" + fieldValue + ")");
		}
		
		else if (fieldName.equals(Constants.TOPIC_IS_INCLUDED_IN_SPEC))
		{
			/* Split up the string into each topic */
			final String[] topicIds = fieldValue.split(",");
			String relatedTopics = "";
			for (final String topicIdString : topicIds)
			{	
				try
				{
					final Integer topicId = Integer.parseInt(topicIdString);
					relatedTopics += (relatedTopics.endsWith(",") || relatedTopics.isEmpty() ? "" : ",") + EntityUtilities.getTopicsInContentSpec(topicId);
				}
				catch (final Exception ex)
				{
					SkynetExceptionUtilities.handleException(ex, true, "An invalid Topic ID was stored for a Content Spec in the database");
				}
			}
			
			fields.add("translatedTopicData.translatedTopic.topicId IN (" + relatedTopics + ")");
		}
		
		else if (fieldName.equals(Constants.TOPIC_IS_NOT_INCLUDED_IN_SPEC))
		{
			/* Split up the string into each topic */
			final String[] topicIds = fieldValue.split(",");
			String relatedTopics = "";
			for (final String topicIdString : topicIds)
			{
				try
				{
					final Integer topicId = Integer.parseInt(topicIdString);
					relatedTopics += (relatedTopics.endsWith(",") || relatedTopics.isEmpty() ? "" : ",") + EntityUtilities.getTopicsInContentSpec(topicId);
				}
				catch (final Exception ex)
				{
					SkynetExceptionUtilities.handleException(ex, true, "An invalid Topic ID was stored for a Content Spec in the database");
				}
			}
			
			fields.add("translatedTopicData.translatedTopic.topicId NOT IN (" + relatedTopics + ")");
		}

		else if (fieldName.equals(Constants.TOPIC_TITLE_FILTER_VAR))
		{
			fields.add("translatedTopicData.translatedTopic.topicId IN (SELECT topic.topicId from Topic topic WHERE LOWER(topic.topicTitle) LIKE LOWER('%" + fieldValue + "%'))");
		}
		
		else if (fieldName.equals(Constants.TOPIC_TITLE_NOT_FILTER_VAR))
		{
			fields.add("translatedTopicData.translatedTopic.topicId IN (SELECT topic.topicId from Topic topic WHERE LOWER(topic.topicTitle) NOT LIKE LOWER('%" + fieldValue + "%'))");
		}

		else if (fieldName.equals(Constants.TOPIC_XML_FILTER_VAR))
		{
			fields.add("translatedTopicData.translatedTopic.topicId IN (SELECT topic.topicId from Topic topic WHERE LOWER(topic.topicXML) LIKE LOWER('%" + fieldValue + "%'))");
		}
		
		else if (fieldName.equals(Constants.TOPIC_XML_NOT_FILTER_VAR))
		{
			fields.add("translatedTopicData.translatedTopic.topicId IN (SELECT topic.topicId from Topic topic WHERE LOWER(topic.topicXML) NOT LIKE LOWER('%" + fieldValue + "%'))");
		}

		else if (fieldName.equals(Constants.TOPIC_DESCRIPTION_FILTER_VAR))
		{
			fields.add("translatedTopicData.translatedTopic.topicId IN (SELECT topic.topicId from Topic topic WHERE LOWER(topic.topicText) LIKE LOWER('%" + fieldValue + "%'))");
		}
		
		else if (fieldName.equals(Constants.TOPIC_DESCRIPTION_NOT_FILTER_VAR))
		{
			fields.add("translatedTopicData.translatedTopic.topicId IN (SELECT topic.topicId from Topic topic WHERE LOWER(topic.topicText) NOT LIKE LOWER('%" + fieldValue + "%'))");
		}

		else if (fieldName.equals(Constants.TOPIC_HAS_RELATIONSHIPS))
		{
			fields.add("translatedTopicData.translatedTopic.topicId IN (SELECT topic.topicId from Topic topic WHERE topic.parentTopicToTopics.size >= 1)");
		}

		else if (fieldName.equals(Constants.TOPIC_HAS_INCOMING_RELATIONSHIPS))
		{
			fields.add("translatedTopicData.translatedTopic.topicId IN (SELECT topic.topicId from Topic topic WHERE topic.childTopicToTopics.size >= 1)");
		}
		
		else if (fieldName.equals(Constants.TOPIC_HAS_NOT_RELATIONSHIPS))
		{
			fields.add("translatedTopicData.translatedTopic.topicId IN (SELECT topic.topicId from Topic topic WHERE topic.parentTopicToTopics.size < 1)");
		}

		else if (fieldName.equals(Constants.TOPIC_HAS_NOT_INCOMING_RELATIONSHIPS))
		{
			fields.add("translatedTopicData.translatedTopic.topicId IN (SELECT topic.topicId from Topic topic WHERE topic.childTopicToTopics.size < 1)");
		}

		else if (fieldName.equals(Constants.TOPIC_RELATED_TO))
		{
			try
			{
				final Integer topicId = Integer.parseInt(fieldValue);
				final String relatedTopics = EntityUtilities.getIncomingRelationshipsTo(topicId);

				fields.add("translatedTopicData.translatedTopic.topicId IN (" + relatedTopics + ")");
			}
			catch (final Exception ex)
			{
				SkynetExceptionUtilities.handleException(ex, false, "An invalid Topic ID was stored for a Filter in the database");
			}
		}
		
		else if (fieldName.equals(Constants.TOPIC_NOT_RELATED_TO))
		{
			try
			{
				final Integer topicId = Integer.parseInt(fieldValue);
				final String relatedTopics = EntityUtilities.getIncomingRelationshipsTo(topicId);

				fields.add("translatedTopicData.translatedTopic.topicId NOT IN (" + relatedTopics + ")");
			}
			catch (final Exception ex)
			{
				SkynetExceptionUtilities.handleException(ex, false, "An invalid Topic ID was stored for a Filter in the database");
			}
		}
		
		else if (fieldName.equals(Constants.TOPIC_RELATED_FROM))
		{
			try
			{
				final Integer topicId = Integer.parseInt(fieldValue);
				final String relatedTopics = EntityUtilities.getOutgoingRelationshipsFrom(topicId);

				fields.add("translatedTopicData.translatedTopic.topicId IN (" + relatedTopics + ")");
			}
			catch (final Exception ex)
			{
				SkynetExceptionUtilities.handleException(ex, false, "An invalid Topic ID was stored for a Filter in the database");
			}
		}
		
		else if (fieldName.equals(Constants.TOPIC_NOT_RELATED_FROM))
		{
			try
			{
				final Integer topicId = Integer.parseInt(fieldValue);
				final String relatedTopics = EntityUtilities.getOutgoingRelationshipsFrom(topicId);

				fields.add("translatedTopicData.translatedTopic.topicId NOT IN (" + relatedTopics + ")");
			}
			catch (final Exception ex)
			{
				SkynetExceptionUtilities.handleException(ex, false, "An invalid Topic ID was stored for a Filter in the database");
			}
		}
		
		else if (fieldName.equals(Constants.TOPIC_TEXT_SEARCH_FILTER_VAR))
		{
			try
			{
				final List<Integer> matchingTopic = EntityUtilities.getTextSearchTopicMatch(fieldValue);

				fields.add("translatedTopicData.translatedTopic.topicId IN (" + CollectionUtilities.toSeperatedString(matchingTopic) + ")");
			}
			catch (final Exception ex)
			{
				SkynetExceptionUtilities.handleException(ex, false, "Could not get a list of topics matching a Lucene query");
			}
		}
		
		else if (fieldName.equals(Constants.TOPIC_HAS_XML_ERRORS))
		{
			try
			{
				final Boolean hasXMLErrors = Boolean.valueOf(fieldValue);
				if (hasXMLErrors)
					fields.add("(translatedTopicData.translatedTopic.topicId IN (SELECT topic.topicId FROM Topic topic WHERE topic.topicSecondOrderData.topicXMLErrors IS NOT NULL AND topic.topicSecondOrderData.topicXMLErrors != ''))");
			}
			catch (final Exception ex)
			{
				SkynetExceptionUtilities.handleException(ex, false, "An invalid boolean value was stored for a Filter in the database");
			}

		}
		
		else if (fieldName.equals(Constants.TOPIC_HAS_NOT_XML_ERRORS))
		{
			try
			{
				final Boolean hasNotXMLErrors = Boolean.valueOf(fieldValue);
				if (hasNotXMLErrors)
					fields.add("(translatedTopicData.translatedTopic.topicId IN (SELECT topic.topicId FROM Topic topic WHERE topic.topicSecondOrderData.topicXMLErrors IS NULL OR topic.topicSecondOrderData.topicXMLErrors = ''))");
			}
			catch (final Exception ex)
			{
				SkynetExceptionUtilities.handleException(ex, false, "An invalid boolean value was stored for a Filter in the database");
			}

		}

		else if (fieldName.equals(Constants.TOPIC_EDITED_IN_LAST_DAYS))
		{
			try
			{
				final Integer days = Integer.parseInt(fieldValue);
				final DateTime date = new DateTime().minusDays(days);
				final String editedTopics = EntityUtilities.getEditedEntitiesString(Topic.class, "topicId", date, null);

				fields.add("translatedTopicData.translatedTopic.topicId IN (" + editedTopics + ")");
			}
			catch (final Exception ex)
			{
				SkynetExceptionUtilities.handleException(ex, false, "An invalid integer was stored for a Filter in the database");
			}
		}
		
		else if (fieldName.equals(Constants.TOPIC_NOT_EDITED_IN_LAST_DAYS))
		{
			try
			{
				final Integer days = Integer.parseInt(fieldValue);
				final DateTime date = new DateTime().minusDays(days);
				final String editedTopics = EntityUtilities.getEditedEntitiesString(Topic.class, "topicId", date, null);

				fields.add("translatedTopicData.translatedTopic.topicId NOT IN (" + editedTopics + ")");
			}
			catch (final Exception ex)
			{
				SkynetExceptionUtilities.handleException(ex, false, "An invalid integer was stored for a Filter in the database");
			}
		}

		else if (fieldName.equals(Constants.TOPIC_STARTDATE_FILTER_VAR))
		{
			startCreateDate = fieldValue;
		}
		else if (fieldName.equals(Constants.TOPIC_ENDDATE_FILTER_VAR))
		{
			endCreateDate = fieldValue;
		}

		else if (fieldName.equals(Constants.TOPIC_STARTEDITDATE_FILTER_VAR))
		{
			try
			{
				startEditDate = ISODateTimeFormat.dateTime().parseDateTime(fieldValue);
			}
			catch (final Exception ex)
			{
				SkynetExceptionUtilities.handleException(ex, false, "An invalid DateTime string was stored by the Filter for the start edit date");
			}
		}
		else if (fieldName.equals(Constants.TOPIC_ENDEDITDATE_FILTER_VAR))
		{
			try
			{
				endEditDate = ISODateTimeFormat.dateTime().parseDateTime(fieldValue);
			}
			catch (final Exception ex)
			{
				SkynetExceptionUtilities.handleException(ex, false, "An invalid DateTime string was stored by the Filter for the end edit date");
			}
		}
		else if (fieldName.equals(Constants.TOPIC_HAS_OPEN_BUGZILLA_BUGS))
		{
			try
			{
				final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
				if (fieldValueBoolean)
				{
					final String topics = EntityUtilities.getTopicsWithOpenBugsString();
					fields.add("translatedTopicData.translatedTopic.topicId IN (" + topics + ")");
				}
			}
			catch (final Exception ex)
			{
				SkynetExceptionUtilities.handleException(ex, false, "Probably an error querying the TopicToBugzillaBug table");
			}
		}
		else if (fieldName.equals(Constants.TOPIC_HAS_NOT_OPEN_BUGZILLA_BUGS))
		{
			try
			{
				final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
				if (fieldValueBoolean)
				{
					final String topics = EntityUtilities.getTopicsWithOpenBugsString();
					fields.add("translatedTopicData.translatedTopic.topicId NOT IN (" + topics + ")");
				}
			}
			catch (final Exception ex)
			{
				SkynetExceptionUtilities.handleException(ex, false, "Probably an error querying the TopicToBugzillaBug table");
			}
		}
		else if (fieldName.equals(Constants.TOPIC_HAS_BUGZILLA_BUGS))
		{
			try
			{
				final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
				if (fieldValueBoolean)
				{
					final String topics = EntityUtilities.getTopicsWithBugsString();
					fields.add("translatedTopicData.translatedTopic.topicId IN (" + topics + ")");
				}
			}
			catch (final Exception ex)
			{
				SkynetExceptionUtilities.handleException(ex, false, "Probably an error querying the TopicToBugzillaBug table");
			}
		}
		else if (fieldName.equals(Constants.TOPIC_HAS_NOT_BUGZILLA_BUGS))
		{
			try
			{
				final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
				if (fieldValueBoolean)
				{
					final String topics = EntityUtilities.getTopicsWithBugsString();
					fields.add("translatedTopicData.translatedTopic.topicId NOT IN (" + topics + ")");
				}
			}
			catch (final Exception ex)
			{
				SkynetExceptionUtilities.handleException(ex, false, "Probably an error querying the TopicToBugzillaBug table");
			}
		}
		else if (fieldName.startsWith(Constants.TOPIC_PROPERTY_TAG))
		{
			try
			{
				final Pattern pattern = Pattern.compile(CommonConstants.PROPERTY_TAG_SEARCH_RE);
				final Matcher matcher = pattern.matcher(fieldValue);
				
				while (matcher.find())
				{
					final String propertyTagIdString = matcher.group("PropertyTagID");
					final String propertyTagValue = matcher.group("PropertyTagValue");
					
					if (propertyTagIdString != null && propertyTagValue != null)
					{
						final Integer propertyTagIdInt = Integer.parseInt(propertyTagIdString);
						final String topics = EntityUtilities.getTopicsWithPropertyTag(propertyTagIdInt, propertyTagValue);
						fields.add("translatedTopicData.translatedTopic.topicId IN (" + topics + ")");					
					}
					
					/* should only match once */
					break;
				}
				
			}
			catch (final Exception ex)
			{
				// could not parse integer, so fail
				SkynetExceptionUtilities.handleException(ex, true, "Probably a malformed URL query parameter for the 'Property Tag' Topic ID");
			}
		}
		
		else if (fieldName.equals(Constants.LATEST_TRANSLATIONS_FILTER_VAR))
		{
			final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
			if (fieldValueBoolean) {
				/* Limit to fully translated topics */
				final String topics = EntityUtilities.getLatestTranslatedTopicsString();
				fields.add("translatedTopicData.translatedTopicDataId IN (" + topics + ")");
			}
		}
		
		else if (fieldName.equals(Constants.NOT_LATEST_TRANSLATIONS_FILTER_VAR))
		{
			final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
			if (fieldValueBoolean) {
				/* Limit to fully translated topics */
				final String topics = EntityUtilities.getLatestTranslatedTopicsString();
				fields.add("translatedTopicData.translatedTopicDataId NOT IN (" + topics + ")");
			}
		}
		
		else if (fieldName.equals(Constants.LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR))
		{
			final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
			if (fieldValueBoolean) {
				/* Limit to the latest completed translated topics */
				final String topics = EntityUtilities.getLatestCompletedTranslatedTopicsString();
				fields.add("translatedTopicData.translatedTopicDataId in (" + topics + ")");
			}
		}
		
		else if (fieldName.equals(Constants.NOT_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR))
		{
			final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
			if (fieldValueBoolean) {
				/* Limit to the latest completed translated topics */
				final String topics = EntityUtilities.getLatestCompletedTranslatedTopicsString();
				fields.add("translatedTopicData.translatedTopicDataId NOT IN (" + topics + ")");
			}
		}
		
		else if (fieldName.equals(Constants.ZANATA_IDS_FILTER_VAR))
		{
			if (fieldValue.trim().length() != 0)
			{
				final StringBuilder fieldQuery = new StringBuilder("(");
				final String[] zanataIds = fieldValue.split(",");
				for (final String zanataId : zanataIds)
				{
					if (fieldQuery.length() != 1)
						fieldQuery.append(" OR ");
					try
					{
						String[] zanataVars = zanataId.split("-");
					
						final Integer topicId = Integer.parseInt(zanataVars[0]);
						final Integer topicRevision = Integer.parseInt(zanataVars[1]);
						
						fieldQuery.append("(translatedTopicData.translatedTopic.topicId = " + topicId + " AND translatedTopicData.translatedTopic.topicRevision = " + topicRevision + ")");
					}
					catch (Exception ex)
					{
						SkynetExceptionUtilities.handleException(ex, true, "Probably an invalid format or value for a zanata id.");
					}
				}
				
				/* Only add the query if we found valid zanata ids */
				if (fieldQuery.length() > 1)
				{
					fieldQuery.append(")");
					fields.add(fieldQuery.toString());
				}
			}
		}
		
		else if (fieldName.equals(Constants.ZANATA_IDS_NOT_FILTER_VAR))
		{
			if (fieldValue.trim().length() != 0)
			{
				final String[] zanataIds = fieldValue.split(",");
				final StringBuilder fieldQuery = new StringBuilder("translatedTopicData NOT IN (SELECT translatedTopicData FROM TranslatedTopicData translatedTopicData WHERE ");
				final int fieldQueryBaseSize = fieldQuery.length();
				for (final String zanataId : zanataIds)
				{
					if (fieldQuery.length() != fieldQueryBaseSize)
						fieldQuery.append(" OR ");
					try
					{
						String[] zanataVars = zanataId.split("-");
					
						final Integer topicId = Integer.parseInt(zanataVars[0]);
						final Integer topicRevision = Integer.parseInt(zanataVars[1]);
						
						fieldQuery.append("(translatedTopicData.translatedTopic.topicId = " + topicId + " AND translatedTopicData.translatedTopic.topicRevision = " + topicRevision + ")");
					}
					catch (Exception ex)
					{
						SkynetExceptionUtilities.handleException(ex, true, "Probably an invalid format or value for a zanata id.");
					}
				}
				
				/* Only add the query if we found valid zanata ids */
				if (fieldQuery.length() > fieldQueryBaseSize)
				{
					fieldQuery.append(")");
					fields.add(fieldQuery.toString());
				}
			}
		}
	}
}
