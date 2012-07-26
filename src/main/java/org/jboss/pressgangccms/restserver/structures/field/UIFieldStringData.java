package org.jboss.pressgangccms.restserver.structures.field;

import java.util.List;

import org.jboss.pressgangccms.utils.common.CollectionUtilities;


public class UIFieldStringData extends UIFieldDataBase<String>
{
	public UIFieldStringData(final String name, final String description)
	{
		super(name, description);
	}
	
	@Override
	public String getData()
	{
		return this.data;
	}

	@Override
	public void setData(final String data)
	{
		this.data = data;
	}
	
	public <T> void setData(final List<T> data) throws Exception {
		this.data = CollectionUtilities.toSeperatedString(data, ",");
	}
	
	@Override
	public String toString()
	{
		return data == null ? null : data.toString();
	}
}
