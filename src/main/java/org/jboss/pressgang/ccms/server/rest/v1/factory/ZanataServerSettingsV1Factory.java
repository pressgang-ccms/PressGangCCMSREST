package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.pressgang.ccms.model.config.ZanataServerConfig;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTZanataServerSettingsV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTElementFactory;

@ApplicationScoped
public class ZanataServerSettingsV1Factory extends RESTElementFactory<RESTZanataServerSettingsV1, ZanataServerConfig> {
    @Override
    public RESTZanataServerSettingsV1 createRESTEntityFromObject(final ZanataServerConfig config, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand) {
        final RESTZanataServerSettingsV1 retValue = new RESTZanataServerSettingsV1();

        retValue.setId(config.getId());
        retValue.setName(config.getName());
        retValue.setUrl(config.getUrl());
        retValue.setProject(config.getProject());
        retValue.setProjectVersion(config.getProjectVersion());

        return retValue;
    }

    @Override
    public void updateObjectFromRESTEntity(final ZanataServerConfig config, final RESTZanataServerSettingsV1 dataObject) {
        if (dataObject.hasParameterSet(RESTZanataServerSettingsV1.NAME_NAME)) {
            config.setName(dataObject.getName());
        }
        if (dataObject.hasParameterSet(RESTZanataServerSettingsV1.URL_NAME)) {
            config.setUrl(dataObject.getUrl());
        }
        if (dataObject.hasParameterSet(RESTZanataServerSettingsV1.PROJECT_NAME)) {
            config.setProject(dataObject.getProject());
        }
        if (dataObject.hasParameterSet(RESTZanataServerSettingsV1.PROJECT_VERSION_NAME)) {
            config.setProjectVersion(dataObject.getProjectVersion());
        }

    }
}
