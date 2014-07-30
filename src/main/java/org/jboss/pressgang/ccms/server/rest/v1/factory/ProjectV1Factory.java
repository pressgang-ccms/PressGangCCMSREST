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

import org.jboss.pressgang.ccms.model.Project;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTProjectCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTProjectCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTProjectV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class ProjectV1Factory extends RESTEntityFactory<RESTProjectV1, Project, RESTProjectCollectionV1, RESTProjectCollectionItemV1> {
    @Inject
    protected TagV1Factory tagFactory;

    @Override
    public RESTProjectV1 createRESTEntityFromDBEntityInternal(final Project entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTProjectV1 retValue = new RESTProjectV1();

        retValue.setId(entity.getProjectId());
        retValue.setDescription(entity.getProjectDescription());
        retValue.setName(entity.getProjectName());

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTv1Constants.TAGS_EXPANSION_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTProjectCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // TAGS
        if (expand != null && expand.contains(RESTProjectV1.TAGS_NAME)) {
            retValue.setTags(
                    RESTEntityCollectionFactory.create(RESTTagCollectionV1.class, tagFactory, entity.getTags(), RESTProjectV1.TAGS_NAME,
                            dataType, expand, baseUrl, revision, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.PROJECT_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(RESTChangeAction<RESTProjectV1> parent, RESTProjectV1 dataObject) {
        if (dataObject.hasParameterSet(RESTProjectV1.TAGS_NAME)
                && dataObject.getTags() != null
                && dataObject.getTags().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getTags(), tagFactory);
        }
    }

    @Override
    public void syncBaseDetails(final Project entity, final RESTProjectV1 dataObject) {
        if (dataObject.hasParameterSet(RESTProjectV1.DESCRIPTION_NAME)) entity.setProjectDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTProjectV1.NAME_NAME)) entity.setProjectName(dataObject.getName());
    }

    @Override
    protected void doDeleteChildAction(final Project entity, final RESTProjectV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTTagV1) {
            final Tag dbEntity = entityManager.find(Tag.class, restEntity.getId());
            if (dbEntity == null) throw new BadRequestException(
                    "No Tag entity was found with the primary key " + restEntity.getId());

            entity.removeRelationshipTo(dbEntity.getTagId());
        }
    }

    @Override
    protected AuditedEntity doCreateChildAction(final Project entity, final RESTProjectV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();
        final AuditedEntity dbEntity;

        if (restEntity instanceof RESTTagV1) {
            dbEntity = entityManager.find(Tag.class, restEntity.getId());
            if (dbEntity == null) {
                throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());
            }

            entity.addRelationshipTo((Tag) dbEntity);
        } else {
            throw new IllegalArgumentException("Item is not a child of Project");
        }

        return dbEntity;
    }

    @Override
    protected AuditedEntity getChildEntityForAction(final Project entity, final RESTProjectV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        final AuditedEntity dbEntity;
        if (restEntity instanceof RESTTagV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTTagV1) restEntity, Tag.class);
            if (dbEntity == null) {
                throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getTags().contains(dbEntity)) {
                throw new BadRequestException(
                        "No Tag entity was found with the primary key " + restEntity.getId() + " for Project " + entity.getId());
            }
        } else {
            throw new IllegalArgumentException("Item is not a child of Project");
        }

        return dbEntity;
    }

    @Override
    protected Class<Project> getDatabaseClass() {
        return Project.class;
    }
}
