package org.jboss.pressgangccms.restserver.structures.field;

import java.util.Date;

import org.jboss.pressgangccms.restserver.utils.SkynetExceptionUtilities;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;


public class UIFieldDateTimeData extends UIFieldDataBase<DateTime>
{
	public UIFieldDateTimeData(final String name, final String description)
	{
		super(name, description);
	}
	
	@Override
	public DateTime getData()
	{
		return this.data == null ? null : data;
	}
	
	public Date getDateData()
	{
		return this.data == null ? null : data.toDate();
	}

	@Override
	public void setData(final DateTime data)
	{
		this.data = data;
	}
	
	public void setData(final Date data)
	{
		this.data = data == null ? null : new DateTime(data);
	}
	
	@Override
	public void setData(final String value)
	{
		try
		{
			if (value == null || value.length() == 0)
				data = null;
			else
				data = new DateTime(ISODateTimeFormat.dateTime().parseDateTime(value));
		}
		catch (final Exception ex)
		{
			SkynetExceptionUtilities.handleException(ex, true, "Probably invalid input from a malformed URL query parameter for the \"" + description + "\" parameter");
		}
	}
	
	@Override
	public String toString()
	{
		return data == null ? null : ISODateTimeFormat.dateTime().print(data);
	}
}
