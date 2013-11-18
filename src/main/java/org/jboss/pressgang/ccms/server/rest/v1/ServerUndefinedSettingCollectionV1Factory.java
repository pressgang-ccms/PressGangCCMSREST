package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.jboss.pressgang.ccms.model.config.ApplicationConfig;
import org.jboss.pressgang.ccms.model.config.UndefinedSetting;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTServerUndefinedSettingCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTApplicationUndefinedSettingCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTServerUndefinedSettingV1;

@ApplicationScoped
public class ServerUndefinedSettingCollectionV1Factory {
    public RESTServerUndefinedSettingCollectionV1 createRESTEntity(final List<UndefinedSetting> undefinedSettings) {
        final RESTServerUndefinedSettingCollectionV1 retValue = new RESTServerUndefinedSettingCollectionV1();

        retValue.setSize(undefinedSettings.size());

        for (final UndefinedSetting setting : undefinedSettings) {
            final RESTServerUndefinedSettingV1 undefinedSetting = new RESTServerUndefinedSettingV1();

            undefinedSetting.setKey(setting.getKey());
            undefinedSetting.setValue(setting.getValue());

            retValue.addItem(undefinedSetting);
        }

        return retValue;
    }

    public void updateFromRESTEntity(final ApplicationConfig applicationConfig, final RESTServerUndefinedSettingCollectionV1
            dataObject) throws ConfigurationException {

        dataObject.removeInvalidChangeItemRequests();
        for (final RESTApplicationUndefinedSettingCollectionItemV1 restEntityItem : dataObject.getItems()) {
            final RESTServerUndefinedSettingV1 restEntity = restEntityItem.getItem();

            if (restEntityItem.returnIsRemoveItem()) {
                applicationConfig.removeProperty(restEntity.getKey());
            } else if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsUpdateItem()) {
                if (restEntity.hasParameterSet(RESTServerUndefinedSettingV1.VALUE_NAME)) {
                    applicationConfig.addUndefinedSetting(restEntity.getKey(), restEntity.getValue());
                }
            }
        }
    }
}
