package org.jboss.pressgang.ccms.restserver.webdav.resources.hierarchy;

import net.java.dev.webdav.jaxrs.xml.elements.MultiStatus;
import net.java.dev.webdav.jaxrs.xml.elements.Response;
import org.jboss.pressgang.ccms.restserver.webdav.managers.DeleteManager;
import org.jboss.pressgang.ccms.restserver.webdav.resources.InternalResource;
import org.jboss.pressgang.ccms.restserver.webdav.resources.MultiStatusReturnValue;
import org.jboss.pressgang.ccms.restserver.webdav.resources.hierarchy.topics.InternalResourceTopicVirtualFolder;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * The root folder of the WebDAV hierarchy.
 */
public class InternalResourceRoot extends InternalResource {
    public InternalResourceRoot(final UriInfo uriInfo, @NotNull final DeleteManager deleteManager, @Nullable final String remoteAddress, final String stringId) {
        super(uriInfo, deleteManager, remoteAddress, stringId);
    }

    @Override
    public MultiStatusReturnValue propfind(final int depth) {

        if (getUriInfo() == null) {
            throw new IllegalStateException("Can not perform propfind without uriInfo");
        }

        if (depth == 0) {
            /* A depth of zero means we are returning information about this item only */
            final Response folder = getFolderProperties(getUriInfo());

            return new MultiStatusReturnValue(207, new MultiStatus(folder));
        } else {
            /* Otherwise we are retuning info on the children in this collection */
            final List<Response> responses = new ArrayList<Response>();

            /* The topic collection */
            responses.add(getFolderProperties(getUriInfo(), InternalResourceTopicVirtualFolder.RESOURCE_NAME));

            final MultiStatus st = new MultiStatus(responses.toArray(new Response[responses.size()]));
            return new MultiStatusReturnValue(207, st);
        }

    }
}
