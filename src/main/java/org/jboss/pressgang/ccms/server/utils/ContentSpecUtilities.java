package org.jboss.pressgang.ccms.server.utils;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.contentspec.utils.CSTransformer;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpec;
import org.jboss.pressgang.ccms.provider.ContentSpecProvider;
import org.jboss.pressgang.ccms.provider.DBProviderFactory;
import org.jboss.pressgang.ccms.wrapper.ContentSpecWrapper;
import org.jboss.resteasy.spi.InternalServerErrorException;
import org.jboss.resteasy.spi.NotFoundException;

public class ContentSpecUtilities extends org.jboss.pressgang.ccms.contentspec.utils.ContentSpecUtilities {

    private ContentSpecUtilities() {
    }

    public static ContentSpecWrapper getContentSpecEntity(final Integer id, final Integer revision, final DBProviderFactory providerFactory) {
        try {
            if (revision == null) {
                return providerFactory.getProvider(ContentSpecProvider.class).getContentSpec(id);
            } else {
                return providerFactory.getProvider(ContentSpecProvider.class).getContentSpec(id, revision);
            }
        } catch (org.jboss.pressgang.ccms.provider.exception.NotFoundException e) {
            throw new NotFoundException(e);
        } catch (org.jboss.pressgang.ccms.provider.exception.InternalServerErrorException e) {
            throw new InternalServerErrorException(e);
        }
    }

    public static org.jboss.pressgang.ccms.contentspec.ContentSpec getContentSpec(final Integer id, final Integer revision,
            final EntityManager entityManager) {
        final DBProviderFactory providerFactory = ProviderUtilities.getDBProviderFactory(entityManager);
        return getContentSpec(id, revision, providerFactory);
    }

    public static org.jboss.pressgang.ccms.contentspec.ContentSpec getContentSpec(final Integer id, final Integer revision,
            final DBProviderFactory providerFactory) {
        final ContentSpecWrapper entity = getContentSpecEntity(id, revision, providerFactory);
        return CSTransformer.transform(entity, providerFactory, false);
    }

    public static String getContentSpecText(final Integer id, final EntityManager entityManager) {
        return getContentSpecText(id, null, entityManager);
    }

    public static String getContentSpecText(final Integer id, final Integer revision, final EntityManager entityManager) {
        return getContentSpecText(id, revision, entityManager, true);
    }

    protected static String getContentSpecText(final Integer id, final Integer revision, final EntityManager entityManager, boolean fix) {
        final DBProviderFactory providerFactory = ProviderUtilities.getDBProviderFactory(entityManager);
        final ContentSpecWrapper entity = getContentSpecEntity(id, revision, providerFactory);

        if (fix && ((ContentSpec) entity.unwrap()).getFailedContentSpec() != null) {
            return fixFailedContentSpec(entity, null, false);
        } else {
            final org.jboss.pressgang.ccms.contentspec.ContentSpec contentSpec = CSTransformer.transform(entity, providerFactory, false);
            return contentSpec.toString(false);
        }
    }

    public static final String fixFailedContentSpec(final ContentSpec contentSpec) {
        return fixFailedContentSpec(contentSpec.getId(), contentSpec.getFailedContentSpec(), null, false);
    }
}
