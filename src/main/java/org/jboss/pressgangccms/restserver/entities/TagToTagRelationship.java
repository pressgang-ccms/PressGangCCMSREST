package org.jboss.pressgangccms.restserver.entities;

// Generated Aug 8, 2011 11:54:01 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.jboss.pressgangccms.restserver.constants.Constants;
import org.jboss.pressgangccms.restserver.entities.base.AuditedEntity;

@Audited
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "TagToTagRelationship")
public class TagToTagRelationship extends AuditedEntity<TagToTagRelationship> implements java.io.Serializable 
{
	private static final long serialVersionUID = 54337112345485162L;
	private int tagToTagRelationshipType;
	private String tagToTagRelationshipDescription;
	private Set<TagToTag> tagToTags = new HashSet<TagToTag>(0);

	public TagToTagRelationship() 
	{
		super(TagToTagRelationship.class);
	}

	public TagToTagRelationship(final int tagToTagRelationshipType) 
	{
		super(TagToTagRelationship.class);
		this.tagToTagRelationshipType = tagToTagRelationshipType;
	}

	public TagToTagRelationship(
			final int tagToTagRelationshipType,
			final String tagToTagRelationshipDescription, Set<TagToTag> tagToTags) 
	{		
		super(TagToTagRelationship.class);
		this.tagToTagRelationshipType = tagToTagRelationshipType;
		this.tagToTagRelationshipDescription = tagToTagRelationshipDescription;
		this.tagToTags = tagToTags;
	}

	@Id
	@Column(name = "TagToTagRelationshipType", unique = true, nullable = false)
	public int getTagToTagRelationshipType() {
		return this.tagToTagRelationshipType;
	}

	public void setTagToTagRelationshipType(final int tagToTagRelationshipType) 
	{
		this.tagToTagRelationshipType = tagToTagRelationshipType;
	}

	//@Column(name = "TagToTagRelationshipDescription", length = 65535)
	@Column(name = "TagToTagRelationshipDescription", columnDefinition="TEXT")
	@Size(max = 65535)
	public String getTagToTagRelationshipDescription() 
	{
		return this.tagToTagRelationshipDescription;
	}

	public void setTagToTagRelationshipDescription(final String tagToTagRelationshipDescription)
	{
		this.tagToTagRelationshipDescription = tagToTagRelationshipDescription;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tagToTagRelationship")
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	@BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
	public Set<TagToTag> getTagToTags() 
	{
		return this.tagToTags;
	}

	public void setTagToTags(final Set<TagToTag> tagToTags) 
	{
		this.tagToTags = tagToTags;
	}

	@Override
	@Transient
	public Integer getId()
	{
		return this.tagToTagRelationshipType;
	}

}
