package org.jboss.pressgang.ccms.restserver.utils;

import info.joseluismartin.beans.PropertyUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.apache.commons.lang.StringUtils;

/**
 * Utility class for dealing with JPA API.
 * 
 * Edited to fix an issue with copying CriteriaQuery's where no where clause exists. And for using the wring method in the
 * getJoinedType() method.
 * 
 * @author Jose Luis Martin
 * @since 1.1
 */
public abstract class JPAUtils {

    private static String ALIAS_PATTERN_STRING = "(?<=from)\\s+(?:\\S+)\\s+(?:as\\s+)*(\\w*)";
    private static Pattern ALIAS_PATTERN = Pattern.compile(ALIAS_PATTERN_STRING, Pattern.CASE_INSENSITIVE);
    private static String FROM_PATTERN_STRING = "(from.*+)";
    private static Pattern FROM_PATTERN = Pattern.compile(FROM_PATTERN_STRING, Pattern.CASE_INSENSITIVE);
    private static volatile long aliasCount = 0;

    /**
     * Result count from a CriteriaQuery
     * 
     * @param em Entity Manager
     * @param criteria Criteria Query to count results
     * @return row count
     */
    public static <T> Long count(EntityManager em, CriteriaQuery<T> criteria) {

        return em.createQuery(countCriteria(em, criteria)).getSingleResult();
    }

    /**
     * Create a row count CriteriaQuery from a CriteriaQuery
     * 
     * @param em entity manager
     * @param criteria source criteria
     * @return row coutnt CriteriaQuery
     */
    public static <T> CriteriaQuery<Long> countCriteria(EntityManager em, CriteriaQuery<T> criteria) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        copyCriteriaNoSelection(criteria, countCriteria);
        countCriteria.select(builder.count(findRoot(countCriteria, criteria.getResultType())));

        return countCriteria;
    }

    /**
     * Gets The result alias, if none set a default one and return it
     * 
     * @param selection
     * @return root alias or generated one
     */
    public static synchronized <T> String getOrCreateAlias(Selection<T> selection) {
        // reset alias count
        if (aliasCount > 1000)
            aliasCount = 0;

        String alias = selection.getAlias();
        if (alias == null) {
            alias = "JDAL_generatedAlias" + aliasCount++;
            selection.alias(alias);
        }
        return alias;

    }

    /**
     * Find Root of result type
     * 
     * @param query criteria query
     * @return the root of result type or null if none
     */
    public static <T> Root<T> findRoot(CriteriaQuery<T> query) {
        return findRoot(query, query.getResultType());
    }

    /**
     * Find the Root with type class on CriteriaQuery Root Set
     * 
     * @param <T> root type
     * @param query criteria query
     * @param clazz root type
     * @return Root<T> of null if none
     */
    public static <T> Root<T> findRoot(CriteriaQuery<?> query, Class<T> clazz) {

        for (Root<?> r : query.getRoots()) {
            if (clazz.equals(r.getJavaType())) {
                return (Root<T>) r.as(clazz);
            }
        }
        return null;
    }

    /**
     * Find Joined Root of type clazz
     * 
     * @param <T>
     * @param query the criteria query
     * @param rootClass the root class
     * @param joinClass the join class
     * @return the Join
     */
    public static <T, K> Join<T, K> findJoinedType(CriteriaQuery<T> query, Class<T> rootClass, Class<K> joinClass) {
        Root<T> root = findRoot(query, rootClass);
        return findJoinedType(root, rootClass, joinClass);
    }
    
    /**
     * Find Joined Root of type clazz
     * 
     * @param <T>
     * @param query the criteria query
     * @param rootClass the root class
     * @param joinClass the join class
     * @return the Join
     */
    @SuppressWarnings("unchecked")
    public static <T, K> Join<T, K> findJoinedType(Root<T> root, Class<T> rootClass, Class<K> joinClass) {
        Join<T, K> join = null;
        for (Join<T, ?> j : root.getJoins()) {
            if (j.getJavaType().equals(joinClass)) {
                join = (Join<T, K>) j;
            }
        }
        return join;
    }

    /**
     * Gets a Path from Path using property path
     * 
     * @param path the base path
     * @param propertyPath property path String like "customer.order.price"
     * @return a new Path for property
     */
    public static Path<?> getPath(Path<?> path, String propertyPath) {
        if (StringUtils.isEmpty(propertyPath))
            return path;

        String name = StringUtils.substringBefore(propertyPath, PropertyUtils.PROPERTY_SEPARATOR);
        Path<?> p = path.get(name);

        return getPath(p, StringUtils.substringAfter(propertyPath, PropertyUtils.PROPERTY_SEPARATOR));

    }

    /**
     * Create a count query string from a query string
     * 
     * @param queryString string to parse
     * @return the count query string
     */
    public static String createCountQueryString(String queryString) {
        return queryString.replaceFirst("^.*(?i)from", "select count (*) from ");
    }

    /**
     * Gets the alias of root entity of JQL query
     * 
     * @param queryString JQL query
     * @return alias of root entity.
     */
    public static String getAlias(String queryString) {
        Matcher m = ALIAS_PATTERN.matcher(queryString);
        return m.find() ? m.group(1) : null;
    }

    /**
     * Add order by clause to queryString
     * 
     * @param queryString JPL Query String
     * @param propertyPath Order properti
     * @param asc true if ascending
     * @return JQL Query String with Order clause appened.
     */
    public static String addOrder(String queryString, String propertyPath, boolean asc) {

        if (StringUtils.containsIgnoreCase(queryString, "order by")) {
            return queryString;
        }

        StringBuilder sb = new StringBuilder(queryString);
        sb.append(" ORDER BY ");
        sb.append(getAlias(queryString));
        sb.append(".");
        sb.append(propertyPath);
        sb.append(" ");
        sb.append(asc ? "ASC" : "DESC");

        return sb.toString();
    }

    /**
     * Gets Query String for selecting primary keys
     * 
     * @param queryString the original query
     * @param name primary key name
     * @return query string
     */
    public static String getKeyQuery(String queryString, String name) {
        Matcher m = FROM_PATTERN.matcher(queryString);
        if (m.find()) {
            StringBuilder sb = new StringBuilder("SELECT ");
            sb.append(getAlias(queryString));
            sb.append(".");
            sb.append(name);
            sb.append(" ");
            sb.append(m.group());
            return sb.toString();
        }

        return null;
    }

    /**
     * Copy Criteria without Selection
     * 
     * @param from source Criteria
     * @param to destination Criteria
     */
    public static void copyCriteriaNoSelection(CriteriaQuery<?> from, CriteriaQuery<?> to) {

        // Copy Roots
        for (Root<?> root : from.getRoots()) {
            Root<?> dest = to.from(root.getJavaType());
            dest.alias(getOrCreateAlias(root));
            copyJoins(root, dest);
        }

        if (from.getGroupList() != null)
            to.groupBy(from.getGroupList());
        to.distinct(from.isDistinct());
        if (from.getGroupRestriction() != null)
            to.having(from.getGroupRestriction());
        if (from.getRestriction() != null)
            to.where(from.getRestriction());
        if (from.getOrderList() != null)
            to.orderBy(from.getOrderList());
    }

    public static <T> void copyCriteria(CriteriaQuery<T> from, CriteriaQuery<T> to) {
        copyCriteriaNoSelection(from, to);
        to.select(from.getSelection());
    }

    /**
     * Copy Joins
     * 
     * @param from source Join
     * @param to destination Join
     */
    public static void copyJoins(From<?, ?> from, From<?, ?> to) {
        for (Join<?, ?> j : from.getJoins()) {
            Join<?, ?> toJoin = to.join(j.getAttribute().getName(), j.getJoinType());
            toJoin.alias(getOrCreateAlias(j));

            copyJoins(j, toJoin);
        }

        for (Fetch<?, ?> f : from.getFetches()) {
            Fetch<?, ?> toFetch = to.fetch(f.getAttribute().getName());
            copyFetches(f, toFetch);

        }
    }

    /**
     * Copy Fetches
     * 
     * @param from source Fetch
     * @param to dest Fetch
     */
    public static void copyFetches(Fetch<?, ?> from, Fetch<?, ?> to) {
        for (Fetch<?, ?> f : from.getFetches()) {
            Fetch<?, ?> toFetch = to.fetch(f.getAttribute().getName());
            // recursively copy fetches
            copyFetches(f, toFetch);
        }
    }
}
