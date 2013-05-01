package org.jboss.pressgang.ccms.restserver.utils;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.contentspec.utils.CSTransformer;
import org.jboss.pressgang.ccms.provider.ContentSpecProvider;
import org.jboss.pressgang.ccms.provider.DBProviderFactory;
import org.jboss.pressgang.ccms.wrapper.ContentSpecWrapper;

public class ContentSpecUtilities {

    public static String getContentSpecText(final Integer id, final EntityManager entityManager) {
        return getContentSpecText(id, null, entityManager);
    }

    public static String getContentSpecText(final Integer id, final Integer revision, final EntityManager entityManager) {
        final DBProviderFactory providerFactory = DBProviderFactory.create(entityManager);
        final ContentSpecWrapper entity;
        if (revision == null) {
            entity = providerFactory.getProvider(ContentSpecProvider.class).getContentSpec(id);
        } else {
            entity = providerFactory.getProvider(ContentSpecProvider.class).getContentSpec(id, revision);
        }
        final CSTransformer transformer = new CSTransformer();

        final org.jboss.pressgang.ccms.contentspec.ContentSpec contentSpec = transformer.transform(entity, providerFactory);
        return contentSpec.toString();
    }
}
