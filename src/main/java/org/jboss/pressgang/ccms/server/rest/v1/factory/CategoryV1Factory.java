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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.Category;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.model.TagToCategory;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTTagInCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTTagInCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
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
    public void collectChangeInformation(final RESTChangeAction<RESTCategoryV1> parent, final RESTCategoryV1 dataObject) {
        // Many To Many - Add will create a mapping
        if (dataObject.hasParameterSet(RESTCategoryV1.TAGS_NAME)
                && dataObject.getTags() != null
                && dataObject.getTags().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getTags(), tagInCategoryFactory);
        }
    }

    @Override
    public void syncBaseDetails(final Category entity, final RESTCategoryV1 dataObject) {
        if (dataObject.hasParameterSet(RESTCategoryV1.DESCRIPTION_NAME)) entity.setCategoryDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTCategoryV1.MUTUALLYEXCLUSIVE_NAME))
            entity.setMutuallyExclusive(dataObject.getMutuallyExclusive());
        if (dataObject.hasParameterSet(RESTCategoryV1.NAME_NAME)) entity.setCategoryName(dataObject.getName());
        if (dataObject.hasParameterSet(RESTCategoryV1.SORT_NAME)) entity.setCategorySort(dataObject.getSort());
    }

    @Override
    public void doDeleteChildAction(final Category entity, final RESTCategoryV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTTagInCategoryV1) {
            final Tag dbEntity = entityManager.find(Tag.class, restEntity.getId());
            if (dbEntity == null)
                throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());

            entity.removeTag(dbEntity);
        }
    }

    @Override
    protected AuditedEntity doCreateChildAction(final Category entity, final RESTCategoryV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();
        final AuditedEntity dbEntity;

        if (restEntity instanceof RESTTagInCategoryV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            final Tag tagEntity = entityManager.find(Tag.class, restEntity.getId());
            if (tagEntity == null)
                throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());

            final TagToCategory ttc = (TagToCategory) dbEntity;
            ttc.setTag(tagEntity);
            entity.addTag(ttc);
        } else {
            throw new IllegalArgumentException("Item is not a child of Category");
        }

        return dbEntity;
    }

    @Override
    protected AuditedEntity getChildEntityForAction(final Category entity, final RESTCategoryV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        final AuditedEntity dbEntity;
        if (restEntity instanceof RESTTagInCategoryV1) {
            final RESTTagInCategoryV1 tagInCategory = (RESTTagInCategoryV1) restEntity;
            dbEntity = entityManager.find(TagToCategory.class, tagInCategory.getRelationshipId());
            if (dbEntity == null) {
                throw new BadRequestException("No TagToCategory entity was found with the primary key " + tagInCategory.getRelationshipId());
            } else if (!entity.getTagToCategories().contains(dbEntity)) {
                throw new BadRequestException(
                        "No TagToCategory entity was found with the primary key " + tagInCategory.getRelationshipId() + " for Category "
                                + entity.getId());
            }
        } else {
            throw new IllegalArgumentException("Item is not a child of Category");
        }

        return dbEntity;
    }

    @Override
    protected Class<Category> getDatabaseClass() {
        return Category.class;
    }
}
