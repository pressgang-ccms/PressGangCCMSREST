package org.jboss.pressgang.ccms.restserver.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * A class extending {@link javax.ws.rs.core.Application} and annotated with @ApplicationPath is the Java EE 6
 * "no XML" approach to activating JAX-RS.
 * <p/>
 * <p>
 * Resources are served relative to the servlet path specified in the {@link javax.ws.rs.ApplicationPath}
 * annotation.
 * </p>
 */
@ApplicationPath("/rest")
public class JaxRsActivator extends Application {
    /*private Set<Object> singletons = new HashSet();
    private Set<Class<?>> classes = new HashSet();

    public JaxRsActivator() {
        // ENDPOINTS
        this.classes.add(RESTv1.class);
        this.classes.add(REST.class);

        // INTERCEPTOR
        this.classes.add(RESTVersionInterceptor.class);

        // STRING CONVERTERS
        this.classes.add(RESTLogDetailsV1JSONConverter.class);
        this.classes.add(RESTBlobConstantV1JSONConverter.class);
        this.classes.add(RESTCategoryV1JSONConverter.class);
        this.classes.add(RESTFilterV1JSONConverter.class);
        this.classes.add(RESTImageV1JSONConverter.class);
        this.classes.add(RESTIntegerConstantV1JSONConverter.class);
        this.classes.add(RESTProjectV1JSONConverter.class);
        this.classes.add(RESTPropertyCategoryV1JSONConverter.class);
        this.classes.add(RESTPropertyTagV1JSONConverter.class);
        this.classes.add(RESTRoleV1JSONConverter.class);
        this.classes.add(RESTStringConstantV1JSONConverter.class);
        this.classes.add(RESTTagV1JSONConverter.class);
        this.classes.add(RESTTopicV1JSONConverter.class);
        this.classes.add(RESTTranslatedTopicV1JSONConverter.class);
        this.classes.add(RESTUserV1JSONConverter.class);
        this.classes.add(RESTContentSpecV1JSONConverter.class);
        this.classes.add(RESTCSNodeV1JSONConverter.class);
        this.classes.add(RESTTranslatedCSNodeV1JSONConverter.class);
        this.classes.add(RESTTranslatedContentSpecV1JSONConverter.class);
        this.classes.add(RESTBlobConstantCollectionV1JSONConverter.class);
        this.classes.add(RESTCategoryCollectionV1JSONConverter.class);
        this.classes.add(RESTFilterCollectionV1JSONConverter.class);
        this.classes.add(RESTImageCollectionV1JSONConverter.class);
        this.classes.add(RESTIntegerConstantCollectionV1JSONConverter.class);
        this.classes.add(RESTProjectCollectionV1JSONConverter.class);
        this.classes.add(RESTPropertyCategoryCollectionV1JSONConverter.class);
        this.classes.add(RESTPropertyTagCollectionV1JSONConverter.class);
        this.classes.add(RESTRoleCollectionV1JSONConverter.class);
        this.classes.add(RESTStringConstantCollectionV1JSONConverter.class);
        this.classes.add(RESTTagCollectionV1JSONConverter.class);
        this.classes.add(RESTTopicCollectionV1JSONConverter.class);
        this.classes.add(RESTTranslatedTopicCollectionV1JSONConverter.class);
        this.classes.add(RESTUserCollectionV1JSONConverter.class);
        this.classes.add(RESTContentSpecCollectionV1JSONConverter.class);
        this.classes.add(RESTCSNodeCollectionV1JSONConverter.class);
        this.classes.add(RESTTranslatedCSNodeCollectionV1JSONConverter.class);
        this.classes.add(RESTTranslatedContentSpecCollectionV1JSONConverter.class);
    }

    public Set<Class<?>> getClasses()
    {
        return this.classes;
    }

    public Set<Object> getSingletons()
    {
        return this.singletons;
    }*/
}
