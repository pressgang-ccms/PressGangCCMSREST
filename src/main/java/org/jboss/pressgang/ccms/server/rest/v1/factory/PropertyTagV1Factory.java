/*
  Copyright 2011-2014 Red Hat, Inc

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

import static com.google.common.base.Strings.isNullOrEmpty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jboss.pressgang.ccms.model.PropertyTag;
import org.jboss.pressgang.ccms.model.PropertyTagCategory;
import org.jboss.pressgang.ccms.model.PropertyTagToPropertyTagCategory;
import org.jboss.pressgang.ccms.model.base.PressGangEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTPropertyTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTPropertyCategoryInPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseAuditedEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTPropertyCategoryInPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTPropertyTagInPropertyCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class PropertyTagV1Factory extends RESTEntityFactory<RESTPropertyTagV1, PropertyTag, RESTPropertyTagCollectionV1,
        RESTPropertyTagCollectionItemV1> {
    @Inject
    protected PropertyCategoryInPropertyTagV1Factory propertyCategoryInPropertyTagFactory;

    @Override
    public RESTPropertyTagV1 createRESTEntityFromDBEntityInternal(final PropertyTag entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTPropertyTagV1 retValue = new RESTPropertyTagV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTPropertyTagInPropertyCategoryV1.PROPERTY_CATEGORIES_NAME);
        expandOptions.add(RESTBaseAuditedEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseAuditedEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getPropertyTagId());
        retValue.setName(entity.getPropertyTagName());
        retValue.setDescription(entity.getPropertyTagDescription());
        retValue.setRegex(entity.getPropertyTagRegex());
        retValue.setIsUnique(entity.getPropertyTagIsUnique());
        retValue.setCanBeNull(entity.isPropertyTagCanBeNull());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseAuditedEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTPropertyTagCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseAuditedEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // PROPERTY CATEGORIES
        if (expand != null && expand.contains(RESTPropertyTagV1.PROPERTY_CATEGORIES_NAME)) {
            retValue.setPropertyCategories(RESTEntityCollectionFactory.create(RESTPropertyCategoryInPropertyTagCollectionV1.class,
                    propertyCategoryInPropertyTagFactory, entity.getPropertyTagToPropertyTagCategoriesList(),
                    RESTPropertyTagV1.PROPERTY_CATEGORIES_NAME, dataType, expand, baseUrl, revision, false, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.PROPERTYTAG_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTPropertyTagV1> parent, final RESTPropertyTagV1 dataObject) {
        if (dataObject.hasParameterSet(RESTPropertyTagV1.PROPERTY_CATEGORIES_NAME)
                && dataObject.getPropertyCategories() != null
                && dataObject.getPropertyCategories().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getPropertyCategories(), propertyCategoryInPropertyTagFactory);
        }
    }

    @Override
    public void syncBaseDetails(final PropertyTag entity, final RESTPropertyTagV1 dataObject) {
        if (dataObject.hasParameterSet(RESTPropertyTagV1.DESCRIPTION_NAME)) entity.setPropertyTagDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTPropertyTagV1.CANBENULL_NAME)) entity.setPropertyTagCanBeNull(dataObject.getCanBeNull());
        if (dataObject.hasParameterSet(RESTPropertyTagV1.NAME_NAME)) entity.setPropertyTagName(dataObject.getName());
        if (dataObject.hasParameterSet(RESTPropertyTagV1.REGEX_NAME)) {
            if (!isNullOrEmpty(dataObject.getRegex())) {
                // Validate that the condition is valid
                try {
                    Pattern.compile(dataObject.getRegex());
                } catch (Exception e) {
                    throw new BadRequestException(e);
                }
            }
            entity.setPropertyTagRegex(dataObject.getRegex());
        }
        if (dataObject.hasParameterSet(RESTPropertyTagV1.ISUNIQUE_NAME)) entity.setPropertyTagIsUnique(dataObject.getIsUnique());
    }

    @Override
    protected void doDeleteChildAction(final PropertyTag entity, final RESTPropertyTagV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTPropertyTagInPropertyCategoryV1) {
            final PropertyTagCategory dbEntity = entityManager.find(PropertyTagCategory.class, restEntity.getId());
            if (dbEntity == null) throw new BadRequestException(
                    "No PropertyTagCategory entity was found with the primary key " + restEntity.getId());

            entity.removePropertyTagCategory(dbEntity);
        }
    }

    @Override
    protected PressGangEntity doCreateChildAction(final PropertyTag entity, final RESTPropertyTagV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();
        final PressGangEntity dbEntity;

        if (restEntity instanceof RESTPropertyCategoryInPropertyTagV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            final PropertyTagCategory propertyTagCategory = entityManager.find(PropertyTagCategory.class, restEntity.getId());
            if (dbEntity == null) throw new BadRequestException(
                    "No PropertyTagCategory entity was found with the primary key " + restEntity.getId());

            final PropertyTagToPropertyTagCategory tagToCategory = (PropertyTagToPropertyTagCategory) dbEntity;
            tagToCategory.setPropertyTagCategory(propertyTagCategory);
            tagToCategory.setPropertyTag(entity);

            entity.getPropertyTagToPropertyTagCategories().add(tagToCategory);
            propertyTagCategory.getPropertyTagToPropertyTagCategories().add(tagToCategory);
        } else {
            throw new IllegalArgumentException("Item is not a child of PropertyTag");
        }

        return dbEntity;
    }

    @Override
    protected PressGangEntity getChildEntityForAction(final PropertyTag entity, final RESTPropertyTagV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();

        final PressGangEntity dbEntity;
        if (restEntity instanceof RESTPropertyCategoryInPropertyTagV1) {
            final RESTPropertyCategoryInPropertyTagV1 propertyCategory = (RESTPropertyCategoryInPropertyTagV1) restEntity;
            dbEntity = entityManager.find(PropertyTagToPropertyTagCategory.class, propertyCategory.getRelationshipId());
            if (dbEntity == null) {
                throw new BadRequestException("No PropertyTagToPropertyTagCategory entity was found with the primary key " + propertyCategory
                        .getRelationshipId());
            } else if (!entity.getPropertyTagToPropertyTagCategories().contains(dbEntity)) {
                throw new BadRequestException(
                        "No PropertyTagToPropertyTagCategory entity was found with the primary key " + propertyCategory.getRelationshipId() +
                                " for PropertyTag " + entity.getId());
            }
        } else {
            throw new IllegalArgumentException("Item is not a child of PropertyTag");
        }

        return dbEntity;
    }

    @Override
    protected Class<PropertyTag> getDatabaseClass() {
        return PropertyTag.class;
    }
}
