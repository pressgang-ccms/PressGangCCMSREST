package org.jboss.pressgang.ccms.restserver.filter.base;

import javax.persistence.criteria.Predicate;

/**
 * An Interface that defines that a Query Builder can search against locales.
 * 
 * @author lnewson
 */
public interface ILocaleFilterQueryBuilder
{
    /**
     * Create a Filter Query Condition that will ensure the entity is a specified locale.
     * 
     * @param locale The locale name that the entity should be for.
     * @return A Predicate object that holds the Query Condition for the match.
     */
    Predicate getMatchingLocaleString(String locale);
    /**
     * Create a Filter Query Condition that will ensure the entity isn't a specified locale.
     * 
     * @param locale The locale name that the entity shouldn't be.
     * @return A Predicate object that holds the Query Condition for the match.
     */
    Predicate getNotMatchingLocaleString(String locale);
}
