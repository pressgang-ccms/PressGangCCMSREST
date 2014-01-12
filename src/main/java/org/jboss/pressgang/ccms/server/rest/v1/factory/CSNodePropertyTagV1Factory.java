package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.pressgang.ccms.model.contentspec.CSNodeToPropertyTag;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.BaseAssignedPropertyTagV1Factory;

@ApplicationScoped
public class CSNodePropertyTagV1Factory extends BaseAssignedPropertyTagV1Factory<CSNodeToPropertyTag, CSNodePropertyTagV1Factory> {
    @Override
    protected Class<CSNodeToPropertyTag> getDatabaseClass() {
        return CSNodeToPropertyTag.class;
    }
}
