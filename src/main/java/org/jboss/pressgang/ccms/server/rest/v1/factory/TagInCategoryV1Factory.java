package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.TagToCategory;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTProjectCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTTagInCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTCategoryInTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTTagInCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTTagInCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

/**
 * This factory is used when creating a collection of Categories for a Tag. It will populate the sort property, which is left
 * null by the CategoryV1Factory.
 */
@ApplicationScoped
public class TagInCategoryV1Factory extends RESTEntityFactory<RESTTagInCategoryV1, TagToCategory, RESTTagInCategoryCollectionV1,
        RESTTagInCategoryCollectionItemV1> {
    @Inject
    protected TagPropertyTagV1Factory tagPropertyTagFactory;
    @Inject
    protected ProjectV1Factory projectFactory;
    @Inject
    protected CategoryInTagV1Factory categoryInTagFactory;
    @Inject
    protected TagV1Factory tagFactory;

    @Override
    public RESTTagInCategoryV1 createRESTEntityFromDBEntityInternal(final TagToCategory entity, final String baseUrl, String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTagInCategoryV1 retValue = new RESTTagInCategoryV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTTagV1.CATEGORIES_NAME);
        expandOptions.add(RESTTagV1.PARENT_TAGS_NAME);
        expandOptions.add(RESTTagV1.CHILD_TAGS_NAME);
        expandOptions.add(RESTTagV1.PROJECTS_NAME);
        expandOptions.add(RESTTagV1.PROPERTIES_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);

        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getTag().getTagId());
        retValue.setRelationshipId(entity.getId());
        retValue.setDescription(entity.getTag().getTagDescription());
        retValue.setName(entity.getTag().getTagName());
        retValue.setRelationshipSort(entity.getSorting());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    RESTEntityCollectionFactory.create(RESTTagInCategoryCollectionV1.class, this, entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        // CATEGORIES
        if (expand != null && expand.contains(RESTTagInCategoryV1.CATEGORIES_NAME)) {
            retValue.setCategories(RESTEntityCollectionFactory.create(RESTCategoryInTagCollectionV1.class, categoryInTagFactory,
                    entity.getTag().getTagToCategoriesList(), RESTTagInCategoryV1.CATEGORIES_NAME, dataType, expand, baseUrl, revision,
                    entityManager));
        }

        // PARENT TAGS
        if (expand != null && expand.contains(RESTTagInCategoryV1.PARENT_TAGS_NAME)) {
            retValue.setParentTags(
                    RESTEntityCollectionFactory.create(RESTTagCollectionV1.class, tagFactory, entity.getTag().getParentTags(),
                            RESTTagInCategoryV1.PARENT_TAGS_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        // CHILD TAGS
        if (expand != null && expand.contains(RESTTagInCategoryV1.CHILD_TAGS_NAME)) {
            retValue.setChildTags(
                    RESTEntityCollectionFactory.create(RESTTagCollectionV1.class, tagFactory, entity.getTag().getChildTags(),
                            RESTTagInCategoryV1.CHILD_TAGS_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        // PROPERTIES
        if (expand != null && expand.contains(RESTTagInCategoryV1.PROPERTIES_NAME)) {
            retValue.setProperties(
                    RESTEntityCollectionFactory.create(RESTAssignedPropertyTagCollectionV1.class, tagPropertyTagFactory,
                            entity.getTag().getPropertyTagsList(), RESTTagInCategoryV1.PROPERTIES_NAME, dataType, expand, baseUrl, revision,
                            entityManager));
        }

        // PROJECTS
        if (expand != null && expand.contains(RESTTagInCategoryV1.PROJECTS_NAME)) {
            retValue.setProjects(
                    RESTEntityCollectionFactory.create(RESTProjectCollectionV1.class, projectFactory, entity.getTag().getProjects(),
                            RESTTagInCategoryV1.PROJECTS_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.TAG_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntityFirstPass(final TagToCategory entity, final RESTTagInCategoryV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTagInCategoryV1.RELATIONSHIP_SORT_NAME)) entity.setSorting(dataObject.getRelationshipSort());
    }

    @Override
    protected Class<TagToCategory> getDatabaseClass() {
        return TagToCategory.class;
    }
}
