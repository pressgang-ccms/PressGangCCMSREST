package org.jboss.pressgangccms.restserver.entities;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.Audited;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.AuditQueryCreator;
import org.jboss.pressgangccms.restserver.entities.base.ToPropertyTag;
import org.jboss.pressgangccms.restserver.utils.SkynetExceptionUtilities;


@Audited
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "TagToPropertyTag")
public class TagToPropertyTag extends ToPropertyTag<TagToPropertyTag>
{
	public static String SELECT_ALL_QUERY = "SELECT tagToPropertyTag FROM TagToPropertyTag AS TagToPropertyTag";
	public static String SELECT_SIZE_QUERY = "SELECT COUNT(tagToPropertyTag) FROM TagToPropertyTag AS TagToPropertyTag";
	
	public TagToPropertyTag()
	{
		super(TagToPropertyTag.class);
	}

	private Integer tagToPropertyTagID;
	private Tag tag;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "TagToPropertyTagID", unique = true, nullable = false)
	public Integer getTagToPropertyTagID()
	{
		return tagToPropertyTagID;
	}

	public void setTagToPropertyTagID(final Integer tagToPropertyTagID)
	{
		this.tagToPropertyTagID = tagToPropertyTagID;
	}

	@ManyToOne
	@JoinColumn(name = "TagID", nullable = false)
	@NotNull
	public Tag getTag()
	{
		return tag;
	}

	public void setTag(final Tag tag)
	{
		this.tag = tag;
	}

	@Override
	@ManyToOne
	@JoinColumn(name = "PropertyTagID", nullable = false)
	@NotNull
	public PropertyTag getPropertyTag()
	{
		return propertyTag;
	}

	@Override
	public void setPropertyTag(final PropertyTag propertyTag)
	{
		this.propertyTag = propertyTag;
	}

	@Override
	@Column(name = "Value", columnDefinition = "TEXT")
	@Size(max = 65535)
	public String getValue()
	{
		return value;
	}

	@Override
	public void setValue(final String value)
	{
		this.value = value;
	}

	@Override
	@Transient
	public Integer getId()
	{
		return this.tagToPropertyTagID;
	}
	
	@Override
	protected boolean testUnique(final EntityManager entityManager, final Number revision)
	{
		try
		{
			if (this.propertyTag.getPropertyTagIsUnique())
			{
				/*
				 * Since having to iterate over thousands of entities is slow,
				 * use a HQL query for the latest versions, for revisions 
				 * though we still have to do it the slow way since we don't know the
				 * revision number.
				 */
				final Long count;
				if (revision == null)
				{
					final String query = TagToPropertyTag.SELECT_SIZE_QUERY + " WHERE tagToPropertyTag.propertyTag = " + this.propertyTag.getId() + " AND tagToPropertyTag.value = '" + this.getValue() + "'";
					count = (Long) entityManager.createQuery(query).getSingleResult();
				}
				else
				{
					final AuditReader reader = AuditReaderFactory.get(entityManager);
					final AuditQueryCreator queryCreator = reader.createQuery();
					final AuditQuery query = queryCreator.forEntitiesAtRevision(TagToPropertyTag.class, revision)
							.addProjection(AuditEntity.id().count("tagToPropertyTagID"))
							.add(AuditEntity.relatedId("propertyTag").eq(this.propertyTag.getId()))
							.add(AuditEntity.property("value").eq(this.getValue()));
					count = (Long) query.getSingleResult();
				}
				
				if (count > 1)
					return false;
			}
		}
		catch (final Exception ex)
		{
			SkynetExceptionUtilities.handleException(ex, false, "Probably an error querying the this.propertyTag.getTagToPropertyTags() property");
			return false;
		}
		
		return true;
	}
}
