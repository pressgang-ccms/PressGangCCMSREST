package org.jboss.pressgang.ccms.server.rest.v1;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.model.FilterTag;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTFilterTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

public class FilterTagV1Factory extends RESTDataObjectFactory<RESTFilterTagV1, FilterTag, RESTFilterTagCollectionV1,
        RESTFilterTagCollectionItemV1> {
    public FilterTagV1Factory() {
        super(FilterTag.class);
    }

    @Override
    public RESTFilterTagV1 createRESTEntityFromDBEntityInternal(final FilterTag entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, boolean expandParentReferences, final EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTFilterTagV1 retValue = new RESTFilterTagV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTFilterTagV1.TAG_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getFilterTagId());
        retValue.setState(entity.getTagState());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    new RESTDataObjectCollectionFactory<RESTFilterTagV1, FilterTag, RESTFilterTagCollectionV1,
                            RESTFilterTagCollectionItemV1>().create(
                            RESTFilterTagCollectionV1.class, new FilterTagV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        // PARENT
        if (expandParentReferences && expand != null && expand.contains(RESTFilterTagV1.FILTER_NAME) && entity.getFilter() != null) {
            retValue.setFilter(new FilterV1Factory().createRESTEntityFromDBEntity(entity.getFilter(), baseUrl, dataType,
                    expand.get(RESTFilterTagV1.FILTER_NAME), revision, expandParentReferences, entityManager));
        }

        // FILTER TAG
        if (expand != null && expand.contains(RESTFilterTagV1.TAG_NAME) && entity.getTag() != null) {
            retValue.setTag(new TagV1Factory().createRESTEntityFromDBEntity(entity.getTag(), baseUrl, dataType,
                    expand.get(RESTFilterTagV1.TAG_NAME), revision, expandParentReferences, entityManager));
        }

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntityFirstPass(final EntityManager entityManager,
            Map<RESTBaseEntityV1<?, ?, ?>, AuditedEntity> newEntityCache, final FilterTag entity, final RESTFilterTagV1 dataObject) {
        if (dataObject.hasParameterSet(RESTFilterTagV1.STATE_NAME))
            entity.setTagState(dataObject.getState());
    }

    @Override
    public void syncDBEntityWithRESTEntitySecondPass(EntityManager entityManager,
            Map<RESTBaseEntityV1<?, ?, ?>, AuditedEntity> newEntityCache, FilterTag entity, RESTFilterTagV1 dataObject) {
        /* Set the Tag for the FilterTag */
        if (dataObject.hasParameterSet(RESTFilterTagV1.TAG_NAME)) {
            final RESTTagV1 restEntity = dataObject.getTag();

            if (restEntity != null) {
                final Tag dbEntity = RESTv1Utilities.findEntity(entityManager, newEntityCache, restEntity, Tag.class);
                if (dbEntity == null)
                    throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());

                entity.setTag(dbEntity);
            } else {
                entity.setTag(null);
            }
        }
    }
}
