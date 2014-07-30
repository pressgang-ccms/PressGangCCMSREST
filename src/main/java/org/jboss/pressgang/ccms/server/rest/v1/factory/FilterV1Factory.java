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

import org.jboss.pressgang.ccms.model.Filter;
import org.jboss.pressgang.ccms.model.FilterCategory;
import org.jboss.pressgang.ccms.model.FilterField;
import org.jboss.pressgang.ccms.model.FilterLocale;
import org.jboss.pressgang.ccms.model.FilterTag;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterFieldCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterLocaleCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTFilterCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterFieldV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterLocaleV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.enums.RESTFilterTypeV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class FilterV1Factory extends RESTEntityFactory<RESTFilterV1, Filter, RESTFilterCollectionV1, RESTFilterCollectionItemV1> {
    @Inject
    private FilterCategoryV1Factory filterCategoryFactory;
    @Inject
    private FilterFieldV1Factory filterFieldFactory;
    @Inject
    private FilterTagV1Factory filterTagFactory;
    @Inject
    private FilterLocaleV1Factory filterLocaleFactory;

    @Override
    public RESTFilterV1 createRESTEntityFromDBEntityInternal(final Filter entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTFilterV1 retValue = new RESTFilterV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTFilterV1.FILTER_CATEGORIES_NAME);
        expandOptions.add(RESTFilterV1.FILTER_LOCALES_NAME);
        expandOptions.add(RESTFilterV1.FILTER_TAGS_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getFilterId());
        retValue.setName(entity.getFilterName());
        retValue.setType(RESTFilterTypeV1.getFilterType(entity.getFilterClassType()));

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTFilterCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // FILTER TAGS
        if (expand != null && expand.contains(RESTFilterV1.FILTER_TAGS_NAME)) {
            retValue.setFilterTags_OTM(
                    RESTEntityCollectionFactory.create(RESTFilterTagCollectionV1.class, filterTagFactory, entity.getFilterTagsList(),
                            RESTFilterV1.FILTER_TAGS_NAME, dataType, expand, baseUrl, revision, false, entityManager));
        }

        // FILTER LOCALES
        if (expand != null && expand.contains(RESTFilterV1.FILTER_LOCALES_NAME)) {
            retValue.setFilterLocales_OTM(RESTEntityCollectionFactory.create(RESTFilterLocaleCollectionV1.class, filterLocaleFactory,
                    entity.getFilterLocalesList(), RESTFilterV1.FILTER_LOCALES_NAME, dataType, expand, baseUrl, revision, false,
                    entityManager));
        }

        // FILTER CATEGORIES
        if (expand != null && expand.contains(RESTFilterV1.FILTER_CATEGORIES_NAME)) {
            retValue.setFilterCategories_OTM(RESTEntityCollectionFactory.create(RESTFilterCategoryCollectionV1.class, filterCategoryFactory,
                    entity.getFilterCategoriesList(), RESTFilterV1.FILTER_CATEGORIES_NAME, dataType, expand, baseUrl, revision, false,
                    entityManager));
        }

        // FILTER FIELDS
        if (expand != null && expand.contains(RESTFilterV1.FILTER_FIELDS_NAME)) {
            retValue.setFilterFields_OTM(
                    RESTEntityCollectionFactory.create(RESTFilterFieldCollectionV1.class, filterFieldFactory, entity.getFilterFieldsList(),
                            RESTFilterV1.FILTER_FIELDS_NAME, dataType, expand, baseUrl, revision, false, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.FILTER_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTFilterV1> parent, final RESTFilterV1 dataObject) {
        /* One To Many - Add will create a child entity */
        if (dataObject.hasParameterSet(RESTFilterV1.FILTER_TAGS_NAME)
                && dataObject.getFilterTags_OTM() != null
                && dataObject.getFilterTags_OTM().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getFilterTags_OTM(), filterTagFactory);
        }

        /* One To Many - Add will create a child entity */
        if (dataObject.hasParameterSet(RESTFilterV1.FILTER_LOCALES_NAME)
                && dataObject.getFilterLocales_OTM() != null
                && dataObject.getFilterLocales_OTM().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getFilterLocales_OTM(), filterLocaleFactory);
        }

        /* One To Many - Add will create a child entity */
        if (dataObject.hasParameterSet(RESTFilterV1.FILTER_CATEGORIES_NAME)
                && dataObject.getFilterCategories_OTM() != null
                && dataObject.getFilterCategories_OTM().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getFilterCategories_OTM(), filterCategoryFactory);
        }

        /* One To Many - Add will create a child entity */
        if (dataObject.hasParameterSet(RESTFilterV1.FILTER_FIELDS_NAME)
                && dataObject.getFilterFields_OTM() != null
                && dataObject.getFilterFields_OTM().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getFilterFields_OTM(), filterFieldFactory);
        }
    }

    @Override
    public void syncBaseDetails(Filter entity, RESTFilterV1 dataObject) {
        if (dataObject.hasParameterSet(RESTFilterV1.NAME_NAME)) entity.setFilterName(dataObject.getName());
        if (dataObject.hasParameterSet(RESTFilterV1.DESCRIPTION_NAME)) entity.setFilterDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTFilterV1.TYPE_NAME))
            entity.setFilterClassType(RESTFilterTypeV1.getFilterTypeId(dataObject.getType()));
    }

    @Override
    public void doDeleteChildAction(final Filter entity, final RESTFilterV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTFilterTagV1) {
            final FilterTag dbEntity = entityManager.find(FilterTag.class, restEntity.getId());
            if (dbEntity == null) throw new BadRequestException("No FilterTag entity was found with the primary key " + restEntity.getId());

            entity.removeFilterTag(dbEntity);
            entityManager.remove(dbEntity);
        } else if (restEntity instanceof RESTFilterCategoryV1) {
            final FilterCategory dbEntity = entityManager.find(FilterCategory.class, restEntity.getId());
            if (dbEntity == null)
                throw new BadRequestException("No FilterCategory entity was found with the primary key " + restEntity.getId());

            entity.removeFilterCategory(dbEntity);
            entityManager.remove(dbEntity);
        } else if (restEntity instanceof RESTFilterFieldV1) {
            final FilterField dbEntity = entityManager.find(FilterField.class, restEntity.getId());
            if (dbEntity == null)
                throw new BadRequestException("No FilterField entity was found with the primary key " + restEntity.getId());

            entity.removeFilterField(dbEntity);
            entityManager.remove(dbEntity);
        } else if (restEntity instanceof RESTFilterLocaleV1) {
            final FilterLocale dbEntity = entityManager.find(FilterLocale.class, restEntity.getId());
            if (dbEntity == null)
                throw new BadRequestException("No FilterLocale entity was found with the primary key " + restEntity.getId());

            entity.removeFilterLocale(dbEntity);
            entityManager.remove(dbEntity);
        }
    }

    @Override
    protected AuditedEntity doCreateChildAction(final Filter entity, final RESTFilterV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();
        final AuditedEntity dbEntity;

        if (restEntity instanceof RESTFilterTagV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            entity.addFilterTag((FilterTag) dbEntity);
        } else if (restEntity instanceof RESTFilterCategoryV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            entity.addFilterCategory((FilterCategory) dbEntity);
        } else if (restEntity instanceof RESTFilterFieldV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            entity.addFilterField((FilterField) dbEntity);
        } else if (restEntity instanceof RESTFilterLocaleV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            entity.addFilterLocale((FilterLocale) dbEntity);
        } else {
            throw new IllegalArgumentException("Item is not a child of Filter");
        }

        return dbEntity;
    }

    @Override
    protected AuditedEntity getChildEntityForAction(final Filter entity, final RESTFilterV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        final AuditedEntity dbEntity;
        if (restEntity instanceof RESTFilterTagV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTFilterTagV1) restEntity, FilterTag.class);
            if (dbEntity == null) {
                throw new BadRequestException("No FilterTag entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getFilterTags().contains(dbEntity)) {
                throw new BadRequestException(
                    "No FilterTag entity was found with the primary key " + restEntity.getId() + " for Filter " + entity.getId());
            }
        } else if (restEntity instanceof RESTFilterCategoryV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTFilterCategoryV1) restEntity, FilterCategory.class);
            if (dbEntity == null) {
                throw new BadRequestException("No FilterCategory entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getFilterCategories().contains(dbEntity)) {
                throw new BadRequestException(
                        "No FilterCategory entity was found with the primary key " + restEntity.getId() + " for Filter " + entity.getId());
            }
        } else if (restEntity instanceof RESTFilterFieldV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTFilterFieldV1) restEntity, FilterField.class);
            if (dbEntity == null) {
                throw new BadRequestException("No FilterField entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getFilterFields().contains(dbEntity)) {
                throw new BadRequestException(
                        "No FilterField entity was found with the primary key " + restEntity.getId() + " for Filter " + entity.getId());
            }
        } else if (restEntity instanceof RESTFilterLocaleV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTFilterLocaleV1) restEntity, FilterLocale.class);
            if (dbEntity == null) {
                throw new BadRequestException("No FilterLocale entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getFilterLocales().contains(dbEntity)) {
                throw new BadRequestException(
                        "No FilterLocale entity was found with the primary key " + restEntity.getId() + " for Filter " + entity.getId());
            }
        } else {
            throw new IllegalArgumentException("Item is not a child of Filter");
        }

        return dbEntity;
    }

    @Override
    protected Class<Filter> getDatabaseClass() {
        return Filter.class;
    }
}
