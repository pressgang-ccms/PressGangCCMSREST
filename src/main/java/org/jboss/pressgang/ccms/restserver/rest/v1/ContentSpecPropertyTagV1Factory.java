package org.jboss.pressgang.ccms.restserver.rest.v1;

import org.jboss.pressgang.ccms.model.contentspec.ContentSpecToPropertyTag;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.BaseAssignedPropertyTagV1Factory;

public class ContentSpecPropertyTagV1Factory extends
        BaseAssignedPropertyTagV1Factory<ContentSpecToPropertyTag, ContentSpecPropertyTagV1Factory> {

    public ContentSpecPropertyTagV1Factory() {
        super(ContentSpecToPropertyTag.class, ContentSpecPropertyTagV1Factory.class);
    }
}
