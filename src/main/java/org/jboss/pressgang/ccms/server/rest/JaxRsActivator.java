/*
  Copyright 2011-2014 Red Hat, Inc

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

package org.jboss.pressgang.ccms.server.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

import net.java.dev.webdav.jaxrs.xml.WebDavContextResolver;
import org.jboss.pressgang.ccms.server.interceptor.ReadOnlyInterceptor;
import org.jboss.pressgang.ccms.server.rest.v1.RESTv1;
import org.jboss.pressgang.ccms.server.rest.v1.interceptor.RESTv1CreateResponseInterceptor;
import org.jboss.pressgang.ccms.server.rest.v1.interceptor.RESTv1VersionHeaderInterceptor;
import org.jboss.pressgang.ccms.server.rest.v1.interceptor.RESTv1VersionInterceptor;
import org.jboss.pressgang.ccms.server.rest.v1.mapper.BadRequestExceptionMapper;
import org.jboss.pressgang.ccms.server.rest.v1.mapper.IllegalArgumentExceptionMapper;
import org.jboss.pressgang.ccms.server.rest.v1.mapper.IllegalStateExceptionMapper;
import org.jboss.pressgang.ccms.server.rest.v1.mapper.InternalServerErrorExceptionMapper;
import org.jboss.pressgang.ccms.server.rest.v1.mapper.JAXBMarshalExceptionMapper;
import org.jboss.pressgang.ccms.server.rest.v1.mapper.JAXBUnmarshalExceptionMapper;
import org.jboss.pressgang.ccms.server.rest.v1.mapper.MethodNotAllowedExceptionMapper;
import org.jboss.pressgang.ccms.server.rest.v1.mapper.NotAcceptableExceptionMapper;
import org.jboss.pressgang.ccms.server.rest.v1.mapper.NotFoundExceptionMapper;
import org.jboss.pressgang.ccms.server.rest.v1.mapper.ReaderExceptionMapper;
import org.jboss.pressgang.ccms.server.rest.v1.mapper.UnauthorizedExceptionMapper;
import org.jboss.pressgang.ccms.server.rest.v1.mapper.WriterExceptionMapper;
import org.jboss.pressgang.ccms.server.webdav.jaxrs.WebDavResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class extending {@link javax.ws.rs.core.Application} and annotated with @ApplicationPath is the Java EE 6
 * "no XML" approach to activating JAX-RS.
 * <p/>
 * <p>
 * Resources are served relative to the servlet path specified in the {@link javax.ws.rs.ApplicationPath}
 * annotation.
 * </p>
 */
@ApplicationPath("/")
public class JaxRsActivator extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(JaxRsActivator.class);
    private final HashSet<Class<?>> classes = new HashSet<Class<?>>();

    public JaxRsActivator() {
        LOGGER.debug("ENTER JaxRsActivator()");

        // Endpoints
        classes.add(REST.class);
        classes.add(RESTv1.class);
        classes.add(WebDavResource.class);

        // Providers
        classes.add(CustomJacksonJsonProvider.class);
        classes.add(WebDavContextResolver.class);

        // Interceptors
        classes.add(RESTv1VersionHeaderInterceptor.class);
        classes.add(RESTv1VersionInterceptor.class);
        classes.add(RESTv1CreateResponseInterceptor.class);
        classes.add(ReadOnlyInterceptor.class);

        // Exception Mappers
        classes.add(BadRequestExceptionMapper.class);
        classes.add(InternalServerErrorExceptionMapper.class);
        classes.add(JAXBMarshalExceptionMapper.class);
        classes.add(JAXBUnmarshalExceptionMapper.class);
        classes.add(MethodNotAllowedExceptionMapper.class);
        classes.add(NotAcceptableExceptionMapper.class);
        classes.add(NotFoundExceptionMapper.class);
        classes.add(ReaderExceptionMapper.class);
        classes.add(UnauthorizedExceptionMapper.class);
        classes.add(WriterExceptionMapper.class);
        classes.add(IllegalArgumentExceptionMapper.class);
        classes.add(IllegalStateExceptionMapper.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
