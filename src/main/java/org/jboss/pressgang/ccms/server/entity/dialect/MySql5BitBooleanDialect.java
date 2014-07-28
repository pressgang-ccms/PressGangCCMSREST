/*
  Copyright 2011-2014 Red Hat

  This file is part of PresGang CCMS.

  PresGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PresGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PresGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.server.entity.dialect;

import java.sql.Types;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 * @author kamiller@redhat.com (Katie Miller)
 */
public class MySql5BitBooleanDialect extends MySQL5InnoDBDialect {

    public MySql5BitBooleanDialect() {
        super();
        registerColumnType(Types.BOOLEAN, "bit");
    }
}
