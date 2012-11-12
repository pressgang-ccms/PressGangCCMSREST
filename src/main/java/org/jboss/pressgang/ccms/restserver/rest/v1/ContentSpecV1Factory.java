package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTContentSpecCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTCSNodeCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTContentSpecCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.join.RESTAssignedCSMetaDataCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.join.RESTAssignedCSMetaDataCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTAssignedPropertyTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTContentSpecV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.join.RESTAssignedCSMetaDataV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTAssignedPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.entity.PropertyTag;
import org.jboss.pressgang.ccms.restserver.entity.Tag;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.CSMetaData;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.CSNode;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.ContentSpec;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.ContentSpecToCSMetaData;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.ContentSpecToPropertyTag;
import org.jboss.pressgang.ccms.restserver.exceptions.CustomConstraintViolationException;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;
import org.jboss.resteasy.spi.BadRequestException;

public class ContentSpecV1Factory extends
        RESTDataObjectFactory<RESTContentSpecV1, ContentSpec, RESTContentSpecCollectionV1, RESTContentSpecCollectionItemV1> {

    public ContentSpecV1Factory() {
        super(ContentSpec.class);
    }

    @Override
    public RESTContentSpecV1 createRESTEntityFromDBEntity(final ContentSpec entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter contentSpec can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTContentSpecV1 retValue = new RESTContentSpecV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        expandOptions.add(RESTContentSpecV1.META_DATA_NAME);
        expandOptions.add(RESTContentSpecV1.NODES_NAME);
        expandOptions.add(RESTContentSpecV1.PROPERTIES_NAME);
        expandOptions.add(RESTContentSpecV1.TAGS_NAME);
        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setTitle(entity.getContentSpecTitle());
        retValue.setLocale(entity.getLocale());

        // REVISIONS
        if (revision == null) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTContentSpecV1, ContentSpec, RESTContentSpecCollectionV1, RESTContentSpecCollectionItemV1>()
                    .create(RESTContentSpecCollectionV1.class, new ContentSpecV1Factory(), entity, entity.getRevisions(entityManager),
                            RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
        }

        // CHILDREN NODES
        retValue.setChildren_OTM(new RESTDataObjectCollectionFactory<RESTCSNodeV1, CSNode, RESTCSNodeCollectionV1, RESTCSNodeCollectionItemV1>()
                .create(RESTCSNodeCollectionV1.class, new CSNodeV1Factory(), entity.getTopCSNodes(),
                        RESTContentSpecV1.NODES_NAME, dataType, expand, baseUrl, expandParentReferences, entityManager));

        // META DATA
        retValue.setMetaData(new RESTDataObjectCollectionFactory<RESTAssignedCSMetaDataV1, ContentSpecToCSMetaData, RESTAssignedCSMetaDataCollectionV1, RESTAssignedCSMetaDataCollectionItemV1>()
                .create(RESTAssignedCSMetaDataCollectionV1.class, new ContentSpecMetaDataV1Factory(),
                        entity.getContentSpecMetaDataList(), RESTContentSpecV1.META_DATA_NAME, dataType, expand, baseUrl,
                        expandParentReferences, entityManager));

        // PROPERTY TAGS
        retValue.setProperties(new RESTDataObjectCollectionFactory<RESTAssignedPropertyTagV1, ContentSpecToPropertyTag, RESTAssignedPropertyTagCollectionV1, RESTAssignedPropertyTagCollectionItemV1>()
                .create(RESTAssignedPropertyTagCollectionV1.class, new ContentSpecPropertyTagV1Factory(),
                        entity.getContentSpecToPropertyTagsList(), RESTContentSpecV1.PROPERTIES_NAME, dataType, expand,
                        baseUrl, revision, entityManager));
        
        // TAGS
        retValue.setTags(new RESTDataObjectCollectionFactory<RESTTagV1, Tag, RESTTagCollectionV1, RESTTagCollectionItemV1>()
                .create(RESTTagCollectionV1.class, new TagV1Factory(), entity.getTags(), RESTContentSpecV1.TAGS_NAME,
                        dataType, expand, baseUrl, entityManager));

        retValue.setLinks(baseUrl, RESTv1Constants.CONTENT_SPEC_URL_NAME, dataType, retValue.getId());
        retValue.setLogDetails(new LogDetailsV1Factory().create(entity, revision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand,
                dataType, baseUrl, entityManager));

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final ContentSpec entity,
            final RESTContentSpecV1 dataObject) throws InvalidParameterException {
        if (dataObject.hasParameterSet(RESTContentSpecV1.TITLE_NAME))
            entity.setContentSpecTitle(dataObject.getTitle());

        if (dataObject.hasParameterSet(RESTContentSpecV1.LOCALE_NAME))
            entity.setLocale(dataObject.getLocale());

        if (dataObject.hasParameterSet(RESTContentSpecV1.LAST_PUBLISHED_NAME))
            entity.setLastPublished(dataObject.getLastPublished());

        entityManager.persist(entity);
        
        /* Many To Many */
        if (dataObject.hasParameterSet(RESTContentSpecV1.PROPERTIES_NAME) && dataObject.getProperties() != null
                && dataObject.getProperties().getItems() != null) {
            dataObject.getProperties().removeInvalidChangeItemRequests();

            /* remove children first */
            for (final RESTAssignedPropertyTagCollectionItemV1 restEntityItem : dataObject.getProperties().getItems()) {
                final RESTAssignedPropertyTagV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final PropertyTag dbEntity = entityManager.find(PropertyTag.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No PropertyTag entity was found with the primary key "
                                + restEntity.getId());

                    entity.removePropertyTag(dbEntity, restEntity.getValue());
                } else if (restEntityItem.returnIsAddItem()) {
                    final PropertyTag dbEntity = entityManager.find(PropertyTag.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No PropertyTag entity was found with the primary key "
                                + restEntity.getId());

                    entity.addPropertyTag(dbEntity, restEntity.getValue());
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final ContentSpecToPropertyTag dbEntity = entityManager.find(ContentSpecToPropertyTag.class,
                            restEntity.getRelationshipId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No ContentSpecToPropertyTag entity was found with the primary key "
                                + restEntity.getRelationshipId());

                    new ContentSpecPropertyTagV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                }
            }
        }
        
        if (dataObject.hasParameterSet(RESTContentSpecV1.TAGS_NAME) && dataObject.getTags() != null
                && dataObject.getTags().getItems() != null) {
            dataObject.getTags().removeInvalidChangeItemRequests();

            /* Remove Tags first to ensure mutual exclusion is done correctly */
            for (final RESTTagCollectionItemV1 restEntityItem : dataObject.getTags().getItems()) {
                final RESTTagV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final Tag dbEntity = entityManager.find(Tag.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No Tag entity was found with the primary key "
                                + restEntity.getId());

                    entity.removeTag(dbEntity);
                }
            }

            for (final RESTTagCollectionItemV1 restEntityItem : dataObject.getTags().getItems()) {
                final RESTTagV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem()) {
                    final Tag dbEntity = entityManager.find(Tag.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No Tag entity was found with the primary key "
                                + restEntity.getId());

                    try {
                        entity.addTag(dbEntity);
                    } catch (CustomConstraintViolationException e) {
                        throw new BadRequestException(e.getMessage());
                    }
                }
            }
        }
        
        if (dataObject.hasParameterSet(RESTContentSpecV1.META_DATA_NAME) && dataObject.getMetaData() != null
                && dataObject.getMetaData().getItems() != null) {
            dataObject.getMetaData().removeInvalidChangeItemRequests();

            /* remove children first */
            for (final RESTAssignedCSMetaDataCollectionItemV1 restEntityItem : dataObject.getMetaData().getItems()) {
                final RESTAssignedCSMetaDataV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final CSMetaData dbEntity = entityManager.find(CSMetaData.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No CSMetaData entity was found with the primary key "
                                + restEntity.getId());

                    entity.removeMetaData(dbEntity, restEntity.getValue());
                } else if (restEntityItem.returnIsAddItem()) {
                    final CSMetaData dbEntity = entityManager.find(CSMetaData.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No CSMetaData entity was found with the primary key "
                                + restEntity.getId());

                    entity.addMetaData(dbEntity, restEntity.getValue());
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final ContentSpecToCSMetaData dbEntity = entityManager.find(ContentSpecToCSMetaData.class,
                            restEntity.getRelationshipId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No ContentSpecToCSMetaData entity was found with the primary key "
                                + restEntity.getRelationshipId());

                    new ContentSpecMetaDataV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                }
            }
        }

        /* One To Many - Add will create a new mapping */
        if (dataObject.hasParameterSet(RESTContentSpecV1.NODES_NAME) && dataObject.getChildren_OTM() != null
                && dataObject.getChildren_OTM().getItems() != null) {
            dataObject.getChildren_OTM().removeInvalidChangeItemRequests();

            for (final RESTCSNodeCollectionItemV1 restEntityItem : dataObject.getChildren_OTM().getItems()) {
                final RESTCSNodeV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final CSNode dbEntity = entityManager.find(CSNode.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No CSNode entity was found with the primary key "
                                + restEntity.getId());

                    entity.removeChild(dbEntity);
                } else if (restEntityItem.returnIsAddItem()) {
                    final CSNode dbEntity = new CSNodeV1Factory().createDBEntityFromRESTEntity(entityManager, restEntity);
                    entityManager.persist(dbEntity);
                    entity.addChild(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final CSNode dbEntity = entityManager.find(CSNode.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No CSNode entity was found with the primary key "
                                + restEntity.getId());

                    new CSNodeV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                }
            }
        }
    }
}
