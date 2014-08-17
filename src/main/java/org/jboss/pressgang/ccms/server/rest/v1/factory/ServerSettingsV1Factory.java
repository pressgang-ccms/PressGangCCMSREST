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

import static com.google.common.base.Strings.isNullOrEmpty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.hibernate.Session;
import org.jboss.pressgang.ccms.model.Locale;
import org.jboss.pressgang.ccms.model.TranslationServer;
import org.jboss.pressgang.ccms.model.config.ApplicationConfig;
import org.jboss.pressgang.ccms.model.config.EntitiesConfig;
import org.jboss.pressgang.ccms.provider.exception.BadRequestException;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTLocaleCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTServerUndefinedEntityCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTServerUndefinedSettingCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTranslationServerExtendedCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTLocaleCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTServerUndefinedEntityCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTServerUndefinedSettingCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTranslationServerExtendedCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTServerEntitiesV1;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTServerSettingsV1;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTServerUndefinedEntityV1;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTServerUndefinedSettingV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTLocaleV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslationServerExtendedV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.CachedEntityLoader;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTElementCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTElementFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.utils.EntityManagerWrapper;
import org.jboss.resteasy.spi.InternalServerErrorException;

@ApplicationScoped
public class ServerSettingsV1Factory extends RESTElementFactory<RESTServerSettingsV1, ApplicationConfig> {
    private final EntitiesConfig entitiesConfig = EntitiesConfig.getInstance();

    @Inject
    private EntityManagerWrapper entityManager;
    @Inject
    private LocaleV1Factory localeFactory;
    @Inject
    private TranslationServerExtendedV1Factory translationServerFactory;
    @Inject
    private ServerUndefinedEntityV1Factory serverUndefinedEntityV1Factory;
    @Inject
    private ServerUndefinedSettingV1Factory serverUndefinedSettingV1Factory;
    @Inject
    private CachedEntityLoader cachedEntityLoader;

    @Override
    public RESTServerSettingsV1 createRESTEntityFromObject(final ApplicationConfig applicationConfig, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand) {
        final RESTServerSettingsV1 retValue = new RESTServerSettingsV1();

        final List<String> expands = new ArrayList<String>();
        expands.add(RESTServerSettingsV1.LOCALES_NAME);
        expands.add(RESTServerSettingsV1.TRANSLATION_SERVERS_NAME);
        retValue.setExpand(expands);

        // Settings
        retValue.setDocBuilderUrl(applicationConfig.getDocBuilderUrl());
        retValue.setUiUrl(applicationConfig.getUIUrl());
        retValue.setDocBookTemplateIds(applicationConfig.getDocBookTemplateStringConstantIds());
        retValue.setSeoCategoryIds(applicationConfig.getSEOCategoryIds());
        retValue.setReadOnly(applicationConfig.getReadOnly());
        retValue.setJmsUpdateFrequency(applicationConfig.getJmsUpdateFrequency());

        // Default Locale
        if (!isNullOrEmpty(applicationConfig.getDefaultLocale())) {
            final Session session = entityManager.unwrap(Session.class);
            final Locale locale = (Locale) session.bySimpleNaturalId(Locale.class).load(applicationConfig.getDefaultLocale());
            final ExpandDataTrunk childExpand = expand == null ? null: expand.get(RESTServerSettingsV1.DEFAULT_LOCALE_NAME);
            retValue.setDefaultLocale(localeFactory.createRESTEntityFromDBEntity(locale, baseUrl, dataType, childExpand));
        } else {
            retValue.setDefaultLocale(null);
        }

        // Undefined Settings
        retValue.setUndefinedSettings(
                RESTElementCollectionFactory.create(RESTServerUndefinedSettingCollectionV1.class, serverUndefinedSettingV1Factory,
                        applicationConfig.getUndefinedSettings(), RESTServerSettingsV1.UNDEFINED_SETTINGS_NAME, dataType, expand, baseUrl));

        // Entities
        final RESTServerEntitiesV1 entities = retValue.getEntities();

        // Tags
        entities.setAbstractTagId(entitiesConfig.getAbstractTagId());
        entities.setAuthorGroupTagId(entitiesConfig.getAuthorGroupTagId());
        entities.setContentSpecTagId(entitiesConfig.getContentSpecTagId());
        entities.setFrozenTagId(entitiesConfig.getFrozenTagId());
        entities.setInfoTagId(entitiesConfig.getInfoTagId());
        entities.setInternalOnlyTagId(entitiesConfig.getInternalOnlyTagId());
        entities.setLegalNoticeTagId(entitiesConfig.getLegalNoticeTagId());
        entities.setObsoleteTagId(entitiesConfig.getObsoleteTagId());
        entities.setReviewTagId(entitiesConfig.getReviewTagId());
        entities.setRevisionHistoryTagId(entitiesConfig.getRevisionHistoryTagId());
        entities.setTaskTagId(entitiesConfig.getTaskTagId());

        // Property Tags
        entities.setAddedByPropertyTagId(entitiesConfig.getAddedByPropertyTagId());
        entities.setBugLinksLastValidatedPropertyTagId(entitiesConfig.getBugLinksLastValidatedPropertyTagId());
        entities.setCspIdPropertyTagId(entitiesConfig.getCSPIDPropertyTagId());
        entities.setEmailPropertyTagId(entitiesConfig.getEmailPropertyTagId());
        entities.setFirstNamePropertyTagId(entitiesConfig.getFirstNamePropertyTagId());
        entities.setFixedUrlPropertyTagId(entitiesConfig.getFixedUrlPropertyTagId());
        entities.setOrgPropertyTagId(entitiesConfig.getOrganizationPropertyTagId());
        entities.setOrgDivisionPropertyTagId(entitiesConfig.getOrganizationDivisionPropertyTagId());
        entities.setOriginalFileNamePropertyTagId(entitiesConfig.getOriginalFileNamePropertyTagId());
        entities.setPressGangWebsitePropertyTagId(entitiesConfig.getPressGangWebsitePropertyTagId());
        entities.setReadOnlyPropertyTagId(entitiesConfig.getReadOnlyPropertyTagId());
        entities.setSurnamePropertyTagId(entitiesConfig.getSurnamePropertyTagId());
        entities.setTagStylePropertyTagId(entitiesConfig.getTagStylePropertyTagId());

        // Categories
        entities.setTypeCategoryId(entitiesConfig.getTypeCategoryId());
        entities.setWriterCategoryId(entitiesConfig.getWriterCategoryId());

        // Blob Constants
        entities.setFailPenguinBlobConstantId(entitiesConfig.getFailPenguinBlobConstantId());
        entities.setRocBook45DTDBlobConstantId(entitiesConfig.getRocBook45DTDBlobConstantId());
        entities.setDocBook50RNGBlobConstantId(entitiesConfig.getDocBook50RNGBlobConstantId());

        // String Constants
        entities.setXmlFormattingStringConstantId(entitiesConfig.getXMLFormattingElementsStringConstantId());
        entities.setDocBookElementsStringConstantId(entitiesConfig.getDocBookElementsStringConstantId());

        // Template String Constants
        entities.setTopicTemplateId(entitiesConfig.getTopicTemplateId());
        entities.setContentSpecTemplateId(entitiesConfig.getContentSpecTemplateId());
        entities.setDocBook45AbstractTopicTemplateId(entitiesConfig.getDB45AbstractTopicTemplateId());
        entities.setDocBook45AuthorGroupTopicTemplateId(entitiesConfig.getDB45AuthorGroupTopicTemplateId());
        entities.setDocBook45InfoTopicTemplateId(entitiesConfig.getDB45InfoTopicTemplateId());
        entities.setDocBook45LegalNoticeTopicTemplateId(entitiesConfig.getDB45LegalNoticeTopicTemplateId());
        entities.setDocBook45RevisionHistoryTopicTemplateId(entitiesConfig.getDB45RevisionHistoryTopicTemplateId());
        entities.setDocBook50AbstractTopicTemplateId(entitiesConfig.getDB50AbstractTopicTemplateId());
        entities.setDocBook50AuthorGroupTopicTemplateId(entitiesConfig.getDB50AuthorGroupTopicTemplateId());
        entities.setDocBook50InfoTopicTemplateId(entitiesConfig.getDB50InfoTopicTemplateId());
        entities.setDocBook50LegalNoticeTopicTemplateId(entitiesConfig.getDB50LegalNoticeTopicTemplateId());
        entities.setDocBook50RevisionHistoryTopicTemplateId(entitiesConfig.getDB50RevisionHistoryTopicTemplateId());

        // Build String Constants
        entities.setArticleStringConstantId(entitiesConfig.getArticleStringConstantId());
        entities.setArticleInfoStringConstantId(entitiesConfig.getArticleInfoStringConstantId());
        entities.setAuthorGroupStringConstantId(entitiesConfig.getAuthorGroupStringConstantId());
        entities.setBookStringConstantId(entitiesConfig.getBookStringConstantId());
        entities.setBookInfoStringConstantId(entitiesConfig.getBookInfoStringConstantId());
        entities.setPomStringConstantId(entitiesConfig.getPOMStringConstantId());
        entities.setPrefaceStringConstantId(entitiesConfig.getPrefaceStringConstantId());
        entities.setPublicanCfgStringConstantId(entitiesConfig.getPublicanCfgStringConstantId());
        entities.setRevisionHistoryStringConstantId(entitiesConfig.getRevisionHistoryStringConstantId());
        entities.setEmptyTopicStringConstantId(entitiesConfig.getEmptyTopicStringConstantId());
        entities.setInvalidInjectionStringConstantId(entitiesConfig.getInvalidInjectionStringConstantId());
        entities.setInvalidTopicStringConstantId(entitiesConfig.getInvalidTopicStringConstantId());

        // Users
        entities.setUnknownUserId(entitiesConfig.getUnknownUserId());

        // Undefined Entities
        entities.setUndefinedEntities(
                RESTElementCollectionFactory.create(RESTServerUndefinedEntityCollectionV1.class, serverUndefinedEntityV1Factory,
                        entitiesConfig.getUndefinedEntities(), RESTServerEntitiesV1.UNDEFINED_ENTITIES_NAME, dataType, expand, baseUrl));

        // Locales
        if (expand != null && expand.contains(RESTServerSettingsV1.LOCALES_NAME)) {
            final List<Locale> locales = cachedEntityLoader.getLocaleEntities();
            retValue.setLocales(RESTEntityCollectionFactory.create(RESTLocaleCollectionV1.class, localeFactory, locales,
                    RESTServerSettingsV1.LOCALES_NAME, dataType, expand, baseUrl, null, entityManager));
        }

        // Translation Servers
        if (expand != null && expand.contains(RESTServerSettingsV1.TRANSLATION_SERVERS_NAME)) {
            final List<TranslationServer> translationServers = cachedEntityLoader.getTranslationServerEntities();
            retValue.setTranslationServers(
                    RESTEntityCollectionFactory.create(RESTTranslationServerExtendedCollectionV1.class, translationServerFactory,
                            translationServers, RESTServerSettingsV1.TRANSLATION_SERVERS_NAME, dataType, expand, baseUrl, null,
                            entityManager));
        }

        return retValue;
    }

    @Override
    public void updateObjectFromRESTEntity(final ApplicationConfig applicationConfig, final RESTServerSettingsV1 dataObject) {
        if (dataObject.hasParameterSet(RESTServerSettingsV1.DEFAULT_LOCALE_NAME))
            applicationConfig.setDefaultLocale(dataObject.getDefaultLocale() == null ? null : dataObject.getDefaultLocale().getValue());
        if (dataObject.hasParameterSet(RESTServerSettingsV1.DOCBOOK_TEMPLATES_NAME))
            applicationConfig.setDocBookTemplateStringConstantIds(dataObject.getDocBookTemplateIds());
        if (dataObject.hasParameterSet(RESTServerSettingsV1.SEO_CATEGORIES_NAME))
            applicationConfig.setSEOCategoryIds(dataObject.getSeoCategoryIds());
        if (dataObject.hasParameterSet(RESTServerSettingsV1.DOCBUILDER_URL_NAME))
            applicationConfig.setDocBuilderUrl(dataObject.getDocBuilderUrl());
        if (dataObject.hasParameterSet(RESTServerSettingsV1.UI_URL_NAME))
            applicationConfig.setUIUrl(dataObject.getUiUrl());
        if (dataObject.hasParameterSet(RESTServerSettingsV1.READONLY_NAME))
            applicationConfig.setReadOnly(dataObject.isReadOnly());
        if (dataObject.hasParameterSet(RESTServerSettingsV1.JMS_UPDATE_FREQUENCY))
            applicationConfig.setJmsUpdateFrequency(dataObject.getJmsUpdateFrequency());

        if (dataObject.hasParameterSet(RESTServerSettingsV1.UNDEFINED_SETTINGS_NAME)) {
            try {
                final RESTServerUndefinedSettingCollectionV1 undefinedSettings = dataObject.getUndefinedSettings();
                undefinedSettings.removeInvalidChangeItemRequests();
                for (final RESTServerUndefinedSettingCollectionItemV1 restEntityItem : undefinedSettings.getItems()) {
                    final RESTServerUndefinedSettingV1 restEntity = restEntityItem.getItem();

                    if (restEntityItem.returnIsRemoveItem()) {
                        applicationConfig.removeProperty(restEntity.getKey());
                    } else if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsUpdateItem()) {
                        if (restEntity.hasParameterSet(RESTServerUndefinedSettingV1.VALUE_NAME)) {
                            applicationConfig.addUndefinedSetting(restEntity.getKey(), restEntity.getValue());
                        }
                    }
                }
            } catch (ConfigurationException e) {
                throw new BadRequestException(e);
            }
        }

        // Locales
        if (dataObject.hasParameterSet(RESTServerSettingsV1.LOCALES_NAME)
                && dataObject.getLocales() != null
                && dataObject.getLocales().getItems() != null) {
            dataObject.getLocales().removeInvalidChangeItemRequests();

            boolean updated = false;
            for (final RESTLocaleCollectionItemV1 item : dataObject.getLocales().getItems()) {
                final RESTLocaleV1 restEntity = item.getItem();

                if (item.returnIsRemoveItem()) {
                    throw new BadRequestException("Cannot delete Locales");
                } else if (item.returnIsAddItem() || item.returnIsUpdateItem()) {
                    final Locale dbEntity;
                    if (item.returnIsAddItem()) {
                        dbEntity = localeFactory.createDBEntity(restEntity);
                    } else {
                        dbEntity = entityManager.find(Locale.class, restEntity.getId());
                    }

                    if (dbEntity == null) {
                        throw new BadRequestException("No Locale entity was found with the primary key " + restEntity.getId());
                    }

                    localeFactory.syncBaseDetails(dbEntity, restEntity);
                    localeFactory.syncAdditionalDetails(dbEntity, restEntity);
                    entityManager.persist(dbEntity);
                    updated = true;
                }
            }

            if (updated) {
                cachedEntityLoader.invalidateLocaleEntities();
            }
        }

        // Translation Settings
        if (dataObject.hasParameterSet(RESTServerSettingsV1.TRANSLATION_SERVERS_NAME)
                && dataObject.getTranslationServers() != null
                && dataObject.getTranslationServers().getItems() != null) {
            dataObject.getTranslationServers().removeInvalidChangeItemRequests();

            boolean updated = false;
            for (final RESTTranslationServerExtendedCollectionItemV1 item : dataObject.getTranslationServers().getItems()) {
                final RESTTranslationServerExtendedV1 restEntity = item.getItem();

                if (item.returnIsRemoveItem()) {
                    throw new BadRequestException("Cannot delete Translation Servers");
                } else if (item.returnIsAddItem() || item.returnIsUpdateItem()) {
                    final TranslationServer dbEntity;
                    if (item.returnIsAddItem()) {
                        dbEntity = translationServerFactory.createDBEntity(restEntity);
                    } else {
                        dbEntity = entityManager.find(TranslationServer.class, restEntity.getId());
                    }

                    if (dbEntity == null) {
                       throw new BadRequestException("No TranslationServer entity was found with the primary key " + restEntity.getId());
                    }

                    translationServerFactory.syncBaseDetails(dbEntity, restEntity);
                    translationServerFactory.syncAdditionalDetails(dbEntity, restEntity);
                    entityManager.persist(dbEntity);
                    updated = true;
                }
            }

            if (updated) {
                cachedEntityLoader.invalidateTranslationServerEntities();
            }
        }

        // Process any additional undefined entities
        if (dataObject.getEntities() != null && dataObject.getEntities().hasParameterSet(RESTServerEntitiesV1.UNDEFINED_ENTITIES_NAME)) {
            try {
                final RESTServerUndefinedEntityCollectionV1 undefinedEntities = dataObject.getEntities().getUndefinedEntities();
                undefinedEntities.removeInvalidChangeItemRequests();
                for (final RESTServerUndefinedEntityCollectionItemV1 restEntityItem : undefinedEntities.getItems()) {
                    final RESTServerUndefinedEntityV1 restEntity = restEntityItem.getItem();

                    if (restEntityItem.returnIsRemoveItem()) {
                        entitiesConfig.removeProperty(restEntity.getKey());
                    } else if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsUpdateItem()) {
                        if (restEntity.hasParameterSet(RESTServerUndefinedEntityV1.VALUE_NAME)) {
                            entitiesConfig.addUndefinedEntity(restEntity.getKey(), restEntity.getValue());
                        }
                    }
                }
            } catch (ConfigurationException e) {
                throw new BadRequestException(e);
            }

            try {
                entitiesConfig.save();
            } catch (ConfigurationException e) {
                throw new InternalServerErrorException(e);
            }
        }

        // Save the Application Settings
        try {
            applicationConfig.save();
        } catch (ConfigurationException e) {
            throw new InternalServerErrorException(e);
        }
    }
}
