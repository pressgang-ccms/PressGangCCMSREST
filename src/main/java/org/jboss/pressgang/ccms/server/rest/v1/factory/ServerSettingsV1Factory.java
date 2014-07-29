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
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.jboss.pressgang.ccms.model.config.ApplicationConfig;
import org.jboss.pressgang.ccms.model.config.EntitiesConfig;
import org.jboss.pressgang.ccms.model.config.ZanataServerConfig;
import org.jboss.pressgang.ccms.provider.exception.BadRequestException;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTServerUndefinedEntityCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTServerUndefinedSettingCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTZanataServerSettingsCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTServerUndefinedEntityCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTServerUndefinedSettingCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTZanataServerSettingsCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTServerEntitiesV1;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTServerSettingsV1;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTServerUndefinedEntityV1;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTServerUndefinedSettingV1;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTZanataServerSettingsV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTElementCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTElementFactory;
import org.jboss.resteasy.spi.InternalServerErrorException;

@ApplicationScoped
public class ServerSettingsV1Factory extends RESTElementFactory<RESTServerSettingsV1, ApplicationConfig> {
    private final EntitiesConfig entitiesConfig = EntitiesConfig.getInstance();

    @Inject
    private ServerUndefinedEntityV1Factory serverUndefinedEntityV1Factory;
    @Inject
    private ServerUndefinedSettingV1Factory serverUndefinedSettingV1Factory;
    @Inject
    private ZanataServerSettingsV1Factory zanataServerSettingsV1Factory;

    @Override
    public RESTServerSettingsV1 createRESTEntityFromObject(final ApplicationConfig applicationConfig, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand) {
        final RESTServerSettingsV1 retValue = new RESTServerSettingsV1();

        // Settings
        retValue.setDefaultLocale(applicationConfig.getDefaultLocale());
        retValue.setLocales(applicationConfig.getLocales());
        retValue.setDocBuilderUrl(applicationConfig.getDocBuilderUrl());
        retValue.setUiUrl(applicationConfig.getUIUrl());
        retValue.setDocBookTemplateIds(applicationConfig.getDocBookTemplateStringConstantIds());
        retValue.setSeoCategoryIds(applicationConfig.getSEOCategoryIds());
        retValue.setReadOnly(applicationConfig.getReadOnly());
        retValue.setJmsUpdateFrequency(applicationConfig.getJmsUpdateFrequency());

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

        // Zanata Servers
        final Map<String, ZanataServerConfig> zanataServers = applicationConfig.getZanataServers();
        retValue.setZanataSettings(
                RESTElementCollectionFactory.create(RESTZanataServerSettingsCollectionV1.class, zanataServerSettingsV1Factory,
                        new ArrayList<ZanataServerConfig>(zanataServers.values()), RESTServerSettingsV1.ZANATA_SETTINGS_NAME, dataType,
                        expand, baseUrl));

        return retValue;
    }

    @Override
    public void updateObjectFromRESTEntity(final ApplicationConfig applicationConfig, final RESTServerSettingsV1 dataObject) {
        if (dataObject.hasParameterSet(RESTServerSettingsV1.DEFAULT_LOCALE_NAME))
            applicationConfig.setDefaultLocale(dataObject.getDefaultLocale());
        if (dataObject.hasParameterSet(RESTServerSettingsV1.LOCALES_NAME))
            applicationConfig.setLocales(dataObject.getLocales());
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

        // Zanata Settings
        if (dataObject.hasParameterSet(RESTServerSettingsV1.ZANATA_SETTINGS_NAME)) {
            final Map<String, ZanataServerConfig> zanataServers = applicationConfig.getZanataServers();

            final RESTZanataServerSettingsCollectionV1 zanataSettings = dataObject.getZanataSettings();
            zanataSettings.removeInvalidChangeItemRequests();
            for (final RESTZanataServerSettingsCollectionItemV1 restEntityItem : zanataSettings.getItems()) {
                final RESTZanataServerSettingsV1 restEntity = restEntityItem.getItem();

                final ZanataServerConfig zanataServerConfig;
                if (zanataServers.containsKey(restEntity.getId())) {
                    zanataServerConfig = zanataServers.get(restEntity.getId());
                } else {
                    zanataServerConfig = new ZanataServerConfig(restEntity.getId());
                }

                if (restEntityItem.returnIsRemoveItem()) {
                    applicationConfig.removeZanataServer(restEntity.getId());
                } else if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsUpdateItem()) {
                    zanataServerSettingsV1Factory.updateObjectFromRESTEntity(zanataServerConfig, restEntity);

                    applicationConfig.addZanataServer(zanataServerConfig);
                }
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
