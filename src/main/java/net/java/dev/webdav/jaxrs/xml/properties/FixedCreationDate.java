/*
  Copyright 2011-2014 Red Hat

  This file is part of PressGang CCMS.

  PressGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PressGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PressGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.java.dev.webdav.jaxrs.xml.properties;

import net.java.dev.webdav.jaxrs.NullArgumentException;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * WebDAV creationdate Property.
 * <p/>
 * This version of the creation date uses a different format to the standard one supplied by the jax-rs webdav
 * extension. The original format caused errors in XML Mind.
 *
 * @author Markus KARG (mkarg@users.dev.java.net)
 * @see <a href="http://www.webdav.org/specs/rfc4918.html#PROPERTY_creationdate">Chapter 15.1 "creationdate Property" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
 */
@XmlRootElement(name = "creationdate")
public final class FixedCreationDate {

    private static final String CREATION_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private Date dateTime;

    /**
     * Creates an empty (thus <em>invalid</em>) instance. Use <em>only</em> to
     * list property name within response to &lt;propname/&gt; request. Not to
     * be used for creation of valid instances of this property; use
     * {@link #FixedCreationDate(java.util.Date)} instead.
     */
    public FixedCreationDate() {
        // Keeping defaults by intention.
    }

    public FixedCreationDate(final Date dateTime) {
        if (dateTime == null)
            throw new NullArgumentException("dateTime");

        this.dateTime = dateTime;
    }

    public final Date getDateTime() {
        return this.dateTime;
    }

    @SuppressWarnings("unused")
    @XmlValue
    private final String getXmlDateTime() {
        return new SimpleDateFormat(CREATION_DATE_FORMAT, Locale.US).format(dateTime);
    }

    @SuppressWarnings("unused")
    private final void setXmlDateTime(final String rfc3339DateTime) throws ParseException {
        this.dateTime = new SimpleDateFormat(CREATION_DATE_FORMAT, Locale.US).parse(rfc3339DateTime);
    }

}
