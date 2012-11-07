package org.jboss.pressgang.ccms.restserver.entity.base;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.Transient;

import org.jboss.pressgang.ccms.restserver.entity.PropertyTag;
import org.jboss.pressgang.ccms.restserver.sort.ParentToPropertyTagIDComparator;
import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;


/**
 * This class provides consistent access to property tags
 */
public abstract class ParentToPropertyTag<T extends AuditedEntity<T>> extends AuditedEntity<T>
{
	protected abstract Set<? extends ToPropertyTag<?>> getPropertyTags();
	
	public ParentToPropertyTag(final Class<T> classType)
	{
		super(classType);
	}
	
	@Transient
	public String getProperties()
	{
		final List<? extends ToPropertyTag<?>> tags = CollectionUtilities.toArrayList(getPropertyTags());
		
		String retValue = "";
		
		Collections.sort(tags, new ParentToPropertyTagIDComparator());
		
		for (final ToPropertyTag<?> tagToPropertyTag : tags)
		{
			final PropertyTag propertyTag = tagToPropertyTag.getPropertyTag();
			
			if (!retValue.isEmpty())
				retValue += "\n";
			
			retValue += propertyTag.getPropertyTagName() + ": " + tagToPropertyTag.getValue();
		}

		
		return retValue;
	}
	
	@Transient
	public List<? extends ToPropertyTag<?>> getSortedToPropertyTags()
	{
		final List<? extends ToPropertyTag<?>> sortedList = CollectionUtilities.toArrayList(getPropertyTags());
		Collections.sort(sortedList, new ParentToPropertyTagIDComparator());
		return sortedList;
	}
	
	public boolean hasProperty(final PropertyTag propertyTag)
	{
		return hasProperty(propertyTag.getPropertyTagId());
	}
	
	public boolean hasProperty(final Integer propertyTag)
	{
		for (final ToPropertyTag<?> toPropertyTag : this.getPropertyTags())
		{
			final PropertyTag myPropertyTag = toPropertyTag.getPropertyTag();
			if (myPropertyTag.getPropertyTagId().equals(propertyTag))
				return true;
		}
		
		return false;
	}
	
	@Transient
	public ToPropertyTag<?> getProperty(final Integer propertyTagId)
	{
		for (final ToPropertyTag<?> toPropertyTag : this.getPropertyTags())
		{
			final PropertyTag propertyTag = toPropertyTag.getPropertyTag();
			if (propertyTag.getPropertyTagId().equals(propertyTagId))
				return toPropertyTag;
		}
		
		return null;
	}
}
