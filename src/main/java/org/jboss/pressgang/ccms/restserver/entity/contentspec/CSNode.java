package org.jboss.pressgang.ccms.restserver.entity.contentspec;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.Audited;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.enums.RESTCSNodeTypeV1;
import org.jboss.pressgang.ccms.restserver.entity.PropertyTag;
import org.jboss.pressgang.ccms.restserver.entity.Topic;
import org.jboss.pressgang.ccms.restserver.entity.base.AuditedEntity;
import org.jboss.pressgang.ccms.restserver.utils.Constants;


@Entity
@Audited
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "ContentSpecNode", uniqueConstraints = {
            @UniqueConstraint(columnNames = { "ContentSpecID", "NextNodeID" }),
            @UniqueConstraint(columnNames = { "ContentSpecID", "PreviousNodeID" })})
public class CSNode extends AuditedEntity<CSNode> implements Serializable {

    private static final long serialVersionUID = -5074781793940947664L;
    public static final String SELECT_ALL_QUERY = "select csNode FROM CSNode AS csNode";
    
    private Integer csNodeId = null;
    private String csNodeTitle = null;
    private RESTCSNodeTypeV1 csNodeType = null;
    private ContentSpec contentSpec = null;
    private CSNode parent = null;
    private CSNode next = null;
    private CSNode previous = null;
    private Integer topicId = null;
    private Integer topicRevision = null;
    private String condition = null;
    private Integer flag = 0;
    private Set<CSNode> children = new HashSet<CSNode>(0);
    private Set<CSNodeToCSNode> relatedFromNodes = new HashSet<CSNodeToCSNode>(0);
    private Set<CSNodeToCSNode> relatedToNodes = new HashSet<CSNodeToCSNode>(0);
    private Set<CSNodeToCSTranslatedString> csNodeToCSTranslatedStrings = new HashSet<CSNodeToCSTranslatedString>(0);
    private Set<CSNodeToPropertyTag> csNodeToPropertyTags = new HashSet<CSNodeToPropertyTag>(0);
    
    private Topic topic;

    public CSNode()
    {
        super(CSNode.class);
    }

    @Override
    @Transient
    public Integer getId() {
        return csNodeId;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ContentSpecNodeID", unique = true, nullable = false)
    public Integer getCSNodeId() {
        return csNodeId;
    }

    public void setCSNodeId(Integer csNodeId) {
        this.csNodeId = csNodeId;
    }

    @Column(name = "NodeTitle", nullable = false, length = 512)
    public String getCSNodeTitle() {
        return csNodeTitle;
    }

    public void setCSNodeTitle(String csNodeTitle) {
        this.csNodeTitle = csNodeTitle;
    }

    @Enumerated
    @Column(name = "NodeType", nullable = false)
    public RESTCSNodeTypeV1 getCSNodeType() {
        return csNodeType;
    }

    public void setCSNodeType(RESTCSNodeTypeV1 csNodeType) {
        this.csNodeType = csNodeType;
    }

    @JoinColumn(name = "ContentSpecID")
    @ManyToOne(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    public ContentSpec getContentSpec() {
        return contentSpec;
    }

    public void setContentSpec(ContentSpec contentSpec) {
        this.contentSpec = contentSpec;
    }

    @JoinColumn(name = "ParentID")
    @ManyToOne(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    public CSNode getParent() {
        return parent;
    }

    public void setParent(CSNode parent) {
        this.parent = parent;
    }

    @JoinColumn(name = "NextNodeID")
    @ManyToOne(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    public CSNode getNext() {
        return next;
    }

    public void setNext(CSNode next) {
        this.next = next;
    }

    @JoinColumn(name = "PreviousNodeID")
    @ManyToOne(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    public CSNode getPrevious() {
        return previous;
    }

    public void setPrevious(CSNode previous) {
        this.previous = previous;
    }

    @Column(name = "TopicID")
    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    @Column(name = "TopicRevision")
    public Integer getTopicRevision() {
        return topicRevision;
    }

    public void setTopicRevision(Integer topicRevision) {
        this.topicRevision = topicRevision;
    }

    @Column(name = "NodeCondition")
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<CSNode> getChildren() {
        return children;
    }
    
    public void setChildren(Set<CSNode> children) {
        this.children = children;
    }
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mainNode", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<CSNodeToCSNode> getRelatedFromNodes() {
        return relatedFromNodes;
    }
    
    public void setRelatedFromNodes(Set<CSNodeToCSNode> relatedFromNodes) {
        this.relatedFromNodes = relatedFromNodes;
    }
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "relatedNode", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<CSNodeToCSNode> getRelatedToNodes() {
        return relatedToNodes;
    }
    
    public void setRelatedToNodes(Set<CSNodeToCSNode> relatedToNodes) {
        this.relatedToNodes = relatedToNodes;
    }

    @Column(name = "Flag")
    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "CSNode", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<CSNodeToCSTranslatedString> getCSNodeToCSTranslatedStrings() {
        return csNodeToCSTranslatedStrings;
    }

    public void setCSNodeToCSTranslatedStrings(Set<CSNodeToCSTranslatedString> csNodeToCSTranslatedStrings) {
        this.csNodeToCSTranslatedStrings = csNodeToCSTranslatedStrings;
    }
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "CSNode", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<CSNodeToPropertyTag> getCSNodeToPropertyTags()
    {
        return csNodeToPropertyTags;
    }

    public void setCSNodeToPropertyTags(Set<CSNodeToPropertyTag> csNodeToPropertyTags)
    {
        this.csNodeToPropertyTags = csNodeToPropertyTags;
    }
    
    @Transient
    public List<CSNode> getChildrenList() {
        return new ArrayList<CSNode>(this.getChildren());
    }
    
    @Transient
    public List<CSTranslatedString> getCSTranslatedStringsList()
    {
        final List<CSTranslatedString> translatedStrings = new ArrayList<CSTranslatedString>();
        
        for (final CSNodeToCSTranslatedString mapping : this.csNodeToCSTranslatedStrings) {
            translatedStrings.add(mapping.getCSTranslatedString());
        }
        
        return translatedStrings;
    }
    
    @Transient
    public List<CSNodeToCSNode> getRelatedFromNodesList() {
        return new ArrayList<CSNodeToCSNode>(this.relatedFromNodes);
    }
    
    @Transient
    public List<CSNodeToCSNode> getRelatedToNodesList() {
        return new ArrayList<CSNodeToCSNode>(this.relatedToNodes);
    }
    
    @Transient
    public void removeChild(final CSNode child)
    {
        final List<CSNode> removeNodes = new ArrayList<CSNode>();
        
        for (final CSNode childNode : this.children) {
            if (childNode.equals(child)) {
                removeNodes.add(childNode);
            }
        }
        
        for (final CSNode removeNode : removeNodes) {
            this.children.remove(removeNode);
            removeNode.setParent(null);
        }
    }
    
    @Transient
    public void addChild(final CSNode child) {
        this.children.add(child);
        child.setParent(this);
    }
    
    @Transient
    public List<CSNodeToPropertyTag> getCSNodeToPropertyTagsList() {
        return new ArrayList<CSNodeToPropertyTag>(this.csNodeToPropertyTags);
    }
    
    public void addPropertyTag(final PropertyTag propertyTag, final String value)
    {
        final CSNodeToPropertyTag mapping = new CSNodeToPropertyTag();
        mapping.setCSNode(this);
        mapping.setPropertyTag(propertyTag);
        mapping.setValue(value);

        this.csNodeToPropertyTags.add(mapping);
        propertyTag.getCSNodeToPropertyTags().add(mapping);
    }

    public void removePropertyTag(final PropertyTag propertyTag, final String value)
    {
        final List<CSNodeToPropertyTag> removeList = new ArrayList<CSNodeToPropertyTag>();

        for (final CSNodeToPropertyTag mapping : this.csNodeToPropertyTags)
        {
            final PropertyTag myPropertyTag = mapping.getPropertyTag();
            if (myPropertyTag.equals(propertyTag) && mapping.getValue().equals(value))
            {
                removeList.add(mapping);
            }
        }

        for (final CSNodeToPropertyTag mapping : removeList)
        {
            this.csNodeToPropertyTags.remove(mapping);
            mapping.getPropertyTag().getCSNodeToPropertyTags().remove(mapping);
        }
    }
    
    public void addTranslatedString(final CSTranslatedString translatedString) {
        final CSNodeToCSTranslatedString mapping = new CSNodeToCSTranslatedString();
        
        mapping.setCSNode(this);
        mapping.setCSTranslatedString(translatedString);
        
        this.csNodeToCSTranslatedStrings.add(mapping);
        translatedString.getCSNodeToCSTranslatedStrings().add(mapping);
    }
    
    public void removeTranslatedString(final CSTranslatedString translatedString)
    {
        final List<CSNodeToCSTranslatedString> removeList = new ArrayList<CSNodeToCSTranslatedString>();

        for (final CSNodeToCSTranslatedString mapping : this.csNodeToCSTranslatedStrings)
        {
            if (mapping.getCSTranslatedString().equals(translatedString))
            {
                removeList.add(mapping);
            }
        }

        for (final CSNodeToCSTranslatedString mapping : removeList)
        {
            this.csNodeToCSTranslatedStrings.remove(mapping);
            mapping.getCSTranslatedString().getCSNodeToCSTranslatedStrings().remove(mapping);
        }
    }
    
    @Transient
    public Topic getTopic(final EntityManager entityManager)
    {
        if (topic == null) 
        {
            /* Find the envers topic */
            final AuditReader reader = AuditReaderFactory.get(entityManager);
            final AuditQuery query = reader.createQuery().forEntitiesAtRevision(Topic.class, this.topicRevision).add(AuditEntity.id().eq(this.topicId));
            topic = (Topic) query.getSingleResult();
        }
        return topic;
    }
}
