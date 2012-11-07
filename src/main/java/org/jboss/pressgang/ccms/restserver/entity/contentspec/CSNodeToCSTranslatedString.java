package org.jboss.pressgang.ccms.restserver.entity.contentspec;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import javax.validation.constraints.NotNull;
import org.jboss.pressgang.ccms.restserver.entity.base.AuditedEntity;


@Entity
@Audited
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "CSNodeToCSTranslatedString", uniqueConstraints = @UniqueConstraint(columnNames = { "ContentSpecNodeID", "ContentSpecTranslatedStringID" }))
public class CSNodeToCSTranslatedString extends AuditedEntity<CSNodeToCSTranslatedString> implements java.io.Serializable
{
    private static final long serialVersionUID = -7516063608506037594L;
    private Integer topicToTagId;
    private CSNode csNode;
    private CSTranslatedString csTranslatedString;

    public CSNodeToCSTranslatedString()
    {
        super(CSNodeToCSTranslatedString.class);
    }

    public CSNodeToCSTranslatedString(final CSNode csMetaData, final CSTranslatedString csTranslatedString) 
    {
        super(CSNodeToCSTranslatedString.class);
        this.csNode = csMetaData;
        this.csTranslatedString = csTranslatedString;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "TopicToTagID", unique = true, nullable = false)
    public Integer getTopicToTagId()
    {
        return this.topicToTagId;
    }

    public void setTopicToTagId(final Integer topicToTagId)
    {
        this.topicToTagId = topicToTagId;
    }

    @ManyToOne
    @JoinColumn(name = "ContentSpecNodeID", nullable = false)
    @NotNull
    public CSNode getCSNode()
    {
        return this.csNode;
    }

    public void setCSNode(final CSNode csNode)
    {
        this.csNode = csNode;
    }

    @ManyToOne
    @JoinColumn(name = "ContentSpecTranslatedStringID", nullable = false)
    @NotNull
    public CSTranslatedString getCSTranslatedString()
    {
        return this.csTranslatedString;
    }

    public void setCSTranslatedString(final CSTranslatedString csTranslatedString)
    {
        this.csTranslatedString = csTranslatedString;
    }

    @Override
    @Transient
    public Integer getId()
    {
        return this.topicToTagId;
    }

}