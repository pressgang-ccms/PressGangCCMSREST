package org.jboss.pressgang.ccms.restserver.rest.v1;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.TagToCategory;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTCategoryInTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTTagInCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTCategoryInTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTTagInCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTCategoryInTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTTagInCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.restserver.utils.EnversUtilities;

public class CategoryInTagV1Factory extends RESTDataObjectFactory<RESTCategoryInTagV1, TagToCategory, RESTCategoryInTagCollectionV1,
        RESTCategoryInTagCollectionItemV1> {

    public CategoryInTagV1Factory() {
        super(TagToCategory.class);
    }

    @Override
    public RESTCategoryInTagV1 createRESTEntityFromDBEntityInternal(final TagToCategory entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTCategoryInTagV1 retValue = new RESTCategoryInTagV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTv1Constants.TAGS_EXPANSION_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getCategory().getCategoryId());
        retValue.setRelationshipId(entity.getId());
        retValue.setName(entity.getCategory().getCategoryName());
        retValue.setDescription(entity.getCategory().getCategoryDescription());
        retValue.setMutuallyExclusive(entity.getCategory().isMutuallyExclusive());
        retValue.setSort(entity.getCategory().getCategorySort());
        retValue.setRelationshipSort(entity.getSorting());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    new RESTDataObjectCollectionFactory<RESTCategoryInTagV1, TagToCategory, RESTCategoryInTagCollectionV1,
                            RESTCategoryInTagCollectionItemV1>().create(
                            RESTCategoryInTagCollectionV1.class, new CategoryInTagV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        // TAGS
        if (expand != null && expand.contains(RESTCategoryInTagV1.TAGS_NAME)) {
            retValue.setTags(
                    new RESTDataObjectCollectionFactory<RESTTagInCategoryV1, TagToCategory, RESTTagInCategoryCollectionV1,
                            RESTTagInCategoryCollectionItemV1>().create(
                            RESTTagInCategoryCollectionV1.class, new TagInCategoryV1Factory(),
                            entity.getCategory().getTagToCategoriesArray(), RESTCategoryInTagV1.TAGS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.CATEGORY_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final TagToCategory entity,
            final RESTCategoryInTagV1 dataObject) {
        if (dataObject.hasParameterSet(RESTCategoryInTagV1.RELATIONSHIP_SORT_NAME)) entity.setSorting(dataObject.getRelationshipSort());
    }
}
