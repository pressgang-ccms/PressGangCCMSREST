package org.jboss.pressgang.ccms.restserver.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.model.Filter;
import org.jboss.pressgang.ccms.model.FilterField;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.structures.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a mechanism to temporarily store and easily convert a set of fields for a filter until it needs to be
 * saved to a database entity. This is also used by the Seam GUI to store the data temporarily.
 */
public class TopicFieldFilter extends BaseFieldFilter {
    private static final Logger log = LoggerFactory.getLogger(TopicFieldFilter.class);

    /**
     * A map of the base filter field names that can not have multiple mappings
     */
    protected static final Map<String, String> singleFilterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = -6343139695468503659L;

        {
            put(CommonFilterConstants.TOPIC_TEXT_SEARCH_FILTER_VAR, CommonFilterConstants.TOPIC_TEXT_SEARCH_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_IDS_FILTER_VAR, CommonFilterConstants.TOPIC_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_IS_INCLUDED_IN_SPEC, CommonFilterConstants.TOPIC_IS_INCLUDED_IN_SPEC_DESC);
            put(CommonFilterConstants.TOPIC_XML_FILTER_VAR, CommonFilterConstants.TOPIC_XML_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_TITLE_FILTER_VAR, CommonFilterConstants.TOPIC_TITLE_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_DESCRIPTION_FILTER_VAR, CommonFilterConstants.TOPIC_DESCRIPTION_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_STARTDATE_FILTER_VAR, CommonFilterConstants.TOPIC_STARTDATE_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_ENDDATE_FILTER_VAR, CommonFilterConstants.TOPIC_ENDDATE_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_STARTEDITDATE_FILTER_VAR, CommonFilterConstants.TOPIC_STARTEDITDATE_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_ENDEDITDATE_FILTER_VAR, CommonFilterConstants.TOPIC_ENDEDITDATE_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_HAS_RELATIONSHIPS, CommonFilterConstants.TOPIC_HAS_RELATIONSHIPS_DESC);
            put(CommonFilterConstants.TOPIC_HAS_INCOMING_RELATIONSHIPS,
                    CommonFilterConstants.TOPIC_HAS_INCOMING_RELATIONSHIPS_DESC);
            put(CommonFilterConstants.TOPIC_RELATED_TO, CommonFilterConstants.TOPIC_RELATED_TO_DESC);
            put(CommonFilterConstants.TOPIC_RELATED_FROM, CommonFilterConstants.TOPIC_RELATED_FROM_DESC);
            put(CommonFilterConstants.TOPIC_HAS_XML_ERRORS, CommonFilterConstants.TOPIC_HAS_XML_ERRORS_DESC);
            put(CommonFilterConstants.TOPIC_EDITED_IN_LAST_DAYS, CommonFilterConstants.TOPIC_EDITED_IN_LAST_DAYS_DESC);
            put(CommonFilterConstants.TOPIC_HAS_OPEN_BUGZILLA_BUGS, CommonFilterConstants.TOPIC_HAS_OPEN_BUGZILLA_BUGS_DESC);
            put(CommonFilterConstants.TOPIC_HAS_BUGZILLA_BUGS, CommonFilterConstants.TOPIC_HAS_BUGZILLA_BUGS_DESC);
            put(CommonFilterConstants.TOPIC_LATEST_TRANSLATIONS_FILTER_VAR,
                    CommonFilterConstants.TOPIC_LATEST_TRANSLATIONS_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR,
                    CommonFilterConstants.TOPIC_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR_DESC);

            put(CommonFilterConstants.TOPIC_IDS_NOT_FILTER_VAR, CommonFilterConstants.TOPIC_IDS_NOT_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_IS_NOT_INCLUDED_IN_SPEC, CommonFilterConstants.TOPIC_IS_NOT_INCLUDED_IN_SPEC_DESC);
            put(CommonFilterConstants.TOPIC_XML_NOT_FILTER_VAR, CommonFilterConstants.TOPIC_XML_NOT_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_TITLE_NOT_FILTER_VAR, CommonFilterConstants.TOPIC_TITLE_NOT_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_DESCRIPTION_NOT_FILTER_VAR,
                    CommonFilterConstants.TOPIC_DESCRIPTION_NOT_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_NOT_RELATED_TO, CommonFilterConstants.TOPIC_NOT_RELATED_TO_DESC);
            put(CommonFilterConstants.TOPIC_NOT_RELATED_FROM, CommonFilterConstants.TOPIC_NOT_RELATED_FROM_DESC);
            put(CommonFilterConstants.TOPIC_NOT_EDITED_IN_LAST_DAYS, CommonFilterConstants.TOPIC_NOT_EDITED_IN_LAST_DAYS_DESC);

            put(CommonFilterConstants.TOPIC_HAS_NOT_XML_ERRORS, CommonFilterConstants.TOPIC_HAS_NOT_XML_ERRORS_DESC);
            put(CommonFilterConstants.TOPIC_HAS_NOT_OPEN_BUGZILLA_BUGS,
                    CommonFilterConstants.TOPIC_HAS_NOT_OPEN_BUGZILLA_BUGS_DESC);
            put(CommonFilterConstants.TOPIC_HAS_NOT_BUGZILLA_BUGS, CommonFilterConstants.TOPIC_HAS_NOT_BUGZILLA_BUGS_DESC);
            put(CommonFilterConstants.TOPIC_HAS_NOT_RELATIONSHIPS, CommonFilterConstants.TOPIC_HAS_NOT_RELATIONSHIPS_DESC);
            put(CommonFilterConstants.TOPIC_HAS_NOT_INCOMING_RELATIONSHIPS,
                    CommonFilterConstants.TOPIC_HAS_NOT_INCOMING_RELATIONSHIPS_DESC);

            put(CommonFilterConstants.TOPIC_NOT_LATEST_TRANSLATIONS_FILTER_VAR,
                    CommonFilterConstants.TOPIC_NOT_LATEST_TRANSLATIONS_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_NOT_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR,
                    CommonFilterConstants.TOPIC_NOT_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR_DESC);
        }
    });

    private FilterFieldStringData topicIds;
    private FilterFieldStringData notTopicIds;
    private FilterFieldIntegerData topicRelatedTo;
    private FilterFieldIntegerData notTopicRelatedTo;
    private FilterFieldIntegerData topicRelatedFrom;
    private FilterFieldIntegerData notTopicRelatedFrom;
    private FilterFieldStringData topicTitle;
    private FilterFieldStringData notTopicTitle;
    private FilterFieldStringData topicDescription;
    private FilterFieldStringData notTopicDescription;
    private FilterFieldDateTimeData startCreateDate;
    private FilterFieldDateTimeData endCreateDate;
    private FilterFieldStringData topicXML;
    private FilterFieldStringData notTopicXML;
    private FilterFieldBooleanData hasRelationships;
    private FilterFieldBooleanData hasIncomingRelationships;
    private FilterFieldStringData topicTextSearch;
    private FilterFieldBooleanData hasXMLErrors;
    private FilterFieldDateTimeData startEditDate;
    private FilterFieldDateTimeData endEditDate;
    private FilterFieldIntegerData editedInLastDays;
    private FilterFieldIntegerData notEditedInLastDays;
    private FilterFieldBooleanData hasOpenBugzillaBugs;
    private FilterFieldBooleanData hasBugzillaBugs;
    private FilterFieldStringMapData propertyTags;
    private FilterFieldStringData topicIncludedInSpec;
    private FilterFieldStringData notTopicIncludedInSpec;
    private FilterFieldBooleanData latestTranslations;
    private FilterFieldBooleanData latestCompletedTranslations;
    private FilterFieldStringData zanataIds;
    private FilterFieldStringData notZanataIds;

    private FilterFieldBooleanData notHasXMLErrors;
    private FilterFieldBooleanData notHasRelationships;
    private FilterFieldBooleanData notHasIncomingRelationships;
    private FilterFieldBooleanData notHasOpenBugzillaBugs;
    private FilterFieldBooleanData notHasBugzillaBugs;
    private FilterFieldBooleanData notLatestTranslations;
    private FilterFieldBooleanData notLatestCompletedTranslations;

    private List<FilterFieldDataBase<?>> multipleFilterVars = new ArrayList<FilterFieldDataBase<?>>();

    public TopicFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        /* Topic ID's */
        topicIds = new FilterFieldStringData(CommonFilterConstants.TOPIC_IDS_FILTER_VAR,
                CommonFilterConstants.TOPIC_IDS_FILTER_VAR_DESC);
        notTopicIds = new FilterFieldStringData(CommonFilterConstants.TOPIC_IDS_NOT_FILTER_VAR,
                CommonFilterConstants.TOPIC_IDS_NOT_FILTER_VAR_DESC);

        /* Zanata ID's */
        zanataIds = new FilterFieldStringData(CommonFilterConstants.ZANATA_IDS_FILTER_VAR,
                CommonFilterConstants.ZANATA_IDS_FILTER_VAR_DESC);
        notZanataIds = new FilterFieldStringData(CommonFilterConstants.ZANATA_IDS_NOT_FILTER_VAR,
                CommonFilterConstants.ZANATA_IDS_NOT_FILTER_VAR_DESC);

        /* Topic Related To */
        topicRelatedTo = new FilterFieldIntegerData(CommonFilterConstants.TOPIC_RELATED_TO,
                CommonFilterConstants.TOPIC_RELATED_TO_DESC);
        notTopicRelatedTo = new FilterFieldIntegerData(CommonFilterConstants.TOPIC_NOT_RELATED_TO,
                CommonFilterConstants.TOPIC_NOT_RELATED_TO_DESC);

        /* Topic Related From */
        topicRelatedFrom = new FilterFieldIntegerData(CommonFilterConstants.TOPIC_RELATED_FROM,
                CommonFilterConstants.TOPIC_RELATED_FROM_DESC);
        notTopicRelatedFrom = new FilterFieldIntegerData(CommonFilterConstants.TOPIC_NOT_RELATED_FROM,
                CommonFilterConstants.TOPIC_NOT_RELATED_FROM_DESC);

        /* Topic Title */
        topicTitle = new FilterFieldStringData(CommonFilterConstants.TOPIC_TITLE_FILTER_VAR,
                CommonFilterConstants.TOPIC_TITLE_FILTER_VAR_DESC);
        notTopicTitle = new FilterFieldStringData(CommonFilterConstants.TOPIC_TITLE_NOT_FILTER_VAR,
                CommonFilterConstants.TOPIC_TITLE_NOT_FILTER_VAR_DESC);

        /* Topic Description */
        topicDescription = new FilterFieldStringData(CommonFilterConstants.TOPIC_DESCRIPTION_FILTER_VAR,
                CommonFilterConstants.TOPIC_DESCRIPTION_FILTER_VAR_DESC);
        notTopicDescription = new FilterFieldStringData(CommonFilterConstants.TOPIC_DESCRIPTION_NOT_FILTER_VAR,
                CommonFilterConstants.TOPIC_DESCRIPTION_NOT_FILTER_VAR_DESC);

        /* Topic is included in content specification */
        topicIncludedInSpec = new FilterFieldStringData(CommonFilterConstants.TOPIC_IS_INCLUDED_IN_SPEC,
                CommonFilterConstants.TOPIC_IS_INCLUDED_IN_SPEC_DESC);
        notTopicIncludedInSpec = new FilterFieldStringData(CommonFilterConstants.TOPIC_IS_NOT_INCLUDED_IN_SPEC,
                CommonFilterConstants.TOPIC_IS_NOT_INCLUDED_IN_SPEC_DESC);

        /* Topic XML */
        topicXML = new FilterFieldStringData(CommonFilterConstants.TOPIC_XML_FILTER_VAR,
                CommonFilterConstants.TOPIC_XML_FILTER_VAR_DESC);
        notTopicXML = new FilterFieldStringData(CommonFilterConstants.TOPIC_XML_NOT_FILTER_VAR,
                CommonFilterConstants.TOPIC_XML_NOT_FILTER_VAR_DESC);

        /* Topic Edited in last days */
        editedInLastDays = new FilterFieldIntegerData(CommonFilterConstants.TOPIC_EDITED_IN_LAST_DAYS,
                CommonFilterConstants.TOPIC_EDITED_IN_LAST_DAYS_DESC);
        notEditedInLastDays = new FilterFieldIntegerData(CommonFilterConstants.TOPIC_NOT_EDITED_IN_LAST_DAYS,
                CommonFilterConstants.TOPIC_NOT_EDITED_IN_LAST_DAYS_DESC);

        /* Has XML Errors */
        hasXMLErrors = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_XML_ERRORS,
                CommonFilterConstants.TOPIC_HAS_XML_ERRORS_DESC);
        notHasXMLErrors = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_NOT_XML_ERRORS,
                CommonFilterConstants.TOPIC_HAS_NOT_XML_ERRORS_DESC);

        /* Has Relationships */
        hasRelationships = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_RELATIONSHIPS,
                CommonFilterConstants.TOPIC_HAS_RELATIONSHIPS_DESC);
        notHasRelationships = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_NOT_RELATIONSHIPS,
                CommonFilterConstants.TOPIC_HAS_NOT_RELATIONSHIPS_DESC);

        /* Has Incoming Relationships */
        hasIncomingRelationships = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_INCOMING_RELATIONSHIPS,
                CommonFilterConstants.TOPIC_HAS_INCOMING_RELATIONSHIPS_DESC);
        notHasIncomingRelationships = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_NOT_INCOMING_RELATIONSHIPS,
                CommonFilterConstants.TOPIC_HAS_NOT_INCOMING_RELATIONSHIPS_DESC);

        /* Has Open Bugzilla Bugs */
        hasOpenBugzillaBugs = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_OPEN_BUGZILLA_BUGS,
                CommonFilterConstants.TOPIC_HAS_OPEN_BUGZILLA_BUGS_DESC);
        notHasOpenBugzillaBugs = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_NOT_OPEN_BUGZILLA_BUGS,
                CommonFilterConstants.TOPIC_HAS_NOT_OPEN_BUGZILLA_BUGS_DESC);

        /* Has Bugzilla Bugs */
        hasBugzillaBugs = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_BUGZILLA_BUGS,
                CommonFilterConstants.TOPIC_HAS_BUGZILLA_BUGS_DESC);
        notHasBugzillaBugs = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_HAS_NOT_BUGZILLA_BUGS,
                CommonFilterConstants.TOPIC_HAS_NOT_BUGZILLA_BUGS_DESC);

        /* Latest Translations */
        latestTranslations = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_LATEST_TRANSLATIONS_FILTER_VAR,
                CommonFilterConstants.TOPIC_LATEST_TRANSLATIONS_FILTER_VAR_DESC);
        notLatestTranslations = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_NOT_LATEST_TRANSLATIONS_FILTER_VAR,
                CommonFilterConstants.TOPIC_NOT_LATEST_TRANSLATIONS_FILTER_VAR_DESC);

        /* Latest Completed Translations */
        latestCompletedTranslations = new FilterFieldBooleanData(
                CommonFilterConstants.TOPIC_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR,
                CommonFilterConstants.TOPIC_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR_DESC);
        notLatestCompletedTranslations = new FilterFieldBooleanData(
                CommonFilterConstants.TOPIC_NOT_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR,
                CommonFilterConstants.TOPIC_NOT_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR_DESC);

        topicTextSearch = new FilterFieldStringData(CommonFilterConstants.TOPIC_TEXT_SEARCH_FILTER_VAR,
                CommonFilterConstants.TOPIC_TEXT_SEARCH_FILTER_VAR_DESC);
        startCreateDate = new FilterFieldDateTimeData(CommonFilterConstants.TOPIC_STARTDATE_FILTER_VAR,
                CommonFilterConstants.TOPIC_STARTDATE_FILTER_VAR_DESC);
        endCreateDate = new FilterFieldDateTimeData(CommonFilterConstants.TOPIC_ENDDATE_FILTER_VAR,
                CommonFilterConstants.TOPIC_ENDDATE_FILTER_VAR_DESC);
        startEditDate = new FilterFieldDateTimeData(CommonFilterConstants.TOPIC_STARTEDITDATE_FILTER_VAR,
                CommonFilterConstants.TOPIC_STARTEDITDATE_FILTER_VAR_DESC);
        endEditDate = new FilterFieldDateTimeData(CommonFilterConstants.TOPIC_ENDEDITDATE_FILTER_VAR,
                CommonFilterConstants.TOPIC_ENDEDITDATE_FILTER_VAR_DESC);
        propertyTags = new FilterFieldStringMapData(CommonFilterConstants.TOPIC_PROPERTY_TAG,
                CommonFilterConstants.TOPIC_PROPERTY_TAG_DESC);

        setupSingleFilterVars();

        setupMultipleFilterVars();
    }

    protected void setupSingleFilterVars() {
        addFilterVar(this.topicTextSearch);
        addFilterVar(this.topicIds);
        addFilterVar(this.topicIncludedInSpec);
        addFilterVar(this.topicXML);
        addFilterVar(this.topicTitle);
        addFilterVar(this.topicDescription);
        addFilterVar(this.startCreateDate);
        addFilterVar(this.endCreateDate);
        addFilterVar(this.startEditDate);
        addFilterVar(this.endEditDate);
        addFilterVar(this.hasRelationships);
        addFilterVar(this.hasIncomingRelationships);
        addFilterVar(this.topicRelatedTo);
        addFilterVar(this.topicRelatedFrom);
        addFilterVar(this.hasXMLErrors);
        addFilterVar(this.editedInLastDays);
        addFilterVar(this.hasBugzillaBugs);
        addFilterVar(this.hasOpenBugzillaBugs);
        addFilterVar(this.latestTranslations);
        addFilterVar(this.latestCompletedTranslations);
        addFilterVar(this.zanataIds);

        addFilterVar(this.notTopicIds);
        addFilterVar(this.notTopicIncludedInSpec);
        addFilterVar(this.notTopicXML);
        addFilterVar(this.notTopicTitle);
        addFilterVar(this.notTopicDescription);
        addFilterVar(this.notTopicRelatedTo);
        addFilterVar(this.notTopicRelatedFrom);
        addFilterVar(this.notZanataIds);

        addFilterVar(this.notHasXMLErrors);
        addFilterVar(this.notHasRelationships);
        addFilterVar(this.notHasIncomingRelationships);
        addFilterVar(this.notHasBugzillaBugs);
        addFilterVar(this.notHasOpenBugzillaBugs);

        addFilterVar(this.notLatestTranslations);
        addFilterVar(this.notLatestCompletedTranslations);
    }

    protected void setupMultipleFilterVars() {
        multipleFilterVars.add(this.propertyTags);
    }

    @Override
    public String getFieldValue(final String fieldName) {
        if (fieldName.startsWith(CommonFilterConstants.TOPIC_PROPERTY_TAG)) {
            final String index = fieldName.replace(CommonFilterConstants.TOPIC_PROPERTY_TAG, "");

            /*
             * index will be empty if the fieldName is just CommonFilterConstants.TOPIC_PROPERTY_TAG, which can happen when
             * another object is looping over the getBaseFilterNames() keyset.
             */
            if (!index.isEmpty()) {
                try {
                    final Integer indexInt = Integer.parseInt(index);

                    /*
                     * propertyTags will be null unless one of the setPropertyTag() method is called
                     */
                    if (this.propertyTags.getData() != null && this.propertyTags.getData().size() > indexInt)
                        return this.propertyTags.getData().get(indexInt);
                } catch (final NumberFormatException ex) {
                    // could not parse integer, so fail
                    log.warn("Probably a malformed URL query parameter for the 'Property Tag' Topic ID", ex);
                    return null;
                }
            }

            return null;
        } else {
            return super.getFieldValue(fieldName);
        }
    }

    @Override
    public void setFieldValue(final String fieldName, final String fieldValue) {
        if (fieldName.startsWith(CommonFilterConstants.TOPIC_PROPERTY_TAG)) {
            try {
                final String index = fieldName.replace(CommonFilterConstants.TOPIC_PROPERTY_TAG, "");
                final Integer indexInt = Integer.parseInt(index);
                this.setPropertyTag(fieldValue, indexInt);
            } catch (final NumberFormatException ex) {
                // could not parse integer, so fail
                log.warn("Probably a malformed URL query parameter for the 'Property Tag' Topic ID", ex);
            }

        } else {
            super.setFieldValue(fieldName, fieldValue);
        }
    }

    public Map<String, String> getFilterValues() {
        final Map<String, String> retValue = new HashMap<String, String>();
        final List<FilterFieldDataBase<?>> filterVars = getFilterVars();
        for (final FilterFieldDataBase<?> uiField : filterVars) {
            retValue.put(uiField.getName(), uiField.getData().toString());
        }

        final Map<String, String> propertyTagValues = propertyTags.getData();

        for (final String propertyTagId : propertyTagValues.keySet()) {
            retValue.put(CommonFilterConstants.TOPIC_PROPERTY_TAG + " " + propertyTagId, propertyTagValues.get(propertyTagId));
        }

        return retValue;
    }

    /**
     * @return A map of the expanded filter field names (i.e. with regular expressions) mapped to their descriptions
     */
    @Override
    public Map<String, String> getFieldNames() {
        final Map<String, String> retValue = super.getFieldNames();
        retValue.putAll(singleFilterNames);
        retValue.put(CommonFilterConstants.TOPIC_PROPERTY_TAG + "\\d+", CommonFilterConstants.TOPIC_PROPERTY_TAG_DESC);

        return retValue;
    }

    /**
     * @return A map of the base filter field names (i.e. with no regular expressions) mapped to their descriptions
     */
    @Override
    public Map<String, String> getBaseFieldNames() {
        final Map<String, String> retValue = new HashMap<String, String>(singleFilterNames);
        retValue.put(CommonFilterConstants.TOPIC_PROPERTY_TAG, CommonFilterConstants.TOPIC_PROPERTY_TAG_DESC);

        return retValue;
    }

    @Override
    public boolean hasFieldName(final String input) {
        boolean retValue = false;
        for (final String name : getFieldNames().keySet()) {
            if (input.matches("^" + name + "$")) {
                retValue = true;
                break;
            }
        }

        return retValue;
    }

    @Override
    public String getFieldDesc(final String input) {
        for (final String name : getFieldNames().keySet()) {
            if (input.matches("^" + name + "$")) {
                return getFieldNames().get(name);
            }
        }

        return "";
    }

    public void loadFilterFields(final Filter filter) {
        resetAllValues();

        for (final FilterField filterField : filter.getFilterFields()) {
            final String field = filterField.getField();
            final String value = filterField.getValue();

            this.setFieldValue(field, value);
        }
    }

    public void setPropertyTag(final String propertyTag, final int index) {
        if (this.propertyTags.getData() == null)
            this.propertyTags.setData(new HashMap<String, String>());

        this.propertyTags.getData().put(Integer.toString(index), propertyTag);
    }

    public FilterFieldStringData getTopicIds() {
        return topicIds;
    }

    public FilterFieldIntegerData getTopicRelatedTo() {
        return topicRelatedTo;
    }

    public FilterFieldIntegerData getTopicRelatedFrom() {
        return topicRelatedFrom;
    }

    public FilterFieldStringData getTopicTitle() {
        return topicTitle;
    }

    public FilterFieldStringData getTopicDescription() {
        return topicDescription;
    }

    public FilterFieldDateTimeData getStartCreateDate() {
        return startCreateDate;
    }

    public FilterFieldDateTimeData getEndCreateDate() {
        return endCreateDate;
    }

    public FilterFieldStringData getTopicXML() {
        return topicXML;
    }

    public FilterFieldBooleanData getHasRelationships() {
        return hasRelationships;
    }

    public FilterFieldBooleanData getHasIncomingRelationships() {
        return hasIncomingRelationships;
    }

    public FilterFieldStringData getTopicTextSearch() {
        return topicTextSearch;
    }

    public FilterFieldBooleanData getHasXMLErrors() {
        return hasXMLErrors;
    }

    public FilterFieldDateTimeData getStartEditDate() {
        return startEditDate;
    }

    public FilterFieldDateTimeData getEndEditDate() {
        return endEditDate;
    }

    public FilterFieldIntegerData getEditedInLastDays() {
        return editedInLastDays;
    }

    public FilterFieldBooleanData getHasOpenBugzillaBugs() {
        return hasOpenBugzillaBugs;
    }

    public FilterFieldBooleanData getHasBugzillaBugs() {
        return hasBugzillaBugs;
    }

    public FilterFieldStringData getTopicIncludedInSpec() {
        return topicIncludedInSpec;
    }

    public FilterFieldBooleanData getLatestTranslations() {
        return latestTranslations;
    }

    public FilterFieldBooleanData getLatestCompletedTranslations() {
        return latestCompletedTranslations;
    }

    public FilterFieldStringData getNotTopicIds() {
        return notTopicIds;
    }

    public FilterFieldIntegerData getNotTopicRelatedTo() {
        return notTopicRelatedTo;
    }

    public FilterFieldIntegerData getNotTopicRelatedFrom() {
        return notTopicRelatedFrom;
    }

    public FilterFieldStringData getNotTopicTitle() {
        return notTopicTitle;
    }

    public FilterFieldStringData getNotTopicDescription() {
        return notTopicDescription;
    }

    public FilterFieldStringData getNotTopicXML() {
        return notTopicXML;
    }

    public FilterFieldStringData getNotTopicIncludedInSpec() {
        return notTopicIncludedInSpec;
    }

    public FilterFieldIntegerData getNotEditedInLastDays() {
        return notEditedInLastDays;
    }

    public void setStartCreateDatePlain(final Date startCreateDate) {
        this.startCreateDate.setData(startCreateDate);
    }

    public Date getStartCreateDatePlain() {
        return this.startCreateDate.getDateData();
    }

    public void setStartEditDatePlain(final Date startEditDate) {
        this.startEditDate.setData(startEditDate);
    }

    public Date getStartEditDatePlain() {
        return this.startEditDate.getDateData();
    }

    public void setEndCreateDatePlain(final Date endCreateDate) {
        this.endCreateDate.setData(endCreateDate);
    }

    public Date getEndCreateDatePlain() {
        return this.endCreateDate.getDateData();
    }

    public void setEndEditDatePlain(final Date endEditDate) {
        this.endEditDate.setData(endEditDate);
    }

    public Date getEndEditDatePlain() {
        return this.endEditDate.getDateData();
    }

    public FilterFieldBooleanData getNotHasXMLErrors() {
        return notHasXMLErrors;
    }

    public FilterFieldBooleanData getNotHasRelationships() {
        return notHasRelationships;
    }

    public FilterFieldBooleanData getNotHasIncomingRelationships() {
        return notHasIncomingRelationships;
    }

    public FilterFieldBooleanData getNotHasOpenBugzillaBugs() {
        return notHasOpenBugzillaBugs;
    }

    public FilterFieldBooleanData getNotHasBugzillaBugs() {
        return notHasBugzillaBugs;
    }

    public FilterFieldBooleanData getNotLatestTranslations() {
        return notLatestTranslations;
    }

    public FilterFieldBooleanData getNotLatestCompletedTranslations() {
        return notLatestCompletedTranslations;
    }
}
