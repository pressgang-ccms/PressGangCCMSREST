package org.jboss.pressgang.ccms.restserver.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.jboss.pressgang.ccms.restserver.entity.base.AuditedEntity;
import org.jboss.pressgang.ccms.restserver.utils.TopicUtilities;

@Audited
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "TopicToTopic", uniqueConstraints = @UniqueConstraint(columnNames = { "MainTopicID", "RelatedTopicID" }))
public class TopicToTopic extends AuditedEntity<TopicToTopic> implements java.io.Serializable {
    private static final long serialVersionUID = -589601408520832737L;
    private Integer topicToTopicId;
    private Topic mainTopic;
    private Topic relatedTopic;
    private RelationshipTag relationshipTag;

    public TopicToTopic() {
        super(TopicToTopic.class);
    }

    public TopicToTopic(final Topic mainTopic, final Topic relatedTopic, final RelationshipTag relationshipTag) {
        super(TopicToTopic.class);
        this.mainTopic = mainTopic;
        this.relatedTopic = relatedTopic;
        this.relationshipTag = relationshipTag;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "TopicToTopicID", unique = true, nullable = false)
    public Integer getTopicToTopicId() {
        return this.topicToTopicId;
    }

    public void setTopicToTopicId(final Integer topicToTopicId) {
        this.topicToTopicId = topicToTopicId;
    }

    @ManyToOne
    @JoinColumn(name = "MainTopicID", nullable = false)
    @NotNull
    public Topic getMainTopic() {
        return this.mainTopic;
    }

    public void setMainTopic(final Topic mainTopic) {
        this.mainTopic = mainTopic;
    }

    @ManyToOne
    @JoinColumn(name = "RelatedTopicID", nullable = false)
    @NotNull
    public Topic getRelatedTopic() {
        return this.relatedTopic;
    }

    public void setRelatedTopic(final Topic relatedTopic) {
        this.relatedTopic = relatedTopic;
    }

    @ManyToOne
    @JoinColumn(name = "RelationshipTagID", nullable = false)
    @NotNull
    public RelationshipTag getRelationshipTag() {
        return relationshipTag;
    }

    public void setRelationshipTag(final RelationshipTag relationshipTag) {
        this.relationshipTag = relationshipTag;
    }

    @Override
    @Transient
    public Integer getId() {
        return this.topicToTopicId;
    }

}
