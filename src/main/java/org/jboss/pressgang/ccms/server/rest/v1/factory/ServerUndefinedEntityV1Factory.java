package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.pressgang.ccms.model.config.UndefinedEntity;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTServerUndefinedEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTElementFactory;

@ApplicationScoped
public class ServerUndefinedEntityV1Factory extends RESTElementFactory<RESTServerUndefinedEntityV1, UndefinedEntity> {
    @Override
    public RESTServerUndefinedEntityV1 createRESTEntityFromObject(final UndefinedEntity entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand) {
        final RESTServerUndefinedEntityV1 undefinedEntity = new RESTServerUndefinedEntityV1();

        undefinedEntity.setKey(entity.getKey());
        undefinedEntity.setValue(entity.getValue());

        return undefinedEntity;
    }

    @Override
    public void updateObjectFromRESTEntity(final UndefinedEntity entity, final RESTServerUndefinedEntityV1 dataObject) {
        entity.setKey(dataObject.getKey());
        entity.setValue(dataObject.getValue());
    }
}
