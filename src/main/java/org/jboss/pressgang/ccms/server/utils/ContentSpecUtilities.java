package org.jboss.pressgang.ccms.server.utils;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.contentspec.constants.CSConstants;
import org.jboss.pressgang.ccms.contentspec.utils.CSTransformer;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpec;
import org.jboss.pressgang.ccms.provider.ContentSpecProvider;
import org.jboss.pressgang.ccms.provider.DBProviderFactory;
import org.jboss.pressgang.ccms.utils.common.HashUtilities;
import org.jboss.pressgang.ccms.wrapper.ContentSpecWrapper;

public class ContentSpecUtilities extends org.jboss.pressgang.ccms.contentspec.utils.ContentSpecUtilities {

    private ContentSpecUtilities() {
    }

    public static String getContentSpecText(final Integer id, final EntityManager entityManager) {
        return getContentSpecText(id, null, entityManager);
    }

    public static String getContentSpecText(final Integer id, final Integer revision, final EntityManager entityManager) {
        final DBProviderFactory providerFactory = ProviderUtilities.getDBProviderFactory(entityManager);
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

    /**
     * Fixes a failed Content Spec so that the ID and CHECKSUM are included. This is primarily an issue when creating new content specs
     * and they are initially invalid.
     *
     * @param contentSpec The content spec to fix.
     * @return The fixed failed content spec string.
     */
    public static String fixFailedContentSpec(final ContentSpec contentSpec) {
        if (contentSpec.getFailedContentSpec() == null || contentSpec.getFailedContentSpec().isEmpty()) {
            return null;
        } else {
            if (contentSpec.getId() == null || getContentSpecChecksum(contentSpec.getFailedContentSpec()) != null) {
                return contentSpec.getFailedContentSpec();
            } else {
                String cleanContentSpec = removeChecksumAndId(contentSpec.getFailedContentSpec());
                return CSConstants.CHECKSUM_TITLE + "=" + HashUtilities.generateMD5(cleanContentSpec) + "\n" + CSConstants.ID_TITLE + "=" +
                        contentSpec.getId() + "\n" + cleanContentSpec;
            }
        }
    }
}
