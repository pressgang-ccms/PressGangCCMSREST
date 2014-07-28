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

package org.jboss.pressgang.ccms.server.utils;

import javax.persistence.EntityManager;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.server.envers.LoggingRevisionEntity;

public class EnversUtilities extends org.jboss.pressgang.ccms.model.utils.EnversUtilities {

    public static <T extends AuditedEntity> LoggingRevisionEntity getRevisionEntity(final EntityManager entityManager, final T entity,
            final Number revision) {
        final AuditReader reader = AuditReaderFactory.get(entityManager);
        if (revision == null) {
            return reader.findRevision(LoggingRevisionEntity.class, getLatestRevision(entityManager, entity));
        } else {
            return reader.findRevision(LoggingRevisionEntity.class, revision);
        }
    }

    public static <T extends AuditedEntity> String getLogMessage(final EntityManager entityManager, final T entity, final Number revision) {
        final LoggingRevisionEntity revEntity = getRevisionEntity(entityManager, entity, revision);
        return revEntity == null ? null : revEntity.getLogMessage();
    }

    public static <T extends AuditedEntity> Integer getLogFlag(final EntityManager entityManager, final T entity, final Number revision) {
        final LoggingRevisionEntity revEntity = getRevisionEntity(entityManager, entity, revision);
        return revEntity == null ? null : revEntity.getLogFlag();
    }

    public static <T extends AuditedEntity> String getLogUsername(final EntityManager entityManager, final T entity,
            final Number revision) {
        final LoggingRevisionEntity revEntity = getRevisionEntity(entityManager, entity, revision);
        return revEntity == null ? null : revEntity.getUserName();
    }
}
