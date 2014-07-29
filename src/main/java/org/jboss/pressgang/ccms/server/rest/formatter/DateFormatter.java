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

package org.jboss.pressgang.ccms.server.rest.formatter;

import java.lang.annotation.Annotation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.resteasy.spi.StringParameterUnmarshaller;
import org.jboss.resteasy.util.FindAnnotation;

public class DateFormatter implements StringParameterUnmarshaller<Date> {
    private SimpleDateFormat formatter;

    @Override
    public void setAnnotations(final Annotation[] annotations) {
        final DateFormat format = FindAnnotation.findAnnotation(annotations, DateFormat.class);
        formatter = new SimpleDateFormat(format.value());
    }

    @Override
    public Date fromString(final String str) {
        try {
            return formatter.parse(str);
        } catch (final ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

}
