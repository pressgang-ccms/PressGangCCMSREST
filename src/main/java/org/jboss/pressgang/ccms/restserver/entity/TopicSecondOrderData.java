package org.jboss.pressgang.ccms.restserver.entity;


import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import javax.validation.constraints.Size;
import org.jboss.pressgang.ccms.restserver.entity.base.AuditedEntity;


@Audited
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "TopicSecondOrderData")
public class TopicSecondOrderData extends AuditedEntity<TopicSecondOrderData> implements java.io.Serializable
{
	private static final long serialVersionUID = 3393132758855818345L;
	private Integer topicSecondOrderDataId;
	private String topicHTMLView;
	private String topicXMLErrors;
	
	public TopicSecondOrderData()
	{
		super(TopicSecondOrderData.class);
	}
	
	public TopicSecondOrderData(final Integer topicSecondOrderDataId)
	{
		super(TopicSecondOrderData.class);
		this.topicSecondOrderDataId = topicSecondOrderDataId;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "TopicSecondOrderDataID")
    public Integer getTopicSecondOrderDataId()
	{
		return this.topicSecondOrderDataId;
	}
	
	public void setTopicSecondOrderDataId(final Integer topicSecondOrderDataId)
	{
		this.topicSecondOrderDataId = topicSecondOrderDataId;
	}
	
	// @Column(name = "TopicHTMLView", length = 65535)
	@Column(name = "TopicHTMLView", columnDefinition = "MEDIUMTEXT")
	@Size(max = 16777215)
	public String getTopicHTMLView()
	{
		return this.topicHTMLView;
	}

	public void setTopicHTMLView(final String topicHTMLView)
	{
		this.topicHTMLView = topicHTMLView;
	}
	
	// @Column(name = "TopicXMLErrors", length = 65535)
	@Column(name = "TopicXMLErrors", columnDefinition = "TEXT")
	@Size(max = 65535)
	public String getTopicXMLErrors()
	{
		return this.topicXMLErrors;
	}

	public void setTopicXMLErrors(final String topicXMLErrors)
	{
		this.topicXMLErrors = topicXMLErrors;
	}

	@Override
	@Transient
	public Integer getId()
	{
		return this.topicSecondOrderDataId;
	}
}
