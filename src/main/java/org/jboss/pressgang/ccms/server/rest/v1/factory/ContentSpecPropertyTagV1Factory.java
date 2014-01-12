package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.pressgang.ccms.model.contentspec.ContentSpecToPropertyTag;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.BaseAssignedPropertyTagV1Factory;

@ApplicationScoped
public class ContentSpecPropertyTagV1Factory extends BaseAssignedPropertyTagV1Factory<ContentSpecToPropertyTag,
        ContentSpecPropertyTagV1Factory> {

    @Override
    protected Class<ContentSpecToPropertyTag> getDatabaseClass() {
        return ContentSpecToPropertyTag.class;
    }
}
