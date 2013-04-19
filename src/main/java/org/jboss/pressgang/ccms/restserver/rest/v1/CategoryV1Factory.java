package org.jboss.pressgang.ccms.restserver.rest.v1;

import javax.persistence.EntityManager;
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
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.restserver.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

class CategoryV1Factory extends RESTDataObjectFactory<RESTCategoryV1, Category, RESTCategoryCollectionV1, RESTCategoryCollectionItemV1> {
    CategoryV1Factory() {
        super(Category.class);
    }

    @Override
    public RESTCategoryV1 createRESTEntityFromDBEntityInternal(final Category entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager) {
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
            retValue.setRevisions(
                    new RESTDataObjectCollectionFactory<RESTCategoryV1, Category, RESTCategoryCollectionV1,
                            RESTCategoryCollectionItemV1>().create(
                            RESTCategoryCollectionV1.class, new CategoryV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        // TAGS
        if (expand != null && expand.contains(RESTCategoryV1.TAGS_NAME)) {
            retValue.setTags(
                    new RESTDataObjectCollectionFactory<RESTTagInCategoryV1, TagToCategory, RESTTagInCategoryCollectionV1,
                            RESTTagInCategoryCollectionItemV1>().create(
                            RESTTagInCategoryCollectionV1.class, new TagInCategoryV1Factory(), entity.getTagToCategoriesArray(),
                            RESTCategoryV1.TAGS_NAME, dataType, expand, baseUrl, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.CATEGORY_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final Category entity, final RESTCategoryV1 dataObject) {
        if (dataObject.hasParameterSet(RESTCategoryV1.DESCRIPTION_NAME)) entity.setCategoryDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTCategoryV1.MUTUALLYEXCLUSIVE_NAME))
            entity.setMutuallyExclusive(dataObject.getMutuallyExclusive());
        if (dataObject.hasParameterSet(RESTCategoryV1.NAME_NAME)) entity.setCategoryName(dataObject.getName());
        if (dataObject.hasParameterSet(RESTCategoryV1.SORT_NAME)) entity.setCategorySort(dataObject.getSort());

        entityManager.persist(entity);

        /* Many To Many - Add will create a mapping */
        if (dataObject.hasParameterSet(
                RESTCategoryV1.TAGS_NAME) && dataObject.getTags() != null && dataObject.getTags().getItems() != null) {
            dataObject.getTags().removeInvalidChangeItemRequests();

            for (final RESTTagInCategoryCollectionItemV1 restEntityItem : dataObject.getTags().getItems()) {
                final RESTTagInCategoryV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsRemoveItem()) {
                    final Tag dbEntity = entityManager.find(Tag.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());

                    if (restEntityItem.returnIsAddItem()) {
                        if (restEntity.hasParameterSet(RESTCategoryInTagV1.RELATIONSHIP_SORT_NAME))
                            entity.addTagRelationship(dbEntity, restEntity.getRelationshipSort());
                        else entity.addTagRelationship(dbEntity);
                    } else if (restEntityItem.returnIsRemoveItem()) {
                        entity.removeTagRelationship(dbEntity);
                    }
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final TagToCategory dbEntity = entityManager.find(TagToCategory.class, restEntity.getRelationshipId());
                    if (dbEntity == null) throw new BadRequestException(
                            "No TagToCategory entity was found with the primary key " + restEntity.getRelationshipId());

                    new TagInCategoryV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                }
            }
        }
    }
}
