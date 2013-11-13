package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.jboss.pressgang.ccms.model.config.EntitiesConfig;
import org.jboss.pressgang.ccms.model.config.UndefinedEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTApplicationUndefinedEntityCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTApplicationUndefinedEntityCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTApplicationUndefinedEntityV1;

@ApplicationScoped
public class ApplicationUndefinedEntityCollectionV1Factory {
    public RESTApplicationUndefinedEntityCollectionV1 createRESTEntity(final List<UndefinedEntity> undefinedEntities) {
        final RESTApplicationUndefinedEntityCollectionV1 retValue = new RESTApplicationUndefinedEntityCollectionV1();

        retValue.setSize(undefinedEntities.size());

        for (final UndefinedEntity entity : undefinedEntities) {
            final RESTApplicationUndefinedEntityV1 undefinedEntity = new RESTApplicationUndefinedEntityV1();

            undefinedEntity.setKey(entity.getKey());
            undefinedEntity.setValue(entity.getValue());

            retValue.addItem(undefinedEntity);
        }

        return retValue;
    }

    public void updateFromRESTEntity(final EntitiesConfig entityConfig, final RESTApplicationUndefinedEntityCollectionV1
            dataObject) throws ConfigurationException {
        dataObject.removeInvalidChangeItemRequests();
        for (final RESTApplicationUndefinedEntityCollectionItemV1 restEntityItem : dataObject.getItems()) {
            final RESTApplicationUndefinedEntityV1 restEntity = restEntityItem.getItem();

            if (restEntityItem.returnIsRemoveItem()) {
                entityConfig.removeProperty(restEntity.getKey());
            } else if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsUpdateItem()) {
                if (restEntity.hasParameterSet(RESTApplicationUndefinedEntityV1.VALUE_NAME)) {
                    entityConfig.addUndefinedProperty(restEntity.getKey(), restEntity.getValue());
                }
            }
        }
    }
}
