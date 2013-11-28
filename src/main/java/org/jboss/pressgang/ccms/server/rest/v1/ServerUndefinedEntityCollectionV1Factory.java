package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.jboss.pressgang.ccms.model.config.EntitiesConfig;
import org.jboss.pressgang.ccms.model.config.UndefinedEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTServerUndefinedEntityCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTApplicationUndefinedEntityCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTServerUndefinedEntityV1;

@ApplicationScoped
public class ServerUndefinedEntityCollectionV1Factory {
    public RESTServerUndefinedEntityCollectionV1 createRESTEntity(final List<UndefinedEntity> undefinedEntities) {
        final RESTServerUndefinedEntityCollectionV1 retValue = new RESTServerUndefinedEntityCollectionV1();

        retValue.setSize(undefinedEntities.size());

        for (final UndefinedEntity entity : undefinedEntities) {
            final RESTServerUndefinedEntityV1 undefinedEntity = new RESTServerUndefinedEntityV1();

            undefinedEntity.setKey(entity.getKey());
            undefinedEntity.setValue(entity.getValue());

            retValue.addItem(undefinedEntity);
        }

        return retValue;
    }

    public void updateFromRESTEntity(final EntitiesConfig entityConfig, final RESTServerUndefinedEntityCollectionV1
            dataObject) throws ConfigurationException {
        dataObject.removeInvalidChangeItemRequests();
        for (final RESTApplicationUndefinedEntityCollectionItemV1 restEntityItem : dataObject.getItems()) {
            final RESTServerUndefinedEntityV1 restEntity = restEntityItem.getItem();

            if (restEntityItem.returnIsRemoveItem()) {
                entityConfig.removeProperty(restEntity.getKey());
            } else if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsUpdateItem()) {
                if (restEntity.hasParameterSet(RESTServerUndefinedEntityV1.VALUE_NAME)) {
                    entityConfig.addUndefinedEntity(restEntity.getKey(), restEntity.getValue());
                }
            }
        }
    }
}
