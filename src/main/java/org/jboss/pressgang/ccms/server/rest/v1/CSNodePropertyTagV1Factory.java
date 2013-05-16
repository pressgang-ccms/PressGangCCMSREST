package org.jboss.pressgang.ccms.server.rest.v1;

import org.jboss.pressgang.ccms.model.contentspec.CSNodeToPropertyTag;
import org.jboss.pressgang.ccms.server.rest.v1.base.BaseAssignedPropertyTagV1Factory;

public class CSNodePropertyTagV1Factory extends BaseAssignedPropertyTagV1Factory<CSNodeToPropertyTag, CSNodePropertyTagV1Factory> {

    public CSNodePropertyTagV1Factory() {
        super(CSNodeToPropertyTag.class, CSNodePropertyTagV1Factory.class);
    }
}
