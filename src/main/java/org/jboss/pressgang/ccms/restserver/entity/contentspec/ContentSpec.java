package org.jboss.pressgang.ccms.restserver.entity.contentspec;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.Matchers.equalTo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import static javax.persistence.GenerationType.IDENTITY;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.enums.RESTContentSpecTypeV1;
import org.jboss.pressgang.ccms.restserver.entity.PropertyTag;
import org.jboss.pressgang.ccms.restserver.entity.Tag;
import org.jboss.pressgang.ccms.restserver.entity.TagToCategory;
import org.jboss.pressgang.ccms.restserver.entity.base.AuditedEntity;
import org.jboss.pressgang.ccms.restserver.exceptions.CustomConstraintViolationException;
import org.jboss.pressgang.ccms.restserver.sort.TagIDComparator;
import org.jboss.pressgang.ccms.restserver.utils.Constants;


@Entity
@Audited
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "ContentSpec")
public class ContentSpec extends AuditedEntity<ContentSpec> implements Serializable {
    
    private static final long serialVersionUID = 5229054857631287690L;
    public static final String SELECT_ALL_QUERY = "select contentSpec from ContentSpec as contentSpec";

    private Integer contentSpecId = null;
    private String contentSpecTitle = null;
    private RESTContentSpecTypeV1 contentSpecType = RESTContentSpecTypeV1.BOOK;
    private String locale = null;
    private Date lastPublished = null;
    private Set<ContentSpecToPropertyTag> contentSpecToPropertyTags = new HashSet<ContentSpecToPropertyTag>(0);
    private Set<ContentSpecToCSMetaData> contentSpecToCSMetaData = new HashSet<ContentSpecToCSMetaData>(0);
    private Set<CSNode> csNodes = new HashSet<CSNode>(0);
    private Set<ContentSpecToTag> contentSpecToTags = new HashSet<ContentSpecToTag>(0);
    
    @Override
    @Transient
    public Integer getId()
    {
        return this.contentSpecId;
    }

    public ContentSpec()
    {
        super(ContentSpec.class);
    }
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ContentSpecID", unique = true, nullable = false)
    public Integer getContentSpecId() {
        return contentSpecId;
    }

    public void setContentSpecId(Integer contentSpecId) {
        this.contentSpecId = contentSpecId;
    }

    @Column(name = "ContentSpecTitle", nullable = false, length = 255)
    public String getContentSpecTitle() {
        return contentSpecTitle;
    }

    public void setContentSpecTitle(String contentSpecTitle) {
        this.contentSpecTitle = contentSpecTitle;
    }

    @Column(name = "Locale", nullable = false, length = 255)
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Enumerated
    @Column(name = "ContentSpecType", nullable = false)
    public RESTContentSpecTypeV1 getContentSpecType() {
        return contentSpecType;
    }

    public void setContentSpecType(RESTContentSpecTypeV1 contentSpecType) {
        this.contentSpecType = contentSpecType;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contentSpec", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<ContentSpecToPropertyTag> getContentSpecToPropertyTags()
    {
        return contentSpecToPropertyTags;
    }
    
    public void setContentSpecToPropertyTags(Set<ContentSpecToPropertyTag> contentSpecToPropertyTags) {
        this.contentSpecToPropertyTags = contentSpecToPropertyTags;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contentSpec", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<ContentSpecToCSMetaData> getContentSpecToCSMetaData() {
        return contentSpecToCSMetaData;
    }

    public void setContentSpecToCSMetaData(Set<ContentSpecToCSMetaData> contentSpecToCSMetaData) {
        this.contentSpecToCSMetaData = contentSpecToCSMetaData;
    }
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contentSpec", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<CSNode> getCSNodes() {
        return csNodes;
    }

    public void setCSNodes(Set<CSNode> csNodes) {
        this.csNodes = csNodes;
    }
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contentSpec", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<ContentSpecToTag> getContentSpecToTags() {
        return this.contentSpecToTags;
    }
    
    public void setContentSpecToTags(Set<ContentSpecToTag> contentSpecToTags) {
        this.contentSpecToTags = contentSpecToTags;
    }

    public Date getLastPublished() {
        return lastPublished;
    }

    public void setLastPublished(Date lastPublished) {
        this.lastPublished = lastPublished;
    }
    
    @Transient
    public List<CSNode> getTopCSNodes() {
        final List<CSNode> nodes = new ArrayList<CSNode>();
        
        for (final CSNode node : csNodes) {
            if (node.getParent() == null) {
                nodes.add(node);
            }
        }
        
        return nodes;
    }
    
    @Transient
    public List<ContentSpecToCSMetaData> getContentSpecMetaDataList()
    {
        return new ArrayList<ContentSpecToCSMetaData>(this.contentSpecToCSMetaData);
    }
    
    @Transient
    public void removeChild(final CSNode child)
    {
        final List<CSNode> removeNodes = new ArrayList<CSNode>();
        
        for (final CSNode childNode : this.csNodes) {
            if (childNode.getId().equals(child.getId())) {
                removeNodes.add(childNode);
            }
        }
        
        for (final CSNode removeNode : removeNodes) {
            this.csNodes.remove(removeNode);
            removeNode.setContentSpec(null);
        }
    }
    
    @Transient
    public void addChild(final CSNode child) {
        this.csNodes.add(child);
        child.setContentSpec(this);
    }
    
    @Transient
    public List<ContentSpecToPropertyTag> getContentSpecToPropertyTagsList() {
        return new ArrayList<ContentSpecToPropertyTag>(this.contentSpecToPropertyTags);
    }
    
    public void addPropertyTag(final PropertyTag propertyTag, final String value)
    {
        final ContentSpecToPropertyTag mapping = new ContentSpecToPropertyTag();
        mapping.setContentSpec(this);
        mapping.setPropertyTag(propertyTag);
        mapping.setValue(value);

        this.contentSpecToPropertyTags.add(mapping);
        propertyTag.getContentSpecToPropertyTags().add(mapping);
    }

    public void removePropertyTag(final PropertyTag propertyTag, final String value)
    {
        final List<ContentSpecToPropertyTag> removeList = new ArrayList<ContentSpecToPropertyTag>();

        for (final ContentSpecToPropertyTag mapping : this.contentSpecToPropertyTags)
        {
            final PropertyTag myPropertyTag = mapping.getPropertyTag();
            if (myPropertyTag.equals(propertyTag) && mapping.getValue().equals(value))
            {
                removeList.add(mapping);
            }
        }

        for (final ContentSpecToPropertyTag mapping : removeList)
        {
            this.contentSpecToPropertyTags.remove(mapping);
            mapping.getPropertyTag().getContentSpecToPropertyTags().remove(mapping);
        }
    }
    
    public void addMetaData(final CSMetaData metaData, final String value)
    {
        final ContentSpecToCSMetaData mapping = new ContentSpecToCSMetaData();
        mapping.setContentSpec(this);
        mapping.setCSMetaData(metaData);
        mapping.setValue(value);

        this.contentSpecToCSMetaData.add(mapping);
        metaData.getContentSpecToCSMetaData().add(mapping);
    }

    public void removeMetaData(final CSMetaData metaData, final String value)
    {
        final List<ContentSpecToCSMetaData> removeList = new ArrayList<ContentSpecToCSMetaData>();

        for (final ContentSpecToCSMetaData mapping : this.contentSpecToCSMetaData)
        {
            final CSMetaData myMetaData = mapping.getCSMetaData();
            if (myMetaData.equals(metaData) && mapping.getValue().equals(value))
            {
                removeList.add(mapping);
            }
        }

        for (final ContentSpecToCSMetaData mapping : removeList)
        {
            this.contentSpecToCSMetaData.remove(mapping);
            mapping.getCSMetaData().getContentSpecToCSMetaData().remove(mapping);
        }
    }
    
    @Transient
    public List<Tag> getTags()
    {
        final List<Tag> retValue = new ArrayList<Tag>();
        for (final ContentSpecToTag contentSpecToTag : this.contentSpecToTags)
        {
            final Tag tag = contentSpecToTag.getTag();
            retValue.add(tag);
        }

        Collections.sort(retValue, new TagIDComparator());

        return retValue;
    }

    public void addTag(final Tag tag) throws CustomConstraintViolationException
    {
        if (filter(having(on(ContentSpecToTag.class).getTag(), equalTo(tag)), this.getContentSpecToTags()).size() == 0)
        {

            // remove any excluded tags
            for (final Tag excludeTag : tag.getExcludedTags())
            {
                if (excludeTag.equals(tag))
                    continue;

                this.removeTag(excludeTag);
            }

            // Remove other tags if the category is mutually exclusive
            for (final TagToCategory category : tag.getTagToCategories())
            {
                if (category.getCategory().isMutuallyExclusive())
                {
                    for (final Tag categoryTag : category.getCategory().getTags())
                    {
                        if (categoryTag.equals(tag))
                            continue;
                        
                        // Check if the Category Tag exists in this topic
                        if (filter(having(on(ContentSpecToTag.class).getTag(), equalTo(categoryTag)), this.getContentSpecToTags()).size() != 0)
                        {
                            throw new CustomConstraintViolationException("Adding Tag " + tag.getTagName() + " (" + tag.getId() + ") failed due to a mutually exclusive constraint violation.");
                        }
                    }
                }
            }
            
            final ContentSpecToTag mapping = new ContentSpecToTag(this, tag);
            this.contentSpecToTags.add(mapping);
            tag.getContentSpecToTags().add(mapping);
        }
    }
    
    public void removeTag(final Tag tag)
    {
        final List<ContentSpecToTag> mappingEntities = filter(having(on(ContentSpecToTag.class).getTag(), equalTo(tag)), this.getContentSpecToTags());
        if (mappingEntities.size() != 0)
        {
            for (final ContentSpecToTag mapping : mappingEntities)
            {
                this.contentSpecToTags.remove(mapping);
                mapping.getTag().getContentSpecToTags().remove(mapping);
            }
        }
    }
}
