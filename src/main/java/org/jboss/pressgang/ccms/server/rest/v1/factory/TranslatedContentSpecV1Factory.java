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

import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedCSNode;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedContentSpec;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTranslatedCSNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTranslatedContentSpecCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTTranslatedContentSpecCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTranslatedCSNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTranslatedContentSpecV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class TranslatedContentSpecV1Factory extends RESTEntityFactory<RESTTranslatedContentSpecV1, TranslatedContentSpec,
        RESTTranslatedContentSpecCollectionV1, RESTTranslatedContentSpecCollectionItemV1> {
    @Inject
    protected ContentSpecV1Factory contentSpecFactory;
    @Inject
    protected TranslatedCSNodeV1Factory translatedCSNodeFactory;

    @Override
    protected RESTTranslatedContentSpecV1 createRESTEntityFromDBEntityInternal(TranslatedContentSpec entity, String baseUrl,
            String dataType, ExpandDataTrunk expand, Number revision, boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTranslatedContentSpecV1 retValue = new RESTTranslatedContentSpecV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        expandOptions.add(RESTTranslatedContentSpecV1.CONTENT_SPEC_NAME);
        expandOptions.add(RESTTranslatedContentSpecV1.TRANSLATED_NODES_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setContentSpecId(entity.getContentSpecId());
        retValue.setContentSpecRevision(entity.getContentSpecRevision());

        // NODE
        if (expandParentReferences && expand != null && expand.contains(
                RESTTranslatedContentSpecV1.CONTENT_SPEC_NAME) && entity.getEnversContentSpec(entityManager) != null) {
            retValue.setContentSpec(
                    contentSpecFactory.createRESTEntityFromDBEntity(entity.getEnversContentSpec(entityManager), baseUrl, dataType,
                            expand.get(RESTTranslatedContentSpecV1.CONTENT_SPEC_NAME), entity.getContentSpecRevision(), true));
            retValue.getContentSpec().setRevision(entity.getContentSpecRevision());
        }

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTTranslatedContentSpecCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // TRANSLATED NODES
        if (expand != null && expand.contains(RESTTranslatedContentSpecV1.TRANSLATED_NODES_NAME)) {
            retValue.setTranslatedNodes_OTM(
                    RESTEntityCollectionFactory.create(RESTTranslatedCSNodeCollectionV1.class, translatedCSNodeFactory,
                            new ArrayList<TranslatedCSNode>(entity.getTranslatedCSNodes()),
                            RESTTranslatedContentSpecV1.TRANSLATED_NODES_NAME, dataType, expand, baseUrl, revision, false, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.TRANSLATED_CONTENT_SPEC_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTTranslatedContentSpecV1> parent,
            final RESTTranslatedContentSpecV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTranslatedContentSpecV1.TRANSLATED_NODES_NAME)
                && dataObject.getTranslatedNodes_OTM() != null
                && dataObject.getTranslatedNodes_OTM().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getTranslatedNodes_OTM(), translatedCSNodeFactory);
        }
    }

    @Override
    public void syncBaseDetails(final TranslatedContentSpec entity, final RESTTranslatedContentSpecV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTranslatedContentSpecV1.CONTENT_SPEC_ID_NAME))
            entity.setContentSpecId(dataObject.getContentSpecId());
        if (dataObject.hasParameterSet(RESTTranslatedContentSpecV1.CONTENT_SPEC_REV_NAME))
            entity.setContentSpecRevision(dataObject.getContentSpecRevision());
    }

    @Override
    protected void doDeleteChildAction(final TranslatedContentSpec entity, final RESTTranslatedContentSpecV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTTranslatedCSNodeV1) {
            final TranslatedCSNode dbEntity = entityManager.find(TranslatedCSNode.class, restEntity.getId());
            if (dbEntity == null) throw new BadRequestException(
                    "No TranslatedCSNode entity was found with the primary key " + restEntity.getId());

            entity.removeTranslatedNode(dbEntity);
        }
    }

    @Override
    protected AuditedEntity doCreateChildAction(final TranslatedContentSpec entity, final RESTTranslatedContentSpecV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();
        final AuditedEntity dbEntity;

        if (restEntity instanceof RESTTranslatedCSNodeV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            entity.addTranslatedNode((TranslatedCSNode) dbEntity);
        } else {
            throw new IllegalArgumentException("Item is not a child of TranslatedContentSpec");
        }

        return dbEntity;
    }

    @Override
    protected AuditedEntity getChildEntityForAction(final TranslatedContentSpec entity, final RESTTranslatedContentSpecV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        final AuditedEntity dbEntity;
        if (restEntity instanceof RESTTranslatedCSNodeV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTTranslatedCSNodeV1) restEntity, TranslatedCSNode.class);
            if (dbEntity == null) {
                throw new BadRequestException("No TranslatedCSNode entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getTranslatedCSNodes().contains(dbEntity)) {
                throw new BadRequestException(
                        "No TranslatedCSNode entity was found with the primary key " + restEntity.getId() + " for TranslatedContentSpec "
                                + entity.getId());
            }
        } else {
            throw new IllegalArgumentException("Item is not a child of TranslatedContentSpec");
        }

        return dbEntity;
    }

    @Override
    protected Class<TranslatedContentSpec> getDatabaseClass() {
        return TranslatedContentSpec.class;
    }
}
