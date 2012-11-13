package org.jboss.pressgang.ccms.restserver.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.entity.Category;
import org.jboss.pressgang.ccms.restserver.entity.Filter;
import org.jboss.pressgang.ccms.restserver.entity.FilterCategory;
import org.jboss.pressgang.ccms.restserver.entity.FilterField;
import org.jboss.pressgang.ccms.restserver.entity.FilterLocale;
import org.jboss.pressgang.ccms.restserver.entity.FilterTag;
import org.jboss.pressgang.ccms.restserver.entity.Project;
import org.jboss.pressgang.ccms.restserver.entity.Tag;
import org.jboss.pressgang.ccms.restserver.filter.base.IFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.base.ILocaleFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.base.ITagFilterQueryBuilder;
import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;

public class FilterUtilities {
    /**
     * Translate the contents of this filter, its tags and categories into
     * variables that can be appended to a url
     * 
     * @return
     */
    public static HashMap<String, String> getUrlVariables(final Filter filter)
    {
        final HashMap<String, String> vars = new HashMap<String, String>();

        for (final FilterTag filterTag : filter.getFilterTags())
        {
            final Tag tag = filterTag.getTag();
            final Integer tagState = filterTag.getTagState();

            if (tagState == CommonFilterConstants.MATCH_TAG_STATE)
            {
                vars.put(CommonFilterConstants.MATCH_TAG + tag.getTagId(), CommonFilterConstants.MATCH_TAG_STATE + "");
            }
            else if (tagState == CommonFilterConstants.NOT_MATCH_TAG_STATE)
            {
                vars.put(CommonFilterConstants.MATCH_TAG + tag.getTagId(), CommonFilterConstants.NOT_MATCH_TAG_STATE + "");
            }
            else if (tagState == CommonFilterConstants.GROUP_TAG_STATE)
            {
                vars.put(CommonFilterConstants.GROUP_TAG + tag.getTagId(), CommonFilterConstants.GROUP_TAG_STATE + "");
            }
        }

        for (final FilterCategory filterCategory : filter.getFilterCategories())
        {
            final Category category = filterCategory.getCategory();
            final Project project = filterCategory.getProject();

            if (filterCategory.getCategoryState() == CommonFilterConstants.CATEGORY_INTERNAL_AND_STATE)
            {
                vars.put(CommonFilterConstants.CATEORY_INTERNAL_LOGIC + category.getCategoryId() + (project == null ? "" : "-" + project.getProjectId()), Constants.AND_LOGIC);
            }

            // don't add a url var for the "or" internal logic, as this is the
            // default

            else if (filterCategory.getCategoryState() == CommonFilterConstants.CATEGORY_EXTERNAL_OR_STATE)
            {
                vars.put(CommonFilterConstants.CATEORY_EXTERNAL_LOGIC + category.getCategoryId() + (project == null ? "" : "-" + project.getProjectId()), Constants.OR_LOGIC);
            }

            // don't add a url var for the "and" external logic, as this is the
            // default
        }

        int count = 1;
        for (final FilterLocale filterLocale : filter.getFilterLocales())
        {
            final Integer localeState = filterLocale.getLocaleState();

            if (localeState == CommonFilterConstants.MATCH_TAG_STATE)
            {
                vars.put(CommonFilterConstants.MATCH_LOCALE + count, filterLocale.getLocaleName() + CommonFilterConstants.MATCH_TAG_STATE);
            }
            else if (localeState == CommonFilterConstants.NOT_MATCH_TAG_STATE)
            {
                vars.put(CommonFilterConstants.MATCH_LOCALE + count, filterLocale.getLocaleName() + CommonFilterConstants.NOT_MATCH_TAG_STATE);
            }
            count++;
        }

        boolean foundFilterField = false;
        boolean foundFilterFieldLogic = false;
        for (final FilterField filterField : filter.getFilterFields())
        {
            vars.put(filterField.getField(), filterField.getValue());
            if (!filterField.getField().equals(CommonFilterConstants.LOGIC_FILTER_VAR))
                foundFilterField = true;
            else
                foundFilterFieldLogic = true;
        }

        /*
         * if we have found some filter fields, but did not find the filter
         * logic, use the default value
         */
        if (foundFilterField && !foundFilterFieldLogic)
            vars.put(CommonFilterConstants.LOGIC_FILTER_VAR, Constants.LOGIC_FILTER_VAR_DEFAULT_VALUE);

        return vars;
    }
    
    public static String buildFilterUrlVars(final Filter filter)
    {
        final HashMap<String, String> vars = getUrlVariables(filter);
        String urlVars = "";

        for (final String urlVarKey : vars.keySet())
        {
            if (urlVars.length() != 0)
                urlVars += "&";
            urlVars += urlVarKey + "=" + vars.get(urlVarKey);
        }

        return urlVars;
    }
    
    /**
     * 
     * @return A string that can be supplied to the PathSegment used by the REST
     *         interface to do queries
     */
    public static String buildRESTQueryString(final Filter filter)
    {
        final HashMap<String, String> vars = getUrlVariables(filter);
        StringBuffer urlVars = new StringBuffer("query");

        for (final String urlVarKey : vars.keySet())
        {
            if (urlVars.length() != 0)
                urlVars.append(";");
            urlVars.append(urlVarKey + "=" + vars.get(urlVarKey));
        }

        return urlVars.toString();
    }
    
    /**
     * This function is used to create the HQL query where clause that is
     * appended to the generic EJBQL (as created in default EntityList objects)
     * select statement. It takes the request parameters to get the tags that
     * are to be included in the clause, groups them by category, and then take
     * additional request parameters to define the boolean operations to use
     * between tags in a category, and between categories.
     * 
     * @return the clause to append to the EJBQL select statement
     */
    public static <T> CriteriaQuery<T> buildQuery(final Filter filter, final IFilterQueryBuilder<T> filterQueryBuilder)
    {
        final CriteriaQuery<T> query = filterQueryBuilder.getBaseCriteriaQuery();
        
        final Predicate condition = buildQueryConditions(filter, filterQueryBuilder);
        
        return condition == null ? query : query.where(condition);
    }

    /**
     * This function is used to create the HQL query where clause that is
     * appended to the generic EJBQL (as created in default EntityList objects)
     * select statement. It takes the request parameters to get the tags that
     * are to be included in the clause, groups them by category, and then take
     * additional request parameters to define the boolean operations to use
     * between tags in a category, and between categories.
     * 
     * @return the clause to append to the EJBQL select statement
     */
    public static <T> Predicate buildQueryConditions(final Filter filter, final IFilterQueryBuilder<T> filterQueryBuilder)
    {
        if (filterQueryBuilder == null)
            return null;

        // the categories to be ANDed will be added to this string
        final List<Predicate> andQueryBlock = new ArrayList<Predicate>();
        // the categories to be ORed will be added to this string
        final List<Predicate> orQueryBlock = new ArrayList<Predicate>();
                
        final CriteriaBuilder queryBuilder = filterQueryBuilder.getCriteriaBuilder();
        final List<Predicate> whereQuery = new ArrayList<Predicate>();

        /* Check if the Query Builder can build using Tags & Categories */
        if (filterQueryBuilder instanceof ITagFilterQueryBuilder)
        {
            final ITagFilterQueryBuilder tagFilterQueryBuilder = (ITagFilterQueryBuilder) filterQueryBuilder;
            
            // loop over the projects that the tags in this filter are assigned to
            for (final Project project : filter.getFilterTagProjects())
            {
                // loop over the categories that the tags in this filter are
                // assigned to
                for (final Category category : filter.getFilterTagCategories())
                {
                    // define the default logic used for the FilterTag categories
                    String catInternalLogic = Constants.OR_LOGIC;
                    String catExternalLogic = Constants.AND_LOGIC;
    
                    /*
                     * Now loop over the FilterCategories, looking for any
                     * categories that specify a particular boolean logic to apply.
                     * Remember that not all FilterTags will have an associated
                     * FilterCategory that specifies the logic to use, in which case
                     * the default logic defined above will be used.
                     */
                    final Set<FilterCategory> filterCatgeories = filter.getFilterCategories();
                    for (final FilterCategory filterCatgeory : filterCatgeories)
                    {
                        final boolean categoryMatch = category.equals(filterCatgeory.getCategory());
                        /*
                         * project or filterCatgeory.getProject() might be null.
                         * CollectionUtilities.isEqual deals with this situation
                         */
                        final boolean projectMatch = CollectionUtilities.isEqual(project, filterCatgeory.getProject());
    
                        if (categoryMatch && projectMatch)
                        {
                            final int categoryState = filterCatgeory.getCategoryState();
    
                            if (categoryState == CommonFilterConstants.CATEGORY_INTERNAL_AND_STATE)
                                catInternalLogic = Constants.AND_LOGIC;
                            else if (categoryState == CommonFilterConstants.CATEGORY_INTERNAL_OR_STATE)
                                catInternalLogic = Constants.OR_LOGIC;
                            else if (categoryState == CommonFilterConstants.CATEGORY_EXTERNAL_AND_STATE)
                                catExternalLogic = Constants.AND_LOGIC;
                            else if (categoryState == CommonFilterConstants.CATEGORY_EXTERNAL_OR_STATE)
                                catExternalLogic = Constants.OR_LOGIC;
                        }
                    }
    
                    /*
                     * now build up the HQL that checks to see if the FilterTags
                     * exist (or not) in this category
                     */
                    final List<Predicate> categoryBlocks = new ArrayList<Predicate>();
    
                    boolean matchedSomeTags = false;
    
                    final Set<FilterTag> filterTags = filter.getFilterTags();
                    for (final FilterTag filterTag : filterTags)
                    {
                        final Tag tag = filterTag.getTag();
    
                        /*
                         * first check to make sure that the FilterTag is actually
                         * associated with the category we are looking at now
                         */
                        if (tag.isInCategory(category) && tag.isInProject(project))
                        {
                            /*
                             * a FilterTag state of 1 "means exists in category",
                             * and 0 means "does not exist in category"
                             */
                            final boolean matchTag = filterTag.getTagState() == CommonFilterConstants.MATCH_TAG_STATE;
                            final boolean notMatchTag = filterTag.getTagState() == CommonFilterConstants.NOT_MATCH_TAG_STATE;
    
                            if (matchTag || notMatchTag)
                            {
                                matchedSomeTags = true;
    
                                if (matchTag)
                                {
                                    /* match the tag in this category */
                                    categoryBlocks.add(tagFilterQueryBuilder.getMatchTagString(tag.getTagId()));
                                }
                                else if (notMatchTag)
                                {
                                    /*
                                     * make sure this tag does not exist in this
                                     * category
                                     */
                                    categoryBlocks.add(tagFilterQueryBuilder.getNotMatchTagString(tag.getTagId()));
                                }
                            }
                        }
                    }
    
                    if (matchedSomeTags)
                    {
                        final Predicate categoryBlock;
                        if (categoryBlocks.size() > 1) {
                            final Predicate[] catBlockArray = categoryBlocks.toArray(new Predicate[categoryBlocks.size()]);
                            if (catInternalLogic.equals(Constants.OR_LOGIC))
                            {
                                categoryBlock = queryBuilder.or(catBlockArray);
                            }
                            else
                            {
                                categoryBlock = queryBuilder.and(catBlockArray);
                            }
                        } else {
                            categoryBlock = categoryBlocks.get(0);
                        }
    
                        // append this clause to the appropriate block
                        if (catExternalLogic.equals(Constants.AND_LOGIC))
                        {
                            andQueryBlock.add(categoryBlock);
                        }
                        else
                        {
                            orQueryBlock.add(categoryBlock);
                        }
                    }
    
                }
            }
        }
        
        /* Check if the Query Builder can build using Locales */
        if (filterQueryBuilder instanceof ILocaleFilterQueryBuilder)
        {
            final ILocaleFilterQueryBuilder localeFilterQueryBuilder = (ILocaleFilterQueryBuilder) filterQueryBuilder;
            
            /*
             * Loop over the locales and add the query
             */
            final List<Predicate> localeBlock = new ArrayList<Predicate>();
            final List<Predicate> notLocaleBlock = new ArrayList<Predicate>();
            for (final FilterLocale filterLocale : filter.getFilterLocales())
            {
                if (filterLocale.getLocaleState() == CommonFilterConstants.MATCH_TAG_STATE)
                {
                    localeBlock.add(localeFilterQueryBuilder.getMatchingLocaleString(filterLocale.getLocaleName()));
                }
                else if (filterLocale.getLocaleState() == CommonFilterConstants.NOT_MATCH_TAG_STATE)
                {
                    notLocaleBlock.add(localeFilterQueryBuilder.getNotMatchingLocaleString(filterLocale.getLocaleName()));
                }
            }
            
            if (!localeBlock.isEmpty() || !notLocaleBlock.isEmpty())
            {
             
                Predicate localeBlockPredicate = null;
                if (localeBlock.size() > 1) {
                    final Predicate[] predicateArray = localeBlock.toArray(new Predicate[localeBlock.size()]);
                    localeBlockPredicate = queryBuilder.or(predicateArray);
                } else if (localeBlock.size() == 1) {
                    localeBlockPredicate = localeBlock.get(0);
                }
                
                Predicate notLocaleBlockPredicate = null;
                if (notLocaleBlock.size() > 1) {
                    final Predicate[] predicateArray = notLocaleBlock.toArray(new Predicate[notLocaleBlock.size()]);
                    notLocaleBlockPredicate = queryBuilder.and(predicateArray);
                } else if (andQueryBlock.size() == 1) {
                    notLocaleBlockPredicate = notLocaleBlock.get(0);
                }
                
                if (localeBlockPredicate != null && notLocaleBlockPredicate != null)
                    andQueryBlock.add(queryBuilder.and(localeBlockPredicate, notLocaleBlockPredicate));
                else if (localeBlockPredicate != null)
                    andQueryBlock.add(localeBlockPredicate);
                else
                    andQueryBlock.add(notLocaleBlockPredicate);
            }
        }

        /*
         * build up the category query if some conditions were specified if not,
         * we will just return an empty string
         */
        if (!andQueryBlock.isEmpty() || !orQueryBlock.isEmpty())
        {
            // add the and categories
            Predicate andQueryPredicate = null;
            if (andQueryBlock.size() > 1) {
                final Predicate[] predicateArray = andQueryBlock.toArray(new Predicate[andQueryBlock.size()]);
                andQueryPredicate = queryBuilder.and(predicateArray);
            } else if (andQueryBlock.size() == 1) {
                andQueryPredicate = andQueryBlock.get(0);
            }

            // add the or categories
            Predicate orQueryPredicate = null;
            if (orQueryBlock.size() > 1) {
                final Predicate[] predicateArray = orQueryBlock.toArray(new Predicate[orQueryBlock.size()]);
                orQueryPredicate = queryBuilder.or(predicateArray);
            } else if (orQueryBlock.size() == 1) {
                orQueryPredicate = orQueryBlock.get(0);
            }
            
            if (andQueryPredicate != null && orQueryPredicate != null)
                whereQuery.add(queryBuilder.or(andQueryPredicate, orQueryPredicate));
            else if (andQueryPredicate != null)
                whereQuery.add(andQueryPredicate);
            else
                whereQuery.add(orQueryPredicate);
        }
        
        /*
         * Do an initial loop over the FilterFields, looking for the field logic
         * value
         */
        for (final FilterField filterField : filter.getFilterFields())
            filterQueryBuilder.processFilterString(filterField.getField(), filterField.getValue());

        final Predicate fieldQuery = filterQueryBuilder.getFilterConditions();
        if (fieldQuery != null)
        {
            whereQuery.add(fieldQuery);
        }

        if (whereQuery.size() > 1)
        {
            final Predicate[] queries = whereQuery.toArray(new Predicate[whereQuery.size()]);
            return queryBuilder.and(queries);
        } else if (whereQuery.size() == 1) {
            return whereQuery.get(0);
        }
                
        return null;
    }
    
    public static String getFilterTitle(final Filter filter)
    {
        String desc = "";

        /* Loop over all the categories that the filter tags belong to */
        for (final Category category : filter.getFilterTagCategories())
        {
            String categoryDesc = "[" + category.getCategoryName() + "] ";
            String tagDesc = "";

            /* This will be shown in the topic list results title */
            String internalLogic = Constants.DEFAULT_INTERNAL_LOGIC;

            /* Find out if the category does not have the default internal logic */
            final Set<FilterCategory> filterCategories = filter.getFilterCategories();
            for (final FilterCategory filterCategory : filterCategories)
            {
                /*
                 * if a filter category has been saved that matches the category
                 * we are looking at now..
                 */
                if (filterCategory.getCategory().equals(category))
                {
                    /*
                     * ...and it defines an internal logic state, update the
                     * internalLogic variable
                     */
                    if (filterCategory.getCategoryState() == CommonFilterConstants.CATEGORY_INTERNAL_AND_STATE)
                        internalLogic = Constants.AND_LOGIC;
                    else if (filterCategory.getCategoryState() == CommonFilterConstants.CATEGORY_INTERNAL_OR_STATE)
                        internalLogic = Constants.OR_LOGIC;
                    break;
                }
            }

            for (final FilterTag tag : filter.getFilterTags())
            {
                final int tagState = tag.getTagState();

                if (tagState == CommonFilterConstants.MATCH_TAG_STATE || tagState == CommonFilterConstants.NOT_MATCH_TAG_STATE)
                {
                    if (tag.getTag().isInCategory(category.getCategoryId()))
                    {
                        if (tagDesc.length() != 0)
                            tagDesc += " " + internalLogic + " ";

                        if (tagState == CommonFilterConstants.NOT_MATCH_TAG_STATE)
                            tagDesc += "Not ";

                        tagDesc += tag.getTag().getTagName();
                    }
                }
            }

            if (desc.length() != 0)
                desc += " ";

            desc += categoryDesc + tagDesc;
        }
        
        if (filter.getFilterLocales().size() != 0)
        {
            String andLocaleDesc = "";
            String orLocaleDesc = "";
            for (final FilterLocale locale : filter.getFilterLocales())
            {
                final int localeState = locale.getLocaleState();
    
                if (localeState == CommonFilterConstants.MATCH_TAG_STATE)
                {
                    if (orLocaleDesc.length() != 0)
                        orLocaleDesc += " OR ";
    
                    orLocaleDesc += locale.getLocaleName();
                }
                else if (localeState == CommonFilterConstants.NOT_MATCH_TAG_STATE)
                {
                    if (andLocaleDesc.length() != 0)
                        andLocaleDesc += " AND ";

                    andLocaleDesc += "Not " + locale.getLocaleName();
                }
            }
            
            if (orLocaleDesc.length() != 0 || andLocaleDesc.length() != 0)
            {
                String combinedDesc = "";
                
                combinedDesc += orLocaleDesc;
                
                if (combinedDesc.length() != 0  && andLocaleDesc.length() != 0)
                    combinedDesc += " AND ";
                
                combinedDesc += andLocaleDesc;
                
                if (desc.length() != 0)
                    desc += " ";
                desc += "[Locale] " + combinedDesc;
                
            }
        }

        if (filter.getFilterFields().size() != 0)
        {
            String searchFilters = "";

            for (final FilterField filterField : filter.getFilterFields())
            {
                if (searchFilters.length() != 0)
                    searchFilters += ", ";
                searchFilters += " " + filterField.getDescription() + " = " + filterField.getValue();
            }

            if (desc.length() != 0)
                desc += " ";

            desc += "[Search Filters]" + searchFilters;
        }

        String groupBy = "";

        for (final FilterTag tag : filter.getFilterTags())
        {
            final int tagState = tag.getTagState();

            if (tagState == CommonFilterConstants.GROUP_TAG_STATE)
            {
                if (groupBy.length() != 0)
                    groupBy += ", ";

                groupBy += tag.getTag().getTagName();
            }
        }

        if (groupBy.length() != 0)
            desc += " [Group By] " + groupBy;

        return desc;
    }
}
