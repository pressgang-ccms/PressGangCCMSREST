package org.jboss.pressgangccms.restserver.sort;

import java.util.Comparator;

import org.jboss.pressgangccms.restserver.entities.Category;

public class CategoryNameComparator implements Comparator<Category>
{
	@Override
	public int compare(final Category o1, final Category o2) 
	{
		if (o1 == null && o2 == null)
			return 0;
		if (o1 == null)
			return -1;
		if (o2 == null)
			return 1;
		
		if (o1.getCategoryName() == null && o2.getCategoryName() == null)
			return 0;
		if (o1.getCategoryName() == null)
			return -1;
		if (o2.getCategoryName() == null)
			return 1;
		
		return o1.getCategoryName().compareTo(o2.getCategoryName());
	}
}
