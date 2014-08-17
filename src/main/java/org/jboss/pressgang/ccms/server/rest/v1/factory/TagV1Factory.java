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
import org.jboss.pressgang.ccms.model.Project;
import org.jboss.pressgang.ccms.model.PropertyTag;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.model.TagToCategory;
import org.jboss.pressgang.ccms.model.TagToPropertyTag;
import org.jboss.pressgang.ccms.model.base.PressGangEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTProjectCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTCategoryInTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTProjectV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseAuditedEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTAssignedPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTCategoryInTagV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class TagV1Factory extends RESTEntityFactory<RESTTagV1, Tag, RESTTagCollectionV1, RESTTagCollectionItemV1> {
    @Inject
    protected CategoryInTagV1Factory categoryInTagFactory;
    @Inject
    protected TagPropertyTagV1Factory tagPropertyTagFactory;
    @Inject
    protected ProjectV1Factory projectFactory;

    @Override
    public RESTTagV1 createRESTEntityFromDBEntityInternal(final Tag entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTagV1 retValue = new RESTTagV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTTagV1.CATEGORIES_NAME);
        expandOptions.add(RESTTagV1.PARENT_TAGS_NAME);
        expandOptions.add(RESTTagV1.CHILD_TAGS_NAME);
        expandOptions.add(RESTTagV1.PROJECTS_NAME);
        expandOptions.add(RESTTagV1.PROPERTIES_NAME);
        expandOptions.add(RESTBaseAuditedEntityV1.LOG_DETAILS_NAME);

        if (revision == null) expandOptions.add(RESTBaseAuditedEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        /* Set simple properties */
        retValue.setId(entity.getTagId());
        retValue.setName(entity.getTagName());
        retValue.setDescription(entity.getTagDescription());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTTopicV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTTagCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseAuditedEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // CATEGORIES
        if (expand != null && expand.contains(RESTTagV1.CATEGORIES_NAME)) {
            retValue.setCategories(RESTEntityCollectionFactory.create(RESTCategoryInTagCollectionV1.class, categoryInTagFactory,
                    entity.getTagToCategoriesList(), RESTTagV1.CATEGORIES_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        // PARENT TAGS
        if (expand != null && expand.contains(RESTTagV1.PARENT_TAGS_NAME)) {
            retValue.setParentTags(
                    RESTEntityCollectionFactory.create(RESTTagCollectionV1.class, this, entity.getParentTags(), RESTTagV1.PARENT_TAGS_NAME,
                            dataType, expand, baseUrl, revision, entityManager));
        }

        // CHILD TAGS
        if (expand != null && expand.contains(RESTTagV1.CHILD_TAGS_NAME)) {
            retValue.setChildTags(
                    RESTEntityCollectionFactory.create(RESTTagCollectionV1.class, this, entity.getChildTags(), RESTTagV1.CHILD_TAGS_NAME,
                            dataType, expand, baseUrl, revision, entityManager));
        }

        // PROPERTY TAGS
        if (expand != null && expand.contains(RESTTagV1.PROPERTIES_NAME)) {
            retValue.setProperties(RESTEntityCollectionFactory.create(RESTAssignedPropertyTagCollectionV1.class, tagPropertyTagFactory,
                    entity.getPropertyTagsList(), RESTTagV1.PROPERTIES_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        // PROJECTS
        if (expand != null && expand.contains(RESTTagV1.PROJECTS_NAME)) {
            retValue.setProjects(RESTEntityCollectionFactory.create(RESTProjectCollectionV1.class, projectFactory, entity.getProjects(),
                    RESTTagV1.PROJECTS_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.TAG_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTTagV1> parent, final RESTTagV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTagV1.PROPERTIES_NAME)
                && dataObject.getProperties() != null
                && dataObject.getProperties().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getProperties(), tagPropertyTagFactory);
        }

        if (dataObject.hasParameterSet(RESTTagV1.CATEGORIES_NAME)
                && dataObject.getCategories() != null
                && dataObject.getCategories().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getCategories(), categoryInTagFactory);
        }

        if (dataObject.hasParameterSet(RESTTagV1.PROJECTS_NAME)
                && dataObject.getProjects() != null
                && dataObject.getProjects().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getProjects(), projectFactory);
        }

        if (dataObject.hasParameterSet(RESTTagV1.CHILD_TAGS_NAME)
                && dataObject.getChildTags() != null
                && dataObject.getChildTags().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getChildTags(), this, RESTTagV1.CHILD_TAGS_NAME);
        }

        if (dataObject.hasParameterSet(RESTTagV1.PARENT_TAGS_NAME)
                && dataObject.getParentTags() != null
                && dataObject.getParentTags().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getParentTags(), this, RESTTagV1.PARENT_TAGS_NAME);
        }
    }

    @Override
    public void syncBaseDetails(final Tag entity, final RESTTagV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTagV1.DESCRIPTION_NAME)) entity.setTagDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTTagV1.NAME_NAME)) entity.setTagName(dataObject.getName());
    }

    @Override
    public void doDeleteChildAction(final Tag entity, final RESTTagV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTCategoryInTagV1) {
            final RESTCategoryInTagV1 categoryInTag = (RESTCategoryInTagV1) restEntity;
            if (categoryInTag.getRelationshipId() == null) {
                final Category dbEntity = entityManager.find(Category.class, categoryInTag.getId());
                if (dbEntity == null)
                    throw new BadRequestException("No Category entity was found with the primary key " + categoryInTag.getRelationshipId());

                dbEntity.removeTag(entity);
            } else {
                final TagToCategory dbEntity = entityManager.find(TagToCategory.class, categoryInTag.getRelationshipId());
                if (dbEntity == null)
                    throw new BadRequestException("No TagToCategory entity was found with the primary key " + categoryInTag.getRelationshipId());

                entity.removeCategory(dbEntity);
            }
        } else if (restEntity instanceof RESTAssignedPropertyTagV1) {
            final RESTAssignedPropertyTagV1 propertyTag = (RESTAssignedPropertyTagV1) restEntity;
            final TagToPropertyTag dbEntity = entityManager.find(TagToPropertyTag.class, propertyTag.getRelationshipId());
            if (dbEntity == null) throw new BadRequestException(
                    "No TagToPropertyTag entity was found with the primary key " + propertyTag.getRelationshipId());

            entity.removePropertyTag(dbEntity);
        } else if (restEntity instanceof RESTProjectV1) {
            final Project project = entityManager.find(Project.class, restEntity.getId());
            if (project == null) {
                throw new BadRequestException("No Project entity was found with the primary key " + restEntity.getId());
            }

            entity.removeProjectRelationship(project);
        } else if (restEntity instanceof RESTTagV1) {
            final Tag dbEntity = entityManager.find(Tag.class, restEntity.getId());
            if (dbEntity == null)
                throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());

            if (RESTTagV1.PARENT_TAGS_NAME.equals(action.getUniqueId())) {
                dbEntity.removeTagRelationship(entity);
            } else {
                entity.removeTagRelationship(dbEntity);
            }
        }
    }

    @Override
    protected PressGangEntity doCreateChildAction(final Tag entity, final RESTTagV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();
        final PressGangEntity dbEntity;

        if (restEntity instanceof RESTCategoryInTagV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            final Category categoryEntity = entityManager.find(Category.class, restEntity.getId());
            if (categoryEntity == null)
                throw new BadRequestException("No Category entity was found with the primary key " + restEntity.getId());

            final TagToCategory ttc = (TagToCategory) dbEntity;
            ttc.setCategory(categoryEntity);
            entity.addCategory(ttc);
        } else if (restEntity instanceof RESTAssignedPropertyTagV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            final PropertyTag propertyTag = entityManager.find(PropertyTag.class, restEntity.getId());
            if (propertyTag == null) {
                throw new BadRequestException("No PropertyTag entity was found with the primary key " + restEntity.getId());
            }

            final TagToPropertyTag tagToProp = (TagToPropertyTag) dbEntity;
            tagToProp.setPropertyTag(propertyTag);
            entity.addPropertyTag(tagToProp);
        } else if (restEntity instanceof RESTProjectV1) {
            dbEntity = entityManager.find(Project.class, restEntity.getId());
            if (dbEntity == null) {
                throw new BadRequestException("No Project entity was found with the primary key " + restEntity.getId());
            }

            entity.addProjectRelationship((Project) dbEntity);
        } else if (restEntity instanceof RESTTagV1) {
            dbEntity = entityManager.find(Tag.class, restEntity.getId());
            if (dbEntity == null) {
                throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());
            }

            if (RESTTagV1.PARENT_TAGS_NAME.equals(action.getUniqueId())) {
                ((Tag) dbEntity).addTagRelationship(entity);
            } else {
                entity.addTagRelationship((Tag) dbEntity);
            }
        } else {
            throw new IllegalArgumentException("Item is not a child of Tag");
        }

        return dbEntity;
    }

    @Override
    protected PressGangEntity getChildEntityForAction(final Tag entity, final RESTTagV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();

        final PressGangEntity dbEntity;
        if (restEntity instanceof RESTCategoryInTagV1) {
            final RESTCategoryInTagV1 categoryInTag = (RESTCategoryInTagV1) restEntity;
            dbEntity = entityManager.find(TagToCategory.class, categoryInTag.getRelationshipId());
            if (dbEntity == null) {
                throw new BadRequestException("No TagToCategory entity was found with the primary key " + categoryInTag.getRelationshipId());
            } else if (!entity.getTagToCategories().contains(dbEntity)) {
                throw new BadRequestException(
                        "No TagToCategory entity was found with the primary key " + categoryInTag.getRelationshipId() + " for Tag "
                                + entity.getId());
            }
        } else if (restEntity instanceof RESTAssignedPropertyTagV1) {
            final RESTAssignedPropertyTagV1 assignedPropertyTag = (RESTAssignedPropertyTagV1) restEntity;
            dbEntity = entityManager.find(TagToPropertyTag.class, assignedPropertyTag.getRelationshipId());
            if (dbEntity == null) {
                throw new BadRequestException("No TagToPropertyTag entity was found with the primary key " + assignedPropertyTag
                        .getRelationshipId());
            } else if (!entity.getTagToPropertyTags().contains(dbEntity)) {
                throw new BadRequestException(
                        "No TagToPropertyTag entity was found with the primary key " + assignedPropertyTag.getRelationshipId() +
                                " for ContentSpec " + entity.getId());
            }
        } else if (restEntity instanceof RESTProjectV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTProjectV1) restEntity, Project.class);
            if (dbEntity == null) {
                throw new BadRequestException("No Project entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getTagToPropertyTags().contains(dbEntity)) {
                throw new BadRequestException(
                        "No Project entity was found with the primary key " + restEntity.getId() + " for Tag " + entity.getId());
            }
        } else if (restEntity instanceof RESTTagV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTTagV1) restEntity, Tag.class);
            if (dbEntity == null) {
                throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());
            } else {
                if (RESTTagV1.PARENT_TAGS_NAME.equals(action.getUniqueId()) && !entity.getParentTags().contains(dbEntity)) {
                    throw new BadRequestException(
                            "No Tag entity was found with the primary key " + restEntity.getId() + " for Tag " + entity.getId());
                } else if (RESTTagV1.CHILD_TAGS_NAME.equals(action.getUniqueId()) && !entity.getChildTags().contains(dbEntity)) {
                    throw new BadRequestException(
                            "No Tag entity was found with the primary key " + restEntity.getId() + " for Tag " + entity.getId());
                }
            }
        } else {
            throw new IllegalArgumentException("Item is not a child of Tag");
        }

        return dbEntity;
    }

    @Override
    protected Class<Tag> getDatabaseClass() {
        return Tag.class;
    }
}
