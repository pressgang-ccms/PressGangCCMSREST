package org.jboss.pressgang.ccms.server.rest.v1.interceptor;

import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;

import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.jaxrsinterfaces.RESTInterfaceV1;
import org.jboss.pressgang.ccms.server.rest.v1.RESTv1;
import org.jboss.pressgang.ccms.utils.common.VersionUtilities;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.interception.AcceptedByMethod;
import org.jboss.resteasy.spi.interception.PostProcessInterceptor;

@Provider
@ServerInterceptor
public class RESTv1VersionHeaderInterceptor implements PostProcessInterceptor, AcceptedByMethod {
    @Override
    public boolean accept(final Class declaring, final Method method) {
        // Only use this interceptor for v1 endpoints.
        return RESTv1.class.equals(declaring);
    }

    @Override
    public void postProcess(ServerResponse response) {
        response.getMetadata().add(RESTv1Constants.X_PRESSGANG_VERSION_HEADER, VersionUtilities.getAPIVersion(RESTInterfaceV1.class));
        response.getMetadata().add(RESTv1Constants.ACCESS_CONTROL_EXPOSE_HEADERS, RESTv1Constants.X_PRESSGANG_VERSION_HEADER);
    }
}