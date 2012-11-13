package org.jboss.pressgang.ccms.restserver.filter.base;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * A set of methods that are the Bare Requirements to create a Filter Query Builder, that will be able to construct a JPA Query
 * from a filter.
 * 
 * @param <T> The Entity Type that the query builder will return values for.
 */
public interface IFilterQueryBuilder<T> {
    void processFilterString(String fieldName, String fieldValue);

    Predicate getFilterConditions();

    CriteriaQuery<T> getBaseCriteriaQuery();

    CriteriaBuilder getCriteriaBuilder();

    Root<T> getCriteriaRoot();
}
