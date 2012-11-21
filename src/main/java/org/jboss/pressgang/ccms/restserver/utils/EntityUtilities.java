package org.jboss.pressgang.ccms.restserver.utils;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
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
import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.jboss.pressgang.ccms.model.BlobConstants;
import org.jboss.pressgang.ccms.model.Category;
import org.jboss.pressgang.ccms.model.Filter;
import org.jboss.pressgang.ccms.model.FilterCategory;
import org.jboss.pressgang.ccms.model.FilterField;
import org.jboss.pressgang.ccms.model.FilterLocale;
import org.jboss.pressgang.ccms.model.FilterTag;
import org.jboss.pressgang.ccms.model.IntegerConstants;
import org.jboss.pressgang.ccms.model.LanguageImage;
import org.jboss.pressgang.ccms.model.Project;
import org.jboss.pressgang.ccms.model.StringConstants;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.model.TagToPropertyTag;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.TopicToBugzillaBug;
import org.jboss.pressgang.ccms.model.TopicToPropertyTag;
import org.jboss.pressgang.ccms.model.TranslatedTopicData;
import org.jboss.pressgang.ccms.model.User;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.filter.base.IFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.builder.TopicFilterQueryBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.contentspec.processor.ContentSpecParser;

public class EntityUtilities {
    private static final Logger log = LoggerFactory.getLogger(EntityUtilities.class);

    public static byte[] loadBlobConstant(final EntityManager entityManager, final Integer id) {
        if (id == null)
            return null;

        final BlobConstants constant = entityManager.find(BlobConstants.class, id);

        if (constant == null) {
            log.error("Expected to find a record in the BlobConstants table with an ID of " + id);
            return null;
        }

        return constant.getConstantValue();
    }

    public static Integer loadIntegerConstant(final EntityManager entityManager, final Integer id) {
        if (id == null)
            return null;

        final IntegerConstants constant = entityManager.find(IntegerConstants.class, id);

        if (constant == null) {
            log.error("Expected to find a record in the IntegerConstants table with an ID of " + id);
            return null;
        }

        return constant.getConstantValue();
    }

    public static String loadStringConstant(final EntityManager entityManager, final Integer id) {
        if (id == null)
            return null;

        final StringConstants constant = entityManager.find(StringConstants.class, id);

        if (constant == null) {
            log.error("Expected to find a record in the StringConstants table with an ID of " + id);
            return null;
        }

        return constant.getConstantValue();
    }

    @SuppressWarnings("unchecked")
    public static User getUserFromUsername(final EntityManager entityManager, final String username) {
        if (username == null)
            return null;

        final Query query = entityManager.createQuery(User.SELECT_ALL_QUERY + " where user.userName = '" + username + "'");
        final List<User> users = query.getResultList();
        return users == null ? null : (users.size() == 1 ? users.get(0) : null);
    }

    public static Filter populateFilter(final EntityManager entityManager, final MultivaluedMap<String, String> paramMap, final String filterName,
            final String tagPrefix, final String groupTagPrefix, final String categoryInternalPrefix,
            final String categoryExternalPrefix, final String localePrefix, final IFieldFilter fieldFilter) {
        final Map<String, String> newParamMap = new HashMap<String, String>();
        for (final String key : paramMap.keySet()) {
            try {
                newParamMap.put(key, URLDecoder.decode(paramMap.getFirst(key), "UTF-8"));
            } catch (final Exception ex) {
                log.warn("The URL query parameter " + key + " with value " + paramMap.getFirst(key)
                        + " could not be URLDecoded", ex);
            }
        }
        return populateFilter(entityManager, newParamMap, filterName, tagPrefix, groupTagPrefix, categoryInternalPrefix,
                categoryExternalPrefix, localePrefix, fieldFilter);

    }

    /**
     * This function takes the url parameters and uses them to populate a Filter object
     */
    public static Filter populateFilter(final EntityManager entityManager, final Map<String, String> paramMap, final String filterName, final String tagPrefix,
            final String groupTagPrefix, final String categoryInternalPrefix, final String categoryExternalPrefix,
            final String localePrefix, final IFieldFilter fieldFilter) {
        // attempt to get the filter id from the url
        Integer filterId = null;
        if (paramMap.containsKey(filterName)) {
            final String filterQueryParam = paramMap.get(filterName);

            try {
                filterId = Integer.parseInt(filterQueryParam);
            } catch (final Exception ex) {
                // filter value was not an integer
                filterId = null;

                log.debug("The filter ID URL query parameter was not an integer. Got " + filterQueryParam
                        + ". Probably a malformed URL.", ex);
            }
        }

        Filter filter = null;

        /* First attempt to populate the filter from a filterID variable */
        if (filterId != null) {
            filter = entityManager.find(Filter.class, filterId);
        }

        /* If that fails, use the other URL params */
        if (filter == null) {
            filter = new Filter();

            for (final String key : paramMap.keySet()) {
                final boolean tagVar = tagPrefix != null && key.startsWith(tagPrefix);
                final boolean groupTagVar = groupTagPrefix != null && key.startsWith(groupTagPrefix);
                final boolean catIntVar = categoryInternalPrefix != null && key.startsWith(categoryInternalPrefix);
                final boolean catExtVar = categoryExternalPrefix != null && key.startsWith(categoryExternalPrefix);
                final boolean localeVar = localePrefix != null && key.matches("^" + localePrefix + "\\d*$");
                final String state = paramMap.get(key);

                // add the filter category states
                if (catIntVar || catExtVar) {
                    /*
                     * get the category and project id data from the variable name
                     */
                    final String catProjDetails = catIntVar ? key.replaceFirst(categoryInternalPrefix, "") : key.replaceFirst(
                            categoryExternalPrefix, "");
                    // split the category and project id out of the data
                    final String[] catProjID = catProjDetails.split("-");

                    /*
                     * some validity checks. make sure we have one or two strings after the split.
                     */
                    if (catProjID.length != 1 && catProjID.length != 2)
                        continue;

                    // try to get the category and project ids
                    Integer catID = null;
                    Integer projID = null;
                    try {
                        catID = Integer.parseInt(catProjID[0]);

                        /*
                         * if the array has just one element, we have only specified the category. in this case the project is
                         * the common project
                         */
                        if (catProjID.length == 2)
                            projID = Integer.parseInt(catProjID[1]);
                    } catch (final Exception ex) {
                        log.debug("Was expecting an integer. Got " + catProjID[0] + ". Probably a malformed URL.", ex);
                        continue;
                    }

                    // at this point we have found a url variable that
                    // contains a catgeory and project id

                    final Category category = entityManager.find(Category.class, catID);
                    final Project project = projID != null ? entityManager.find(Project.class, projID) : null;

                    Integer dbState;

                    if (catIntVar) {
                        if (state.equals(Constants.AND_LOGIC))
                            dbState = CommonFilterConstants.CATEGORY_INTERNAL_AND_STATE;
                        else
                            dbState = CommonFilterConstants.CATEGORY_INTERNAL_OR_STATE;
                    } else {
                        if (state.equals(Constants.AND_LOGIC))
                            dbState = CommonFilterConstants.CATEGORY_EXTERNAL_AND_STATE;
                        else
                            dbState = CommonFilterConstants.CATEGORY_EXTERNAL_OR_STATE;
                    }

                    final FilterCategory filterCategory = new FilterCategory();
                    filterCategory.setFilter(filter);
                    filterCategory.setProject(project);
                    filterCategory.setCategory(category);
                    filterCategory.setCategoryState(dbState);

                    filter.getFilterCategories().add(filterCategory);
                }

                // add the filter tag states
                else if (tagVar) {
                    try {
                        final Integer tagId = Integer.parseInt(key.replaceFirst(tagPrefix, ""));
                        final Integer intState = Integer.parseInt(state);

                        // get the Tag object that the tag id represents
                        final Tag tag = entityManager.getReference(Tag.class, tagId);

                        if (tag != null) {
                            final FilterTag filterTag = new FilterTag();
                            filterTag.setTag(tag);
                            filterTag.setTagState(intState);
                            filterTag.setFilter(filter);
                            filter.getFilterTags().add(filterTag);
                        }
                    } catch (final Exception ex) {
                        log.debug("Probably an invalid tag query pramater. Parameter: " + key + " Value: " + state, ex);
                    }
                }

                else if (groupTagVar) {
                    final Integer tagId = Integer.parseInt(key.replaceFirst(groupTagPrefix, ""));
                    // final Integer intState = Integer.parseInt(state);

                    // get the Tag object that the tag id represents
                    final Tag tag = entityManager.getReference(Tag.class, tagId);

                    if (tag != null) {
                        final FilterTag filterTag = new FilterTag();
                        filterTag.setTag(tag);
                        filterTag.setTagState(CommonFilterConstants.GROUP_TAG_STATE);
                        filterTag.setFilter(filter);
                        filter.getFilterTags().add(filterTag);
                    }
                } else if (localeVar) {
                    try {
                        final String localeName = state.replaceAll("\\d", "");
                        final Integer intState = Integer.parseInt(state.replaceAll("[^\\d]", ""));

                        final FilterLocale filterLocale = new FilterLocale();
                        filterLocale.setLocaleName(localeName);
                        filterLocale.setLocaleState(intState);
                        filterLocale.setFilter(filter);
                        filter.getFilterLocales().add(filterLocale);
                    } catch (final Exception ex) {
                        log.debug("Probably an invalid locale query pramater. Parameter: " + key + " Value: " + state, ex);
                    }
                }

                // add the filter field states
                else {
                    if (fieldFilter.hasFieldName(key)) {
                        final FilterField filterField = new FilterField();
                        filterField.setFilter(filter);
                        filterField.setField(key);
                        filterField.setValue(state);
                        filterField.setDescription(fieldFilter.getFieldDesc(key));
                        filter.getFilterFields().add(filterField);
                    }
                }

            }

        }

        return filter;
    }

    public static Filter populateFilter(final MultivaluedMap<String, String> paramMap, final IFieldFilter fieldFilter) {
        return populateFilter(paramMap, fieldFilter);
    }

    public static Filter populateFilter(final EntityManager entityManager, final MultivaluedMap<String, String> paramMap, final String localePrefix,
            final IFieldFilter fieldFilter) {
        return populateFilter(entityManager, paramMap, null, null, null, null, null, localePrefix, fieldFilter);
    }

    /**
     * @return A list of topic ids that have open bugzilla bugs assigned to them
     */
    @SuppressWarnings("unchecked")
    public static List<Integer> getTopicsWithOpenBugs(final EntityManager entityManager) {
        final List<TopicToBugzillaBug> results = entityManager.createQuery(
                TopicToBugzillaBug.SELECT_ALL_QUERY + " WHERE topicToBugzillaBug.bugzillaBug.bugzillaBugOpen = true")
                .getResultList();
        final List<Integer> retValue = new ArrayList<Integer>();
        for (final TopicToBugzillaBug map : results)
            retValue.add(map.getTopic().getTopicId());
        return retValue;
    }

    /**
     * @return A comma separated list of topic ids that have been included in a content spec
     * @throws Exception
     */
    public static List<Integer> getTopicsInContentSpec(final EntityManager entityManager, final Integer contentSpecTopicID) {
        try {
            final Topic contentSpec = entityManager.find(Topic.class, contentSpecTopicID);

            if (contentSpec == null)
                return null;

            final ContentSpecParser csp = new ContentSpecParser("http://localhost:8080/TopicIndex/");
            if (csp.parse(contentSpec.getTopicXML())) {
                final List<Integer> topicIds = csp.getReferencedTopicIds();
                if (topicIds.size() == 0)
                    return CollectionUtilities.toArrayList(Constants.NULL_TOPIC_ID);

                return topicIds;
            }
        } catch (final Exception ex) {
            log.warn(
                    "An invalid Topic ID was stored for a Content Spec in the database, or the topic was not a valid content spec",
                    ex);
        }

        return null;
    }

    public static String getTopicsInContentSpecString(final EntityManager entityManager, final Integer contentSpecTopicID) throws Exception {
        final List<Integer> topics = getTopicsInContentSpec(entityManager, contentSpecTopicID);
        if (topics == null || topics.size() == 0)
            return Constants.NULL_TOPIC_ID_STRING;

        return CollectionUtilities.toSeperatedString(topics);
    }

    /**
     * @return A comma separated list of topic ids that have open bugzilla bugs assigned to them
     */
    public static String getTopicsWithOpenBugsString(final EntityManager entityManager) {
        final List<Integer> topics = getTopicsWithOpenBugs(entityManager);
        if (topics.size() == 0)
            return Constants.NULL_TOPIC_ID_STRING;

        return CollectionUtilities.toSeperatedString(topics);
    }

    /**
     * @return A list of topic ids that have bugzilla bugs assigned to them
     */
    @SuppressWarnings("unchecked")
    public static List<Integer> getTopicsWithBugs(final EntityManager entityManager) {
        final List<TopicToBugzillaBug> results = entityManager.createQuery(TopicToBugzillaBug.SELECT_ALL_QUERY).getResultList();
        final List<Integer> retValue = new ArrayList<Integer>();
        for (final TopicToBugzillaBug map : results)
            if (!retValue.contains(map.getTopic().getTopicId()))
                retValue.add(map.getTopic().getTopicId());
        return retValue;
    }

    @SuppressWarnings("unchecked")
    public static List<Integer> getTopicsWithPropertyTag(final EntityManager entityManager, final Integer propertyTagIdInt, final String propertyTagValue) {
        final Query query = entityManager
                .createQuery(TopicToPropertyTag.SELECT_ALL_QUERY
                        + " WHERE topicToPropertyTag.propertyTag.propertyTagId = :propTagId AND topicToPropertyTag.value = :propTagValue");
        query.setParameter("propTagId", propertyTagIdInt);
        query.setParameter("propTagValue", propertyTagValue);
        final List<TopicToPropertyTag> mappings = query.getResultList();
        if (mappings.size() == 0)
            return null;

        final List<Integer> retValue = new ArrayList<Integer>();
        for (final TopicToPropertyTag mapping : mappings) {
            retValue.add(mapping.getTopic().getTopicId());
        }

        return retValue;
    }

    public static String getTopicsWithPropertyTagString(final EntityManager entityManager, final Integer propertyTagIdInt, final String propertyTagValue) {
        final List<Integer> topics = getTopicsWithPropertyTag(entityManager, propertyTagIdInt, propertyTagValue);
        if (topics.size() == 0)
            return Constants.NULL_TOPIC_ID_STRING;

        return CollectionUtilities.toSeperatedString(topics);
    }

    @SuppressWarnings("unchecked")
    public static List<Integer> getTagsWithPropertyTag(final EntityManager entityManager, final Integer propertyTagIdInt, final String propertyTagValue) {
        final Query query = entityManager.createQuery(TagToPropertyTag.SELECT_ALL_QUERY
                + " WHERE tagToPropertyTag.propertyTag.propertyTagId = :propTagId AND tagToPropertyTag.value = :propTagValue");
        query.setParameter("propTagId", propertyTagIdInt);
        query.setParameter("propTagValue", propertyTagValue);
        final List<TagToPropertyTag> mappings = query.getResultList();
        if (mappings.size() == 0)
            return null;

        final List<Integer> retValue = new ArrayList<Integer>();
        for (final TagToPropertyTag mapping : mappings) {
            retValue.add(mapping.getTag().getTagId());
        }

        return retValue;
    }

    public static String getTagsWithPropertyTagString(final EntityManager entityManager, final Integer propertyTagIdInt, final String propertyTagValue) {
        final List<Integer> tags = getTagsWithPropertyTag(entityManager, propertyTagIdInt, propertyTagValue);
        if (tags.size() == 0)
            return Constants.NULL_TOPIC_ID_STRING;

        return CollectionUtilities.toSeperatedString(tags);
    }

    /**
     * @return A comma separated list of topic ids that have bugzilla bugs assigned to them
     */
    public static String getTopicsWithBugsString(final EntityManager entityManager) {
        final List<Integer> topics = getTopicsWithBugs(entityManager);
        if (topics.size() == 0)
            return Constants.NULL_TOPIC_ID_STRING;

        return CollectionUtilities.toSeperatedString(topics);
    }

    @SuppressWarnings("unchecked")
    public static <E> List<Integer> getEditedEntities(final EntityManager entityManager, final Class<E> type, final String pkColumnName, final DateTime startDate,
            final DateTime endDate) {
        if (startDate == null && endDate == null)
            return null;

        final AuditReader reader = AuditReaderFactory.get(entityManager);

        final AuditQuery query = reader.createQuery().forRevisionsOfEntity(type, true, false)
                .addOrder(AuditEntity.revisionProperty("timestamp").asc())
                .addProjection(AuditEntity.property("originalId." + pkColumnName).distinct());

        if (startDate != null)
            query.add(AuditEntity.revisionProperty("timestamp").ge(startDate.toDate().getTime()));

        if (endDate != null)
            query.add(AuditEntity.revisionProperty("timestamp").le(endDate.toDate().getTime()));

        final List<Integer> entityIds = query.getResultList();

        return entityIds;
    }

    public static <E> String getEditedEntitiesString(final EntityManager entityManager, final Class<E> type, final String pkColumnName, final DateTime startDate,
            final DateTime endDate) {
        final List<Integer> ids = getEditedEntities(entityManager, type, pkColumnName, startDate, endDate);
        if (ids != null && ids.size() != 0)
            return CollectionUtilities.toSeperatedString(ids);
        return Constants.NULL_TOPIC_ID_STRING;
    }

    @SuppressWarnings("unchecked")
    public static List<Integer> getTextSearchTopicMatch(final EntityManager entityManager, final String phrase) {
        final List<Integer> retValue = new ArrayList<Integer>();

        try {
            // get the Hibernate session from the EntityManager
            final Session session = (Session) entityManager.getDelegate();
            // get a Hibernate full text session. we use the Hibernate version,
            // instead of the JPA version,
            // because we can use the Hibernate versions to do projections
            final FullTextSession fullTextSession = Search.getFullTextSession(session);
            // create a query parser
            final QueryParser parser = new QueryParser(Version.LUCENE_31, "TopicSearchText", fullTextSession.getSearchFactory()
                    .getAnalyzer(Topic.class));
            // parse the query string
            final org.apache.lucene.search.Query query = parser.parse(phrase);

            // build a lucene query
            /*
             * final org.apache.lucene.search.Query query = qb .keyword() .onFields("TopicSearchText") .matching(phrase)
             * .createQuery();
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
            for (final Object[] projection : results) {
                final Integer id = (Integer) projection[0];
                retValue.add(id);
            }
        } catch (final Exception ex) {
            log.error("Probably an error using Lucene", ex);
        }

        /*
         * an empty list will be interpreted as no restriction as opposed to return none. so add a non existent topic id so no
         * matches are made
         */
        if (retValue.size() == 0)
            retValue.add(-1);

        return retValue;
    }

    @SuppressWarnings("unchecked")
    public static HashMap<Integer, ArrayList<Integer>> populateExclusionTags(final EntityManager entityManager) {
        final HashMap<Integer, ArrayList<Integer>> retValue = new HashMap<Integer, ArrayList<Integer>>();
        List<Tag> tagList = entityManager.createQuery(Tag.SELECT_ALL_QUERY).getResultList();
        for (final Tag tag : tagList)
            retValue.put(tag.getTagId(), tag.getExclusionTagIDs());
        return retValue;
    }

    /**
     * This function is used to get a list of the currently selected tags. This is used to prepopulate the selected tags when
     * creating a new topic
     */
    public static String urlTagParameters(final FacesContext context) {
        final Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();

        String variables = "";

        for (final String key : paramMap.keySet()) {
            if (key.startsWith(CommonFilterConstants.MATCH_TAG)) {
                if (variables.length() == 0)
                    variables += "?";
                else
                    variables += "&";

                variables += key + "=" + paramMap.get(key);
            }
        }

        return variables;
    }

    /**
     * A utility function that is used to append url parameters to a collection of url parameters
     * 
     * @param params The existing url parameter string
     * @param name The parameter name
     * @param value The parameter value
     * @return The url parameters that were passed in via params, with the new parameter appended to it
     */
    public static String addParameter(final String params, final String name, final String value) {
        // Just use an empty string as the default value
        return addParameter(params, name, value, "");
    }

    /**
     * A utility function that is used to append url parameters to a collection of url parameters
     * 
     * @param params The existing url parameter string
     * @param name The parameter name
     * @param value The parameter value
     * @param defaultValue Used in place of value if it is null
     * @return The url parameters that were passed in via params, with the new parameter appended to it
     */
    public static String addParameter(final String params, final String name, final String value, final String defaultValue) {
        String newParams = params;

        if (newParams.length() == 0)
            newParams += "?";
        else
            newParams += "&";

        newParams += name + "=" + (value == null ? defaultValue : value);

        return newParams;
    }

    public static String cleanStringForJavaScriptVariableName(final String input) {
        return input.replaceAll("[^a-zA-Z]", "");
    }

    public static List<Integer> getOutgoingRelatedTopicIDs(final EntityManager entityManager, final Integer topicRelatedTo) {
        try {
            if (topicRelatedTo != null) {
                final Topic topic = entityManager.find(Topic.class, topicRelatedTo);
                return topic.getRelatedTopicIDs();
            }
        } catch (final Exception ex) {
            log.error(topicRelatedTo + " is probably not a valid Topic ID", ex);
        }

        return null;
    }

    public static List<Integer> getIncomingRelatedTopicIDs(final EntityManager entityManager, final Integer topicRelatedFrom) {
        try {
            if (topicRelatedFrom != null) {
                final Topic topic = entityManager.find(Topic.class, topicRelatedFrom);
                return topic.getIncomingRelatedTopicIDs();
            }
        } catch (final Exception ex) {
            log.error(topicRelatedFrom + " is probably not a valid Topic ID", ex);
        }

        return null;
    }

    public static String getIncomingRelationshipsTo(final EntityManager entityManager, final Integer topicId) {
        final List<Integer> ids = getIncomingRelatedTopicIDs(entityManager, topicId);
        if (ids != null && ids.size() != 0)
            return CollectionUtilities.toSeperatedString(ids);
        return Constants.NULL_TOPIC_ID_STRING;
    }

    public static String getOutgoingRelationshipsFrom(final EntityManager entityManager, final Integer topicId) {
        final List<Integer> ids = getOutgoingRelatedTopicIDs(entityManager, topicId);
        if (ids != null && ids.size() != 0)
            return CollectionUtilities.toSeperatedString(ids);
        return Constants.NULL_TOPIC_ID_STRING;
    }

    public static List<Topic> getTopicsFromFilter(final EntityManager entityManager, final Filter filter) {
        // build the query that will be used to get the topics
        final CriteriaQuery<Topic> query = FilterUtilities.buildQuery(filter, new TopicFilterQueryBuilder(entityManager));

        // get the base topic list
        final List<Topic> topicList = entityManager.createQuery(query).getResultList();

        return topicList;
    }

    @SuppressWarnings("unchecked")
    public static List<Integer> getLatestTranslatedTopics(final EntityManager entityManager) {
        String query = TranslatedTopicData.SELECT_ALL_QUERY;
        query += " where translatedTopicData.translatedTopic.topicRevision = (Select MAX(B.translatedTopic.topicRevision) FROM TranslatedTopicData B WHERE translatedTopicData.translatedTopic.topicId = B.translatedTopic.topicId AND B.translationLocale = translatedTopicData.translationLocale GROUP BY B.translatedTopic.topicId)";
        final List<TranslatedTopicData> results = entityManager.createQuery(query).getResultList();
        final List<Integer> retValue = new ArrayList<Integer>();
        for (final TranslatedTopicData topic : results)
            if (!retValue.contains(topic.getTranslatedTopicDataId()))
                retValue.add(topic.getTranslatedTopicDataId());
        return retValue;
    }

    @SuppressWarnings("unchecked")
    public static List<Integer> getLatestCompletedTranslatedTopics(final EntityManager entityManager) {
        String query = TranslatedTopicData.SELECT_ALL_QUERY;
        query += " where translatedTopicData.translatedTopic.topicRevision = (Select MAX(B.translatedTopic.topicRevision) FROM TranslatedTopicData B WHERE translatedTopicData.translatedTopic.topicId = B.translatedTopic.topicId AND B.translationLocale = translatedTopicData.translationLocale AND B.translationPercentage >= 100 GROUP BY B.translatedTopic.topicId)";
        final List<TranslatedTopicData> results = entityManager.createQuery(query).getResultList();
        final List<Integer> retValue = new ArrayList<Integer>();
        for (final TranslatedTopicData topic : results)
            if (!retValue.contains(topic.getTranslatedTopicDataId()))
                retValue.add(topic.getTranslatedTopicDataId());
        return retValue;
    }

    public static String getLatestTranslatedTopicsString(final EntityManager entityManager) {
        final List<Integer> topics = getLatestTranslatedTopics(entityManager);
        if (topics.size() == 0)
            return Constants.NULL_TOPIC_ID_STRING;

        return CollectionUtilities.toSeperatedString(topics);
    }

    public static String getLatestCompletedTranslatedTopicsString(final EntityManager entityManager) {
        final List<Integer> topics = getLatestCompletedTranslatedTopics(entityManager);
        if (topics.size() == 0)
            return Constants.NULL_TOPIC_ID_STRING;

        return CollectionUtilities.toSeperatedString(topics);
    }

    public static List<String> getLocales(final EntityManager entityManager) {
        final String localeConstant = loadStringConstant(entityManager, CommonConstants.LOCALES_STRING_CONSTANT_ID);

        if (localeConstant != null) {
            final List<String> locales = CollectionUtilities.replaceStrings(CollectionUtilities
                    .sortAndReturn(CollectionUtilities.toArrayList(localeConstant.split("[\\s\r\n]*,[\\s\r\n]*"))), "_", "-");
            return locales;
        } else {
            return new ArrayList<String>();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Integer> getImagesWithFileName(final EntityManager entityManager, final String filename) {
        final Query query = entityManager.createQuery(LanguageImage.SELECT_ALL_QUERY
                + " WHERE LOWER(languageImage.originalFileName) LIKE LOWER(:filename)");
        query.setParameter("filename", "%" + filename + "%");
        final List<LanguageImage> mappings = query.getResultList();
        if (mappings.size() == 0)
            return null;

        final List<Integer> retValue = new ArrayList<Integer>();
        for (final LanguageImage mapping : mappings) {
            retValue.add(mapping.getImageFile().getId());
        }

        return retValue;
    }
}
