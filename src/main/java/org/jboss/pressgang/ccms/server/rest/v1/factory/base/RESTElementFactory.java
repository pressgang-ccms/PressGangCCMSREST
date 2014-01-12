package org.jboss.pressgang.ccms.server.rest.v1.factory.base;

import org.jboss.pressgang.ccms.rest.v1.elements.base.RESTBaseElementV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;

/**
 * Defines a factory that can create REST entity objects from JPA entities, and update JPA entities from REST entities
 *
 * @param <T> The REST object type
 * @param <U> The database object type
 */
public abstract class RESTElementFactory<T extends RESTBaseElementV1<T>, U> {

    /**
     * Create a REST Entity representation from an Object.
     *
     * @param object   The entity that is to be transformed into a REST Entity.
     * @param baseUrl  The REST url that was used to access this REST entity
     * @param dataType The type of the returned data (XML or JSON)
     * @param expand   The Object that contains details about what fields should be expanded.
     * @return A new REST entity populated with the values in a database entity
     */
    public abstract T createRESTEntityFromObject(final U object, final String baseUrl, final String dataType, final ExpandDataTrunk expand);

    /**
     * Populates the values of an object from a REST entity
     *
     * @param object     The database entity to be synced from the REST Entity.
     * @param dataObject The REST entity object.
     */
    public abstract void updateObjectFromRESTEntity(final U object, final T dataObject);
}
