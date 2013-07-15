package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.pressgang.ccms.model.TopicToPropertyTag;
import org.jboss.pressgang.ccms.server.rest.v1.base.BaseAssignedPropertyTagV1Factory;

@ApplicationScoped
public class TopicPropertyTagV1Factory extends BaseAssignedPropertyTagV1Factory<TopicToPropertyTag, TopicPropertyTagV1Factory> {
    @Override
    protected Class<TopicToPropertyTag> getDatabaseClass() {
        return TopicToPropertyTag.class;
    }
}