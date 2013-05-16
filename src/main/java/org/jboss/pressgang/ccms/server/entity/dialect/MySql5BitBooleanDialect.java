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
