package org.jboss.pressgangccms.restserver.utils;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.pressgangccms.restserver.constants.Constants;
import org.jboss.pressgangccms.restserver.entities.BlobConstants;
import org.jboss.pressgangccms.restserver.entities.IntegerConstants;
import org.jboss.pressgangccms.restserver.entities.StringConstants;
import org.jboss.pressgangccms.restserver.entities.Topic;
import org.jboss.pressgangccms.utils.common.CollectionUtilities;

/**
 * This class provides a number of static methods for easy and safe lookup of
 * database entities.
 * @author Matthew Casperson
 */
public class EntityUtilities
{
	@PersistenceContext(unitName="EntityUtilities") static EntityManager entityManager;
	
	public static byte[] loadBlobConstant(final EntityManager entityManager, final Integer id)
	{
		if (id == null)
			return null;

		final BlobConstants constant = entityManager.find(BlobConstants.class, id);

		if (constant == null)
		{
			System.out.println("Expected to find a record in the BlobConstants table with an ID of " + id);
			return null;
		}

		return constant.getConstantValue();
	}

	public static Integer loadIntegerConstant(final EntityManager entityManager, final Integer id)
	{
		if (id == null)
			return null;

		final IntegerConstants constant = entityManager.find(IntegerConstants.class, id);

		if (constant == null)
		{
			System.out.println("Expected to find a record in the IntegerConstants table with an ID of " + id);
			return null;
		}

		return constant.getConstantValue();
	}

	public static String loadStringConstant(final EntityManager entityManager, final Integer id)
	{
		if (id == null)
			return null;

		final StringConstants constant = entityManager.find(StringConstants.class, id);

		if (constant == null)
		{
			System.out.println("Expected to find a record in the StringConstants table with an ID of " + id);
			return null;
		}

		return constant.getConstantValue();
	}
	
	/**
	 * @return A comma separated list of topic ids that have been included in a
	 *         content spec
	 * @throws Exception
	 */
	public static String getTopicsInContentSpec(final Integer contentSpecTopicID) throws Exception
	{
		try
		{
			final Topic contentSpec = entityManager.find(Topic.class, contentSpecTopicID);

			if (contentSpec == null)
				return Constants.NULL_TOPIC_ID;

			final ContentSpecParser csp = new ContentSpecParser("http://localhost:8080/TopicIndex/");
			if (csp.parse(contentSpec.getTopicXML()))
			{
				final List<Integer> topicIds = csp.getReferencedTopicIds();
				if (topicIds.size() == 0)
					return Constants.NULL_TOPIC_ID;

				return CollectionUtilities.toSeperatedString(topicIds);
			}
			else
			{
				return Constants.NULL_TOPIC_ID;
			}
		}
		catch (final Exception ex)
		{
			SkynetExceptionUtilities.handleException(ex, true, "An invalid Topic ID was stored for a Content Spec in the database, or the topic was not a valid content spec");
			return Constants.NULL_TOPIC_ID;
		}
	}
}
