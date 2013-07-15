package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.Project;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTProjectCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTProjectCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTProjectV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class ProjectV1Factory extends RESTDataObjectFactory<RESTProjectV1, Project, RESTProjectCollectionV1, RESTProjectCollectionItemV1> {
    @Inject
    protected TagV1Factory tagFactory;

    @Override
    public RESTProjectV1 createRESTEntityFromDBEntityInternal(final Project entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTProjectV1 retValue = new RESTProjectV1();

        retValue.setId(entity.getProjectId());
        retValue.setDescription(entity.getProjectDescription());
        retValue.setName(entity.getProjectName());

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTv1Constants.TAGS_EXPANSION_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTDataObjectCollectionFactory.create(RESTProjectCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // TAGS
        if (expand != null && expand.contains(RESTProjectV1.TAGS_NAME)) {
            retValue.setTags(
                    RESTDataObjectCollectionFactory.create(RESTTagCollectionV1.class, tagFactory, entity.getTags(), RESTProjectV1.TAGS_NAME,
                            dataType, expand, baseUrl, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.PROJECT_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntityFirstPass(final Project entity, final RESTProjectV1 dataObject) {
        if (dataObject.hasParameterSet(RESTProjectV1.DESCRIPTION_NAME)) entity.setProjectDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTProjectV1.NAME_NAME)) entity.setProjectName(dataObject.getName());
    }

    @Override
    public void syncDBEntityWithRESTEntitySecondPass(Project entity, RESTProjectV1 dataObject) {
        /* Many To Many - Add will create a mapping */
        if (dataObject.hasParameterSet(
                RESTProjectV1.TAGS_NAME) && dataObject.getTags() != null && dataObject.getTags().getItems() != null) {
            dataObject.getTags().removeInvalidChangeItemRequests();

            for (final RESTTagCollectionItemV1 restEntityItem : dataObject.getTags().getItems()) {
                final RESTTagV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsRemoveItem()) {
                    final Tag dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, restEntity, Tag.class);
                    if (dbEntity == null)
                        throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());

                    if (restEntityItem.returnIsAddItem()) {
                        entity.addRelationshipTo(dbEntity);
                    } else if (restEntityItem.returnIsRemoveItem()) {
                        entity.removeRelationshipTo(dbEntity.getTagId());
                    }
                }
            }
        }
    }

    @Override
    protected Class<Project> getDatabaseClass() {
        return Project.class;
    }
}
