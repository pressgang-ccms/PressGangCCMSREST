package org.jboss.pressgang.ccms.server.filter.builder;

import java.util.List;

import javax.persistence.EntityManager;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.server.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.server.utils.EntityUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.regexp.NamedMatcher;
import com.google.code.regexp.NamedPattern;

public class TagFilterQueryBuilder extends BaseFilterQueryBuilder<Tag> {
    private static final Logger log = LoggerFactory.getLogger(TagFilterQueryBuilder.class);

    public TagFilterQueryBuilder(final EntityManager entityManager) {
        super(Tag.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.TAG_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeperatedListCondition("tagId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.TAG_NAME_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("tagName", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.TAG_DESCRIPTION_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("tagDescription", fieldValue);
        } else if (fieldName.startsWith(CommonFilterConstants.TOPIC_PROPERTY_TAG)) {
            try {
                final NamedPattern pattern = NamedPattern.compile(CommonConstants.PROPERTY_TAG_SEARCH_RE);
                final NamedMatcher matcher = pattern.matcher(fieldName);

                while (matcher.find()) {
                    final String propertyTagIdString = matcher.group("PropertyTagID");

                    if (propertyTagIdString != null && fieldValue != null) {
                        final Integer propertyTagIdInt = Integer.parseInt(propertyTagIdString);
                        final List<Integer> tagIds = EntityUtilities.getTagsWithPropertyTag(getEntityManager(), propertyTagIdInt, fieldValue);
                        addIdInCollectionCondition("tagId", tagIds);
                    }

                    /* should only match once */
                    break;
                }

            } catch (final NumberFormatException ex) {
                /*
                 * could not parse integer, so fail. this shouldn't happen though, as the string is matched by a regex that will
                 * only allow numbers
                 */
                log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
            }
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }
}
