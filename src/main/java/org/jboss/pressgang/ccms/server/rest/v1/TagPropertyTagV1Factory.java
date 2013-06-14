package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.pressgang.ccms.model.TagToPropertyTag;
import org.jboss.pressgang.ccms.server.rest.v1.base.BaseAssignedPropertyTagV1Factory;

@ApplicationScoped
public class TagPropertyTagV1Factory extends BaseAssignedPropertyTagV1Factory<TagToPropertyTag, TagPropertyTagV1Factory> {
    @Override
    protected Class<TagToPropertyTag> getDatabaseClass() {
        return TagToPropertyTag.class;
    }
}
