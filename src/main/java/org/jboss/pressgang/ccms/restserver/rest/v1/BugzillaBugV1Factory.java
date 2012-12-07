package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.BugzillaBug;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTBugzillaBugCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTBugzillaBugCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTBugzillaBugV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.restserver.utils.EnversUtilities;

public class BugzillaBugV1Factory extends
        RESTDataObjectFactory<RESTBugzillaBugV1, BugzillaBug, RESTBugzillaBugCollectionV1, RESTBugzillaBugCollectionItemV1> {

    public BugzillaBugV1Factory() {
        super(BugzillaBug.class);
    }

    @Override
    public RESTBugzillaBugV1 createRESTEntityFromDBEntityInternal(final BugzillaBug entity, final String baseUrl, String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTBugzillaBugV1 retValue = new RESTBugzillaBugV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getBugzillaBugId());
        retValue.setIsOpen(entity.getBugzillaBugOpen());
        retValue.setBugId(entity.getBugzillaBugBugzillaId());
        retValue.setSummary(entity.getBugzillaBugSummary());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTBugzillaBugV1, BugzillaBug, RESTBugzillaBugCollectionV1, RESTBugzillaBugCollectionItemV1>()
                    .create(RESTBugzillaBugCollectionV1.class, new BugzillaBugV1Factory(), entity, EnversUtilities.getRevisions(entityManager, entity),
                            RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
        }

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final BugzillaBug entity,
            final RESTBugzillaBugV1 dataObject) {
        if (dataObject.hasParameterSet(RESTBugzillaBugV1.BUG_ID))
            entity.setBugzillaBugBugzillaId(dataObject.getBugId());
        if (dataObject.hasParameterSet(RESTBugzillaBugV1.BUG_ISOPEN))
            entity.setBugzillaBugOpen(dataObject.getIsOpen());
        if (dataObject.hasParameterSet(RESTBugzillaBugV1.BUG_SUMMARY))
            entity.setBugzillaBugSummary(dataObject.getSummary());
    }

}
