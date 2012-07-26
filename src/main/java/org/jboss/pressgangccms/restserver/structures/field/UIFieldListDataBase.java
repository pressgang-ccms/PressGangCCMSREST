package org.jboss.pressgangccms.restserver.structures.field;

import java.util.ArrayList;
import java.util.List;

public abstract class UIFieldListDataBase<T> extends UIFieldDataBase<List<T>>
{

	/** The data stored within this UIField */
	protected List<T> data = new ArrayList<T>();
	/** Whether or not this object has been "negated" */
	protected boolean negated = false;
	/** The name of the data field */
	protected String name = "";
	/** The description */
	protected String description = "";
	
	public UIFieldListDataBase(final String name, final String description)
	{
		super(name, description);
	}
	
	public boolean isNegated()
	{
		return negated;
	}

	public void setNegated(final boolean negated)
	{
		this.negated = negated;
	}

	public String getName()
	{
		return name;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}
}
