package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterFieldCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTFilterFieldCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterFieldV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.entity.FilterField;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;

public class FilterFieldV1Factory extends
        RESTDataObjectFactory<RESTFilterFieldV1, FilterField, RESTFilterFieldCollectionV1, RESTFilterFieldCollectionItemV1> {

    public FilterFieldV1Factory() {
        super(FilterField.class);
    }

    @Override
    public RESTFilterFieldV1 createRESTEntityFromDBEntity(final FilterField entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter filterEntity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTFilterFieldV1 retValue = new RESTFilterFieldV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getFilterFieldId());
        retValue.setDescription(entity.getDescription());
        retValue.setName(entity.getField());
        retValue.setValue(entity.getValue());

        // REVISIONS
        if (revision == null) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTFilterFieldV1, FilterField, RESTFilterFieldCollectionV1, RESTFilterFieldCollectionItemV1>()
                    .create(RESTFilterFieldCollectionV1.class, new FilterFieldV1Factory(), entity, entity.getRevisions(entityManager),
                            RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
        }

        if (expandParentReferences && expand != null && entity.getFilter() != null) {
            retValue.setFilter(new FilterV1Factory().createRESTEntityFromDBEntity(entity.getFilter(), baseUrl, dataType,
                    expand.get(RESTFilterFieldV1.FILTER_NAME), revision, expandParentReferences, entityManager));
        }

        retValue.setLogDetails(new LogDetailsV1Factory().create(entity, revision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand,
                dataType, baseUrl, entityManager));

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final FilterField entity,
            final RESTFilterFieldV1 dataObject) {
        if (dataObject.hasParameterSet(RESTFilterFieldV1.DESCRIPTION_NAME))
            entity.setDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTFilterFieldV1.NAME_NAME))
            entity.setField(dataObject.getName());
        if (dataObject.hasParameterSet(RESTFilterFieldV1.VALUE_NAME))
            entity.setValue(dataObject.getValue());

        entityManager.persist(entity);
    }

}
