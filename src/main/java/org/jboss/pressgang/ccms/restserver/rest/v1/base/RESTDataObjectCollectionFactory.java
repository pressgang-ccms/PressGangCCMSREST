package org.jboss.pressgang.ccms.restserver.rest.v1.base;

import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataDetails;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.entity.base.AuditedEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory used to create collections of REST entity objects
 * 
 * @param <T> The type of REST entity to work with
 * @param <U> The type of database entity to work with
 * @param <V> The type of REST collection to work with
 */
public class RESTDataObjectCollectionFactory<T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> {
    private static final Logger log = LoggerFactory.getLogger(RESTDataObjectCollectionFactory.class);

    public V create(final Class<V> clazz, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final List<U> entities,
            final String expandName, final String dataType, final ExpandDataTrunk parentExpand, final String baseUrl,
            final EntityManager entityManager) {
        return create(clazz, dataObjectFactory, entities, null, null, null, expandName, dataType, parentExpand, baseUrl, true,
                entityManager);
    }

    public V create(final Class<V> clazz, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final List<U> entities,
            final String expandName, final String dataType, final ExpandDataTrunk parentExpand, final String baseUrl,
            final Number revision, final EntityManager entityManager) {
        return create(clazz, dataObjectFactory, entities, null, revision, null, expandName, dataType, parentExpand, baseUrl,
                true, entityManager);
    }

    public V create(final Class<V> clazz, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final U parent,
            final List<Number> revisions, final String expandName, final String dataType, final ExpandDataTrunk parentExpand,
            final String baseUrl, final EntityManager entityManager) {
        return create(clazz, dataObjectFactory, null, parent, null, revisions, expandName, dataType, parentExpand, baseUrl,
                true, entityManager);
    }

    public V create(final Class<V> clazz, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final U parent,
            final List<Number> revisions, final String expandName, final String dataType, final ExpandDataTrunk parentExpand,
            final String baseUrl, final Number revision, final EntityManager entityManager) {
        return create(clazz, dataObjectFactory, null, parent, revision, revisions, expandName, dataType, parentExpand, baseUrl,
                true, entityManager);
    }

    public V create(final Class<V> clazz, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final List<U> entities,
            final String expandName, final String dataType, final ExpandDataTrunk parentExpand, final String baseUrl,
            final boolean expandParentReferences, final EntityManager entityManager) {
        return create(clazz, dataObjectFactory, entities, null, null, null, expandName, dataType, parentExpand, baseUrl,
                expandParentReferences, entityManager);
    }

    public V create(final Class<V> clazz, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final List<U> entities,
            final String expandName, final String dataType, final ExpandDataTrunk parentExpand, final String baseUrl,
            final Number revision, final boolean expandParentReferences, final EntityManager entityManager) {
        return create(clazz, dataObjectFactory, entities, null, revision, null, expandName, dataType, parentExpand, baseUrl,
                expandParentReferences, entityManager);
    }

    /**
     * @param clazz The Class of the Collection Object that should be returned by the method.
     * @param dataObjectFactory The factory to convert the database entity to a REST entity
     * @param entities A collection of numbers mapped to database entities. If isRevsionMap is true, these numbers are envers
     *        revision numbers. If isRevsionMap is false, these numbers have no meaning.
     * @param parent The parent from which to find previous versions
     * @param parentRevision The revision number of the Parent entity, if it's not the latest version.
     * @param revisions A list of Envers revision numbers that we want to add to the collection
     * @param expandName The name of the collection that we are working with
     * @param dataType The type of data that is returned through the REST interface
     * @param parentExpand The parent objects expansion details
     * @param baseUrl The base of the url that was used to access this collection
     * @param expandParentReferences If any Parent references in entities should be expanded.
     * @param entityManager The EntityManager being used to provide data for the collection.
     * @return a REST collection from a collection of database entities.
     */
    public V create(final Class<V> clazz, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final List<U> entities,
            final U parent, final Number parentRevision, final List<Number> revisions, final String expandName,
            final String dataType, final ExpandDataTrunk parentExpand, final String baseUrl,
            final boolean expandParentReferences, final EntityManager entityManager) {
        V retValue = null;

        try {
            retValue = clazz.newInstance();
        } catch (final Exception ex) {
            return null;
        }

        /*
         * either the entities collection needs to be set, or the revisions and parent
         */
        if (!(entities != null || (revisions != null && parent != null)))
            return retValue;

        final boolean usingRevisions = entities == null;

        retValue.setExpand(expandName);
        /*
         * Un-comment this to make it so that size is always shown. Don't forget to comment out line 154 below.
         */
        // retValue.setSize(usingRevisions ? revisions.size() : entities.size());

        final ExpandDataTrunk expand = parentExpand != null ? parentExpand.get(expandName) : null;

        try {
            if (expand != null) {
                if (expand.getTrunk().getName().equals(expandName)) {
                    assert baseUrl != null : "Parameter baseUrl can not be null if parameter expand is not null";

                    final ExpandDataDetails indexes = expand.getTrunk();

                    /*
                     * Un-comment this to make it so that size is only shown when the expand is set. Don't forget to comment out
                     * line 136 above.
                     */
                    retValue.setSize(usingRevisions ? revisions.size() : entities.size());

                    int start = 0;
                    if (indexes.getStart() != null) {
                        final int startIndex = indexes.getStart();
                        final int size = usingRevisions ? revisions.size() : entities.size();
                        if (startIndex < 0) {
                            start = Math.max(0, size + startIndex);
                        } else {
                            start = Math.min(startIndex, size - 1);
                        }
                    }

                    int end = usingRevisions ? revisions.size() : entities.size();
                    if (indexes.getEnd() != null) {
                        final int endIndex = indexes.getEnd();
                        final int size = usingRevisions ? revisions.size() : entities.size();
                        if (endIndex < 0) {
                            end = Math.max(0, size + endIndex + 1);
                        } else {
                            end = Math.min(endIndex, size);
                        }
                    }

                    final int fixedStart = Math.min(start, end);
                    final int fixedEnd = Math.max(start, end);

                    retValue.setStartExpandIndex(fixedStart);
                    retValue.setEndExpandIndex(fixedEnd);

                    /*
                     * if (indexes.getRecursive() != null && indexes.getRecursive()) { if (expand.getBranches() == null) {
                     * expand.setBranches(CollectionUtilities.toArrayList(expand)); } else { expand.getBranches().add(expand); }
                     * }
                     */

                    for (int i = fixedStart; i < fixedEnd; i++) {
                        U dbEntity = null;

                        Number revision = parentRevision;
                        if (usingRevisions) {
                            /*
                             * Looking up an Envers previous version is an expensive operation. So instead of getting a complete
                             * collection and only adding those we need to the REST collection (like we do with standard related
                             * entities in the database), when it comes to Envers we only retrieve the previous versions when
                             * they are specifically requested.
                             * 
                             * This means that we only have to request the list of revision numbers (supplied to us via the
                             * revisions parameter) instead of having to request every revision.
                             */
                            final AuditedEntity<U> parentAuditedEntity = (AuditedEntity<U>) parent;
                            revision = revisions.get(i);
                            dbEntity = parentAuditedEntity.getRevision(entityManager, revision);

                        } else {
                            dbEntity = entities.get(i);
                        }

                        if (dbEntity != null) {
                            final T restEntity = dataObjectFactory.createRESTEntityFromDBEntity(dbEntity, baseUrl, dataType,
                                    expand, revision, expandParentReferences, entityManager);

                            /*
                             * if the entities keyset relates to the revision numbers, copy that data across
                             */
                            if (usingRevisions) {
                                restEntity.setRevision(revisions.get(i).intValue());
                            }

                            retValue.addItem(restEntity);
                        }

                    }
                }
            }
        } catch (final Exception ex) {
            log.error("An error creating or populating a RESTBaseCollectionV1", ex);
        }

        return retValue;
    }
}
