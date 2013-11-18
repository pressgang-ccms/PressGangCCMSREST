package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.configuration.ConfigurationException;
import org.jboss.pressgang.ccms.provider.exception.BadRequestException;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTServerEntitiesV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTServerSettingsV1;
import org.jboss.pressgang.ccms.model.config.ApplicationConfig;
import org.jboss.pressgang.ccms.model.config.EntitiesConfig;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.jboss.resteasy.spi.InternalServerErrorException;

@ApplicationScoped
public class ServerSettingsV1Factory {
    private final ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
    private final EntitiesConfig entitiesConfig = EntitiesConfig.getInstance();

    @Inject
    private EntityManager entityManager;
    @Inject
    private ServerUndefinedEntityCollectionV1Factory serverUndefinedEntityCollectionV1Factory;
    @Inject
    private ServerUndefinedSettingCollectionV1Factory serverUndefinedSettingCollectionV1Factory;

    public RESTServerSettingsV1 createRESTEntity() {
        final RESTServerSettingsV1 retValue = new RESTServerSettingsV1();

        // Application
        retValue.setDefaultLocale(applicationConfig.getDefaultLocale());
        retValue.setLocales(applicationConfig.getLocales());
        retValue.setDocBuilderUrl(applicationConfig.getDocBuilderUrl());
        retValue.setUiUrl(applicationConfig.getUIUrl());
        retValue.setDocBookTemplateIds(applicationConfig.getDocBookTemplateStringConstantIds());
        retValue.setUndefinedSettings(
                serverUndefinedSettingCollectionV1Factory.createRESTEntity(applicationConfig.getUndefinedProperties()));

        // Entities
        // Tags
        retValue.getEntities().setAbstractTagId(entitiesConfig.getAbstractTagId());
        retValue.getEntities().setAuthorGroupTagId(entitiesConfig.getAuthorGroupTagId());
        retValue.getEntities().setContentSpecTagId(entitiesConfig.getContentSpecTagId());
        retValue.getEntities().setLegalNoticeTagId(entitiesConfig.getLegalNoticeTagId());
        retValue.getEntities().setReviewTagId(entitiesConfig.getReviewTagId());
        retValue.getEntities().setRevisionHistoryTagId(entitiesConfig.getRevisionHistoryTagId());

        // Property Tags
        retValue.getEntities().setFixedUrlPropertyTagId(entitiesConfig.getFixedUrlPropertyTagId());
        retValue.getEntities().setOriginalFileNamePropertyTagId(entitiesConfig.getOriginalFileNamePropertyTagId());
        retValue.getEntities().setTagStylePropertyTagId(entitiesConfig.getTagStylePropertyTagId());

        // Categories
        retValue.getEntities().setTypeCategoryId(entitiesConfig.getTypeCategoryId());
        retValue.getEntities().setWriterCategoryId(entitiesConfig.getWriterCategoryId());

        // Blob Constants
        retValue.getEntities().setRocBookDTDBlobConstantId(entitiesConfig.getRocBookDTDBlobConstantId());

        // String Constants
        retValue.getEntities().setXmlFormattingStringConstantId(entitiesConfig.getXMLFormattingElementsStringConstantId());
        retValue.getEntities().setDocbookElementsStringConstantId(entitiesConfig.getDocBookElementsStringConstantId());
        retValue.getEntities().setTopicTemplateStringConstantId(entitiesConfig.getTopicTemplateStringConstantId());
        retValue.getEntities().setContentSpecTemplateStringConstantId(entitiesConfig.getContentSpecTemplateStringConstantId());

        // Users
        retValue.getEntities().setUnknownUserId(entitiesConfig.getUnknownUserId());

        retValue.getEntities().setUndefinedEntities(
                serverUndefinedEntityCollectionV1Factory.createRESTEntity(entitiesConfig.getUndefinedProperties()));

        return retValue;
    }

    public void updateFromRESTEntity(final RESTServerSettingsV1 dataObject) {
        if (dataObject.hasParameterSet(RESTServerSettingsV1.DEFAULT_LOCALE_NAME)) {
            applicationConfig.setDefaultLocale(dataObject.getDefaultLocale());
            // Set the default locale system property
            System.setProperty(CommonConstants.DEFAULT_LOCALE_PROPERTY, dataObject.getDefaultLocale());
        }
        if (dataObject.hasParameterSet(RESTServerSettingsV1.LOCALES_NAME)) applicationConfig.setLocales(dataObject.getLocales());
        if (dataObject.hasParameterSet(RESTServerSettingsV1.DOCBOOK_ELEMENTS_NAME))
            applicationConfig.setDocBookTemplateStringConstantIds(dataObject.getDocBookTemplateIds());
        if (dataObject.hasParameterSet(RESTServerSettingsV1.DOCBUILDER_URL_NAME))
            applicationConfig.setDocBuilderUrl(dataObject.getDocBuilderUrl());
        if (dataObject.hasParameterSet(RESTServerSettingsV1.UI_URL_NAME)) applicationConfig.setUIUrl(dataObject.getUiUrl());

        if (dataObject.hasParameterSet(RESTServerSettingsV1.UNDEFINED_SETTINGS_NAME)) {
            try {
                serverUndefinedSettingCollectionV1Factory.updateFromRESTEntity(applicationConfig, dataObject.getUndefinedSettings());
            } catch (ConfigurationException e) {
                throw new BadRequestException(e);
            }
        }

        // Process any additional undefined entities
        if (dataObject.getEntities() != null && dataObject.getEntities().hasParameterSet(
                RESTServerEntitiesV1.UNDEFINED_ENTITIES_NAME)) {
            try {
                serverUndefinedEntityCollectionV1Factory.updateFromRESTEntity(entitiesConfig,
                        dataObject.getEntities().getUndefinedEntities());
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
