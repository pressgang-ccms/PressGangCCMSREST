package org.jboss.pressgang.ccms.restserver.filter.base;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.TopicToBugzillaBug;
import org.jboss.pressgang.ccms.model.TopicToPropertyTag;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.base.ILocaleFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.base.ITagFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.utils.EntityUtilities;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.regexp.NamedMatcher;
import com.google.code.regexp.NamedPattern;

/**
 * Provides the query elements required by Filter.buildQuery() to get a list of Topic elements
 */
public abstract class BaseTopicFilterQueryBuilder<T> extends BaseFilterQueryBuilder<T> implements ITagFilterQueryBuilder,
        ILocaleFilterQueryBuilder {
    private static final Logger log = LoggerFactory.getLogger(BaseTopicFilterQueryBuilder.class);

    private final Subquery<Topic> topicQuery;
    private final Root<Topic> from;
    private final boolean useTopicSubquery;

    public BaseTopicFilterQueryBuilder(final Class<T> clazz, final EntityManager entityManager) {
        this(clazz, entityManager, false);
    }

    public BaseTopicFilterQueryBuilder(final Class<T> clazz, final EntityManager entityManager, final boolean useTopicSubquery) {
        super(clazz, entityManager);
        this.useTopicSubquery = useTopicSubquery;

        if (useTopicSubquery) {
            topicQuery = super.getCriteriaQuery().subquery(Topic.class);
            from = topicQuery.from(Topic.class);
        } else {
            topicQuery = null;
            from = null;
        }
    }

    private DateTime startEditDate;
    private DateTime endEditDate;
    private Date startCreateDate;
    private Date endCreateDate;

    @Override
    protected Path<?> getRootPath() {
        if (useTopicSubquery) {
            return from;
        } else {
            return super.getRootPath();
        }
    }

    protected Path<?> getOriginalRootPath() {
        return super.getRootPath();
    }

    protected Subquery<Topic> getTopicSubquery() {
        return topicQuery;
    }

    @Override
    public Predicate getFilterConditions() {
        if (startCreateDate != null || endCreateDate != null) {
            final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
            Predicate thisRestriction = null;

            if (startCreateDate != null) {
                thisRestriction = criteriaBuilder.greaterThanOrEqualTo(getRootPath().get("topicTimeStamp").as(Date.class),
                        startCreateDate);
            }

            if (endCreateDate != null) {
                final Predicate restriction = criteriaBuilder.lessThanOrEqualTo(
                        getRootPath().get("topicTimeStamp").as(Date.class), startCreateDate);

                if (startCreateDate != null) {
                    thisRestriction = criteriaBuilder.and(thisRestriction, restriction);
                } else {
                    thisRestriction = restriction;
                }
            }

            if (thisRestriction != null) {
                addFieldCondition(thisRestriction);
            }
        }

        if (startEditDate != null || endEditDate != null) {
            final List<Integer> ids = EntityUtilities.getEditedEntities(getEntityManager(), Topic.class, "topicId", startEditDate, endEditDate);
            addIdInCollectionCondition("topicId", ids);
        }

        return super.getFilterConditions();
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.LOGIC_FILTER_VAR)) {
            filterFieldsLogic = fieldValue;
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeperatedListCondition("topicId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_IDS_NOT_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdNotInCommaSeperatedListCondition("topicId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_IS_INCLUDED_IN_SPEC)) {
            /* Split up the string into each topic */
            final String[] topicIds = fieldValue.split(",");
            final Set<Integer> relatedTopicIds = new HashSet<Integer>();
            for (final String topicIdString : topicIds) {
                try {
                    final Integer topicId = Integer.parseInt(topicIdString);
                    final List<Integer> csTopicIds = EntityUtilities.getTopicsInContentSpec(getEntityManager(), topicId);
                    if (csTopicIds != null) {
                        relatedTopicIds.addAll(csTopicIds);
                    }
                } catch (final NumberFormatException ex) {
                    log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
                }
            }

            addIdInCollectionCondition("topicId", relatedTopicIds);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_IS_NOT_INCLUDED_IN_SPEC)) {
            /* Split up the string into each topic */
            final String[] topicIds = fieldValue.split(",");
            final Set<Integer> relatedTopicIds = new HashSet<Integer>();
            for (final String topicIdString : topicIds) {
                try {
                    final Integer topicId = Integer.parseInt(topicIdString);
                    final List<Integer> csTopicIds = EntityUtilities.getTopicsInContentSpec(getEntityManager(), topicId);
                    if (csTopicIds != null) {
                        relatedTopicIds.addAll(csTopicIds);
                    }
                } catch (final NumberFormatException ex) {
                    log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
                }
            }

            addIdNotInCollectionCondition("topicId", relatedTopicIds);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_TITLE_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("topicTitle", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_TITLE_NOT_FILTER_VAR)) {
            addNotLikeIgnoresCaseCondition("topicTitle", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_XML_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("topicXML", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_XML_NOT_FILTER_VAR)) {
            addNotLikeIgnoresCaseCondition("topicXML", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_DESCRIPTION_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("topicText", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_DESCRIPTION_NOT_FILTER_VAR)) {
            addNotLikeIgnoresCaseCondition("topicText", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_RELATIONSHIPS)) {
            this.addSizeGreaterThanOrEqualToCondition("parentTopicToTopics", 1);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_INCOMING_RELATIONSHIPS)) {
            this.addSizeGreaterThanOrEqualToCondition("childTopicToTopics", 1);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_NOT_RELATIONSHIPS)) {
            this.addSizeLessThanCondition("parentTopicToTopics", 1);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_NOT_INCOMING_RELATIONSHIPS)) {
            this.addSizeLessThanCondition("childTopicToTopics", 1);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_RELATED_TO)) {
            try {
                final Integer topicId = Integer.parseInt(fieldValue);
                final List<Integer> relatedTopicIds = EntityUtilities.getIncomingRelatedTopicIDs(getEntityManager(), topicId);
                addIdInCollectionCondition("topicId", relatedTopicIds);
            } catch (final NumberFormatException ex) {
                log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_NOT_RELATED_TO)) {
            try {
                final Integer topicId = Integer.parseInt(fieldValue);
                final List<Integer> relatedTopicIds = EntityUtilities.getIncomingRelatedTopicIDs(getEntityManager(), topicId);
                addIdNotInCollectionCondition("topicId", relatedTopicIds);
            } catch (final NumberFormatException ex) {
                log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_RELATED_FROM)) {
            try {
                final Integer topicId = Integer.parseInt(fieldValue);
                final List<Integer> relatedTopicIds = EntityUtilities.getOutgoingRelatedTopicIDs(getEntityManager(), topicId);
                addIdInCollectionCondition("topicId", relatedTopicIds);
            } catch (final NumberFormatException ex) {
                log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_NOT_RELATED_FROM)) {
            try {
                final Integer topicId = Integer.parseInt(fieldValue);
                final List<Integer> relatedTopicIds = EntityUtilities.getOutgoingRelatedTopicIDs(getEntityManager(), topicId);
                addIdNotInCollectionCondition("topicId", relatedTopicIds);
            } catch (final NumberFormatException ex) {
                log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_TEXT_SEARCH_FILTER_VAR)) {
            final List<Integer> matchingTopicIds = EntityUtilities.getTextSearchTopicMatch(getEntityManager(), fieldValue);
            addIdInCollectionCondition("topicId", matchingTopicIds);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_XML_ERRORS)) {
            final Boolean hasXMLErrors = Boolean.valueOf(fieldValue);
            if (hasXMLErrors) {
                final Predicate notEmptyPredicate = getCriteriaBuilder().notEqual(
                        getRootPath().get("topicSecondOrderData").get("topicXMLErrors"), "");
                final Predicate notNullPredicate = getCriteriaBuilder().isNotNull(
                        getRootPath().get("topicSecondOrderData").get("topicXMLErrors"));
                addFieldCondition(getCriteriaBuilder().and(notEmptyPredicate, notNullPredicate));
            }

        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_NOT_XML_ERRORS)) {
            final Boolean hasNotXMLErrors = Boolean.valueOf(fieldValue);
            if (hasNotXMLErrors) {
                final Predicate emptyPredicate = getCriteriaBuilder().equal(
                        getRootPath().get("topicSecondOrderData").get("topicXMLErrors"), "");
                final Predicate nullPredicate = getCriteriaBuilder().isNull(
                        getRootPath().get("topicSecondOrderData").get("topicXMLErrors"));
                addFieldCondition(getCriteriaBuilder().or(emptyPredicate, nullPredicate));
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_EDITED_IN_LAST_DAYS)) {
            try {
                final Integer days = Integer.parseInt(fieldValue);
                final DateTime date = new DateTime().minusDays(days);
                final List<Integer> editedTopicIds = EntityUtilities.getEditedEntities(getEntityManager(), Topic.class, "topicId", date, null);
                addIdInCollectionCondition("topicId", editedTopicIds);
            } catch (final Exception ex) {
                log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_NOT_EDITED_IN_LAST_DAYS)) {
            try {
                final Integer days = Integer.parseInt(fieldValue);
                final DateTime date = new DateTime().minusDays(days);
                final List<Integer> editedTopicIds = EntityUtilities.getEditedEntities(getEntityManager(), Topic.class, "topicId", date, null);
                addIdNotInCollectionCondition("topicId", editedTopicIds);
            } catch (final NumberFormatException ex) {
                log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_STARTDATE_FILTER_VAR)) {
            try {
                startCreateDate = ISODateTimeFormat.dateTime().parseDateTime(fieldValue).toDate();
            } catch (final Exception ex) {
                log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_ENDDATE_FILTER_VAR)) {
            try {
                endCreateDate = ISODateTimeFormat.dateTime().parseDateTime(fieldValue).toDate();
            } catch (final Exception ex) {
                log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_STARTEDITDATE_FILTER_VAR)) {
            try {
                startEditDate = ISODateTimeFormat.dateTime().parseDateTime(fieldValue);
            } catch (final Exception ex) {
                log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_ENDEDITDATE_FILTER_VAR)) {
            try {
                endEditDate = ISODateTimeFormat.dateTime().parseDateTime(fieldValue);
            } catch (final Exception ex) {
                log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_OPEN_BUGZILLA_BUGS)) {
            final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
            if (fieldValueBoolean) {
                addFieldCondition(getCriteriaBuilder().exists(getOpenBugzillaSubquery()));
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_NOT_OPEN_BUGZILLA_BUGS)) {
            final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
            if (fieldValueBoolean) {
                addFieldCondition(getCriteriaBuilder().not(getCriteriaBuilder().exists(getOpenBugzillaSubquery())));
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_BUGZILLA_BUGS)) {
            final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
            if (fieldValueBoolean) {
                addSizeGreaterThanOrEqualToCondition("topicToBugzillaBugs", 1);
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_NOT_BUGZILLA_BUGS)) {
            final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
            if (fieldValueBoolean) {
                addSizeLessThanCondition("topicToBugzillaBugs", 1);
            }
        } else if (fieldName.startsWith(CommonFilterConstants.TOPIC_PROPERTY_TAG)) {
            try {
                final NamedPattern pattern = NamedPattern.compile(CommonConstants.PROPERTY_TAG_SEARCH_RE);
                final NamedMatcher matcher = pattern.matcher(fieldName);

                while (matcher.find()) {
                    final String propertyTagIdString = matcher.group("PropertyTagID");

                    if (propertyTagIdString != null && fieldValue != null) {
                        final Integer propertyTagIdInt = Integer.parseInt(propertyTagIdString);
                        final Subquery<TopicToPropertyTag> subquery = getPropertyTagSubquery(propertyTagIdInt, fieldValue);                       
                        this.addFieldCondition(getCriteriaBuilder().exists(subquery));
                    }

                    /* should only match once */
                    break;
                }

            } catch (final NumberFormatException ex) {
                /*
                 * could not parse integer, so fail. this shouldn't happen though, as the string is matched by a regex that will
                 * only allow numbers
                 */
                log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
            }
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }
    
    /**
     * Create a Subquery to check if a topic has a property tag with a specific value.
     * 
     * @param propertyTagId The ID of the property tag to be checked.
     * @param propertyTagValue The Value that the property tag should have.
     * @return A subquery that can be used in an exists statement to see if a topic has a property tag with the specified value.
     */
    private Subquery<TopicToPropertyTag> getPropertyTagSubquery(final Integer propertyTagId, final String propertyTagValue) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<TopicToPropertyTag> subQuery = getCriteriaQuery().subquery(TopicToPropertyTag.class);
        final Root<TopicToPropertyTag> root = subQuery.from(TopicToPropertyTag.class);
        subQuery.select(root);

        // Create the Condition for the subquery
        final Predicate topicIdMatch = criteriaBuilder.equal(getRootPath(), root.get("topic"));
        final Predicate propertyTagIdMatch = criteriaBuilder.equal(getRootPath().get("propertyTag").get("propertyTagId"), propertyTagId);
        final Predicate propertyTagValueMatch = criteriaBuilder.equal(getRootPath().get("value"), propertyTagValue);
        subQuery.where(criteriaBuilder.and(topicIdMatch, propertyTagIdMatch, propertyTagValueMatch));
        
        return subQuery;
    }
    
    /**
     * Create a Subquery to check if a topic has open bugs.
     * 
     * @return A subquery that can be used in an exists statement to see if a topic has open bugs.
     */
    private Subquery<TopicToBugzillaBug> getOpenBugzillaSubquery() {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<TopicToBugzillaBug> subQuery = getCriteriaQuery().subquery(TopicToBugzillaBug.class);
        final Root<TopicToBugzillaBug> root = subQuery.from(TopicToBugzillaBug.class);
        subQuery.select(root);
        
        // Create the Condition for the subquery
        final Predicate topicIdMatch = criteriaBuilder.equal(getRootPath(), root.get("topic"));
        final Predicate bugOpenMatch = criteriaBuilder.isTrue(root.get("bugzillaBug").get("bugzillaBugOpen").as(Boolean.class));
        subQuery.where(criteriaBuilder.and(topicIdMatch, bugOpenMatch));
        
        return subQuery;
    }
}
