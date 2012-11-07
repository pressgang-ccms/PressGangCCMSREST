package org.jboss.pressgang.ccms.restserver.filter.base;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface IFilterQueryBuilder<T>
{
	void processFilterString(String fieldName, String fieldValue);
	Predicate getFilterConditions();
	CriteriaQuery<T> getBaseCriteriaQuery();
	CriteriaBuilder getCriteriaBuilder();
	Root<T> getCriteriaRoot();
}
