package org.jboss.pressgang.ccms.restserver.rest.v1;

import org.jboss.pressgang.ccms.model.TopicToPropertyTag;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.BaseAssignedPropertyTagV1Factory;

public class TopicPropertyTagV1Factory extends BaseAssignedPropertyTagV1Factory<TopicToPropertyTag, TopicPropertyTagV1Factory> {

    public TopicPropertyTagV1Factory() {
        super(TopicToPropertyTag.class, TopicPropertyTagV1Factory.class);
    }
}
