package org.jboss.pressgang.ccms.restserver.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.entity.Filter;
import org.jboss.pressgang.ccms.restserver.entity.FilterField;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.structures.field.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the options used by the objects that extend the ExtendedTopicList class to filter a query to retrieve
 * Topic entities.
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
    private UIFieldStringMapData propertyTags;
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

    private List<UIFieldDataBase<?>> multipleFilterVars = new ArrayList<UIFieldDataBase<?>>();

    public TopicFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        /* Topic ID's */
        topicIds = new UIFieldStringData(CommonFilterConstants.TOPIC_IDS_FILTER_VAR,
                CommonFilterConstants.TOPIC_IDS_FILTER_VAR_DESC);
        notTopicIds = new UIFieldStringData(CommonFilterConstants.TOPIC_IDS_NOT_FILTER_VAR,
                CommonFilterConstants.TOPIC_IDS_NOT_FILTER_VAR_DESC);

        /* Zanata ID's */
        zanataIds = new UIFieldStringData(CommonFilterConstants.ZANATA_IDS_FILTER_VAR,
                CommonFilterConstants.ZANATA_IDS_FILTER_VAR_DESC);
        notZanataIds = new UIFieldStringData(CommonFilterConstants.ZANATA_IDS_NOT_FILTER_VAR,
                CommonFilterConstants.ZANATA_IDS_NOT_FILTER_VAR_DESC);

        /* Topic Related To */
        topicRelatedTo = new UIFieldIntegerData(CommonFilterConstants.TOPIC_RELATED_TO,
                CommonFilterConstants.TOPIC_RELATED_TO_DESC);
        notTopicRelatedTo = new UIFieldIntegerData(CommonFilterConstants.TOPIC_NOT_RELATED_TO,
                CommonFilterConstants.TOPIC_NOT_RELATED_TO_DESC);

        /* Topic Related From */
        topicRelatedFrom = new UIFieldIntegerData(CommonFilterConstants.TOPIC_RELATED_FROM,
                CommonFilterConstants.TOPIC_RELATED_FROM_DESC);
        notTopicRelatedFrom = new UIFieldIntegerData(CommonFilterConstants.TOPIC_NOT_RELATED_FROM,
                CommonFilterConstants.TOPIC_NOT_RELATED_FROM_DESC);

        /* Topic Title */
        topicTitle = new UIFieldStringData(CommonFilterConstants.TOPIC_TITLE_FILTER_VAR,
                CommonFilterConstants.TOPIC_TITLE_FILTER_VAR_DESC);
        notTopicTitle = new UIFieldStringData(CommonFilterConstants.TOPIC_TITLE_NOT_FILTER_VAR,
                CommonFilterConstants.TOPIC_TITLE_NOT_FILTER_VAR_DESC);

        /* Topic Description */
        topicDescription = new UIFieldStringData(CommonFilterConstants.TOPIC_DESCRIPTION_FILTER_VAR,
                CommonFilterConstants.TOPIC_DESCRIPTION_FILTER_VAR_DESC);
        notTopicDescription = new UIFieldStringData(CommonFilterConstants.TOPIC_DESCRIPTION_NOT_FILTER_VAR,
                CommonFilterConstants.TOPIC_DESCRIPTION_NOT_FILTER_VAR_DESC);

        /* Topic is included in content specification */
        topicIncludedInSpec = new UIFieldStringData(CommonFilterConstants.TOPIC_IS_INCLUDED_IN_SPEC,
                CommonFilterConstants.TOPIC_IS_INCLUDED_IN_SPEC_DESC);
        notTopicIncludedInSpec = new UIFieldStringData(CommonFilterConstants.TOPIC_IS_NOT_INCLUDED_IN_SPEC,
                CommonFilterConstants.TOPIC_IS_NOT_INCLUDED_IN_SPEC_DESC);

        /* Topic XML */
        topicXML = new UIFieldStringData(CommonFilterConstants.TOPIC_XML_FILTER_VAR,
                CommonFilterConstants.TOPIC_XML_FILTER_VAR_DESC);
        notTopicXML = new UIFieldStringData(CommonFilterConstants.TOPIC_XML_NOT_FILTER_VAR,
                CommonFilterConstants.TOPIC_XML_NOT_FILTER_VAR_DESC);

        /* Topic Edited in last days */
        editedInLastDays = new UIFieldIntegerData(CommonFilterConstants.TOPIC_EDITED_IN_LAST_DAYS,
                CommonFilterConstants.TOPIC_EDITED_IN_LAST_DAYS_DESC);
        notEditedInLastDays = new UIFieldIntegerData(CommonFilterConstants.TOPIC_NOT_EDITED_IN_LAST_DAYS,
                CommonFilterConstants.TOPIC_NOT_EDITED_IN_LAST_DAYS_DESC);

        /* Has XML Errors */
        hasXMLErrors = new UIFieldBooleanData(CommonFilterConstants.TOPIC_HAS_XML_ERRORS,
                CommonFilterConstants.TOPIC_HAS_XML_ERRORS_DESC);
        notHasXMLErrors = new UIFieldBooleanData(CommonFilterConstants.TOPIC_HAS_NOT_XML_ERRORS,
                CommonFilterConstants.TOPIC_HAS_NOT_XML_ERRORS_DESC);

        /* Has Relationships */
        hasRelationships = new UIFieldBooleanData(CommonFilterConstants.TOPIC_HAS_RELATIONSHIPS,
                CommonFilterConstants.TOPIC_HAS_RELATIONSHIPS_DESC);
        notHasRelationships = new UIFieldBooleanData(CommonFilterConstants.TOPIC_HAS_NOT_RELATIONSHIPS,
                CommonFilterConstants.TOPIC_HAS_NOT_RELATIONSHIPS_DESC);

        /* Has Incoming Relationships */
        hasIncomingRelationships = new UIFieldBooleanData(CommonFilterConstants.TOPIC_HAS_INCOMING_RELATIONSHIPS,
                CommonFilterConstants.TOPIC_HAS_INCOMING_RELATIONSHIPS_DESC);
        notHasIncomingRelationships = new UIFieldBooleanData(CommonFilterConstants.TOPIC_HAS_NOT_INCOMING_RELATIONSHIPS,
                CommonFilterConstants.TOPIC_HAS_NOT_INCOMING_RELATIONSHIPS_DESC);

        /* Has Open Bugzilla Bugs */
        hasOpenBugzillaBugs = new UIFieldBooleanData(CommonFilterConstants.TOPIC_HAS_OPEN_BUGZILLA_BUGS,
                CommonFilterConstants.TOPIC_HAS_OPEN_BUGZILLA_BUGS_DESC);
        notHasOpenBugzillaBugs = new UIFieldBooleanData(CommonFilterConstants.TOPIC_HAS_NOT_OPEN_BUGZILLA_BUGS,
                CommonFilterConstants.TOPIC_HAS_NOT_OPEN_BUGZILLA_BUGS_DESC);

        /* Has Bugzilla Bugs */
        hasBugzillaBugs = new UIFieldBooleanData(CommonFilterConstants.TOPIC_HAS_BUGZILLA_BUGS,
                CommonFilterConstants.TOPIC_HAS_BUGZILLA_BUGS_DESC);
        notHasBugzillaBugs = new UIFieldBooleanData(CommonFilterConstants.TOPIC_HAS_NOT_BUGZILLA_BUGS,
                CommonFilterConstants.TOPIC_HAS_NOT_BUGZILLA_BUGS_DESC);

        /* Latest Translations */
        latestTranslations = new UIFieldBooleanData(CommonFilterConstants.TOPIC_LATEST_TRANSLATIONS_FILTER_VAR,
                CommonFilterConstants.TOPIC_LATEST_TRANSLATIONS_FILTER_VAR_DESC);
        notLatestTranslations = new UIFieldBooleanData(CommonFilterConstants.TOPIC_NOT_LATEST_TRANSLATIONS_FILTER_VAR,
                CommonFilterConstants.TOPIC_NOT_LATEST_TRANSLATIONS_FILTER_VAR_DESC);

        /* Latest Completed Translations */
        latestCompletedTranslations = new UIFieldBooleanData(
                CommonFilterConstants.TOPIC_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR,
                CommonFilterConstants.TOPIC_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR_DESC);
        notLatestCompletedTranslations = new UIFieldBooleanData(
                CommonFilterConstants.TOPIC_NOT_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR,
                CommonFilterConstants.TOPIC_NOT_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR_DESC);

        topicTextSearch = new UIFieldStringData(CommonFilterConstants.TOPIC_TEXT_SEARCH_FILTER_VAR,
                CommonFilterConstants.TOPIC_TEXT_SEARCH_FILTER_VAR_DESC);
        startCreateDate = new UIFieldDateTimeData(CommonFilterConstants.TOPIC_STARTDATE_FILTER_VAR,
                CommonFilterConstants.TOPIC_STARTDATE_FILTER_VAR_DESC);
        endCreateDate = new UIFieldDateTimeData(CommonFilterConstants.TOPIC_ENDDATE_FILTER_VAR,
                CommonFilterConstants.TOPIC_ENDDATE_FILTER_VAR_DESC);
        startEditDate = new UIFieldDateTimeData(CommonFilterConstants.TOPIC_STARTEDITDATE_FILTER_VAR,
                CommonFilterConstants.TOPIC_STARTEDITDATE_FILTER_VAR_DESC);
        endEditDate = new UIFieldDateTimeData(CommonFilterConstants.TOPIC_ENDEDITDATE_FILTER_VAR,
                CommonFilterConstants.TOPIC_ENDEDITDATE_FILTER_VAR_DESC);
        propertyTags = new UIFieldStringMapData(CommonFilterConstants.TOPIC_PROPERTY_TAG,
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
        final List<UIFieldDataBase<?>> filterVars = getFilterVars();
        for (final UIFieldDataBase<?> uiField : filterVars) {
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

    public UIFieldStringData getTopicIds() {
        return topicIds;
    }

    public UIFieldIntegerData getTopicRelatedTo() {
        return topicRelatedTo;
    }

    public UIFieldIntegerData getTopicRelatedFrom() {
        return topicRelatedFrom;
    }

    public UIFieldStringData getTopicTitle() {
        return topicTitle;
    }

    public UIFieldStringData getTopicDescription() {
        return topicDescription;
    }

    public UIFieldDateTimeData getStartCreateDate() {
        return startCreateDate;
    }

    public UIFieldDateTimeData getEndCreateDate() {
        return endCreateDate;
    }

    public UIFieldStringData getTopicXML() {
        return topicXML;
    }

    public UIFieldBooleanData getHasRelationships() {
        return hasRelationships;
    }

    public UIFieldBooleanData getHasIncomingRelationships() {
        return hasIncomingRelationships;
    }

    public UIFieldStringData getTopicTextSearch() {
        return topicTextSearch;
    }

    public UIFieldBooleanData getHasXMLErrors() {
        return hasXMLErrors;
    }

    public UIFieldDateTimeData getStartEditDate() {
        return startEditDate;
    }

    public UIFieldDateTimeData getEndEditDate() {
        return endEditDate;
    }

    public UIFieldIntegerData getEditedInLastDays() {
        return editedInLastDays;
    }

    public UIFieldBooleanData getHasOpenBugzillaBugs() {
        return hasOpenBugzillaBugs;
    }

    public UIFieldBooleanData getHasBugzillaBugs() {
        return hasBugzillaBugs;
    }

    public UIFieldStringData getTopicIncludedInSpec() {
        return topicIncludedInSpec;
    }

    public UIFieldBooleanData getLatestTranslations() {
        return latestTranslations;
    }

    public UIFieldBooleanData getLatestCompletedTranslations() {
        return latestCompletedTranslations;
    }

    public UIFieldStringData getNotTopicIds() {
        return notTopicIds;
    }

    public UIFieldIntegerData getNotTopicRelatedTo() {
        return notTopicRelatedTo;
    }

    public UIFieldIntegerData getNotTopicRelatedFrom() {
        return notTopicRelatedFrom;
    }

    public UIFieldStringData getNotTopicTitle() {
        return notTopicTitle;
    }

    public UIFieldStringData getNotTopicDescription() {
        return notTopicDescription;
    }

    public UIFieldStringData getNotTopicXML() {
        return notTopicXML;
    }

    public UIFieldStringData getNotTopicIncludedInSpec() {
        return notTopicIncludedInSpec;
    }

    public UIFieldIntegerData getNotEditedInLastDays() {
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

    public UIFieldBooleanData getNotHasXMLErrors() {
        return notHasXMLErrors;
    }

    public UIFieldBooleanData getNotHasRelationships() {
        return notHasRelationships;
    }

    public UIFieldBooleanData getNotHasIncomingRelationships() {
        return notHasIncomingRelationships;
    }

    public UIFieldBooleanData getNotHasOpenBugzillaBugs() {
        return notHasOpenBugzillaBugs;
    }

    public UIFieldBooleanData getNotHasBugzillaBugs() {
        return notHasBugzillaBugs;
    }

    public UIFieldBooleanData getNotLatestTranslations() {
        return notLatestTranslations;
    }

    public UIFieldBooleanData getNotLatestCompletedTranslations() {
        return notLatestCompletedTranslations;
    }
}
