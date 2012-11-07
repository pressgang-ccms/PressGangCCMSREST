package org.jboss.pressgang.ccms.restserver.utils;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.pressgang.ccms.restserver.entity.Category;

/**
 * A class that contains a bunch of static functions to return common collections of entities
 */
@Stateless
public class EntityQueries {
    
    @PersistenceContext
    private static EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public static List<Category> getAllCategories() {
        return entityManager.createQuery(Category.SELECT_ALL_QUERY).getResultList();
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getAllPropertiesFromEntity(final String entity, final String property) {
        final String query = "select entity." + property + " from " + entity + " entity";
        return entityManager.createQuery(query).getResultList();
    }
}
