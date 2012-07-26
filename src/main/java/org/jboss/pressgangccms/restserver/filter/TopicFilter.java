package org.jboss.pressgangccms.restserver.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.pressgangccms.restserver.constants.Constants;
import org.jboss.pressgangccms.restserver.entities.Filter;
import org.jboss.pressgangccms.restserver.entities.FilterField;
import org.jboss.pressgangccms.restserver.entities.Topic;
import org.jboss.pressgangccms.restserver.structures.field.UIFieldBooleanData;
import org.jboss.pressgangccms.restserver.structures.field.UIFieldDataBase;
import org.jboss.pressgangccms.restserver.structures.field.UIFieldDateTimeData;
import org.jboss.pressgangccms.restserver.structures.field.UIFieldIntegerData;
import org.jboss.pressgangccms.restserver.structures.field.UIFieldStringData;
import org.jboss.pressgangccms.restserver.structures.field.UIFieldStringListData;
import org.jboss.pressgangccms.restserver.utils.EntityUtilities;
import org.jboss.pressgangccms.restserver.utils.SkynetExceptionUtilities;

/**
 * This class represents the options used by the objects that extend the
 * ExtendedTopicList class to filter a query to retrieve Topic entities.
 */
public class TopicFilter
{
	private UIFieldStringData topicIds;
	private UIFieldStringData notTopicIds;
	private UIFieldIntegerData topicRelatedTo;
	private UIFieldIntegerData notTopicRelatedTo;
	private UIFieldIntegerData topicRelatedFrom;
	private UIFieldIntegerData notTopicRelatedFrom;
	private UIFieldStringData topicTitle;
	private UIFieldStringData notTopicTitle;
	private UIFieldStringData topicDescription;
	private UIFieldStringData notTopicDescription;
	private UIFieldDateTimeData startCreateDate;
	private UIFieldDateTimeData endCreateDate;
	private UIFieldStringData topicXML;
	private UIFieldStringData notTopicXML;
	private UIFieldStringData logic;
	private UIFieldBooleanData hasRelationships;
	private UIFieldBooleanData hasIncomingRelationships;
	private UIFieldStringData topicTextSearch;
	private UIFieldBooleanData hasXMLErrors;
	private UIFieldDateTimeData startEditDate;
	private UIFieldDateTimeData endEditDate;
	private UIFieldIntegerData editedInLastDays;
	private UIFieldIntegerData notEditedInLastDays;
	private UIFieldBooleanData hasOpenBugzillaBugs;
	private UIFieldBooleanData hasBugzillaBugs;
	private UIFieldStringListData propertyTags;
	private UIFieldStringData topicIncludedInSpec;
	private UIFieldStringData notTopicIncludedInSpec;
	private UIFieldBooleanData latestTranslations;
	private UIFieldBooleanData latestCompletedTranslations;
	private UIFieldStringData zanataIds;
	private UIFieldStringData notZanataIds;
	
	private UIFieldBooleanData notHasXMLErrors;
	private UIFieldBooleanData notHasRelationships;
	private UIFieldBooleanData notHasIncomingRelationships;
	private UIFieldBooleanData notHasOpenBugzillaBugs;
	private UIFieldBooleanData notHasBugzillaBugs;
	private UIFieldBooleanData notLatestTranslations;
	private UIFieldBooleanData notLatestCompletedTranslations;
	
	private List<UIFieldDataBase<?>> singleFilterVars = new ArrayList<UIFieldDataBase<?>>();
	private List<UIFieldDataBase<?>> multipleFilterVars = new ArrayList<UIFieldDataBase<?>>();
	
	public TopicFilter()
	{
		resetAllValues();
	}

	private void resetAllValues()
	{
		/* Topic ID's */
		topicIds = new UIFieldStringData(Constants.TOPIC_IDS_FILTER_VAR, Constants.TOPIC_IDS_FILTER_VAR_DESC);
		notTopicIds = new UIFieldStringData(Constants.TOPIC_IDS_NOT_FILTER_VAR, Constants.TOPIC_IDS_NOT_FILTER_VAR_DESC);
		
		/* Zanata ID's */
		zanataIds = new UIFieldStringData(Constants.ZANATA_IDS_FILTER_VAR, Constants.ZANATA_IDS_FILTER_VAR_DESC);
		notZanataIds = new UIFieldStringData(Constants.ZANATA_IDS_NOT_FILTER_VAR, Constants.ZANATA_IDS_NOT_FILTER_VAR_DESC);
		
		/* Topic Related To */
		topicRelatedTo = new UIFieldIntegerData(Constants.TOPIC_RELATED_TO, Constants.TOPIC_RELATED_TO_DESC);
		notTopicRelatedTo = new UIFieldIntegerData(Constants.TOPIC_NOT_RELATED_TO, Constants.TOPIC_NOT_RELATED_TO_DESC);
		
		/* Topic Related From */
		topicRelatedFrom = new UIFieldIntegerData(Constants.TOPIC_RELATED_FROM, Constants.TOPIC_RELATED_FROM_DESC);
		notTopicRelatedFrom = new UIFieldIntegerData(Constants.TOPIC_NOT_RELATED_FROM, Constants.TOPIC_NOT_RELATED_FROM_DESC);		
		
		/* Topic Title */
		topicTitle = new UIFieldStringData(Constants.TOPIC_TITLE_FILTER_VAR, Constants.TOPIC_TITLE_FILTER_VAR_DESC);
		notTopicTitle = new UIFieldStringData(Constants.TOPIC_TITLE_NOT_FILTER_VAR, Constants.TOPIC_TITLE_NOT_FILTER_VAR_DESC);
	
		/* Topic Description */
		topicDescription = new UIFieldStringData(Constants.TOPIC_DESCRIPTION_FILTER_VAR, Constants.TOPIC_DESCRIPTION_FILTER_VAR_DESC);
		notTopicDescription = new UIFieldStringData(Constants.TOPIC_DESCRIPTION_NOT_FILTER_VAR, Constants.TOPIC_DESCRIPTION_NOT_FILTER_VAR_DESC);
		
		/* Topic is included in content specification */
		topicIncludedInSpec = new UIFieldStringData(Constants.TOPIC_IS_INCLUDED_IN_SPEC, Constants.TOPIC_IS_INCLUDED_IN_SPEC_DESC);
		notTopicIncludedInSpec = new UIFieldStringData(Constants.TOPIC_IS_NOT_INCLUDED_IN_SPEC, Constants.TOPIC_IS_NOT_INCLUDED_IN_SPEC_DESC);
		
		/* Topic XML */
		topicXML = new UIFieldStringData(Constants.TOPIC_XML_FILTER_VAR, Constants.TOPIC_XML_FILTER_VAR_DESC);
		notTopicXML = new UIFieldStringData(Constants.TOPIC_XML_FILTER_VAR, Constants.TOPIC_XML_FILTER_VAR_DESC);

		/* Topic Edited in last days */
		editedInLastDays = new UIFieldIntegerData(Constants.TOPIC_EDITED_IN_LAST_DAYS, Constants.TOPIC_EDITED_IN_LAST_DAYS_DESC);
		notEditedInLastDays = new UIFieldIntegerData(Constants.TOPIC_NOT_EDITED_IN_LAST_DAYS, Constants.TOPIC_NOT_EDITED_IN_LAST_DAYS_DESC);
		
		/* Has XML Errors */
		hasXMLErrors = new UIFieldBooleanData(Constants.TOPIC_HAS_XML_ERRORS, Constants.TOPIC_HAS_XML_ERRORS_DESC);
		notHasXMLErrors = new UIFieldBooleanData(Constants.TOPIC_HAS_NOT_XML_ERRORS, Constants.TOPIC_HAS_NOT_XML_ERRORS_DESC);
		
		/* Has Relationships */
		hasRelationships = new UIFieldBooleanData(Constants.TOPIC_HAS_RELATIONSHIPS, Constants.TOPIC_HAS_RELATIONSHIPS_DESC);
		notHasRelationships = new UIFieldBooleanData(Constants.TOPIC_HAS_NOT_RELATIONSHIPS, Constants.TOPIC_HAS_NOT_RELATIONSHIPS_DESC);
		
		/* Has Incoming Relationships */
		hasIncomingRelationships = new UIFieldBooleanData(Constants.TOPIC_HAS_INCOMING_RELATIONSHIPS, Constants.TOPIC_HAS_INCOMING_RELATIONSHIPS_DESC);
		notHasIncomingRelationships = new UIFieldBooleanData(Constants.TOPIC_HAS_NOT_INCOMING_RELATIONSHIPS, Constants.TOPIC_HAS_NOT_INCOMING_RELATIONSHIPS_DESC);
		
		/* Has Open Bugzilla Bugs */
		hasOpenBugzillaBugs = new UIFieldBooleanData(Constants.TOPIC_HAS_OPEN_BUGZILLA_BUGS, Constants.TOPIC_HAS_OPEN_BUGZILLA_BUGS_DESC);
		notHasOpenBugzillaBugs = new UIFieldBooleanData(Constants.TOPIC_HAS_NOT_OPEN_BUGZILLA_BUGS, Constants.TOPIC_HAS_NOT_OPEN_BUGZILLA_BUGS_DESC);
		
		/* Has Bugzilla Bugs */
		hasBugzillaBugs = new UIFieldBooleanData(Constants.TOPIC_HAS_BUGZILLA_BUGS, Constants.TOPIC_HAS_BUGZILLA_BUGS_DESC);
		notHasBugzillaBugs = new UIFieldBooleanData(Constants.TOPIC_HAS_NOT_BUGZILLA_BUGS, Constants.TOPIC_HAS_NOT_BUGZILLA_BUGS_DESC);
		
		/* Latest Translations */
		latestTranslations = new UIFieldBooleanData(Constants.LATEST_TRANSLATIONS_FILTER_VAR, Constants.LATEST_TRANSLATIONS_FILTER_VAR_DESC);
		notLatestTranslations = new UIFieldBooleanData(Constants.NOT_LATEST_TRANSLATIONS_FILTER_VAR, Constants.NOT_LATEST_TRANSLATIONS_FILTER_VAR_DESC);
		
		/* Latest Completed Translations */
		latestCompletedTranslations = new UIFieldBooleanData(Constants.LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR, Constants.LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR_DESC);
		notLatestCompletedTranslations = new UIFieldBooleanData(Constants.NOT_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR, Constants.NOT_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR_DESC);
		
		topicTextSearch = new UIFieldStringData(Constants.TOPIC_TEXT_SEARCH_FILTER_VAR, Constants.TOPIC_TEXT_SEARCH_FILTER_VAR_DESC);
		startCreateDate = new UIFieldDateTimeData(Constants.TOPIC_STARTDATE_FILTER_VAR, Constants.TOPIC_STARTDATE_FILTER_VAR_DESC);
		endCreateDate = new UIFieldDateTimeData(Constants.TOPIC_ENDDATE_FILTER_VAR, Constants.TOPIC_ENDDATE_FILTER_VAR_DESC);
		logic = new UIFieldStringData(Constants.TOPIC_LOGIC_FILTER_VAR, Constants.TOPIC_LOGIC_FILTER_VAR_DESC);
		startEditDate = new UIFieldDateTimeData(Constants.TOPIC_STARTEDITDATE_FILTER_VAR, Constants.TOPIC_STARTEDITDATE_FILTER_VAR_DESC);
		endEditDate = new UIFieldDateTimeData(Constants.TOPIC_ENDEDITDATE_FILTER_VAR, Constants.TOPIC_ENDEDITDATE_FILTER_VAR_DESC);
		propertyTags = new UIFieldStringListData(Constants.TOPIC_PROPERTY_TAG, Constants.TOPIC_PROPERTY_TAG_DESC);
		
		setupSingleFilterVars();
		
		setupMultipleFilterVars();
	}
	
	protected void setupSingleFilterVars()
	{
		singleFilterVars.clear();
		singleFilterVars.add(this.topicTextSearch);
		singleFilterVars.add(this.topicIds);
		singleFilterVars.add(this.topicIncludedInSpec);
		singleFilterVars.add(this.topicXML);
		singleFilterVars.add(this.topicTitle);
		singleFilterVars.add(this.topicDescription);
		singleFilterVars.add(this.startCreateDate);
		singleFilterVars.add(this.endCreateDate);
		singleFilterVars.add(this.startEditDate);
		singleFilterVars.add(this.endEditDate);
		singleFilterVars.add(this.logic);
		singleFilterVars.add(this.hasRelationships);
		singleFilterVars.add(this.hasIncomingRelationships);
		singleFilterVars.add(this.topicRelatedTo);
		singleFilterVars.add(this.topicRelatedFrom);
		singleFilterVars.add(this.hasXMLErrors);
		singleFilterVars.add(this.editedInLastDays);
		singleFilterVars.add(this.hasBugzillaBugs);
		singleFilterVars.add(this.hasOpenBugzillaBugs);
		singleFilterVars.add(this.latestTranslations);
		singleFilterVars.add(this.latestCompletedTranslations);
		singleFilterVars.add(this.zanataIds);
		
		singleFilterVars.add(this.notTopicIds);
		singleFilterVars.add(this.notTopicIncludedInSpec);
		singleFilterVars.add(this.notTopicXML);
		singleFilterVars.add(this.notTopicTitle);
		singleFilterVars.add(this.notTopicDescription);
		singleFilterVars.add(this.notTopicRelatedTo);
		singleFilterVars.add(this.notTopicRelatedFrom);
		singleFilterVars.add(this.notZanataIds);
		
		singleFilterVars.add(this.notHasXMLErrors);
		singleFilterVars.add(this.notHasRelationships);
		singleFilterVars.add(this.notHasIncomingRelationships);
		singleFilterVars.add(this.notHasBugzillaBugs);
		singleFilterVars.add(this.notHasOpenBugzillaBugs);
		
		singleFilterVars.add(this.notLatestTranslations);
		singleFilterVars.add(this.notLatestCompletedTranslations);
	}
	
	protected void setupMultipleFilterVars()
	{
		multipleFilterVars.add(this.propertyTags);
	}
	
	protected List<UIFieldDataBase<?>> getAllFilterVars()
	{
		final List<UIFieldDataBase<?>> fields = new ArrayList<UIFieldDataBase<?>>();
		fields.addAll(singleFilterVars);
		fields.addAll(multipleFilterVars);
		return fields;
	}

	public List<Integer> getRelatedTopicIDs()
	{
		final List<Integer> retValue = EntityUtilities.getOutgoingRelatedTopicIDs(this.topicRelatedFrom.getData());
		/*
		 * The EntityQuery class treats an empty list as null, and will not
		 * attempt to apply a restriction against it. If there are no topics
		 * returned, add -1 to the list to ensure that no topics are matched,
		 * but the list is not empty and therefore counted as a restriction.
		 */
		if (retValue != null && retValue.size() == 0)
			retValue.add(-1);
		return retValue;
	}

	public List<Integer> getIncomingRelatedTopicIDs()
	{
		final List<Integer> retValue = EntityUtilities.getIncomingRelatedTopicIDs(this.topicRelatedTo.getData());
		if (retValue != null && retValue.size() == 0)
			retValue.add(-1);
		return retValue;
	}

	public List<Integer> getEditedTopics()
	{
		final List<Integer> retValue = EntityUtilities.getEditedEntities(Topic.class, "topicId", this.startEditDate.getData(), this.endEditDate.getData());
		return retValue;
	}

	public String getFieldValue(final String fieldName)
	{
		if (fieldName.startsWith(Constants.TOPIC_PROPERTY_TAG))
		{
			try
			{
				final String index = fieldName.replace(Constants.TOPIC_PROPERTY_TAG, "");

				/*
				 * index will be empty if the fieldName is just
				 * Constants.TOPIC_PROPERTY_TAG, which can happen when another
				 * object is looping over the getBaseFilterNames() keyset.
				 */
				if (!index.isEmpty())
				{
					final Integer indexInt = Integer.parseInt(index);

					/*
					 * propertyTags will be null unless one of the
					 * setPropertyTag() method is called
					 */
					if (this.propertyTags.getData() != null && this.propertyTags.getData().size() > indexInt)
						return this.propertyTags.getData().get(indexInt);
				}
				return null;
			}
			catch (final Exception ex)
			{
				// could not parse integer, so fail
				SkynetExceptionUtilities.handleException(ex, true, "Probably a malformed URL query parameter for the 'Property Tag' Topic ID");
				return null;
			}

		}
		else
		{
			for (final UIFieldDataBase<?> uiField : singleFilterVars)
			{
				if (fieldName.equals(uiField.getName()))
				{
					return uiField.toString();
				}
			}
		}

		return null;
	}

	public void setFieldValue(final String fieldName, final String fieldValue)
	{
		if (fieldName.startsWith(Constants.TOPIC_PROPERTY_TAG))
		{
			try
			{
				final String index = fieldName.replace(Constants.TOPIC_PROPERTY_TAG, "");
				final Integer indexInt = Integer.parseInt(index);
				this.setPropertyTag(fieldValue, indexInt);
			}
			catch (final Exception ex)
			{
				// could not parse integer, so fail
				SkynetExceptionUtilities.handleException(ex, true, "Probably a malformed URL query parameter for the 'Property Tag' Topic ID");
			}

		}
		else
		{
			for (final UIFieldDataBase<?> uiField : singleFilterVars)
			{
				if (fieldName.equals(uiField.getName()))
				{
					uiField.setData(fieldValue);
				}
			}
		}
	}

	public Map<String, String> getFilterValues()
	{
		final Map<String, String> retValue = new HashMap<String, String>();
		for (final UIFieldDataBase<?> uiField : singleFilterVars)
		{
			retValue.put(uiField.getName(), uiField.getData().toString());
		}
		
		int count = 1;
		for (final String propertyTag : propertyTags.getData())
		{
			retValue.put(Constants.TOPIC_PROPERTY_TAG + " " + count, propertyTag);
			++count;
		}

		return retValue;
	}

	/**
	 * @return A map of the expanded filter field names (i.e. with regular
	 *         expressions) mapped to their descriptions
	 */
	private static Map<String, String> getFilterNames()
	{
		final Map<String, String> retValue = getSingleFilterNames();
		retValue.put(Constants.TOPIC_PROPERTY_TAG + "\\d+", Constants.TOPIC_PROPERTY_TAG_DESC);
		
		return retValue;
	}

	/**
	 * @return A map of the base filter field names (i.e. with no regular
	 *         expressions) mapped to their descriptions
	 */
	public static Map<String, String> getBaseFilterNames()
	{
		final Map<String, String> retValue = getSingleFilterNames();
		retValue.put(Constants.TOPIC_PROPERTY_TAG, Constants.TOPIC_PROPERTY_TAG_DESC);

		return retValue;
	}

	/**
	 * @return A map of the base filter field names that can not have multiple
	 *         mappings
	 */
	private static Map<String, String> getSingleFilterNames()
	{
		final Map<String, String> retValue = new HashMap<String, String>();
		retValue.put(Constants.TOPIC_TEXT_SEARCH_FILTER_VAR, Constants.TOPIC_TEXT_SEARCH_FILTER_VAR_DESC);
		retValue.put(Constants.TOPIC_IDS_FILTER_VAR, Constants.TOPIC_IDS_FILTER_VAR_DESC);
		retValue.put(Constants.TOPIC_IS_INCLUDED_IN_SPEC, Constants.TOPIC_IS_INCLUDED_IN_SPEC_DESC);
		retValue.put(Constants.TOPIC_XML_FILTER_VAR, Constants.TOPIC_XML_FILTER_VAR_DESC);
		retValue.put(Constants.TOPIC_TITLE_FILTER_VAR, Constants.TOPIC_TITLE_FILTER_VAR_DESC);
		retValue.put(Constants.TOPIC_DESCRIPTION_FILTER_VAR, Constants.TOPIC_DESCRIPTION_FILTER_VAR_DESC);
		retValue.put(Constants.TOPIC_STARTDATE_FILTER_VAR, Constants.TOPIC_STARTDATE_FILTER_VAR_DESC);
		retValue.put(Constants.TOPIC_ENDDATE_FILTER_VAR, Constants.TOPIC_ENDDATE_FILTER_VAR_DESC);
		retValue.put(Constants.TOPIC_STARTEDITDATE_FILTER_VAR, Constants.TOPIC_STARTEDITDATE_FILTER_VAR_DESC);
		retValue.put(Constants.TOPIC_ENDEDITDATE_FILTER_VAR, Constants.TOPIC_ENDEDITDATE_FILTER_VAR_DESC);
		retValue.put(Constants.TOPIC_LOGIC_FILTER_VAR, Constants.TOPIC_LOGIC_FILTER_VAR_DESC);
		retValue.put(Constants.TOPIC_HAS_RELATIONSHIPS, Constants.TOPIC_HAS_RELATIONSHIPS_DESC);
		retValue.put(Constants.TOPIC_HAS_INCOMING_RELATIONSHIPS, Constants.TOPIC_HAS_INCOMING_RELATIONSHIPS_DESC);
		retValue.put(Constants.TOPIC_RELATED_TO, Constants.TOPIC_RELATED_TO_DESC);
		retValue.put(Constants.TOPIC_RELATED_FROM, Constants.TOPIC_RELATED_FROM_DESC);
		retValue.put(Constants.TOPIC_HAS_XML_ERRORS, Constants.TOPIC_HAS_XML_ERRORS_DESC);
		retValue.put(Constants.TOPIC_EDITED_IN_LAST_DAYS, Constants.TOPIC_EDITED_IN_LAST_DAYS_DESC);
		retValue.put(Constants.TOPIC_HAS_OPEN_BUGZILLA_BUGS, Constants.TOPIC_HAS_OPEN_BUGZILLA_BUGS_DESC);
		retValue.put(Constants.TOPIC_HAS_BUGZILLA_BUGS, Constants.TOPIC_HAS_BUGZILLA_BUGS_DESC);
		retValue.put(Constants.LATEST_TRANSLATIONS_FILTER_VAR, Constants.LATEST_TRANSLATIONS_FILTER_VAR_DESC);
		retValue.put(Constants.LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR, Constants.LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR_DESC);
		retValue.put(Constants.ZANATA_IDS_FILTER_VAR, Constants.ZANATA_IDS_FILTER_VAR_DESC);
		
		retValue.put(Constants.TOPIC_IDS_NOT_FILTER_VAR, Constants.TOPIC_IDS_NOT_FILTER_VAR_DESC);
		retValue.put(Constants.TOPIC_IS_NOT_INCLUDED_IN_SPEC, Constants.TOPIC_IS_NOT_INCLUDED_IN_SPEC_DESC);
		retValue.put(Constants.TOPIC_XML_NOT_FILTER_VAR, Constants.TOPIC_XML_NOT_FILTER_VAR_DESC);
		retValue.put(Constants.TOPIC_TITLE_NOT_FILTER_VAR, Constants.TOPIC_TITLE_NOT_FILTER_VAR_DESC);
		retValue.put(Constants.TOPIC_DESCRIPTION_NOT_FILTER_VAR, Constants.TOPIC_DESCRIPTION_NOT_FILTER_VAR_DESC);
		retValue.put(Constants.TOPIC_NOT_RELATED_TO, Constants.TOPIC_NOT_RELATED_TO_DESC);
		retValue.put(Constants.TOPIC_NOT_RELATED_FROM, Constants.TOPIC_NOT_RELATED_FROM_DESC);
		retValue.put(Constants.TOPIC_NOT_EDITED_IN_LAST_DAYS, Constants.TOPIC_NOT_EDITED_IN_LAST_DAYS_DESC);
		retValue.put(Constants.ZANATA_IDS_NOT_FILTER_VAR, Constants.ZANATA_IDS_NOT_FILTER_VAR_DESC);
		
		retValue.put(Constants.TOPIC_HAS_NOT_XML_ERRORS, Constants.TOPIC_HAS_NOT_XML_ERRORS_DESC);
		retValue.put(Constants.TOPIC_HAS_NOT_OPEN_BUGZILLA_BUGS, Constants.TOPIC_HAS_NOT_OPEN_BUGZILLA_BUGS_DESC);
		retValue.put(Constants.TOPIC_HAS_NOT_BUGZILLA_BUGS, Constants.TOPIC_HAS_NOT_BUGZILLA_BUGS_DESC);
		retValue.put(Constants.TOPIC_HAS_NOT_RELATIONSHIPS, Constants.TOPIC_HAS_NOT_RELATIONSHIPS_DESC);
		retValue.put(Constants.TOPIC_HAS_NOT_INCOMING_RELATIONSHIPS, Constants.TOPIC_HAS_NOT_INCOMING_RELATIONSHIPS_DESC);
		
		retValue.put(Constants.NOT_LATEST_TRANSLATIONS_FILTER_VAR, Constants.NOT_LATEST_TRANSLATIONS_FILTER_VAR_DESC);
		retValue.put(Constants.NOT_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR, Constants.NOT_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR_DESC);
		
		return retValue;
	}

	public static boolean hasFilterName(final String input)
	{
		boolean retValue = false;
		for (final String name : getFilterNames().keySet())
		{
			if (input.matches("^" + name + "$"))
			{
				retValue = true;
				break;
			}
		}

		return retValue;
	}

	public static String getFilterDesc(final String input)
	{
		for (final String name : getFilterNames().keySet())
		{
			if (input.matches("^" + name + "$"))
			{
				return getFilterNames().get(name);
			}
		}

		return "";
	}

	public void syncWithFilter(final Filter filter)
	{
		for (final FilterField field : filter.getFilterFields())
			this.setFieldValue(field.getField(), field.getValue());
	}

	public void loadFilterFields(final Filter filter)
	{
		resetAllValues();

		for (final FilterField filterField : filter.getFilterFields())
		{
			final String field = filterField.getField();
			final String value = filterField.getValue();

			this.setFieldValue(field, value);
		}
	}

	public void setPropertyTag(final String propertyTag, final int index)
	{
		if (this.propertyTags.getData() == null)
			this.propertyTags.setData(new ArrayList<String>());

		if (this.propertyTags.getData().size() < index)
		{
			final int start = this.propertyTags.getData().size();
			for (int i = start; i < index; ++i)
			{
				this.propertyTags.getData().add("");
			}
		}

		this.propertyTags.getData().set(index, propertyTag);
	}
	
	public UIFieldStringData getTopicIds()
	{
		return topicIds;
	}

	public UIFieldIntegerData getTopicRelatedTo()
	{
		return topicRelatedTo;
	}

	public UIFieldIntegerData getTopicRelatedFrom()
	{
		return topicRelatedFrom;
	}

	public UIFieldStringData getTopicTitle()
	{
		return topicTitle;
	}

	public UIFieldStringData getTopicDescription()
	{
		return topicDescription;
	}
	
	public UIFieldDateTimeData getStartCreateDate()
	{
		return startCreateDate;
	}

	public UIFieldDateTimeData getEndCreateDate()
	{
		return endCreateDate;
	}

	public UIFieldStringData getTopicXML()
	{
		return topicXML;
	}

	public UIFieldStringData getLogic()
	{
		return logic;
	}

	public UIFieldBooleanData getHasRelationships()
	{
		return hasRelationships;
	}

	public UIFieldBooleanData getHasIncomingRelationships()
	{
		return hasIncomingRelationships;
	}

	public UIFieldStringData getTopicTextSearch()
	{
		return topicTextSearch;
	}

	public UIFieldBooleanData getHasXMLErrors()
	{
		return hasXMLErrors;
	}

	public UIFieldDateTimeData getStartEditDate()
	{
		return startEditDate;
	}
	
	public UIFieldDateTimeData getEndEditDate()
	{
		return endEditDate;
	}

	public UIFieldIntegerData getEditedInLastDays()
	{
		return editedInLastDays;
	}

	public UIFieldBooleanData getHasOpenBugzillaBugs()
	{
		return hasOpenBugzillaBugs;
	}

	public UIFieldBooleanData getHasBugzillaBugs()
	{
		return hasBugzillaBugs;
	}

	public UIFieldStringData getTopicIncludedInSpec()
	{
		return topicIncludedInSpec;
	}

	public UIFieldBooleanData getLatestTranslations()
	{
		return latestTranslations;
	}

	public UIFieldBooleanData getLatestCompletedTranslations()
	{
		return latestCompletedTranslations;
	}

	public UIFieldStringData getNotTopicIds()
	{
		return notTopicIds;
	}
	
	public UIFieldIntegerData getNotTopicRelatedTo()
	{
		return notTopicRelatedTo;
	}

	public UIFieldIntegerData getNotTopicRelatedFrom()
	{
		return notTopicRelatedFrom;
	}

	public UIFieldStringData getNotTopicTitle()
	{
		return notTopicTitle;
	}

	public UIFieldStringData getNotTopicDescription()
	{
		return notTopicDescription;
	}

	public UIFieldStringData getNotTopicXML()
	{
		return notTopicXML;
	}

	public UIFieldStringData getNotTopicIncludedInSpec()
	{
		return notTopicIncludedInSpec;
	}

	public UIFieldIntegerData getNotEditedInLastDays()
	{
		return notEditedInLastDays;
	}
	
	public void setStartCreateDatePlain(final Date startCreateDate)
	{
		this.startCreateDate.setData(startCreateDate);
	}

	public Date getStartCreateDatePlain()
	{
		return this.startCreateDate.getDateData();
	}

	public void setStartEditDatePlain(final Date startEditDate)
	{
		this.startEditDate.setData(startEditDate);
	}

	public Date getStartEditDatePlain()
	{
		return this.startEditDate.getDateData();
	}

	public void setEndCreateDatePlain(final Date endCreateDate)
	{
		this.endCreateDate.setData(endCreateDate);
	}

	public Date getEndCreateDatePlain()
	{
		return this.endCreateDate.getDateData();
	}

	public void setEndEditDatePlain(final Date endEditDate)
	{
		this.endEditDate.setData(endEditDate);
	}

	public Date getEndEditDatePlain()
	{
		return this.endEditDate.getDateData();
	}

	public UIFieldBooleanData getNotHasXMLErrors()
	{
		return notHasXMLErrors;
	}

	public UIFieldBooleanData getNotHasRelationships()
	{
		return notHasRelationships;
	}

	public UIFieldBooleanData getNotHasIncomingRelationships()
	{
		return notHasIncomingRelationships;
	}

	public UIFieldBooleanData getNotHasOpenBugzillaBugs()
	{
		return notHasOpenBugzillaBugs;
	}
	
	public UIFieldBooleanData getNotHasBugzillaBugs()
	{
		return notHasBugzillaBugs;
	}

	public UIFieldBooleanData getNotLatestTranslations()
	{
		return notLatestTranslations;
	}

	public UIFieldBooleanData getNotLatestCompletedTranslations()
	{
		return notLatestCompletedTranslations;
	}

	public UIFieldStringData getZanataIds()
	{
		return zanataIds;
	}

	public UIFieldStringData getNotZanataIds()
	{
		return notZanataIds;
	}
}
