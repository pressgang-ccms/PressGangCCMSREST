package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTFilterCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTProjectV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.entity.Category;
import org.jboss.pressgang.ccms.restserver.entity.FilterCategory;
import org.jboss.pressgang.ccms.restserver.entity.Project;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;


public class FilterCategoryV1Factory
        extends
        RESTDataObjectFactory<RESTFilterCategoryV1, FilterCategory, RESTFilterCategoryCollectionV1, RESTFilterCategoryCollectionItemV1> {

    public FilterCategoryV1Factory() {
        super(FilterCategory.class);
    }

    @Override
    public RESTFilterCategoryV1 createRESTEntityFromDBEntity(final FilterCategory entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter filterCategory can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTFilterCategoryV1 retValue = new RESTFilterCategoryV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTFilterCategoryV1.CATEGORY_NAME);
        expandOptions.add(RESTFilterCategoryV1.PROJECT_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getFilterCategoryId());
        retValue.setState(entity.getCategoryState());

        // REVISIONS
        if (revision == null) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTFilterCategoryV1, FilterCategory, RESTFilterCategoryCollectionV1, RESTFilterCategoryCollectionItemV1>()
                    .create(RESTFilterCategoryCollectionV1.class, new FilterCategoryV1Factory(), entity, entity.getRevisions(entityManager),
                            RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
        }

        if (expandParentReferences && entity.getFilter() != null && expand != null && entity.getFilter() != null) {
            retValue.setFilter(new FilterV1Factory().createRESTEntityFromDBEntity(entity.getFilter(), baseUrl, dataType,
                    expand.get(RESTFilterCategoryV1.FILTER_NAME), revision, expandParentReferences, entityManager));
        }

        // FILTER CATEGORY
        if (expand != null && expand.contains(RESTFilterCategoryV1.CATEGORY_NAME) && entity.getCategory() != null)
            retValue.setCategory(new CategoryV1Factory().createRESTEntityFromDBEntity(entity.getCategory(), baseUrl, dataType,
                    expand.get(RESTFilterCategoryV1.CATEGORY_NAME), revision, expandParentReferences, entityManager));
        
        // FILTER PROJECT
        if (expand != null && expand.contains(RESTFilterCategoryV1.PROJECT_NAME) && entity.getProject() != null)
            retValue.setProject(new ProjectV1Factory().createRESTEntityFromDBEntity(entity.getProject(), baseUrl, dataType,
                    expand.get(RESTFilterCategoryV1.PROJECT_NAME), revision, expandParentReferences, entityManager));

        retValue.setLogDetails(new LogDetailsV1Factory().create(entity, revision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand,
                dataType, baseUrl, entityManager));

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final FilterCategory entity,
            final RESTFilterCategoryV1 dataObject) throws InvalidParameterException {

        if (dataObject.hasParameterSet(RESTFilterCategoryV1.STATE_NAME))
            entity.setCategoryState(dataObject.getState());

        /* Set the Category for the FilterCategory */
        if (dataObject.hasParameterSet(RESTFilterCategoryV1.CATEGORY_NAME)) {
            final RESTCategoryV1 restEntity = dataObject.getCategory();

            if (restEntity != null) {
                final Category dbEntity = entityManager.find(Category.class, restEntity.getId());
                if (dbEntity == null)
                    throw new InvalidParameterException("No Category entity was found with the primary key "
                            + restEntity.getId());

                entity.setCategory(dbEntity);
            } else {
                entity.setCategory(null);
            }
        }

        /* Set the Project for the FilterCategory */
        if (dataObject.hasParameterSet(RESTFilterCategoryV1.PROJECT_NAME)) {
            final RESTProjectV1 restEntity = dataObject.getProject();

            if (restEntity != null) {
                final Project dbEntity = entityManager.find(Project.class, restEntity.getId());
                if (dbEntity == null)
                    throw new InvalidParameterException("No Project entity was found with the primary key "
                            + restEntity.getId());

                entity.setProject(dbEntity);
            } else {
                entity.setProject(null);
            }
        }
    }

}
