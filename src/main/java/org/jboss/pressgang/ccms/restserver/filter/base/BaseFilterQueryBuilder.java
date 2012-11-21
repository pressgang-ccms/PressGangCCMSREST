package org.jboss.pressgang.ccms.restserver.filter.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.utils.Constants;
import org.jboss.pressgang.ccms.restserver.utils.JPAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseFilterQueryBuilder<T> implements IFilterQueryBuilder<T> {
    private static final Logger log = LoggerFactory.getLogger(BaseFilterQueryBuilder.class);

    protected String filterFieldsLogic = Constants.LOGIC_FILTER_VAR_DEFAULT_VALUE;
    private final List<Predicate> fieldConditions = new ArrayList<Predicate>();
    private final CriteriaBuilder criteriaBuilder;
    private final CriteriaQuery<T> criteriaQuery;
    private final Root<T> from;
    private final Class<T> clazz;
    private final EntityManager entityManager;

    public BaseFilterQueryBuilder(final Class<T> clazz, final EntityManager entityManager) {
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
        this.entityManager = entityManager;
        this.clazz = clazz;
        this.criteriaQuery = criteriaBuilder.createQuery(clazz);
        this.from = criteriaQuery.from(clazz);
        criteriaQuery.select(from);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue)
    {
        if (fieldName.equals(CommonFilterConstants.LOGIC_FILTER_VAR))
        {
            filterFieldsLogic = fieldValue;
        } else {
            log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
        }
    }

    @Override
    public Predicate getFilterConditions() {
        if (fieldConditions.isEmpty())
            return null;

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
    public CriteriaQuery<T> getBaseCriteriaQuery() {
        final CriteriaQuery<T> clone = criteriaBuilder.createQuery(clazz);
        JPAUtils.copyCriteria(criteriaQuery, clone);
        return clone;
    }

    protected CriteriaQuery<T> getCriteriaQuery() {
        return criteriaQuery;
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return criteriaBuilder;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected Root<T> getCriteriaRoot() {
        return from;
    }
    
    @Override
    public void reset() {
        this.fieldConditions.clear();
    }

    protected Path<?> getRootPath() {
        return from;
    }

    protected List<Predicate> getFieldConditions() {
        return fieldConditions;
    }

    protected void addFieldCondition(final Predicate condition) {
        this.fieldConditions.add(condition);
    }

    /**
     * Add a Field Search Condition that will search a field for a specified value using the following SQL logic:
     * {@code LOWER(field) LIKE LOWER('%value%')}
     * 
     * @param propertyName The name of the field as defined in the Entity mapping class.
     * @param value The value to search against.
     */
    protected void addLikeIgnoresCaseCondition(final String propertyName, final String value) {
        final Expression<String> propertyNameField = getCriteriaBuilder().lower(
                getRootPath().get(propertyName).as(String.class));
        fieldConditions.add(getCriteriaBuilder().like(propertyNameField, "%" + value.toLowerCase() + "%"));
    }

    /**
     * Add a Field Search Condition that will search a field for values that aren't like a specified value using the following
     * SQL logic: {@code LOWER(field) NOT LIKE LOWER('%value%')}
     * 
     * @param propertyName The name of the field as defined in the Entity mapping class.
     * @param value The value to search against.
     */
    protected void addNotLikeIgnoresCaseCondition(final String propertyName, final String value) {
        final Expression<String> propertyNameField = getCriteriaBuilder().lower(
                getRootPath().get(propertyName).as(String.class));
        fieldConditions.add(getCriteriaBuilder().notLike(propertyNameField, "%" + value.toLowerCase() + "%"));
    }

    /**
     * Add a Field Search Condition that will check if the size of a collection in an entity is greater than or equal to the
     * specified size.
     * 
     * @param propertyName The name of the collection as defined in the Entity mapping class.
     * @param size The size that the collection should be greater than or equal to.
     */
    protected void addSizeGreaterThanOrEqualToCondition(final String propertyName, final Integer size) {
        final Expression<Integer> propertySizeExpression = getCriteriaBuilder().size(
                getRootPath().get(propertyName).as(Set.class));
        fieldConditions.add(getCriteriaBuilder().ge(propertySizeExpression, size));
    }

    /**
     * Add a Field Search Condition that will check if the size of a collection in an entity is greater than the specified size.
     * 
     * @param propertyName The name of the collection as defined in the Entity mapping class.
     * @param size The size that the collection should be greater than.
     */
    protected void addSizeGreaterThanCondition(final String propertyName, final Integer size) {
        final Expression<Integer> propertySizeExpression = getCriteriaBuilder().size(
                getRootPath().get(propertyName).as(Set.class));
        fieldConditions.add(getCriteriaBuilder().gt(propertySizeExpression, size));
    }

    /**
     * Add a Field Search Condition that will check if the size of a collection in an entity is less than or equal to the
     * specified size.
     * 
     * @param propertyName The name of the collection as defined in the Entity mapping class.
     * @param size The size that the collection should be less than or equal to.
     */
    protected void addSizeLessThanOrEqualToCondition(final String propertyName, final Integer size) {
        final Expression<Integer> propertySizeExpression = getCriteriaBuilder().size(
                getRootPath().get(propertyName).as(Set.class));
        fieldConditions.add(getCriteriaBuilder().le(propertySizeExpression, size));
    }

    /**
     * Add a Field Search Condition that will check if the size of a collection in an entity is less than the specified size.
     * 
     * @param propertyName The name of the collection as defined in the Entity mapping class.
     * @param size The size that the collection should be less than.
     */
    protected void addSizeLessThanCondition(final String propertyName, final Integer size) {
        final Expression<Integer> propertySizeExpression = getCriteriaBuilder().size(
                getRootPath().get(propertyName).as(Set.class));
        fieldConditions.add(getCriteriaBuilder().lt(propertySizeExpression, size));
    }

    /**
     * Add a Field Search Condition that will check if the id field exists in an array of values. eg. {@code field IN (values)}
     * 
     * @param propertyName The name of the field id as defined in the Entity mapping class.
     * @param values The List of Ids to be compared to.
     */
    protected void addIdInCollectionCondition(final String propertyName, final Collection<Integer> values) {
        if (values == null || values.isEmpty()) {
            fieldConditions.add(getCriteriaBuilder().equal(getRootPath().get(propertyName), Constants.NULL_TOPIC_ID));
        } else {
            fieldConditions.add(getRootPath().get(propertyName).in(values));
        }
    }

    /**
     * Add a Field Search Condition that will check if the id field exists in an array of id values that are represented as a
     * String. eg. {@code field IN (values)}
     * 
     * @param propertyName The name of the field id as defined in the Entity mapping class.
     * @param values The array of Ids to be compared to.
     * @throws NumberFormatException Thrown if one of the Strings cannot be converted to an Integer.
     */
    protected void addIdInArrayCondition(final String propertyName, final String[] values) throws NumberFormatException {
        final Set<Integer> idValues = new HashSet<Integer>();
        for (final String value : values) {
            idValues.add(Integer.parseInt(value.trim()));
        }
        addIdInCollectionCondition(propertyName, idValues);
    }

    /**
     * Add a Field Search Condition that will check if the id field exists in a comma separated list of ids. eg.
     * {@code field IN (value)}
     * 
     * @param propertyName The name of the field id as defined in the Entity mapping class.
     * @param value The comma separated list of ids.
     * @throws NumberFormatException Thrown if one of the Strings cannot be converted to an Integer.
     */
    protected void addIdInCommaSeperatedListCondition(final String propertyName, final String value)
            throws NumberFormatException {
        addIdInArrayCondition(propertyName, value.split(","));
    }

    /**
     * Add a Field Search Condition that will check if the id field does not exist in an array of values. eg.
     * {@code field NOT IN (values)}
     * 
     * @param propertyName The name of the field id as defined in the Entity mapping class.
     * @param values The List of Ids to be compared to.
     */
    protected void addIdNotInCollectionCondition(final String propertyName, final Collection<Integer> values) {
        if (values != null && !values.isEmpty()) {
            fieldConditions.add(getCriteriaBuilder().not(getRootPath().get(propertyName).in(values)));
        }
    }

    /**
     * Add a Field Search Condition that will check if the id field does not exist in an array of id values that are represented
     * as a String. eg. {@code field NOT IN (values)}
     * 
     * @param propertyName The name of the field id as defined in the Entity mapping class.
     * @param values The array of Ids to be compared to.
     * @throws NumberFormatException Thrown if one of the Strings cannot be converted to an Integer.
     */
    protected void addIdNotInArrayCondition(final String propertyName, final String[] values) throws NumberFormatException {
        final Set<Integer> idValues = new HashSet<Integer>();
        for (final String value : values) {
            idValues.add(Integer.parseInt(value.trim()));
        }
        addIdNotInCollectionCondition(propertyName, idValues);
    }

    /**
     * Add a Field Search Condition that will check if the id field does not exist in a comma separated list of ids. eg.
     * {@code field NOT IN (value)}
     * 
     * @param propertyName The name of the field id as defined in the Entity mapping class.
     * @param value The comma separated list of ids.
     * @throws NumberFormatException Thrown if one of the Strings cannot be converted to an Integer.
     */
    protected void addIdNotInCommaSeperatedListCondition(final String propertyName, final String value)
            throws NumberFormatException {
        addIdNotInArrayCondition(propertyName, value.split(","));
    }
}
