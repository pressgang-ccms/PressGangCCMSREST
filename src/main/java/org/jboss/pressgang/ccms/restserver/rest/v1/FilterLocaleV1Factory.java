package org.jboss.pressgang.ccms.restserver.rest.v1;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.Filter;
import org.jboss.pressgang.ccms.model.FilterLocale;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterLocaleCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTFilterLocaleCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterLocaleV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.restserver.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

public class FilterLocaleV1Factory extends RESTDataObjectFactory<RESTFilterLocaleV1, FilterLocale, RESTFilterLocaleCollectionV1,
        RESTFilterLocaleCollectionItemV1> {

    public FilterLocaleV1Factory() {
        super(FilterLocale.class);
    }

    @Override
    public RESTFilterLocaleV1 createRESTEntityFromDBEntityInternal(final FilterLocale entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, boolean expandParentReferences, final EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTFilterLocaleV1 retValue = new RESTFilterLocaleV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getFilterLocaleId());
        retValue.setState(entity.getLocaleState());
        retValue.setLocale(entity.getLocaleName());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    new RESTDataObjectCollectionFactory<RESTFilterLocaleV1, FilterLocale, RESTFilterLocaleCollectionV1,
                            RESTFilterLocaleCollectionItemV1>().create(
                            RESTFilterLocaleCollectionV1.class, new FilterLocaleV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        // PARENT
        if (expandParentReferences && expand != null && expand.contains(RESTFilterLocaleV1.FILTER_NAME) && entity.getFilter() != null) {
            retValue.setFilter(new FilterV1Factory().createRESTEntityFromDBEntity(entity.getFilter(), baseUrl, dataType,
                    expand.get(RESTFilterLocaleV1.FILTER_NAME), revision, expandParentReferences, entityManager));
        }

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final FilterLocale entity,
            final RESTFilterLocaleV1 dataObject) throws BadRequestException {
        if (dataObject.hasParameterSet(RESTFilterLocaleV1.LOCALE_NAME))
            entity.setLocaleName(dataObject.getLocale());
        if (dataObject.hasParameterSet(RESTFilterLocaleV1.STATE_NAME))
            entity.setLocaleState(dataObject.getState());

        if (dataObject.hasParameterSet(RESTFilterLocaleV1.FILTER_NAME)) {
            final RESTFilterV1 restEntity = dataObject.getFilter();

            if (restEntity != null) {
                final Filter dbEntity = entityManager.find(Filter.class, restEntity.getId());
                if (dbEntity == null)
                    throw new BadRequestException("No Filter entity was found with the primary key " + restEntity.getId());

                entity.setFilter(dbEntity);
            }
        }

        entityManager.persist(entity);
    }

}
