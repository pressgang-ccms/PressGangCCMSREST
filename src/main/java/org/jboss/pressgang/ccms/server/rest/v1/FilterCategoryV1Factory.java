package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.Category;
import org.jboss.pressgang.ccms.model.FilterCategory;
import org.jboss.pressgang.ccms.model.Project;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTFilterCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTProjectV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class FilterCategoryV1Factory extends RESTDataObjectFactory<RESTFilterCategoryV1, FilterCategory, RESTFilterCategoryCollectionV1,
        RESTFilterCategoryCollectionItemV1> {
    @Inject
    protected FilterV1Factory filterFactory;
    @Inject
    protected CategoryV1Factory categoryFactory;
    @Inject
    protected ProjectV1Factory projectFactory;

    @Override
    public RESTFilterCategoryV1 createRESTEntityFromDBEntityInternal(final FilterCategory entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTFilterCategoryV1 retValue = new RESTFilterCategoryV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTFilterCategoryV1.CATEGORY_NAME);
        expandOptions.add(RESTFilterCategoryV1.PROJECT_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getFilterCategoryId());
        retValue.setState(entity.getCategoryState());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTDataObjectCollectionFactory.create(RESTFilterCategoryCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // PARENT
        if (expandParentReferences && expand != null && expand.contains(RESTFilterCategoryV1.FILTER_NAME) && entity.getFilter() != null) {
            retValue.setFilter(filterFactory.createRESTEntityFromDBEntity(entity.getFilter(), baseUrl, dataType,
                    expand.get(RESTFilterCategoryV1.FILTER_NAME), revision, expandParentReferences));
        }

        // FILTER CATEGORY
        if (expand != null && expand.contains(RESTFilterCategoryV1.CATEGORY_NAME) && entity.getCategory() != null) retValue.setCategory(
                categoryFactory.createRESTEntityFromDBEntity(entity.getCategory(), baseUrl, dataType,
                        expand.get(RESTFilterCategoryV1.CATEGORY_NAME), revision, expandParentReferences));

        // FILTER PROJECT
        if (expand != null && expand.contains(RESTFilterCategoryV1.PROJECT_NAME) && entity.getProject() != null) retValue.setProject(
                projectFactory.createRESTEntityFromDBEntity(entity.getProject(), baseUrl, dataType,
                        expand.get(RESTFilterCategoryV1.PROJECT_NAME), revision, expandParentReferences));

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final FilterCategory entity, final RESTFilterCategoryV1 dataObject) {

        if (dataObject.hasParameterSet(RESTFilterCategoryV1.STATE_NAME)) entity.setCategoryState(dataObject.getState());

        // Set the Category for the FilterCategory
        if (dataObject.hasParameterSet(RESTFilterCategoryV1.CATEGORY_NAME)) {
            final RESTCategoryV1 restEntity = dataObject.getCategory();

            if (restEntity != null) {
                final Category dbEntity = entityManager.find(Category.class, restEntity.getId());
                if (dbEntity == null)
                    throw new BadRequestException("No Category entity was found with the primary key " + restEntity.getId());

                entity.setCategory(dbEntity);
            } else {
                entity.setCategory(null);
            }
        }

        // Set the Project for the FilterCategory
        if (dataObject.hasParameterSet(RESTFilterCategoryV1.PROJECT_NAME)) {
            final RESTProjectV1 restEntity = dataObject.getProject();

            if (restEntity != null) {
                final Project dbEntity = entityManager.find(Project.class, restEntity.getId());
                if (dbEntity == null)
                    throw new BadRequestException("No Project entity was found with the primary key " + restEntity.getId());

                entity.setProject(dbEntity);
            } else {
                entity.setProject(null);
            }
        }
    }

    @Override
    protected Class<FilterCategory> getDatabaseClass() {
        return FilterCategory.class;
    }

}
