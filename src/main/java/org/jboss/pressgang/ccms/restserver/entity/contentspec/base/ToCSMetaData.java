package org.jboss.pressgang.ccms.restserver.entity.contentspec.base;

import org.jboss.pressgang.ccms.restserver.entity.base.AuditedEntity;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.CSMetaData;

/**
 * This class provides consistent access to the details of a property tag
 */
public abstract class ToCSMetaData<T extends AuditedEntity<T>> extends AuditedEntity<T>
{
	protected CSMetaData csMetaData;
	protected String value;

	public ToCSMetaData(final Class<T> classType)
	{
		super(classType);
	}

	public abstract CSMetaData getCSMetaData();

	public abstract void setCSMetaData(final CSMetaData csMetaData);

	public abstract String getValue();

	public abstract void setValue(final String value);
}
