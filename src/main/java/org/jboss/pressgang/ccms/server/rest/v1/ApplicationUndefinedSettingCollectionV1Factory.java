package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.jboss.pressgang.ccms.model.config.ApplicationConfig;
import org.jboss.pressgang.ccms.model.config.UndefinedSetting;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTApplicationUndefinedSettingCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTApplicationUndefinedSettingCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTApplicationUndefinedSettingV1;

@ApplicationScoped
public class ApplicationUndefinedSettingCollectionV1Factory {
    public RESTApplicationUndefinedSettingCollectionV1 createRESTEntity(final List<UndefinedSetting> undefinedSettings) {
        final RESTApplicationUndefinedSettingCollectionV1 retValue = new RESTApplicationUndefinedSettingCollectionV1();

        retValue.setSize(undefinedSettings.size());

        for (final UndefinedSetting setting : undefinedSettings) {
            final RESTApplicationUndefinedSettingV1 undefinedSetting = new RESTApplicationUndefinedSettingV1();

            undefinedSetting.setKey(setting.getKey());
            undefinedSetting.setValue(setting.getValue());

            retValue.addItem(undefinedSetting);
        }

        return retValue;
    }

    public void updateFromRESTEntity(final ApplicationConfig applicationConfig, final RESTApplicationUndefinedSettingCollectionV1
            dataObject) throws ConfigurationException {

        dataObject.removeInvalidChangeItemRequests();
        for (final RESTApplicationUndefinedSettingCollectionItemV1 restEntityItem : dataObject.getItems()) {
            final RESTApplicationUndefinedSettingV1 restEntity = restEntityItem.getItem();

            if (restEntityItem.returnIsRemoveItem()) {
                applicationConfig.removeProperty(restEntity.getKey());
            } else if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsUpdateItem()) {
                if (restEntity.hasParameterSet(RESTApplicationUndefinedSettingV1.VALUE_NAME)) {
                    applicationConfig.addUndefinedProperty(restEntity.getKey(), restEntity.getValue());
                }
            }
        }
    }
}
