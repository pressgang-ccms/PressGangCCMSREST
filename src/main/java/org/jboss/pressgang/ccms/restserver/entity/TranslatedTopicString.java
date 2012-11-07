package org.jboss.pressgang.ccms.restserver.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import javax.validation.constraints.NotNull;
import org.jboss.pressgang.ccms.restserver.entity.base.BaseTranslatedString;


@Entity
@Audited
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "TranslatedTopicString")
public class TranslatedTopicString extends BaseTranslatedString<TranslatedTopicString> implements java.io.Serializable
{
	private static final long serialVersionUID = 5185674451816385008L;
	private Integer translatedTopicStringID;
	private TranslatedTopicData translatedTopicData;
	
	
	public TranslatedTopicString()
	{
		super(TranslatedTopicString.class);
	}
	
	@Transient
	public Integer getId()
	{
		return this.translatedTopicStringID;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "TranslatedTopicStringID", unique = true, nullable = false)
	public Integer getTranslatedTopicStringID()
	{
		return translatedTopicStringID;
	}

	public void setTranslatedTopicStringID(final Integer translatedTopicStringID)
	{
		this.translatedTopicStringID = translatedTopicStringID;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TranslatedTopicDataID", nullable = false)
	@NotNull
	public TranslatedTopicData getTranslatedTopicData()
	{
		return translatedTopicData;
	}

	public void setTranslatedTopicData(final TranslatedTopicData translatedTopicData)
	{
		this.translatedTopicData = translatedTopicData;
	}
}
