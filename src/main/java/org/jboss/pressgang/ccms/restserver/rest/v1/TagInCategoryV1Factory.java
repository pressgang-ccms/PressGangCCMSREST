package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.rest.v1.collections.RESTProjectCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTProjectCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTAssignedPropertyTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTTagInCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTCategoryInTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTCategoryInTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTTagInCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTProjectV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTAssignedPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTCategoryInTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTTagInCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.entity.Project;
import org.jboss.pressgang.ccms.restserver.entity.Tag;
import org.jboss.pressgang.ccms.restserver.entity.TagToCategory;
import org.jboss.pressgang.ccms.restserver.entity.TagToPropertyTag;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;

/**
 * This factory is used when creating a collection of Categories for a Tag. It will populate the sort property, which is left
 * null by the CategoryV1Factory.
 */
public class TagInCategoryV1Factory
        extends
        RESTDataObjectFactory<RESTTagInCategoryV1, TagToCategory, RESTTagInCategoryCollectionV1, RESTTagInCategoryCollectionItemV1> {

    public TagInCategoryV1Factory() {
        super(TagToCategory.class);
    }

    @Override
    public RESTTagInCategoryV1 createRESTEntityFromDBEntity(final TagToCategory entity, final String baseUrl, String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
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

        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getTag().getTagId());
        retValue.setRelationshipId(entity.getId());
        retValue.setDescription(entity.getTag().getTagDescription());
        retValue.setName(entity.getTag().getTagName());
        retValue.setRelationshipSort(entity.getSorting());

        // REVISIONS
        if (revision == null) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTTagInCategoryV1, TagToCategory, RESTTagInCategoryCollectionV1, RESTTagInCategoryCollectionItemV1>()
                    .create(RESTTagInCategoryCollectionV1.class, new TagInCategoryV1Factory(), entity, entity.getRevisions(entityManager),
                            RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
        }

        // CATEGORIES
        retValue.setCategories(new RESTDataObjectCollectionFactory<RESTCategoryInTagV1, TagToCategory, RESTCategoryInTagCollectionV1, RESTCategoryInTagCollectionItemV1>()
                .create(RESTCategoryInTagCollectionV1.class, new CategoryInTagV1Factory(), entity.getTag()
                        .getTagToCategoriesArray(), RESTv1Constants.TAGS_EXPANSION_NAME, dataType, expand, baseUrl, entityManager));
        
        // PARENT TAGS
        retValue.setParentTags(new RESTDataObjectCollectionFactory<RESTTagV1, Tag, RESTTagCollectionV1, RESTTagCollectionItemV1>()
                .create(RESTTagCollectionV1.class, new TagV1Factory(), entity.getTag().getParentTags(),
                        RESTTagV1.PARENT_TAGS_NAME, dataType, expand, baseUrl, entityManager));
        
        // CHILD TAGS
        retValue.setChildTags(new RESTDataObjectCollectionFactory<RESTTagV1, Tag, RESTTagCollectionV1, RESTTagCollectionItemV1>()
                .create(RESTTagCollectionV1.class, new TagV1Factory(), entity.getTag().getChildTags(),
                        RESTTagV1.CHILD_TAGS_NAME, dataType, expand, baseUrl, entityManager));
        
        // PROPERTIES
        retValue.setProperties(new RESTDataObjectCollectionFactory<RESTAssignedPropertyTagV1, TagToPropertyTag, RESTAssignedPropertyTagCollectionV1, RESTAssignedPropertyTagCollectionItemV1>()
                .create(RESTAssignedPropertyTagCollectionV1.class, new TagPropertyTagV1Factory(), entity.getTag()
                        .getTagToPropertyTagsArray(), RESTTagV1.PROPERTIES_NAME, dataType, expand, baseUrl, entityManager));
        
        // PROJECTS
        retValue.setProjects(new RESTDataObjectCollectionFactory<RESTProjectV1, Project, RESTProjectCollectionV1, RESTProjectCollectionItemV1>()
                .create(RESTProjectCollectionV1.class, new ProjectV1Factory(), entity.getTag().getProjects(),
                        RESTTagV1.PROJECTS_NAME, dataType, expand, baseUrl, entityManager));

        retValue.setLinks(baseUrl, RESTv1Constants.TAG_URL_NAME, dataType, retValue.getId());
        retValue.setLogDetails(new LogDetailsV1Factory().create(entity, revision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand,
                dataType, baseUrl, entityManager));

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final TagToCategory entity,
            final RESTTagInCategoryV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTagInCategoryV1.RELATIONSHIP_SORT_NAME))
            entity.setSorting(dataObject.getRelationshipSort());

        entityManager.persist(entity);
    }
}
