package org.jboss.pressgangccms.restserver.entities.base;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Transient;

import org.jboss.pressgangccms.restserver.entities.PropertyTag;

/**
 * This class provides consistent access to the details of a property tag
 */
public abstract class ToPropertyTag<T extends AuditedEntity<T>> extends AuditedEntity<T>
{
	@PersistenceContext(unitName="ToPropertyTag") EntityManager entityManager;
	
	protected PropertyTag propertyTag;
	protected String value;

	public ToPropertyTag(final Class<T> classType)
	{
		super(classType);
	}
	
	@Transient
	public boolean isValid(final Number revision)
	{
		return isValid(entityManager, revision);
	}

	@Transient
	public boolean isValid(final EntityManager entityManager, final Number revision)
	{
		if (this.propertyTag == null)
			return false;
		
		if (this.value == null)
			return this.propertyTag.isPropertyTagCanBeNull();
		
		if (!testUnique(entityManager, revision))
			return false;
				
		return this.value.matches(this.propertyTag.getPropertyTagRegex());
	}

	protected abstract boolean testUnique(final EntityManager entityManager, final Number revision);

	public abstract PropertyTag getPropertyTag();

	public abstract void setPropertyTag(final PropertyTag propertyTag);

	public abstract String getValue();

	public abstract void setValue(final String value);
}
