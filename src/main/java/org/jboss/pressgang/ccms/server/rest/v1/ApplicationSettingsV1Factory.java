package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.configuration.ConfigurationException;
import org.jboss.pressgang.ccms.provider.exception.BadRequestException;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTApplicationEntitiesV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTApplicationSettingsV1;
import org.jboss.pressgang.ccms.server.config.ApplicationConfig;
import org.jboss.pressgang.ccms.server.config.EntitiesConfig;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.jboss.resteasy.spi.InternalServerErrorException;

@ApplicationScoped
public class ApplicationSettingsV1Factory {
    private final ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
    private final EntitiesConfig entitiesConfig = EntitiesConfig.getInstance();

    @Inject
    private EntityManager entityManager;
    @Inject
    private ApplicationUndefinedEntityCollectionV1Factory applicationUndefinedEntityCollectionV1Factory;
    @Inject
    private ApplicationUndefinedSettingCollectionV1Factory applicationUndefinedSettingCollectionV1Factory;

    public RESTApplicationSettingsV1 createRESTEntity() {
        final RESTApplicationSettingsV1 retValue = new RESTApplicationSettingsV1();

        // Application
        retValue.setDefaultLocale(applicationConfig.getDefaultLocale());
        retValue.setLocales(applicationConfig.getLocales());
        retValue.setDocBuilderUrl(applicationConfig.getDocBuilderUrl());
        retValue.setUiUrl(applicationConfig.getUIUrl());
        retValue.setDocBookTemplateIds(applicationConfig.getDocBookTemplateStringConstantIds());
        retValue.setUndefinedSettings(
                applicationUndefinedSettingCollectionV1Factory.createRESTEntity(applicationConfig.getUndefinedProperties()));

        // Entities
        // Tags
        retValue.getEntities().setContentSpecTagId(entitiesConfig.getContentSpecTagId());
        retValue.getEntities().setReviewTagId(entitiesConfig.getReviewTagId());

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
                applicationUndefinedEntityCollectionV1Factory.createRESTEntity(entitiesConfig.getUndefinedProperties()));

        return retValue;
    }

    public void updateFromRESTEntity(final RESTApplicationSettingsV1 dataObject) {
        if (dataObject.hasParameterSet(RESTApplicationSettingsV1.DEFAULT_LOCALE_NAME)) {
            applicationConfig.setDefaultLocale(dataObject.getDefaultLocale());
            // Set the default locale system property
            System.setProperty(CommonConstants.DEFAULT_LOCALE_PROPERTY, dataObject.getDefaultLocale());
        }
        if (dataObject.hasParameterSet(RESTApplicationSettingsV1.LOCALES_NAME)) applicationConfig.setLocales(dataObject.getLocales());
        if (dataObject.hasParameterSet(RESTApplicationSettingsV1.DOCBOOK_ELEMENTS_NAME))
            applicationConfig.setDocBookTemplateStringConstantIds(dataObject.getDocBookTemplateIds());
        if (dataObject.hasParameterSet(RESTApplicationSettingsV1.DOCBUILDER_URL_NAME))
            applicationConfig.setDocBuilderUrl(dataObject.getDocBuilderUrl());
        if (dataObject.hasParameterSet(RESTApplicationSettingsV1.UI_URL_NAME)) applicationConfig.setUIUrl(dataObject.getUiUrl());

        if (dataObject.hasParameterSet(RESTApplicationSettingsV1.UNDEFINED_SETTINGS_NAME)) {
            try {
                applicationUndefinedSettingCollectionV1Factory.updateFromRESTEntity(applicationConfig, dataObject.getUndefinedSettings());
            } catch (ConfigurationException e) {
                throw new BadRequestException(e);
            }
        }

        // Process any additional undefined entities
        if (dataObject.getEntities() != null && dataObject.getEntities().hasParameterSet(
                RESTApplicationEntitiesV1.UNDEFINED_ENTITIES_NAME)) {
            try {
                applicationUndefinedEntityCollectionV1Factory.updateFromRESTEntity(entitiesConfig,
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
