package org.jboss.pressgangccms.restserver.utils;

import javax.persistence.EntityManager;

import org.jboss.pressgangccms.restserver.entities.BlobConstants;
import org.jboss.pressgangccms.restserver.entities.IntegerConstants;
import org.jboss.pressgangccms.restserver.entities.StringConstants;

/**
 * This class provides a number of static methods for easy and safe lookup of
 * database entities.
 * @author Matthew Casperson
 */
public class EntityUtilities
{
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
}
