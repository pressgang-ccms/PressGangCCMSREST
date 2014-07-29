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
import org.jboss.pressgang.ccms.rest.v1.collections.RESTPropertyCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTPropertyCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTPropertyTagInPropertyCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTPropertyTagInPropertyCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTPropertyCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTPropertyTagInPropertyCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
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
    public void syncDBEntityWithRESTEntityFirstPass(final PropertyTagCategory entity, final RESTPropertyCategoryV1 dataObject) {
        if (dataObject.hasParameterSet(RESTPropertyCategoryV1.DESCRIPTION_NAME))
            entity.setPropertyTagCategoryDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTPropertyCategoryV1.NAME_NAME)) entity.setPropertyTagCategoryName(dataObject.getName());

        // Many to Many
        if (dataObject.hasParameterSet(
                RESTPropertyCategoryV1.PROPERTY_TAGS_NAME) && dataObject.getPropertyTags() != null && dataObject.getPropertyTags()
                .getItems() != null) {
            dataObject.getPropertyTags().removeInvalidChangeItemRequests();

            for (final RESTPropertyTagInPropertyCategoryCollectionItemV1 restEntityItem : dataObject.getPropertyTags().getItems()) {
                final RESTPropertyTagInPropertyCategoryV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final PropertyTag dbEntity = entityManager.find(PropertyTag.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new BadRequestException("No PropertyTag entity was found with the primary key " + restEntity.getId());

                    entity.removePropertyTag(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final PropertyTagToPropertyTagCategory dbEntity = entityManager.find(PropertyTagToPropertyTagCategory.class,
                            restEntity.getRelationshipId());
                    if (dbEntity == null) throw new BadRequestException(
                            "No PropertyTagToPropertyTagCategory entity was found with the primary key " + restEntity.getRelationshipId());
                    if (!entity.getPropertyTagToPropertyTagCategories().contains(dbEntity)) throw new BadRequestException(
                            "No PropertyTagToPropertyTagCategory entity was found with the primary key " + restEntity.getRelationshipId()
                                    + " for PropertyCategory " + entity.getId());

                    propertyTagInPropertyCategoryFactory.updateDBEntityFromRESTEntity(dbEntity, restEntity);
                }
            }
        }
    }

    @Override
    public void syncDBEntityWithRESTEntitySecondPass(PropertyTagCategory entity, RESTPropertyCategoryV1 dataObject) {
        // Many to Many
        if (dataObject.hasParameterSet(
                RESTPropertyCategoryV1.PROPERTY_TAGS_NAME) && dataObject.getPropertyTags() != null && dataObject.getPropertyTags()
                .getItems() != null) {
            dataObject.getPropertyTags().removeInvalidChangeItemRequests();

            for (final RESTPropertyTagInPropertyCategoryCollectionItemV1 restEntityItem : dataObject.getPropertyTags().getItems()) {
                final RESTPropertyTagInPropertyCategoryV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem()) {
                    final PropertyTag dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, restEntity, PropertyTag.class);
                    if (dbEntity == null)
                        throw new BadRequestException("No PropertyTag entity was found with the primary key " + restEntity.getId());

                    entity.addPropertyTag(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final PropertyTagToPropertyTagCategory dbEntity = entityManager.find(PropertyTagToPropertyTagCategory.class,
                            restEntity.getRelationshipId());
                    if (dbEntity == null) throw new BadRequestException(
                            "No PropertyTagToPropertyTagCategory entity was found with the primary key " + restEntity.getRelationshipId());
                    if (!entity.getPropertyTagToPropertyTagCategories().contains(dbEntity)) throw new BadRequestException(
                            "No PropertyTagToPropertyTagCategory entity was found with the primary key " + restEntity.getRelationshipId()
                                    + " for PropertyCategory " + entity.getId());

                    propertyTagInPropertyCategoryFactory.syncDBEntityWithRESTEntitySecondPass(dbEntity, restEntity);
                }
            }
        }
    }

    @Override
    protected Class<PropertyTagCategory> getDatabaseClass() {
        return PropertyTagCategory.class;
    }
}
