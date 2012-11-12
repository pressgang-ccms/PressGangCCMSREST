package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.rest.v1.collections.RESTProjectCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTProjectCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTProjectV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.entity.Project;
import org.jboss.pressgang.ccms.restserver.entity.Tag;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;

public class ProjectV1Factory extends
        RESTDataObjectFactory<RESTProjectV1, Project, RESTProjectCollectionV1, RESTProjectCollectionItemV1> {
    ProjectV1Factory() {
        super(Project.class);
    }

    @Override
    public RESTProjectV1 createRESTEntityFromDBEntity(final Project entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter topic can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTProjectV1 retValue = new RESTProjectV1();

        retValue.setId(entity.getProjectId());
        retValue.setDescription(entity.getProjectDescription());
        retValue.setName(entity.getProjectName());

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTv1Constants.TAGS_EXPANSION_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        // REVISIONS
        if (revision == null) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTProjectV1, Project, RESTProjectCollectionV1, RESTProjectCollectionItemV1>()
                    .create(RESTProjectCollectionV1.class, new ProjectV1Factory(), entity, entity.getRevisions(entityManager),
                            RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
        }
        
        // TAGS
        retValue.setTags(new RESTDataObjectCollectionFactory<RESTTagV1, Tag, RESTTagCollectionV1, RESTTagCollectionItemV1>()
                .create(RESTTagCollectionV1.class, new TagV1Factory(), entity.getTags(), RESTv1Constants.TAGS_EXPANSION_NAME,
                        dataType, expand, baseUrl, entityManager));

        retValue.setLinks(baseUrl, RESTv1Constants.PROJECT_URL_NAME, dataType, retValue.getId());
        retValue.setLogDetails(new LogDetailsV1Factory().create(entity, revision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand,
                dataType, baseUrl, entityManager));

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final Project entity,
            final RESTProjectV1 dataObject) throws InvalidParameterException {
        if (dataObject.hasParameterSet(RESTProjectV1.DESCRIPTION_NAME))
            entity.setProjectDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTProjectV1.NAME_NAME))
            entity.setProjectName(dataObject.getName());

        entityManager.persist(entity);

        /* Many To Many - Add will create a mapping */
        if (dataObject.hasParameterSet(RESTProjectV1.TAGS_NAME) && dataObject.getTags() != null
                && dataObject.getTags().getItems() != null) {
            dataObject.getTags().removeInvalidChangeItemRequests();

            for (final RESTTagCollectionItemV1 restEntityItem : dataObject.getTags().getItems()) {
                final RESTTagV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsRemoveItem()) {
                    final Tag dbEntity = entityManager.find(Tag.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No Tag entity was found with the primary key "
                                + restEntity.getId());

                    if (restEntityItem.returnIsAddItem()) {
                        entity.addRelationshipTo(dbEntity);
                    } else if (restEntityItem.returnIsRemoveItem()) {
                        entity.removeRelationshipTo(dbEntity.getTagId());
                    }
                }
            }
        }
    }
}
