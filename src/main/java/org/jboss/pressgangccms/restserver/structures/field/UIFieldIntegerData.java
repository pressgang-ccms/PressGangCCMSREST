package org.jboss.pressgangccms.restserver.structures.field;

import org.jboss.pressgangccms.restserver.utils.SkynetExceptionUtilities;

public class UIFieldIntegerData extends UIFieldDataBase<Integer>
{
	public UIFieldIntegerData(final String name, final String description)
	{
		super(name, description);
	}
	
	@Override
	public Integer getData()
	{
		return this.data;
	}

	@Override
	public void setData(final Integer data)
	{
		this.data = data;
	}
	
	@Override
	public void setData(final String value)
	{
		try
		{
			this.data = (value == null ? null : Integer.parseInt(value));
		}
		catch (final Exception ex)
		{
			// could not parse integer, so silently fail
			SkynetExceptionUtilities.handleException(ex, true, "Probably a malformed URL query parameter for the \"" + description + "\" parameter");
		}
	}
	
	@Override
	public String toString()
	{
		return data == null ? null : data.toString();
	}
}
