/*
  Copyright 2011-2014 Red Hat, Inc

  This file is part of PressGang CCMS.

  PressGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PressGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PressGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.pressgang.ccms.model.config.UndefinedSetting;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTServerUndefinedSettingV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTElementFactory;

@ApplicationScoped
public class ServerUndefinedSettingV1Factory extends RESTElementFactory<RESTServerUndefinedSettingV1, UndefinedSetting> {
    @Override
    public RESTServerUndefinedSettingV1 createRESTEntityFromObject(final UndefinedSetting setting, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand) {
        final RESTServerUndefinedSettingV1 undefinedSetting = new RESTServerUndefinedSettingV1();

        undefinedSetting.setKey(setting.getKey());
        undefinedSetting.setValue(setting.getValue());

        return undefinedSetting;
    }

    @Override
    public void updateObjectFromRESTEntity(final UndefinedSetting setting, final RESTServerUndefinedSettingV1 dataObject) {
        setting.setKey(dataObject.getKey());
        setting.setValue(dataObject.getValue());
    }
}
