package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.pressgang.ccms.model.config.UndefinedSetting;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTServerUndefinedSettingV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTElementFactory;

@ApplicationScoped
public class ServerUndefinedSettingV1Factory extends RESTElementFactory<RESTServerUndefinedSettingV1, UndefinedSetting> {
    @Override
    public RESTServerUndefinedSettingV1 createRESTEntityFromObject(final UndefinedSetting setting, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand) {
        final RESTServerUndefinedSettingV1 undefinedSetting = new RESTServerUndefinedSettingV1();

        undefinedSetting.setKey(setting.getKey());
        undefinedSetting.setValue(setting.getValue());

        return undefinedSetting;
    }

    @Override
    public void updateObjectFromRESTEntity(final UndefinedSetting setting, final RESTServerUndefinedSettingV1 dataObject) {
        setting.setKey(dataObject.getKey());
        setting.setValue(dataObject.getValue());
    }
}
