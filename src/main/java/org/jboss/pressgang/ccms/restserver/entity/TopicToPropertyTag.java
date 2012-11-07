package org.jboss.pressgang.ccms.restserver.entity;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.Audited;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.AuditQueryCreator;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import org.jboss.pressgang.ccms.restserver.entity.base.ToPropertyTag;

@Audited
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "TopicToPropertyTag")
public class TopicToPropertyTag extends ToPropertyTag<TopicToPropertyTag> {
    public static String SELECT_ALL_QUERY = "SELECT topicToPropertyTag FROM TopicToPropertyTag AS TopicToPropertyTag";
    public static String SELECT_SIZE_QUERY = "SELECT COUNT(topicToPropertyTag) FROM TopicToPropertyTag AS TopicToPropertyTag";
    private Integer TopicToPropertyTagID;
    private Topic topic;

    public TopicToPropertyTag() {
        super(TopicToPropertyTag.class);
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "TopicToPropertyTagID", unique = true, nullable = false)
    public Integer getTopicToPropertyTagID() {
        return TopicToPropertyTagID;
    }

    public void setTopicToPropertyTagID(final Integer TopicToPropertyTagID) {
        this.TopicToPropertyTagID = TopicToPropertyTagID;
    }

    @ManyToOne
    @JoinColumn(name = "TopicID", nullable = false)
    @NotNull
    public Topic getTopic() {
        return topic;
    }

    public void setTopic(final Topic topic) {
        this.topic = topic;
    }

    @Override
    @ManyToOne
    @JoinColumn(name = "PropertyTagID", nullable = false)
    @NotNull
    public PropertyTag getPropertyTag() {
        return propertyTag;
    }

    @Override
    public void setPropertyTag(final PropertyTag propertyTag) {
        this.propertyTag = propertyTag;
    }

    @Override
    @Column(name = "Value", columnDefinition = "TEXT")
    @Size(max = 65535)
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    @Transient
    public Integer getId() {
        return this.TopicToPropertyTagID;
    }

    @Override
    protected boolean testUnique(final EntityManager entityManager, final Number revision) {
        if (this.propertyTag.getPropertyTagIsUnique()) {
            /*
             * Since having to iterate over thousands of entities is slow, use a HQL query to find the count for us.
             */
            final Long count;
            if (revision == null) {
                final String query = TopicToPropertyTag.SELECT_SIZE_QUERY + " WHERE topicToPropertyTag.propertyTag = "
                        + this.propertyTag.getId() + " AND topicToPropertyTag.value = '" + this.getValue() + "'";
                count = (Long) entityManager.createQuery(query).getSingleResult();
            } else {
                final AuditReader reader = AuditReaderFactory.get(entityManager);
                final AuditQueryCreator queryCreator = reader.createQuery();
                final AuditQuery query = queryCreator.forEntitiesAtRevision(TopicToPropertyTag.class, revision)
                        .addProjection(AuditEntity.id().count("topicToPropertyTagID"))
                        .add(AuditEntity.relatedId("propertyTag").eq(this.propertyTag.getId()))
                        .add(AuditEntity.property("value").eq(this.getValue()));
                query.setCacheable(true);
                count = (Long) query.getSingleResult();
            }

            if (count > 1)
                return false;
        }

        return true;
    }
}
