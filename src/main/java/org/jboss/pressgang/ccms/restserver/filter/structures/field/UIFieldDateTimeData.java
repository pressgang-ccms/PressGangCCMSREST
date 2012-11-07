package org.jboss.pressgang.ccms.restserver.filter.structures.field;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UIFieldDateTimeData extends UIFieldDataBase<DateTime> {
    private static final Logger log = LoggerFactory.getLogger(UIFieldDateTimeData.class);

    public UIFieldDateTimeData(final String name, final String description) {
        super(name, description);
    }

    @Override
    public DateTime getData() {
        return this.data == null ? null : data;
    }

    public Date getDateData() {
        return this.data == null ? null : data.toDate();
    }

    @Override
    public void setData(final DateTime data) {
        this.data = data;
    }

    public void setData(final Date data) {
        this.data = data == null ? null : new DateTime(data);
    }

    @Override
    public void setData(final String value) {
        try {
            if (value == null || value.length() == 0)
                data = null;
            else
                data = new DateTime(ISODateTimeFormat.dateTime().parseDateTime(value));
        } catch (final Exception ex) {
            log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", description, value);
        }
    }

    @Override
    public String toString() {
        return data == null ? null : ISODateTimeFormat.dateTime().print(data);
    }
}
