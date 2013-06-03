package org.jboss.pressgang.ccms.server.rest.v1;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.model.Project;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.model.TagToCategory;
import org.jboss.pressgang.ccms.model.TagToPropertyTag;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTProjectCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTProjectCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTAssignedPropertyTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTCategoryInTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTTagInCategoryCollectionItemV1;
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
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

/**
 * This factory is used when creating a collection of Categories for a Tag. It will populate the sort property, which is left
 * null by the CategoryV1Factory.
 */
public class TagInCategoryV1Factory extends RESTDataObjectFactory<RESTTagInCategoryV1, TagToCategory, RESTTagInCategoryCollectionV1,
        RESTTagInCategoryCollectionItemV1> {

    public TagInCategoryV1Factory() {
        super(TagToCategory.class);
    }

    @Override
    public RESTTagInCategoryV1 createRESTEntityFromDBEntityInternal(final TagToCategory entity, final String baseUrl, String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager) {
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
                    new RESTDataObjectCollectionFactory<RESTTagInCategoryV1, TagToCategory, RESTTagInCategoryCollectionV1,
                            RESTTagInCategoryCollectionItemV1>().create(
                            RESTTagInCategoryCollectionV1.class, new TagInCategoryV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        // CATEGORIES
        if (expand != null && expand.contains(RESTTagInCategoryV1.CATEGORIES_NAME)) {
            retValue.setCategories(
                    new RESTDataObjectCollectionFactory<RESTCategoryInTagV1, TagToCategory, RESTCategoryInTagCollectionV1,
                            RESTCategoryInTagCollectionItemV1>().create(
                            RESTCategoryInTagCollectionV1.class, new CategoryInTagV1Factory(), entity.getTag().getTagToCategoriesArray(),
                            RESTTagInCategoryV1.CATEGORIES_NAME, dataType, expand, baseUrl, entityManager));
        }

        // PARENT TAGS
        if (expand != null && expand.contains(RESTTagInCategoryV1.PARENT_TAGS_NAME)) {
            retValue.setParentTags(
                    new RESTDataObjectCollectionFactory<RESTTagV1, Tag, RESTTagCollectionV1, RESTTagCollectionItemV1>().create(
                            RESTTagCollectionV1.class, new TagV1Factory(), entity.getTag().getParentTags(),
                            RESTTagInCategoryV1.PARENT_TAGS_NAME, dataType, expand, baseUrl, entityManager));
        }

        // CHILD TAGS
        if (expand != null && expand.contains(RESTTagInCategoryV1.CHILD_TAGS_NAME)) {
            retValue.setChildTags(
                    new RESTDataObjectCollectionFactory<RESTTagV1, Tag, RESTTagCollectionV1, RESTTagCollectionItemV1>().create(
                            RESTTagCollectionV1.class, new TagV1Factory(), entity.getTag().getChildTags(),
                            RESTTagInCategoryV1.CHILD_TAGS_NAME, dataType, expand, baseUrl, entityManager));
        }

        // PROPERTIES
        if (expand != null && expand.contains(RESTTagInCategoryV1.PROPERTIES_NAME)) {
            retValue.setProperties(
                    new RESTDataObjectCollectionFactory<RESTAssignedPropertyTagV1, TagToPropertyTag, RESTAssignedPropertyTagCollectionV1,
                            RESTAssignedPropertyTagCollectionItemV1>().create(
                            RESTAssignedPropertyTagCollectionV1.class, new TagPropertyTagV1Factory(),
                            entity.getTag().getTagToPropertyTagsArray(), RESTTagInCategoryV1.PROPERTIES_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        // PROJECTS
        if (expand != null && expand.contains(RESTTagInCategoryV1.PROJECTS_NAME)) {
            retValue.setProjects(
                    new RESTDataObjectCollectionFactory<RESTProjectV1, Project, RESTProjectCollectionV1,
                            RESTProjectCollectionItemV1>().create(
                            RESTProjectCollectionV1.class, new ProjectV1Factory(), entity.getTag().getProjects(),
                            RESTTagInCategoryV1.PROJECTS_NAME, dataType, expand, baseUrl, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.TAG_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntityFirstPass(final EntityManager entityManager,
            Map<RESTBaseEntityV1<?, ?, ?>, AuditedEntity> newEntityCache, final TagToCategory entity, final RESTTagInCategoryV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTagInCategoryV1.RELATIONSHIP_SORT_NAME))
            entity.setSorting(dataObject.getRelationshipSort());

        entityManager.persist(entity);
    }
}
