package org.jboss.pressgang.ccms.restserver.filter.builder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.TopicToTag;
import org.jboss.pressgang.ccms.model.TranslatedTopic;
import org.jboss.pressgang.ccms.model.TranslatedTopicData;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseTopicFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides the query elements required by Filter.buildQuery() to get a list of Topic elements
 */
public class TranslatedTopicDataFilterQueryBuilder extends BaseTopicFilterQueryBuilder<TranslatedTopicData> {
    private static final Logger log = LoggerFactory.getLogger(TranslatedTopicDataFilterQueryBuilder.class);
    
    private final List<Predicate> fieldConditions = new ArrayList<Predicate>();
    private final Join<TranslatedTopicData, TranslatedTopic> translatedTopic;

    @SuppressWarnings("unchecked")
    public TranslatedTopicDataFilterQueryBuilder(final EntityManager entityManager) {
        super(TranslatedTopicData.class, entityManager, true);
        translatedTopic = ((Root<TranslatedTopicData>) getOriginalRootPath()).join("translatedTopic");
    }

    @Override
    public Predicate getMatchTagString(final Integer tagId) {
        final CriteriaBuilder queryBuilder = getCriteriaBuilder();
        final Subquery<TopicToTag> subQuery = getCriteriaQuery().subquery(TopicToTag.class);
        final Root<TopicToTag> from = subQuery.from(TopicToTag.class);
        final Predicate topic = queryBuilder.equal(from.get("topic").get("topicId"), translatedTopic.get("topicId"));
        final Predicate tag = queryBuilder.equal(from.get("tag").get("tagId"), tagId);
        subQuery.select(from);
        subQuery.where(queryBuilder.and(topic, tag));

        return queryBuilder.exists(subQuery);
    }

    @Override
    public Predicate getNotMatchTagString(final Integer tagId) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<TopicToTag> subQuery = getCriteriaQuery().subquery(TopicToTag.class);
        final Root<TopicToTag> from = subQuery.from(TopicToTag.class);
        final Predicate topic = criteriaBuilder.equal(from.get("topic").get("topicId"), translatedTopic.get("topicId"));
        final Predicate tag = criteriaBuilder.equal(from.get("tag").get("tagId"), tagId);
        subQuery.select(from);
        subQuery.where(criteriaBuilder.and(topic, tag));

        return criteriaBuilder.not(criteriaBuilder.exists(subQuery));
    }

    @Override
    public Predicate getMatchingLocaleString(final String locale) {
        if (locale == null)
            return null;

        final String defaultLocale = System.getProperty(CommonConstants.DEFAULT_LOCALE_PROPERTY);

        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Predicate localePredicate = criteriaBuilder.equal(getOriginalRootPath().get("translationLocale"), locale);

        if (defaultLocale != null && defaultLocale.toLowerCase().equals(locale.toLowerCase()))
            return criteriaBuilder.or(localePredicate, criteriaBuilder.isNull(getOriginalRootPath().get("translationLocale")));

        return localePredicate;
    }

    @Override
    public Predicate getNotMatchingLocaleString(final String locale) {
        if (locale == null)
            return null;

        final String defaultLocale = System.getProperty(CommonConstants.DEFAULT_LOCALE_PROPERTY);

        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Predicate localePredicate = criteriaBuilder.notEqual(getOriginalRootPath().get("translationLocale"), locale);

        if (defaultLocale != null && defaultLocale.toLowerCase().equals(locale.toLowerCase()))
            return criteriaBuilder.and(localePredicate,
                    criteriaBuilder.isNotNull(getOriginalRootPath().get("translationLocale")));

        return localePredicate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Predicate getFilterConditions() {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        // Get the topic Subquery
        final Subquery<Topic> topicSubquery = getTopicSubquery();
        // Get the conditions for the topic
        final Predicate topicConditions = super.getFilterConditions();
        if (topicConditions != null) {
            // Create the condition for the join: translatedTopic.topicId = topic.topicId
            final Predicate topicEqual = criteriaBuilder.equal(translatedTopic.get("topicId"), getRootPath().get("topicId"));
            // Join the topic conditions with the equals for the EXISTS statement
            final Predicate topicSubqueryCondition = criteriaBuilder.and(topicConditions, topicEqual);
            // Finish the subquery
            topicSubquery.select((Root<Topic>) getRootPath());
            topicSubquery.where(topicSubqueryCondition);
    
            // Add the condition to the field conditions
            final Predicate topicExistsCondition = criteriaBuilder.exists(topicSubquery);
            fieldConditions.add(topicExistsCondition);
        }
        
        if (fieldConditions.isEmpty()) return null;

        // Generate the final Predicate condition
        if (fieldConditions.size() > 1) {
            final Predicate[] predicates = fieldConditions.toArray(new Predicate[fieldConditions.size()]);
            if (this.filterFieldsLogic.equalsIgnoreCase(Constants.OR_LOGIC)) {
                return criteriaBuilder.or(predicates);
            } else {
                return criteriaBuilder.and(predicates);
            }
        } else {
            return fieldConditions.get(0);
        }
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.TOPIC_LATEST_TRANSLATIONS_FILTER_VAR)) {
            final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
            if (fieldValueBoolean) {
                final Subquery<Integer> lastestRevisionQuery = getLastestRevisionSubquery();
                fieldConditions.add(this.getCriteriaBuilder().equal(translatedTopic.get("topicRevision"), lastestRevisionQuery));
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_NOT_LATEST_TRANSLATIONS_FILTER_VAR)) {
            final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
            if (fieldValueBoolean) {
                final Subquery<Integer> lastestRevisionQuery = getLastestRevisionSubquery();
                fieldConditions.add(this.getCriteriaBuilder().notEqual(translatedTopic.get("topicRevision"), lastestRevisionQuery));
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR)) {
            final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
            if (fieldValueBoolean) {
                final Subquery<Integer> lastestRevisionQuery = getLastestCompleteRevisionSubquery();
                fieldConditions.add(this.getCriteriaBuilder().equal(translatedTopic.get("topicRevision"), lastestRevisionQuery));
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_NOT_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR)) {
            final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
            if (fieldValueBoolean) {
                final Subquery<Integer> lastestRevisionQuery = getLastestCompleteRevisionSubquery();
                fieldConditions.add(this.getCriteriaBuilder().notEqual(translatedTopic.get("topicRevision"), lastestRevisionQuery));
            }
        } else if (fieldName.equals(CommonFilterConstants.ZANATA_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0) {
                final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
                final List<Predicate> conditions = new ArrayList<Predicate>();

                final String[] zanataIds = fieldValue.split(",");
                for (final String zanataId : zanataIds) {
                    try {
                        String[] zanataVars = zanataId.split("-");

                        final Integer topicId = Integer.parseInt(zanataVars[0]);
                        final Integer topicRevision = Integer.parseInt(zanataVars[1]);

                        final Predicate topicIdCondition = criteriaBuilder.equal(translatedTopic.get("topicId"), topicId);
                        final Predicate topicRevisionCondition = criteriaBuilder.equal(translatedTopic.get("topicRevision"),
                                topicRevision);

                        conditions.add(criteriaBuilder.and(topicIdCondition, topicRevisionCondition));
                    } catch (NumberFormatException ex) {
                        log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
                    }
                }

                /* Only add the query if we found valid zanata ids */
                if (conditions.size() > 1) {
                    final Predicate[] predicates = conditions.toArray(new Predicate[conditions.size()]);
                    fieldConditions.add(criteriaBuilder.or(predicates));
                } else if (conditions.size() == 1) {
                    fieldConditions.add(conditions.get(0));
                }
            }
        } else if (fieldName.equals(CommonFilterConstants.ZANATA_IDS_NOT_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0) {
                final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
                final List<Predicate> conditions = new ArrayList<Predicate>();

                final String[] zanataIds = fieldValue.split(",");
                for (final String zanataId : zanataIds) {
                    try {
                        String[] zanataVars = zanataId.split("-");

                        final Integer topicId = Integer.parseInt(zanataVars[0]);
                        final Integer topicRevision = Integer.parseInt(zanataVars[1]);

                        final Predicate topicIdCondition = criteriaBuilder.equal(translatedTopic.get("topicId"), topicId);
                        final Predicate topicRevisionCondition = criteriaBuilder.equal(translatedTopic.get("topicRevision"),
                                topicRevision);

                        conditions.add(criteriaBuilder.and(topicIdCondition, topicRevisionCondition));
                    } catch (NumberFormatException ex) {
                        log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
                    }
                }

                /* Only add the query if we found valid zanata ids */
                if (conditions.size() > 1) {
                    final Predicate[] predicates = conditions.toArray(new Predicate[conditions.size()]);
                    fieldConditions.add(criteriaBuilder.not(criteriaBuilder.or(predicates)));
                } else if (conditions.size() == 1) {
                    fieldConditions.add(conditions.get(0));
                }
            }
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }
    
    @Override
    public void reset() {
        super.reset();
        this.fieldConditions.clear();
    }
    
    /**
     * Create a Subquery to get the latest revision for a translated topic and locale.
     * 
     * @return A subquery that will return the maximum revision for a translated topic and locale.
     */
    protected Subquery<Integer> getLastestRevisionSubquery() {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<Integer> subQuery = getCriteriaQuery().subquery(Integer.class);
        final Root<TranslatedTopicData> root = subQuery.from(TranslatedTopicData.class);
        subQuery.select(criteriaBuilder.max(root.get("translatedTopic").get("topicRevision").as(Integer.class)));
        
        final Predicate topicIdMatch = criteriaBuilder.equal(root.get("translatedTopic").get("topicId"), translatedTopic.get("topicId"));
        final Predicate localeMatch = criteriaBuilder.equal(this.getOriginalRootPath().get("translationLocale"), root.get("translationLocale"));
        subQuery.where(criteriaBuilder.and(topicIdMatch, localeMatch));
        
        subQuery.groupBy(root.get("translatedTopic").get("topicId"));
        
        return subQuery;
    }
    
    protected Subquery<Integer> getLastestCompleteRevisionSubquery() {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<Integer> subQuery = getCriteriaQuery().subquery(Integer.class);
        final Root<TranslatedTopicData> root = subQuery.from(TranslatedTopicData.class);
        subQuery.select(criteriaBuilder.max(root.get("translatedTopic").get("topicRevision").as(Integer.class)));
        
        final Predicate topicIdMatch = criteriaBuilder.equal(root.get("translatedTopic").get("topicId"), translatedTopic.get("topicId"));
        final Predicate localeMatch = criteriaBuilder.equal(this.getOriginalRootPath().get("translationLocale"), root.get("translationLocale"));
        final Predicate complete = criteriaBuilder.ge(root.get("translationPercentage").as(Integer.class), 100);
        subQuery.where(criteriaBuilder.and(topicIdMatch, localeMatch, complete));
        
        subQuery.groupBy(root.get("translatedTopic").get("topicId"));
        
        return subQuery;
    }
}
