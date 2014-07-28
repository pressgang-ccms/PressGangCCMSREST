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

import org.jboss.pressgang.ccms.model.ImageFile;
import org.jboss.pressgang.ccms.model.LanguageImage;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTImageCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTLanguageImageCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTImageCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTLanguageImageCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTImageV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTLanguageImageV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
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
    public void syncDBEntityWithRESTEntityFirstPass(final ImageFile entity, final RESTImageV1 dataObject) {
        if (dataObject.hasParameterSet(RESTImageV1.DESCRIPTION_NAME)) entity.setDescription(dataObject.getDescription());

        /* One To Many - Add will create a child entity */
        if (dataObject.hasParameterSet(
                RESTImageV1.LANGUAGEIMAGES_NAME) && dataObject.getLanguageImages_OTM() != null && dataObject.getLanguageImages_OTM()
                .getItems() != null) {
            dataObject.getLanguageImages_OTM().removeInvalidChangeItemRequests();

            for (final RESTLanguageImageCollectionItemV1 restEntityItem : dataObject.getLanguageImages_OTM().getItems()) {
                final RESTLanguageImageV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem()) {
                    final LanguageImage dbEntity = languageImageFactory.createDBEntityFromRESTEntity(restEntity);
                    entity.addLanguageImage(dbEntity);
                } else if (restEntityItem.returnIsRemoveItem()) {
                    final LanguageImage dbEntity = entityManager.find(LanguageImage.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new BadRequestException("No LanguageImage entity was found with the primary key " + restEntity.getId());

                    entity.removeLanguageImage(dbEntity);
                    entityManager.remove(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final LanguageImage dbEntity = entityManager.find(LanguageImage.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new BadRequestException("No LanguageImage entity was found with the primary key " + restEntity.getId());
                    if (!entity.getLanguageImages().contains(dbEntity)) throw new BadRequestException(
                            "No LanguageImage entity was found with the primary key " + restEntity.getId() + " for Image " + entity.getId
                                    ());

                    languageImageFactory.updateDBEntityFromRESTEntity(dbEntity, restEntity);
                }
            }
        }
    }

    @Override
    public void syncDBEntityWithRESTEntitySecondPass(ImageFile entity, RESTImageV1 dataObject) {
        // One To Many - Do the second pass on the add or update items
        if (dataObject.hasParameterSet(
                RESTImageV1.LANGUAGEIMAGES_NAME) && dataObject.getLanguageImages_OTM() != null && dataObject.getLanguageImages_OTM()
                .getItems() != null) {
            dataObject.getLanguageImages_OTM().removeInvalidChangeItemRequests();

            for (final RESTLanguageImageCollectionItemV1 restEntityItem : dataObject.getLanguageImages_OTM().getItems()) {
                final RESTLanguageImageV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsUpdateItem()) {
                    final LanguageImage dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, restEntity,
                            LanguageImage.class);
                    if (dbEntity == null)
                        throw new BadRequestException("No LanguageImage entity was found with the primary key " + restEntity.getId());

                    languageImageFactory.syncDBEntityWithRESTEntitySecondPass(dbEntity, restEntity);
                }
            }
        }
    }

    @Override
    protected Class<ImageFile> getDatabaseClass() {
        return ImageFile.class;
    }
}
