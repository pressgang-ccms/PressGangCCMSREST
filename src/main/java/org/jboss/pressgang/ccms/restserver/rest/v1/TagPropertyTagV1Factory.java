package org.jboss.pressgang.ccms.restserver.rest.v1;

import org.jboss.pressgang.ccms.model.TagToPropertyTag;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.BaseAssignedPropertyTagV1Factory;

public class TagPropertyTagV1Factory extends BaseAssignedPropertyTagV1Factory<TagToPropertyTag, TagPropertyTagV1Factory> {
    public TagPropertyTagV1Factory() {
        super(TagToPropertyTag.class, TagPropertyTagV1Factory.class);
    }
}
