/*
  Copyright 2011-2014 Red Hat

  This file is part of PressGang CCMS.

  PressGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PressGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PressGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.PropertyTag;
import org.jboss.pressgang.ccms.model.PropertyTagCategory;
import org.jboss.pressgang.ccms.model.PropertyTagToPropertyTagCategory;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTPropertyCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTPropertyCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTPropertyTagInPropertyCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTPropertyCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTPropertyTagInPropertyCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class PropertyCategoryV1Factory extends RESTEntityFactory<RESTPropertyCategoryV1, PropertyTagCategory,
        RESTPropertyCategoryCollectionV1, RESTPropertyCategoryCollectionItemV1> {
    @Inject
    protected PropertyTagInPropertyCategoryV1Factory propertyTagInPropertyCategoryFactory;

    @Override
    public RESTPropertyCategoryV1 createRESTEntityFromDBEntityInternal(final PropertyTagCategory entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTPropertyCategoryV1 retValue = new RESTPropertyCategoryV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTPropertyCategoryV1.PROPERTY_TAGS_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setName(entity.getPropertyTagCategoryName());
        retValue.setDescription(entity.getPropertyTagCategoryDescription());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTPropertyCategoryCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // PROPERTY TAGS
        if (expand != null && expand.contains(RESTPropertyCategoryV1.PROPERTY_TAGS_NAME)) {
            retValue.setPropertyTags(RESTEntityCollectionFactory.create(RESTPropertyTagInPropertyCategoryCollectionV1.class,
                    propertyTagInPropertyCategoryFactory, entity.getPropertyTagToPropertyTagCategoriesList(),
                    RESTPropertyCategoryV1.PROPERTY_TAGS_NAME, dataType, expand, baseUrl, revision, false, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.PROPERTY_CATEGORY_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(RESTChangeAction<RESTPropertyCategoryV1> parent, RESTPropertyCategoryV1 dataObject) {
        if (dataObject.hasParameterSet(RESTPropertyCategoryV1.PROPERTY_TAGS_NAME)
                && dataObject.getPropertyTags() != null
                && dataObject.getPropertyTags().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getPropertyTags(), propertyTagInPropertyCategoryFactory);
        }
    }

    @Override
    public void syncBaseDetails(final PropertyTagCategory entity, final RESTPropertyCategoryV1 dataObject) {
        if (dataObject.hasParameterSet(RESTPropertyCategoryV1.DESCRIPTION_NAME))
            entity.setPropertyTagCategoryDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTPropertyCategoryV1.NAME_NAME)) entity.setPropertyTagCategoryName(dataObject.getName());
    }

    @Override
    protected void doDeleteChildAction(final PropertyTagCategory entity, final RESTPropertyCategoryV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTPropertyTagInPropertyCategoryV1) {
            final PropertyTag dbEntity = entityManager.find(PropertyTag.class, restEntity.getId());
            if (dbEntity == null) throw new BadRequestException(
                    "No PropertyTag entity was found with the primary key " + restEntity.getId());

            entity.removePropertyTag(dbEntity);
        }
    }

    @Override
    protected AuditedEntity doCreateChildAction(final PropertyTagCategory entity, final RESTPropertyCategoryV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();
        final AuditedEntity dbEntity;

        if (restEntity instanceof RESTPropertyTagInPropertyCategoryV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            final PropertyTag propertyTag = entityManager.find(PropertyTag.class, restEntity.getId());
            if (dbEntity == null) throw new BadRequestException(
                    "No PropertyTag entity was found with the primary key " + restEntity.getId());

            final PropertyTagToPropertyTagCategory tagToCategory = (PropertyTagToPropertyTagCategory) dbEntity;
            tagToCategory.setPropertyTag(propertyTag);
            tagToCategory.setPropertyTagCategory(entity);

            entity.getPropertyTagToPropertyTagCategories().add(tagToCategory);
            propertyTag.getPropertyTagToPropertyTagCategories().add(tagToCategory);
        } else {
            throw new IllegalArgumentException("Item is not a child of PropertyCategory");
        }

        return dbEntity;
    }

    @Override
    protected AuditedEntity getChildEntityForAction(final PropertyTagCategory entity, final RESTPropertyCategoryV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        final AuditedEntity dbEntity;
        if (restEntity instanceof RESTPropertyTagInPropertyCategoryV1) {
            final RESTPropertyTagInPropertyCategoryV1 propertyTag = (RESTPropertyTagInPropertyCategoryV1) restEntity;
            dbEntity = entityManager.find(PropertyTagToPropertyTagCategory.class, propertyTag.getRelationshipId());
            if (dbEntity == null) {
                throw new BadRequestException("No PropertyTagToPropertyTagCategory entity was found with the primary key " + propertyTag
                        .getRelationshipId());
            } else if (!entity.getPropertyTagToPropertyTagCategories().contains(dbEntity)) {
                throw new BadRequestException(
                        "No PropertyTagToPropertyTagCategory entity was found with the primary key " + propertyTag.getRelationshipId() +
                                " for PropertyTagCategory " + entity.getId());
            }
        } else {
            throw new IllegalArgumentException("Item is not a child of PropertyTagCategory");
        }

        return dbEntity;
    }

    @Override
    protected Class<PropertyTagCategory> getDatabaseClass() {
        return PropertyTagCategory.class;
    }
}
