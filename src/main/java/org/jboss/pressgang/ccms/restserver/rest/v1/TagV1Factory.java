package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.rest.v1.collections.RESTProjectCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTAssignedPropertyTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTCategoryInTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTProjectCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTCategoryInTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTProjectV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTAssignedPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTCategoryInTagV1;
import org.jboss.pressgang.ccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.entity.Category;
import org.jboss.pressgang.ccms.restserver.entity.Project;
import org.jboss.pressgang.ccms.restserver.entity.PropertyTag;
import org.jboss.pressgang.ccms.restserver.entity.Tag;
import org.jboss.pressgang.ccms.restserver.entity.TagToCategory;
import org.jboss.pressgang.ccms.restserver.entity.TagToPropertyTag;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.BaseRESTv1;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;


public class TagV1Factory extends RESTDataObjectFactory<RESTTagV1, Tag, RESTTagCollectionV1, RESTTagCollectionItemV1> {
    TagV1Factory() {
        super(Tag.class);
    }

    @Override
    public RESTTagV1 createRESTEntityFromDBEntity(final Tag entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter topic can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTagV1 retValue = new RESTTagV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTTagV1.CATEGORIES_NAME);
        expandOptions.add(RESTTagV1.PARENT_TAGS_NAME);
        expandOptions.add(RESTTagV1.CHILD_TAGS_NAME);
        expandOptions.add(RESTTagV1.PROJECTS_NAME);
        expandOptions.add(RESTTagV1.PROPERTIES_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);

        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        /* Set simple properties */
        retValue.setId(entity.getTagId());
        retValue.setName(entity.getTagName());
        retValue.setDescription(entity.getTagDescription());

        // REVISIONS
        if (revision == null) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTTagV1, Tag, RESTTagCollectionV1, RESTTagCollectionItemV1>()
                    .create(RESTTagCollectionV1.class, new TagV1Factory(), entity, entity.getRevisions(entityManager),
                            RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
        }
        
        // CATEGORIES
        retValue.setCategories(new RESTDataObjectCollectionFactory<RESTCategoryInTagV1, TagToCategory, RESTCategoryInTagCollectionV1, RESTCategoryInTagCollectionItemV1>()
                .create(RESTCategoryInTagCollectionV1.class, new CategoryInTagV1Factory(), entity.getTagToCategoriesArray(),
                        RESTTagV1.CATEGORIES_NAME, dataType, expand, baseUrl, entityManager));
        
        // PARENT TAGS
        retValue.setParentTags(new RESTDataObjectCollectionFactory<RESTTagV1, Tag, RESTTagCollectionV1, RESTTagCollectionItemV1>()
                .create(RESTTagCollectionV1.class, new TagV1Factory(), entity.getParentTags(), RESTTagV1.PARENT_TAGS_NAME,
                        dataType, expand, baseUrl, entityManager));
        
        // CHILD TAGS
        retValue.setChildTags(new RESTDataObjectCollectionFactory<RESTTagV1, Tag, RESTTagCollectionV1, RESTTagCollectionItemV1>()
                .create(RESTTagCollectionV1.class, new TagV1Factory(), entity.getChildTags(), RESTTagV1.CHILD_TAGS_NAME,
                        dataType, expand, baseUrl, entityManager));
        
        // PROPERTY TAGS
        retValue.setProperties(new RESTDataObjectCollectionFactory<RESTAssignedPropertyTagV1, TagToPropertyTag, RESTAssignedPropertyTagCollectionV1, RESTAssignedPropertyTagCollectionItemV1>()
                .create(RESTAssignedPropertyTagCollectionV1.class, new TagPropertyTagV1Factory(),
                        entity.getTagToPropertyTagsArray(), RESTTagV1.PROPERTIES_NAME, dataType, expand, baseUrl, entityManager));
        
        // PROJECTS
        retValue.setProjects(new RESTDataObjectCollectionFactory<RESTProjectV1, Project, RESTProjectCollectionV1, RESTProjectCollectionItemV1>()
                .create(RESTProjectCollectionV1.class, new ProjectV1Factory(), entity.getProjects(), RESTTagV1.PROJECTS_NAME,
                        dataType, expand, baseUrl, entityManager));

        retValue.setLinks(baseUrl, BaseRESTv1.TAG_URL_NAME, dataType, retValue.getId());
        retValue.setLogDetails(new LogDetailsV1Factory().create(entity, revision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand,
                dataType, baseUrl, entityManager));

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final Tag entity, final RESTTagV1 dataObject)
            throws InvalidParameterException {
        if (dataObject.hasParameterSet(RESTTagV1.DESCRIPTION_NAME))
            entity.setTagDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTTagV1.NAME_NAME))
            entity.setTagName(dataObject.getName());

        entityManager.persist(entity);

        if (dataObject.hasParameterSet(RESTTagV1.CATEGORIES_NAME) && dataObject.getCategories() != null
                && dataObject.getCategories().getItems() != null) {
            dataObject.getCategories().removeInvalidChangeItemRequests();

            for (final RESTCategoryInTagCollectionItemV1 restEntityItem : dataObject.getCategories().getItems()) {
                final RESTCategoryInTagV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsRemoveItem()) {
                    final Category dbEntity = entityManager.find(Category.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No Category entity was found with the primary key "
                                + restEntity.getId());

                    if (restEntityItem.returnIsAddItem()) {
                        if (restEntity.hasParameterSet(RESTCategoryV1.SORT_NAME))
                            dbEntity.addTagRelationship(entity, restEntity.getSort());
                        else
                            dbEntity.addTagRelationship(entity);
                    } else if (restEntityItem.returnIsRemoveItem()) {
                        dbEntity.removeTagRelationship(entity);
                    }
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final TagToCategory dbEntity = entityManager.find(TagToCategory.class, restEntity.getRelationshipId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No TagToCategory entity was found with the primary key "
                                + restEntity.getRelationshipId());

                    new CategoryInTagV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                }
            }
        }

        if (dataObject.hasParameterSet(RESTTagV1.PROJECTS_NAME) && dataObject.getProjects() != null
                && dataObject.getProjects().getItems() != null) {
            dataObject.getProjects().removeInvalidChangeItemRequests();

            for (final RESTProjectCollectionItemV1 restEntityItem : dataObject.getProjects().getItems()) {
                final RESTProjectV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsRemoveItem()) {
                    final Project dbEntity = entityManager.find(Project.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No Project entity was found with the primary key "
                                + restEntity.getId());

                    if (restEntityItem.returnIsAddItem()) {
                        dbEntity.addRelationshipTo(entity);
                    } else if (restEntityItem.returnIsRemoveItem()) {
                        dbEntity.removeRelationshipTo(entity.getId());
                    }
                }
            }
        }

        if (dataObject.hasParameterSet(RESTTagV1.CHILD_TAGS_NAME) && dataObject.getChildTags() != null
                && dataObject.getChildTags().getItems() != null) {
            dataObject.getChildTags().removeInvalidChangeItemRequests();

            for (final RESTTagCollectionItemV1 restEntityItem : dataObject.getChildTags().getItems()) {
                final RESTTagV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsRemoveItem()) {
                    final Tag dbEntity = entityManager.find(Tag.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No Tag entity was found with the primary key "
                                + restEntity.getId());

                    if (restEntityItem.returnIsAddItem()) {
                        entity.addTagRelationship(dbEntity);
                    } else if (restEntityItem.returnIsRemoveItem()) {
                        entity.removeTagRelationship(dbEntity);
                    }
                }
            }
        }

        if (dataObject.hasParameterSet(RESTTagV1.PARENT_TAGS_NAME) && dataObject.getParentTags() != null
                && dataObject.getParentTags().getItems() != null) {
            dataObject.getParentTags().removeInvalidChangeItemRequests();

            for (final RESTTagCollectionItemV1 restEntityItem : dataObject.getParentTags().getItems()) {
                final RESTTagV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsRemoveItem()) {
                    final Tag dbEntity = entityManager.find(Tag.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No Tag entity was found with the primary key "
                                + restEntity.getId());

                    if (restEntityItem.returnIsAddItem()) {
                        dbEntity.addTagRelationship(entity);
                    } else if (restEntityItem.returnIsRemoveItem()) {
                        dbEntity.removeTagRelationship(entity);
                    }
                }
            }
        }

        if (dataObject.hasParameterSet(RESTTagV1.PROPERTIES_NAME) && dataObject.getProperties() != null
                && dataObject.getProperties().getItems() != null) {
            dataObject.getProperties().removeInvalidChangeItemRequests();

            for (final RESTAssignedPropertyTagCollectionItemV1 restEntityItem : dataObject.getProperties().getItems()) {
                final RESTAssignedPropertyTagV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsRemoveItem()) {
                    final PropertyTag dbEntity = entityManager.find(PropertyTag.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No PropertyTag entity was found with the primary key "
                                + restEntity.getId());

                    if (restEntityItem.returnIsAddItem()) {
                        entity.addPropertyTag(dbEntity, restEntity.getValue());
                    } else if (restEntityItem.returnIsRemoveItem()) {
                        entity.removePropertyTag(dbEntity, restEntity.getValue());
                    }
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final TagToPropertyTag dbEntity = entityManager
                            .find(TagToPropertyTag.class, restEntity.getRelationshipId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No TagToPropertyTag entity was found with the primary key "
                                + restEntity.getRelationshipId());

                    new TagPropertyTagV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                }
            }
        }
    }

}
