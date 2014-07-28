/*
  Copyright 2011-2014 Red Hat

  This file is part of PresGang CCMS.

  PresGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PresGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PresGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.Category;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.model.TagToCategory;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTTagInCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTTagInCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTCategoryInTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTTagInCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class CategoryV1Factory extends RESTEntityFactory<RESTCategoryV1, Category, RESTCategoryCollectionV1,
        RESTCategoryCollectionItemV1> {
    @Inject
    protected TagInCategoryV1Factory tagInCategoryFactory;

    @Override
    public RESTCategoryV1 createRESTEntityFromDBEntityInternal(final Category entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTCategoryV1 retValue = new RESTCategoryV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTv1Constants.TAGS_EXPANSION_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getCategoryId());
        retValue.setName(entity.getCategoryName());
        retValue.setDescription(entity.getCategoryDescription());
        retValue.setMutuallyExclusive(entity.isMutuallyExclusive());
        retValue.setSort(entity.getCategorySort());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTCategoryCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // TAGS
        if (expand != null && expand.contains(RESTCategoryV1.TAGS_NAME)) {
            retValue.setTags(RESTEntityCollectionFactory.create(RESTTagInCategoryCollectionV1.class, tagInCategoryFactory,
                    entity.getTagToCategoriesArray(), RESTCategoryV1.TAGS_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.CATEGORY_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntityFirstPass(final Category entity, final RESTCategoryV1 dataObject) {
        if (dataObject.hasParameterSet(RESTCategoryV1.DESCRIPTION_NAME)) entity.setCategoryDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTCategoryV1.MUTUALLYEXCLUSIVE_NAME))
            entity.setMutuallyExclusive(dataObject.getMutuallyExclusive());
        if (dataObject.hasParameterSet(RESTCategoryV1.NAME_NAME)) entity.setCategoryName(dataObject.getName());
        if (dataObject.hasParameterSet(RESTCategoryV1.SORT_NAME)) entity.setCategorySort(dataObject.getSort());

        // Many To Many - Add will create a mapping
        if (dataObject.hasParameterSet(
                RESTCategoryV1.TAGS_NAME) && dataObject.getTags() != null && dataObject.getTags().getItems() != null) {
            dataObject.getTags().removeInvalidChangeItemRequests();

            for (final RESTTagInCategoryCollectionItemV1 restEntityItem : dataObject.getTags().getItems()) {
                final RESTTagInCategoryV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final Tag dbEntity = entityManager.find(Tag.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());

                    entity.removeTag(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final TagToCategory dbEntity = entityManager.find(TagToCategory.class, restEntity.getRelationshipId());
                    if (dbEntity == null) throw new BadRequestException(
                            "No TagToCategory entity was found with the primary key " + restEntity.getRelationshipId());
                    if (!entity.getTagToCategories().contains(dbEntity)) throw new BadRequestException(
                            "No TagToCategory entity was found with the primary key " + restEntity.getRelationshipId() + " for Category "
                                    + entity.getId());

                    tagInCategoryFactory.updateDBEntityFromRESTEntity(dbEntity, restEntity);
                }
            }
        }
    }

    @Override
    public void syncDBEntityWithRESTEntitySecondPass(Category entity, RESTCategoryV1 dataObject) {
        // Many To Many - Add will create a mapping
        if (dataObject.hasParameterSet(
                RESTCategoryV1.TAGS_NAME) && dataObject.getTags() != null && dataObject.getTags().getItems() != null) {
            dataObject.getTags().removeInvalidChangeItemRequests();

            for (final RESTTagInCategoryCollectionItemV1 restEntityItem : dataObject.getTags().getItems()) {
                final RESTTagInCategoryV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem()) {
                    final Tag dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, restEntity, Tag.class);
                    if (dbEntity == null)
                        throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());

                    if (restEntity.hasParameterSet(RESTCategoryInTagV1.RELATIONSHIP_SORT_NAME)) {
                        entity.addTag(dbEntity, restEntity.getRelationshipSort());
                    } else {
                        entity.addTag(dbEntity);
                    }
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final TagToCategory dbEntity = entityManager.find(TagToCategory.class, restEntity.getRelationshipId());
                    if (dbEntity == null) throw new BadRequestException(
                            "No TagToCategory entity was found with the primary key " + restEntity.getRelationshipId());
                    if (!entity.getTagToCategories().contains(dbEntity)) throw new BadRequestException(
                            "No TagToCategory entity was found with the primary key " + restEntity.getRelationshipId() + " for Category "
                                    + entity.getId());

                    tagInCategoryFactory.syncDBEntityWithRESTEntitySecondPass(dbEntity, restEntity);
                }
            }
        }
    }

    @Override
    protected Class<Category> getDatabaseClass() {
        return Category.class;
    }
}
