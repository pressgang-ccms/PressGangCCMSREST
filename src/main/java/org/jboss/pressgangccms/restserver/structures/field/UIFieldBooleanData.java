package org.jboss.pressgangccms.restserver.structures.field;

import org.jboss.pressgangccms.restserver.utils.SkynetExceptionUtilities;


public class UIFieldBooleanData extends UIFieldDataBase<Boolean>
{
	public UIFieldBooleanData(final String name, final String description)
	{
		super(name, description);
	}
	
	@Override
	public Boolean getData() {
		return this.data;
	}

	@Override
	public void setData(final Boolean data) {
		this.data = data ? true : null;
	}
	
	@Override
	public void setData(final String value)
	{
		try
		{
			this.data = (value == null ? null : (Boolean.parseBoolean(value) ? true : null));
		}
		catch (final Exception ex)
		{
			// could not parse boolean, so silently fail
			SkynetExceptionUtilities.handleException(ex, true, "Probably a malformed URL query parameter for the \"" + description + "\" parameter");
		}
	}
	
	@Override
	public String toString()
	{
		return data == null ? "" : data.toString();
	}
}
