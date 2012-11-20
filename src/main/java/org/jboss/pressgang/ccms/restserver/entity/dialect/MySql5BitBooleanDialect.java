package org.jboss.pressgang.ccms.restserver.entity.dialect;

import org.hibernate.dialect.MySQL5InnoDBDialect;

import java.sql.Types;

/**
 * @author kamiller@redhat.com (Katie Miller)
 */
public class MySql5BitBooleanDialect extends MySQL5InnoDBDialect {

    public MySql5BitBooleanDialect() {
        super();
        registerColumnType(Types.BOOLEAN, "bit");
    }
}
