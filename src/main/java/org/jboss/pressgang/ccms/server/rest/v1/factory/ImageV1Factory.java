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

import org.jboss.pressgang.ccms.model.ImageFile;
import org.jboss.pressgang.ccms.model.LanguageImage;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTImageCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTLanguageImageCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTImageCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTImageV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTLanguageImageV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class ImageV1Factory extends RESTEntityFactory<RESTImageV1, ImageFile, RESTImageCollectionV1, RESTImageCollectionItemV1> {
    @Inject
    protected LanguageImageV1Factory languageImageFactory;

    @Override
    public RESTImageV1 createRESTEntityFromDBEntityInternal(final ImageFile entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTImageV1 retValue = new RESTImageV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTImageV1.LANGUAGEIMAGES_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) {
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        }
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getImageFileId());
        retValue.setDescription(entity.getDescription());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTImageCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // LANGUAGE IMAGES
        if (expand != null && expand.contains(RESTImageV1.LANGUAGEIMAGES_NAME)) {
            retValue.setLanguageImages_OTM(
                    RESTEntityCollectionFactory.create(RESTLanguageImageCollectionV1.class, languageImageFactory,
                            entity.getLanguageImagesArray(), RESTImageV1.LANGUAGEIMAGES_NAME, dataType, expand, baseUrl, revision, false,
                            entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.IMAGE_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTImageV1> parent, final RESTImageV1 dataObject) {
        if (dataObject.hasParameterSet(RESTImageV1.LANGUAGEIMAGES_NAME)
                && dataObject.getLanguageImages_OTM() != null
                && dataObject.getLanguageImages_OTM().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getLanguageImages_OTM(), languageImageFactory);
        }
    }

    @Override
    public void syncBaseDetails(final ImageFile entity, final RESTImageV1 dataObject) {
        if (dataObject.hasParameterSet(RESTImageV1.DESCRIPTION_NAME)) entity.setDescription(dataObject.getDescription());
    }

    @Override
    protected void doDeleteChildAction(final ImageFile entity, final RESTImageV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTLanguageImageV1) {
            final LanguageImage dbEntity = entityManager.find(LanguageImage.class, restEntity.getId());
            if (dbEntity == null) throw new BadRequestException(
                    "No LanguageImage entity was found with the primary key " + restEntity.getId());

            entity.removeLanguageImage(dbEntity);
            entityManager.remove(dbEntity);
        }
    }

    @Override
    protected AuditedEntity doCreateChildAction(final ImageFile entity, final RESTImageV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();
        final AuditedEntity dbEntity;

        if (restEntity instanceof RESTLanguageImageV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            entity.addLanguageImage((LanguageImage) dbEntity);
        } else {
            throw new IllegalArgumentException("Item is not a child of Image");
        }

        return dbEntity;
    }

    @Override
    protected AuditedEntity getChildEntityForAction(final ImageFile entity, final RESTImageV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        final AuditedEntity dbEntity;
        if (restEntity instanceof RESTLanguageImageV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTLanguageImageV1) restEntity, LanguageImage.class);
            if (dbEntity == null) {
                throw new BadRequestException("No LanguageImage entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getLanguageImages().contains(dbEntity)) {
                throw new BadRequestException(
                        "No LanguageImage entity was found with the primary key " + restEntity.getId() + " for Image " + entity.getId());
            }
        } else {
            throw new IllegalArgumentException("Item is not a child of Image");
        }

        return dbEntity;
    }

    @Override
    protected Class<ImageFile> getDatabaseClass() {
        return ImageFile.class;
    }
}
