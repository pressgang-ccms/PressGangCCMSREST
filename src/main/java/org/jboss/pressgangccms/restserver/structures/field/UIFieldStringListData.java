package org.jboss.pressgangccms.restserver.structures.field;

import java.util.Arrays;
import java.util.List;

import org.jboss.pressgangccms.restserver.utils.SkynetExceptionUtilities;
import org.jboss.pressgangccms.utils.common.CollectionUtilities;

public class UIFieldStringListData extends UIFieldListDataBase<String>
{
	public UIFieldStringListData(final String name, final String description)
	{
		super(name, description);
	}
	
	@Override
	public List<String> getData() {
		return this.data;
	}

	@Override
	public void setData(List<String> data) {
		this.data = data;
	}

	@Override
	public void setData(String value) {
		try
		{
			this.data = Arrays.asList(value.split(","));
		}
		catch (final Exception ex)
		{
			// could not parse, so silently fail
			SkynetExceptionUtilities.handleException(ex, true, "Probably a malformed URL query parameter for the \"" + description + "\" parameter");
		}
	}

	@Override
	public String toString()
	{
		return data == null ? null : CollectionUtilities.toSeperatedString(data, ",");
	}
}
