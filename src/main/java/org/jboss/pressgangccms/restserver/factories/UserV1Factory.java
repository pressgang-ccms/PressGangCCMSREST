package org.jboss.pressgangccms.restserver.factories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgangccms.rest.v1.collections.RESTRoleCollectionV1;
import org.jboss.pressgangccms.rest.v1.collections.RESTUserCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTRoleV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTUserV1;
import org.jboss.pressgangccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgangccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgangccms.restserver.BaseRESTv1;
import org.jboss.pressgangccms.restserver.entities.Role;
import org.jboss.pressgangccms.restserver.entities.User;
import org.jboss.resteasy.spi.BadRequestException;

public class UserV1Factory extends RESTDataObjectFactory<RESTUserV1, User, RESTUserCollectionV1>
{
	public UserV1Factory()
	{
		super(User.class);
	}

	@Override
	RESTUserV1 createRESTEntityFromDBEntity(final User entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager)
	{
		assert entity != null : "Parameter topic can not be null";
		assert baseUrl != null : "Parameter baseUrl can not be null";
		
		final RESTUserV1 retValue = new RESTUserV1();

		final List<String> expandOptions = new ArrayList<String>();
		expandOptions.add( RESTUserV1.ROLES_NAME);

		if (revision == null)
			expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
		
		
		retValue.setId(entity.getUserId());
		retValue.setName(entity.getUserName());
		retValue.setDescription(entity.getDescription());
		
		if (revision == null)
		{
			retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTUserV1, User, RESTUserCollectionV1>().create(RESTUserCollectionV1.class, new UserV1Factory(), entity, entity.getRevisions(), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
		}
		
		retValue.setRoles(new RESTDataObjectCollectionFactory<RESTRoleV1, Role, RESTRoleCollectionV1>().create(RESTRoleCollectionV1.class, new RoleV1Factory(), entity.getRoles(), RESTUserV1.ROLES_NAME, dataType, expand, baseUrl, entityManager));

		retValue.setLinks(baseUrl, BaseRESTv1.USER_URL_NAME, dataType, retValue.getId());

		return retValue;
	}

	@Override
	public
	void syncDBEntityWithRESTEntity(final EntityManager entityManager, final User entity, final RESTUserV1 dataObject)
	{
		if (dataObject.hasParameterSet(RESTUserV1.DESCRIPTION_NAME))
			entity.setDescription(dataObject.getDescription());
		if (dataObject.hasParameterSet(RESTUserV1.NAME_NAME))
			entity.setUserName(dataObject.getName());
		
		entityManager.persist(entity);

		if (dataObject.hasParameterSet(RESTUserV1.ROLES_NAME) && dataObject.getRoles() != null && dataObject.getRoles().getItems() != null)
		{
			for (final RESTRoleV1 restEntity : dataObject.getRoles().getItems())
			{
				if (restEntity.getAddItem() || restEntity.getRemoveItem())
				{
					final Role dbEntity = entityManager.find(Role.class, restEntity.getId());
					if (dbEntity == null)
						throw new BadRequestException("No entity was found with the primary key " + restEntity.getId());

					if (restEntity.getAddItem())
					{
						entity.addRole(dbEntity);
					}
					else if (restEntity.getRemoveItem())
					{
						entity.removeRole(dbEntity);
					}
				}
			}
		}
	}
}
