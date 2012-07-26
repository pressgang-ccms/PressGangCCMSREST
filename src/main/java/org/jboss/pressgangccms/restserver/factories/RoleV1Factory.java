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
import org.jboss.pressgangccms.restserver.entities.RoleToRoleRelationship;
import org.jboss.pressgangccms.restserver.entities.User;
import org.jboss.pressgangccms.restserver.factories.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgangccms.restserver.factories.base.RESTDataObjectFactory;
import org.jboss.resteasy.spi.BadRequestException;

public class RoleV1Factory extends RESTDataObjectFactory<RESTRoleV1, Role, RESTRoleCollectionV1>
{
	public RoleV1Factory()
	{
		super(Role.class);
	}

	@Override
	public RESTRoleV1 createRESTEntityFromDBEntity(final Role entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager)
	{
		assert entity != null : "Parameter topic can not be null";
		assert baseUrl != null : "Parameter baseUrl can not be null";

		final RESTRoleV1 retValue = new RESTRoleV1();

		retValue.setId(entity.getRoleId());
		retValue.setName(entity.getRoleName());
		retValue.setDescription(entity.getDescription());
		
		final List<String> expandOptions = new ArrayList<String>();
		expandOptions.add(RESTRoleV1.USERS_NAME);
		expandOptions.add(RESTRoleV1.CHILDROLES_NAME);
		expandOptions.add(RESTRoleV1.PARENTROLES_NAME);
		if (revision == null)
			expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
		
		retValue.setExpand(expandOptions);	

		if (revision == null)
		{
			retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTRoleV1, Role, RESTRoleCollectionV1>().create(RESTRoleCollectionV1.class, new RoleV1Factory(), entity, entity.getRevisions(), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
		}
		retValue.setUsers(new RESTDataObjectCollectionFactory<RESTUserV1, User, RESTUserCollectionV1>().create(RESTUserCollectionV1.class, new UserV1Factory(), entity.getUsers(), RESTRoleV1.USERS_NAME, dataType, expand, baseUrl, entityManager));
		retValue.setParentRoles(new RESTDataObjectCollectionFactory<RESTRoleV1, Role, RESTRoleCollectionV1>().create(RESTRoleCollectionV1.class, new RoleV1Factory(), entity.getParentRoles(), RESTRoleV1.PARENTROLES_NAME, dataType, expand, baseUrl, entityManager));
		retValue.setChildRoles(new RESTDataObjectCollectionFactory<RESTRoleV1, Role, RESTRoleCollectionV1>().create(RESTRoleCollectionV1.class, new RoleV1Factory(), entity.getChildRoles(), RESTRoleV1.CHILDROLES_NAME, dataType, expand, baseUrl, entityManager));

		retValue.setLinks(baseUrl, BaseRESTv1.ROLE_URL_NAME, dataType, retValue.getId());

		return retValue;
	}

	@Override
	public
	void syncDBEntityWithRESTEntity(final EntityManager entityManager, final Role entity, final RESTRoleV1 dataObject)
	{
		if (dataObject.hasParameterSet(RESTUserV1.DESCRIPTION_NAME))
			entity.setDescription(dataObject.getDescription());
		if (dataObject.hasParameterSet(RESTUserV1.NAME_NAME))
			entity.setRoleName(dataObject.getName());
		
		entityManager.persist(entity);

		/* Many To Many - Add will create a mapping */
		if (dataObject.hasParameterSet(RESTRoleV1.USERS_NAME) && dataObject.getUsers() != null && dataObject.getUsers().getItems() != null)
		{
			for (final RESTUserV1 restEntity : dataObject.getUsers().getItems())
			{
				final User dbEntity = entityManager.find(User.class, restEntity.getId());

				if (restEntity.getAddItem() || restEntity.getRemoveItem())
				{
					if (dbEntity == null)
						throw new BadRequestException("No User entity was found with the primary key " + restEntity.getId());
				}
				
				if (restEntity.getAddItem())
				{
					entity.addUser(dbEntity);
				}
				else if (restEntity.getRemoveItem())
				{
					entity.removeUser(dbEntity);
				}
			}
		}
		
		/* Many To Many - Add will create a mapping */
		if (dataObject.hasParameterSet(RESTRoleV1.PARENTROLES_NAME) && dataObject.getParentRoles() != null && dataObject.getParentRoles().getItems() != null)
		{
			for (final RESTRoleV1 restEntity : dataObject.getParentRoles().getItems())
			{
				final Role dbEntity = entityManager.find(Role.class, restEntity.getId());
				final RoleToRoleRelationship dbRelationshipEntity = entityManager.find(RoleToRoleRelationship.class, restEntity.getRelationshipId());
				
				if (restEntity.getAddItem() || restEntity.getRemoveItem())
				{
					if (dbEntity == null)
						throw new BadRequestException("No Role entity was found with the primary key " + restEntity.getId());
					else if (dbRelationshipEntity == null)
						throw new BadRequestException("No RoleToRoleRelationship entity was found with the primary key " + restEntity.getRelationshipId());
				}

				if (restEntity.getAddItem())
				{					
					entity.addParentRole(dbEntity, dbRelationshipEntity);
				}
				else if (restEntity.getRemoveItem())
				{
					entity.removeParentRole(dbEntity, dbRelationshipEntity);
				}
			}
		}
		
		/* Many To Many - Add will create a mapping */
		if (dataObject.hasParameterSet(RESTRoleV1.CHILDROLES_NAME) && dataObject.getChildRoles() != null && dataObject.getChildRoles().getItems() != null)
		{
			for (final RESTRoleV1 restEntity : dataObject.getChildRoles().getItems())
			{
				final Role dbEntity = entityManager.find(Role.class, restEntity.getId());
				final RoleToRoleRelationship dbRelationshipEntity = entityManager.find(RoleToRoleRelationship.class, restEntity.getRelationshipId());
				
				if (restEntity.getAddItem() || restEntity.getRemoveItem())
				{
					if (dbEntity == null)
						throw new BadRequestException("No Role entity was found with the primary key " + restEntity.getId());
					else if (dbRelationshipEntity == null)
						throw new BadRequestException("No RoleToRoleRelationship entity was found with the primary key " + restEntity.getRelationshipId());
				}

				if (restEntity.getAddItem())
				{					
					entity.addChildRole(dbEntity, dbRelationshipEntity);
				}
				else if (restEntity.getRemoveItem())
				{
					entity.removeChildRole(dbEntity, dbRelationshipEntity);
				}
			}
		}
	}
}
