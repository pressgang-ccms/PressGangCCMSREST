package org.jboss.pressgang.ccms.server.filter.base;

import javax.persistence.criteria.Predicate;

/**
 * An Interface that defines that a Query Builder can search against matching tags.
 * 
 * @author lnewson
 */
public interface ITagFilterQueryBuilder
{
    /**
     * Create a Filter Query Condition that will ensure the entity has a matching specified tag.
     * 
     * @param tagId The ID of the tag that should exist for the entity.
     * @return A Predicate object that holds the Query Condition for the match.
     */
    Predicate getMatchTagString(Integer tagId);
    /**
     * Create a Filter Query Condition that will ensure the entity doesn't have a matching specified tag.
     * 
     * @param tagId The ID of the tag that shouldn't exist for the entity.
     * @return A Predicate object that holds the Query Condition for the match.
     */
    Predicate getNotMatchTagString(Integer tagId);
}
