package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.PropertyTag;
import org.jboss.pressgang.ccms.model.PropertyTagCategory;
import org.jboss.pressgang.ccms.model.PropertyTagToPropertyTagCategory;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTPropertyCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTPropertyCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTPropertyTagInPropertyCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTPropertyTagInPropertyCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTPropertyCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTPropertyTagInPropertyCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.restserver.utils.EnversUtilities;

public class PropertyCategoryV1Factory
        extends
        RESTDataObjectFactory<RESTPropertyCategoryV1, PropertyTagCategory, RESTPropertyCategoryCollectionV1, RESTPropertyCategoryCollectionItemV1> {
    public PropertyCategoryV1Factory() {
        super(PropertyTagCategory.class);
    }

    @Override
    public RESTPropertyCategoryV1 createRESTEntityFromDBEntityInternal(final PropertyTagCategory entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTPropertyCategoryV1 retValue = new RESTPropertyCategoryV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTPropertyCategoryV1.PROPERTY_TAGS_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setName(entity.getPropertyTagCategoryName());
        retValue.setDescription(entity.getPropertyTagCategoryDescription());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTPropertyCategoryV1, PropertyTagCategory, RESTPropertyCategoryCollectionV1, RESTPropertyCategoryCollectionItemV1>()
                    .create(RESTPropertyCategoryCollectionV1.class, new PropertyCategoryV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType,
                            expand, baseUrl, entityManager));
        }

        // PROPERTY TAGS
        if (expand != null && expand.contains(RESTPropertyCategoryV1.PROPERTY_TAGS_NAME)) {
            retValue.setPropertyTags(new RESTDataObjectCollectionFactory<RESTPropertyTagInPropertyCategoryV1, PropertyTagToPropertyTagCategory, RESTPropertyTagInPropertyCategoryCollectionV1, RESTPropertyTagInPropertyCategoryCollectionItemV1>()
                    .create(RESTPropertyTagInPropertyCategoryCollectionV1.class, new PropertyTagInPropertyCategoryV1Factory(),
                            entity.getPropertyTagToPropertyTagCategoriesList(), RESTPropertyCategoryV1.PROPERTY_TAGS_NAME,
                            dataType, expand, baseUrl, revision, false, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.PROPERTY_CATEGORY_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final PropertyTagCategory entity,
            final RESTPropertyCategoryV1 dataObject) throws InvalidParameterException {
        if (dataObject.hasParameterSet(RESTPropertyCategoryV1.DESCRIPTION_NAME))
            entity.setPropertyTagCategoryDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTPropertyCategoryV1.NAME_NAME))
            entity.setPropertyTagCategoryName(dataObject.getName());

        entityManager.persist(entity);

        if (dataObject.hasParameterSet(RESTPropertyCategoryV1.PROPERTY_TAGS_NAME) && dataObject.getPropertyTags() != null
                && dataObject.getPropertyTags().getItems() != null) {
            dataObject.getPropertyTags().removeInvalidChangeItemRequests();

            for (final RESTPropertyTagInPropertyCategoryCollectionItemV1 restEntityItem : dataObject.getPropertyTags()
                    .getItems()) {
                final RESTPropertyTagInPropertyCategoryV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsRemoveItem()) {
                    final PropertyTag dbEntity = entityManager.find(PropertyTag.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No PropertyTag entity was found with the primary key "
                                + restEntity.getId());

                    if (restEntityItem.returnIsAddItem()) {
                        entity.addPropertyTag(dbEntity);
                    } else if (restEntityItem.returnIsRemoveItem()) {
                        entity.removePropertyTag(dbEntity);
                    }
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final PropertyTagToPropertyTagCategory dbEntity = entityManager.find(
                            PropertyTagToPropertyTagCategory.class, restEntity.getRelationshipId());
                    if (dbEntity == null)
                        throw new InvalidParameterException(
                                "No PropertyTagToPropertyTagCategory entity was found with the primary key "
                                        + restEntity.getRelationshipId());

                    new PropertyTagInPropertyCategoryV1Factory()
                            .syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                }
            }
        }
    }
}