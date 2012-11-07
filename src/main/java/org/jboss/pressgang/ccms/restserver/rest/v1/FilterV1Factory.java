package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterFieldCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterLocaleCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTFilterCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTFilterCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTFilterFieldCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTFilterLocaleCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTFilterTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterFieldV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterLocaleV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.entity.Filter;
import org.jboss.pressgang.ccms.restserver.entity.FilterCategory;
import org.jboss.pressgang.ccms.restserver.entity.FilterField;
import org.jboss.pressgang.ccms.restserver.entity.FilterLocale;
import org.jboss.pressgang.ccms.restserver.entity.FilterTag;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.BaseRESTv1;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;


public class FilterV1Factory extends
        RESTDataObjectFactory<RESTFilterV1, Filter, RESTFilterCollectionV1, RESTFilterCollectionItemV1> {
    public FilterV1Factory() {
        super(Filter.class);
    }

    @Override
    public RESTFilterV1 createRESTEntityFromDBEntity(final Filter entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter filterTag can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTFilterV1 retValue = new RESTFilterV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTFilterV1.FILTER_CATEGORIES_NAME);
        expandOptions.add(RESTFilterV1.FILTER_LOCALES_NAME);
        expandOptions.add(RESTFilterV1.FILTER_TAGS_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getFilterId());
        retValue.setName(entity.getFilterName());

        // REVISIONS
        if (revision == null) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTFilterV1, Filter, RESTFilterCollectionV1, RESTFilterCollectionItemV1>()
                    .create(RESTFilterCollectionV1.class, new FilterV1Factory(), entity, entity.getRevisions(entityManager),
                            RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
        }

        // FILTER TAGS
        retValue.setFilterTags_OTM(new RESTDataObjectCollectionFactory<RESTFilterTagV1, FilterTag, RESTFilterTagCollectionV1, RESTFilterTagCollectionItemV1>()
                .create(RESTFilterTagCollectionV1.class, new FilterTagV1Factory(), entity.getFilterTagsList(),
                        RESTFilterV1.FILTER_TAGS_NAME, dataType, expand, baseUrl, false, entityManager));
        
        // FILTER LOCALES
        retValue.setFilterLocales_OTM(new RESTDataObjectCollectionFactory<RESTFilterLocaleV1, FilterLocale, RESTFilterLocaleCollectionV1, RESTFilterLocaleCollectionItemV1>()
                .create(RESTFilterLocaleCollectionV1.class, new FilterLocaleV1Factory(), entity.getFilterLocalesList(),
                        RESTFilterV1.FILTER_LOCALES_NAME, dataType, expand, baseUrl, false, entityManager));
        
        // FILTER CATEGORIES
        retValue.setFilterCategories_OTM(new RESTDataObjectCollectionFactory<RESTFilterCategoryV1, FilterCategory, RESTFilterCategoryCollectionV1, RESTFilterCategoryCollectionItemV1>()
                .create(RESTFilterCategoryCollectionV1.class, new FilterCategoryV1Factory(), entity.getFilterCategoriesList(),
                        RESTFilterV1.FILTER_CATEGORIES_NAME, dataType, expand, baseUrl, false, entityManager));
        
        // FILTER FIELDS
        retValue.setFilterFields_OTM(new RESTDataObjectCollectionFactory<RESTFilterFieldV1, FilterField, RESTFilterFieldCollectionV1, RESTFilterFieldCollectionItemV1>()
                .create(RESTFilterFieldCollectionV1.class, new FilterFieldV1Factory(), entity.getFilterFieldsList(),
                        RESTFilterV1.FILTER_FIELDS_NAME, dataType, expand, baseUrl, false, entityManager));

        retValue.setLinks(baseUrl, BaseRESTv1.FILTER_URL_NAME, dataType, retValue.getId());
        retValue.setLogDetails(new LogDetailsV1Factory().create(entity, revision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand,
                dataType, baseUrl, entityManager));

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final Filter entity, final RESTFilterV1 dataObject)
            throws InvalidParameterException {
        if (dataObject.hasParameterSet(RESTFilterV1.NAME_NAME))
            entity.setFilterName(dataObject.getName());
        if (dataObject.hasParameterSet(RESTFilterV1.DESCRIPTION_NAME))
            entity.setFilterDescription(dataObject.getDescription());

        entityManager.persist(entity);

        /* One To Many - Add will create a child entity */
        if (dataObject.hasParameterSet(RESTFilterV1.FILTER_TAGS_NAME) && dataObject.getFilterTags_OTM() != null
                && dataObject.getFilterTags_OTM().getItems() != null) {
            dataObject.getFilterTags_OTM().removeInvalidChangeItemRequests();

            for (final RESTFilterTagCollectionItemV1 restEntityItem : dataObject.getFilterTags_OTM().getItems()) {
                final RESTFilterTagV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final FilterTag dbEntity = entityManager.find(FilterTag.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No FilterTag entity was found with the primary key "
                                + restEntity.getId());

                    entity.getFilterTags().remove(dbEntity);
                    entityManager.remove(dbEntity);
                } else if (restEntityItem.returnIsAddItem()) {
                    final FilterTag dbEntity = new FilterTag();
                    dbEntity.setFilter(entity);
                    new FilterTagV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                    entity.getFilterTags().add(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final FilterTag dbEntity = entityManager.find(FilterTag.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No FilterTag entity was found with the primary key "
                                + restEntity.getId());

                    new FilterTagV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                }
            }
        }

        /* One To Many - Add will create a child entity */
        if (dataObject.hasParameterSet(RESTFilterV1.FILTER_LOCALES_NAME) && dataObject.getFilterLocales_OTM() != null
                && dataObject.getFilterLocales_OTM().getItems() != null) {
            dataObject.getFilterLocales_OTM().removeInvalidChangeItemRequests();

            for (final RESTFilterLocaleCollectionItemV1 restEntityItem : dataObject.getFilterLocales_OTM().getItems()) {
                final RESTFilterLocaleV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final FilterLocale dbEntity = entityManager.find(FilterLocale.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No FilterLocale entity was found with the primary key "
                                + restEntity.getId());

                    entity.getFilterLocales().remove(dbEntity);
                    entityManager.remove(dbEntity);
                } else if (restEntityItem.returnIsAddItem()) {
                    final FilterLocale dbEntity = new FilterLocale();
                    dbEntity.setFilter(entity);
                    new FilterLocaleV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                    entity.getFilterLocales().add(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final FilterLocale dbEntity = entityManager.find(FilterLocale.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No FilterLocale entity was found with the primary key "
                                + restEntity.getId());

                    new FilterLocaleV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                }
            }
        }

        /* One To Many - Add will create a child entity */
        if (dataObject.hasParameterSet(RESTFilterV1.FILTER_CATEGORIES_NAME) && dataObject.getFilterCategories_OTM() != null
                && dataObject.getFilterCategories_OTM().getItems() != null) {
            dataObject.getFilterCategories_OTM().removeInvalidChangeItemRequests();

            for (final RESTFilterCategoryCollectionItemV1 restEntityItem : dataObject.getFilterCategories_OTM().getItems()) {
                final RESTFilterCategoryV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final FilterCategory dbEntity = entityManager.find(FilterCategory.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No FilterCategory entity was found with the primary key "
                                + restEntity.getId());

                    entity.getFilterCategories().remove(dbEntity);
                    entityManager.remove(dbEntity);
                } else if (restEntityItem.returnIsAddItem()) {
                    final FilterCategory dbEntity = new FilterCategory();
                    dbEntity.setFilter(entity);
                    new FilterCategoryV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                    entity.getFilterCategories().add(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final FilterCategory dbEntity = entityManager.find(FilterCategory.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No FilterCategory entity was found with the primary key "
                                + restEntity.getId());

                    new FilterCategoryV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                }
            }
        }

        /* One To Many - Add will create a child entity */
        if (dataObject.hasParameterSet(RESTFilterV1.FILTER_FIELDS_NAME) && dataObject.getFilterFields_OTM() != null
                && dataObject.getFilterFields_OTM().getItems() != null) {
            dataObject.getFilterFields_OTM().removeInvalidChangeItemRequests();

            for (final RESTFilterFieldCollectionItemV1 restEntityItem : dataObject.getFilterFields_OTM().getItems()) {
                final RESTFilterFieldV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final FilterField dbEntity = entityManager.find(FilterField.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No FilterField entity was found with the primary key "
                                + restEntity.getId());

                    entity.getFilterCategories().remove(dbEntity);
                    entityManager.remove(dbEntity);
                } else if (restEntityItem.returnIsAddItem()) {
                    final FilterField dbEntity = new FilterField();
                    dbEntity.setFilter(entity);
                    new FilterFieldV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                    entity.getFilterFields().add(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final FilterField dbEntity = entityManager.find(FilterField.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No FilterField entity was found with the primary key "
                                + restEntity.getId());

                    new FilterFieldV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                }
            }
        }
    }

}
