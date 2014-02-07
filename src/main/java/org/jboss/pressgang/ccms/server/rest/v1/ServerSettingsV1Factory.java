package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.configuration.ConfigurationException;
import org.jboss.pressgang.ccms.model.config.ApplicationConfig;
import org.jboss.pressgang.ccms.model.config.EntitiesConfig;
import org.jboss.pressgang.ccms.provider.exception.BadRequestException;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTServerEntitiesV1;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTServerSettingsV1;
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

        // Settings
        retValue.setDefaultLocale(applicationConfig.getDefaultLocale());
        retValue.setLocales(applicationConfig.getLocales());
        retValue.setDocBuilderUrl(applicationConfig.getDocBuilderUrl());
        retValue.setUiUrl(applicationConfig.getUIUrl());
        retValue.setDocBookTemplateIds(applicationConfig.getDocBookTemplateStringConstantIds());
        retValue.setSeoCategoryIds(applicationConfig.getSEOCategoryIds());
        retValue.setUndefinedSettings(serverUndefinedSettingCollectionV1Factory.createRESTEntity(applicationConfig.getUndefinedSettings()));

        // Entities
        final RESTServerEntitiesV1 entities = retValue.getEntities();

        // Tags
        entities.setAbstractTagId(entitiesConfig.getAbstractTagId());
        entities.setAuthorGroupTagId(entitiesConfig.getAuthorGroupTagId());
        entities.setContentSpecTagId(entitiesConfig.getContentSpecTagId());
        entities.setFrozenTagId(entitiesConfig.getFrozenTagId());
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
        entities.setTopicTemplateStringConstantId(entitiesConfig.getTopicTemplateStringConstantId());
        entities.setContentSpecTemplateStringConstantId(entitiesConfig.getContentSpecTemplateStringConstantId());
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
        entities.setUndefinedEntities(serverUndefinedEntityCollectionV1Factory.createRESTEntity(entitiesConfig.getUndefinedEntities()));

        return retValue;
    }

    public void updateFromRESTEntity(final RESTServerSettingsV1 dataObject) {
        if (dataObject.hasParameterSet(RESTServerSettingsV1.DEFAULT_LOCALE_NAME))
            applicationConfig.setDefaultLocale(dataObject.getDefaultLocale());
        if (dataObject.hasParameterSet(RESTServerSettingsV1.LOCALES_NAME)) applicationConfig.setLocales(dataObject.getLocales());
        if (dataObject.hasParameterSet(RESTServerSettingsV1.DOCBOOK_TEMPLATES_NAME))
            applicationConfig.setDocBookTemplateStringConstantIds(dataObject.getDocBookTemplateIds());
        if (dataObject.hasParameterSet(RESTServerSettingsV1.SEO_CATEGORIES_NAME))
            applicationConfig.setSEOCategoryIds(dataObject.getSeoCategoryIds());
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
        if (dataObject.getEntities() != null && dataObject.getEntities().hasParameterSet(RESTServerEntitiesV1.UNDEFINED_ENTITIES_NAME)) {
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
