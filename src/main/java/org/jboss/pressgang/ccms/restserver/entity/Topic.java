package org.jboss.pressgang.ccms.restserver.entity;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static javax.persistence.GenerationType.IDENTITY;
import static org.hamcrest.Matchers.equalTo;
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
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceException;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import net.htmlparser.jericho.Source;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.jboss.pressgang.ccms.restserver.entity.base.ParentToPropertyTag;
import org.jboss.pressgang.ccms.restserver.entity.base.ToPropertyTag;
import org.jboss.pressgang.ccms.restserver.exceptions.CustomConstraintViolationException;
import org.jboss.pressgang.ccms.restserver.sort.TagIDComparator;
import org.jboss.pressgang.ccms.restserver.sort.TopicIDComparator;
import org.jboss.pressgang.ccms.restserver.sort.TopicToTopicMainTopicIDSort;
import org.jboss.pressgang.ccms.restserver.sort.TopicToTopicRelatedTopicIDSort;
import org.jboss.pressgang.ccms.restserver.utils.Constants;
import org.jboss.pressgang.ccms.restserver.utils.TopicUtilities;
import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;

@Entity
@Audited
@Indexed
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "Topic")
public class Topic extends ParentToPropertyTag<Topic> implements java.io.Serializable {
    public static final String SELECT_ALL_QUERY = "SELECT topic FROM Topic as Topic";
    private static final long serialVersionUID = 5580473587657911655L;

    private Integer topicId;
    private String topicText;
    private Date topicTimeStamp;
    private String topicTitle;
    private Set<TopicToTag> topicToTags = new HashSet<TopicToTag>(0);
    private Set<TopicToTopic> parentTopicToTopics = new HashSet<TopicToTopic>(0);
    private Set<TopicToTopic> childTopicToTopics = new HashSet<TopicToTopic>(0);
    private Set<TopicToTopicSourceUrl> topicToTopicSourceUrls = new HashSet<TopicToTopicSourceUrl>(0);
    private Set<TopicToPropertyTag> topicToPropertyTags = new HashSet<TopicToPropertyTag>(0);
    private Set<TopicToBugzillaBug> topicToBugzillaBugs = new HashSet<TopicToBugzillaBug>(0);
    private String topicXML;
    private TopicSecondOrderData topicSecondOrderData;
    private String topicLocale = CommonConstants.DEFAULT_LOCALE;

    @Override
    @Transient
    public Integer getId() {
        return this.topicId;
    }

    public Topic() {
        super(Topic.class);
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "TopicID", unique = true, nullable = false)
    public Integer getTopicId() {
        return this.topicId;
    }
    
    public void setTopicId(final Integer topicId) {
        this.topicId = topicId;
    }

    @Column(name = "TopicLocale", length = 45)
    @NotNull
    @Size(max = 45)
    public String getTopicLocale() {
        return this.topicLocale == null ? CommonConstants.DEFAULT_LOCALE : this.topicLocale;
    }
    
    public void setTopicLocale(final String topicLocale) {
        this.topicLocale = topicLocale;
    }
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "relatedTopic")
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<TopicToTopic> getChildTopicToTopics() {
        return childTopicToTopics;
    }
    
    public void setChildTopicToTopics(final Set<TopicToTopic> childTopicToTopics) {
        this.childTopicToTopics = childTopicToTopics;
    }
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mainTopic", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<TopicToTopic> getParentTopicToTopics() {
        return parentTopicToTopics;
    }
    
    public void setParentTopicToTopics(final Set<TopicToTopic> parentTopicToTopics) {
        this.parentTopicToTopics = parentTopicToTopics;
    }

    @OneToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "TopicToTopicSecondOrderData", joinColumns = { @JoinColumn(name = "TopicID", unique = true) }, inverseJoinColumns = { @JoinColumn(name = "TopicSecondOrderDataID") })
    @NotAudited
    public TopicSecondOrderData getTopicSecondOrderData() {
        return topicSecondOrderData;
    }
    
    public void setTopicSecondOrderData(TopicSecondOrderData topicSecondOrderData) {
        this.topicSecondOrderData = topicSecondOrderData;
    }

    @Column(name = "TopicText", columnDefinition = "TEXT")
    @Size(max = 65535)
    public String getTopicText() {
        return this.topicText;
    }
    
    public void setTopicText(final String topicText) {
        this.topicText = topicText;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TopicTimeStamp", nullable = false, length = 0)
    @NotNull
    public Date getTopicTimeStamp() {
        return this.topicTimeStamp;
    }
    
    public void setTopicTimeStamp(final Date topicTimeStamp) {
        this.topicTimeStamp = topicTimeStamp;
    }

    @Column(name = "TopicTitle", nullable = false, length = 255)
    @NotNull
    @Size(max = 255)
    public String getTopicTitle() {
        return this.topicTitle;
    }
    
    public void setTopicTitle(final String topicTitle) {
        this.topicTitle = topicTitle;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<TopicToTag> getTopicToTags() {
        return this.topicToTags;
    }
    
    public void setTopicToTags(final Set<TopicToTag> topicToTags) {
        this.topicToTags = topicToTags;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<TopicToTopicSourceUrl> getTopicToTopicSourceUrls() {
        return this.topicToTopicSourceUrls;
    }
    
    public void setTopicToTopicSourceUrls(final Set<TopicToTopicSourceUrl> topicToTopicSourceUrls) {
        this.topicToTopicSourceUrls = topicToTopicSourceUrls;
    }

    @Column(name = "TopicXML", columnDefinition = "MEDIUMTEXT")
    @Size(max = 16777215)
    public String getTopicXML() {
        return this.topicXML;
    }
    
    public void setTopicXML(final String topicXML) {
        this.topicXML = topicXML;
    }
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<TopicToPropertyTag> getTopicToPropertyTags() {
        return topicToPropertyTags;
    }

    public void setTopicToPropertyTags(Set<TopicToPropertyTag> topicToPropertyTags) {
        this.topicToPropertyTags = topicToPropertyTags;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<TopicToBugzillaBug> getTopicToBugzillaBugs() {
        return this.topicToBugzillaBugs;
    }

    public void setTopicToBugzillaBugs(final Set<TopicToBugzillaBug> topicToBugzillaBugs) {
        this.topicToBugzillaBugs = topicToBugzillaBugs;
    }

    /**
     * This function will take the XML in the topicXML String and use it to generate a text only view that will be used by
     * Hibernate Search. The text extraction uses Jericho - http://jericho.htmlparser.net/
     */
    @Transient
    @Field(name = "TopicSearchText", index = Index.YES, analyze=Analyze.YES, store = Store.YES)
    public String getTopicSearchText() {
        if (this.topicXML == null)
            return "";

        final Source source = new Source(this.topicXML);
        source.fullSequentialParse();
        return source.getTextExtractor().toString();
    }
    
    @Transient
    public String getTopicRendered() {
        if (this.topicSecondOrderData == null)
            return null;

        return topicSecondOrderData.getTopicHTMLView();
    }
    
    public void setTopicRendered(final String value) {
        if (this.topicSecondOrderData == null)
            this.topicSecondOrderData = new TopicSecondOrderData();

        this.topicSecondOrderData.setTopicHTMLView(value);
    }

    @Transient
    public String getTopicXMLErrors() {
        if (this.topicSecondOrderData == null)
            return null;

        return topicSecondOrderData.getTopicXMLErrors();
    }
    
    public void setTopicXMLErrors(final String value) {
        if (this.topicSecondOrderData == null) {
            this.topicSecondOrderData = new TopicSecondOrderData();
        }

        this.topicSecondOrderData.setTopicXMLErrors(value);
    }
    
    @SuppressWarnings("unused")
    @PostPersist
    private void onPostPersist() {
        TopicUtilities.render(this);
    }

    @SuppressWarnings("unused")
    @PostUpdate
    private void onPostUpdate() {
        TopicUtilities.render(this);
    }

    @SuppressWarnings("unused")
    @PrePersist
    private void onPrePresist() {
        this.topicTimeStamp = new Date();
        TopicUtilities.validateAndFixTags(this);
        TopicUtilities.validateAndFixRelationships(this);
    }

    @SuppressWarnings("unused")
    @PreUpdate
    private void onPreUpdate() {
        TopicUtilities.validateAndFixTags(this);
        TopicUtilities.validateAndFixRelationships(this);
    }

    @Transient
    public boolean isRelatedTo(final Integer relatedTopicId) {
        for (final TopicToTopic topicToTopic : this.getParentTopicToTopics())
            if (topicToTopic.getRelatedTopic().topicId.equals(relatedTopicId))
                return true;

        return false;
    }

    @Transient
    public boolean isRelatedTo(final Topic relatedTopic, final RelationshipTag relationshipTag) {
        for (final TopicToTopic topicToTopic : this.getParentTopicToTopics())
            if (topicToTopic.getRelatedTopic().equals(relatedTopic)
                    && topicToTopic.getRelationshipTag().equals(relationshipTag))
                return true;

        return false;
    }

    @Transient
    public boolean isRelatedTo(final Topic relatedTopic) {
        for (final TopicToTopic topicToTopic : this.getParentTopicToTopics())
            if (topicToTopic.getRelatedTopic().equals(relatedTopic))
                return true;

        return false;
    }

    @Transient
    public boolean isTaggedWith(final Integer tagId) {
        for (final TopicToTag topicToTag : this.getTopicToTags())
            if (topicToTag.getTag().getTagId().equals(tagId))
                return true;

        return false;
    }

    @Transient
    public boolean isTaggedWith(final Tag tag) {
        for (final TopicToTag topicToTag : this.getTopicToTags())
            if (topicToTag.getTag().equals(tag))
                return true;

        return false;
    }
    
    public boolean addRelationshipFrom(final EntityManager entityManager, final Integer topicId, final Integer relationshipTagId) {
        final Topic topic = entityManager.getReference(Topic.class, topicId);
        final RelationshipTag relationshipTag = entityManager.getReference(RelationshipTag.class, relationshipTagId);
        return addRelationshipFrom(topic, relationshipTag);
    }

    public boolean addRelationshipFrom(final EntityManager entityManager, final Topic topic, final Integer relationshipTagId) {
        final RelationshipTag relationshipTag = entityManager.getReference(RelationshipTag.class, relationshipTagId);
        return addRelationshipFrom(topic, relationshipTag);
    }

    public boolean addRelationshipFrom(final Topic relatedTopic, final RelationshipTag relationshipTag) {
        if (!this.isRelatedTo(relatedTopic, relationshipTag)) {
            final TopicToTopic topicToTopic = new TopicToTopic(relatedTopic, this, relationshipTag);
            this.getChildTopicToTopics().add(topicToTopic);
            relatedTopic.getParentTopicToTopics().add(topicToTopic);
            return true;
        }

        return false;
    }

    public boolean addRelationshipTo(final EntityManager entityManager, final Integer topicId, final Integer relationshipTagId) {
        final Topic topic = entityManager.getReference(Topic.class, topicId);
        final RelationshipTag relationshipTag = entityManager.getReference(RelationshipTag.class, relationshipTagId);
        return addRelationshipTo(topic, relationshipTag);
    }

    public boolean addRelationshipTo(final EntityManager entityManager, final Topic topic, final Integer relationshipTagId) {
        final RelationshipTag relationshipTag = entityManager.getReference(RelationshipTag.class, relationshipTagId);
        return addRelationshipTo(topic, relationshipTag);
    }

    public boolean addRelationshipTo(final Topic relatedTopic, final RelationshipTag relationshipTag) {
        if (!this.isRelatedTo(relatedTopic, relationshipTag)) {
            final TopicToTopic topicToTopic = new TopicToTopic(this, relatedTopic, relationshipTag);
            this.getParentTopicToTopics().add(topicToTopic);
            relatedTopic.getChildTopicToTopics().add(topicToTopic);
            return true;
        }

        return false;
    }

    public void addTag(final EntityManager entityManager, final int tagID) throws CustomConstraintViolationException {
        final Tag tag = entityManager.getReference(Tag.class, tagID);
        addTag(tag);
    }

    public void addTag(final Tag tag) throws CustomConstraintViolationException {
        if (filter(having(on(TopicToTag.class).getTag(), equalTo(tag)), this.getTopicToTags()).size() == 0) {

            // remove any excluded tags
            for (final Tag excludeTag : tag.getExcludedTags()) {
                if (excludeTag.equals(tag))
                    continue;

                this.removeTag(excludeTag);
            }

            // Remove other tags if the category is mutually exclusive
            for (final TagToCategory category : tag.getTagToCategories()) {
                if (category.getCategory().isMutuallyExclusive()) {
                    for (final Tag categoryTag : category.getCategory().getTags()) {
                        if (categoryTag.equals(tag))
                            continue;

                        // Check if the Category Tag exists in this topic
                        if (filter(having(on(TopicToTag.class).getTag(), equalTo(categoryTag)), this.getTopicToTags()).size() != 0) {
                            throw new CustomConstraintViolationException("Adding Tag " + tag.getTagName() + " (" + tag.getId()
                                    + ") failed due to a mutually exclusive constraint violation.");
                        }
                    }
                }
            }

            final TopicToTag mapping = new TopicToTag(this, tag);
            this.topicToTags.add(mapping);
            tag.getTopicToTags().add(mapping);
        }
    }

    public void addTopicSourceUrl(final TopicSourceUrl topicSourceUrl) {
        if (filter(having(on(TopicToTopicSourceUrl.class).getTopicSourceUrl(), equalTo(topicSourceUrl)),
                this.getTopicToTopicSourceUrls()).size() == 0) {
            this.topicToTopicSourceUrls.add(new TopicToTopicSourceUrl(topicSourceUrl, this));
        }
    }

    public void addBugzillaBug(final BugzillaBug entity) {
        if (filter(having(on(TopicToBugzillaBug.class).getBugzillaBug(), equalTo(entity)), this.topicToBugzillaBugs).size() == 0) {
            final TopicToBugzillaBug mapping = new TopicToBugzillaBug(entity, this);
            this.topicToBugzillaBugs.add(mapping);
            entity.getTopicToBugzillaBugs().add(mapping);
        }
    }

    @Transient
    public List<Integer> getIncomingRelatedTopicIDs() {
        final List<Integer> retValue = new ArrayList<Integer>();
        for (final TopicToTopic topicToTopic : this.getChildTopicToTopics())
            retValue.add(topicToTopic.getMainTopic().getTopicId());
        return retValue;
    }

    @Transient
    public List<Topic> getOutgoingRelatedTopicsArray() {
        final ArrayList<Topic> retValue = new ArrayList<Topic>();
        for (final TopicToTopic topicToTopic : this.getParentTopicToTopics()) {
            retValue.add(topicToTopic.getRelatedTopic());
        }
        return retValue;
    }

    @Transient
    public List<Topic> getIncomingRelatedTopicsArray() {
        final ArrayList<Topic> retValue = new ArrayList<Topic>();
        for (final TopicToTopic topicToTopic : this.getChildTopicToTopics())
            retValue.add(topicToTopic.getMainTopic());

        Collections.sort(retValue, new TopicIDComparator());

        return retValue;
    }

    @Transient
    public Topic getRelatedTopicByID(final Integer id) {
        for (final Topic topic : this.getOutgoingRelatedTopicsArray())
            if (topic.getTopicId().equals(id))
                return topic;
        return null;
    }

    @Transient
    public List<Integer> getRelatedTopicIDs() {
        final List<Integer> retValue = new ArrayList<Integer>();
        for (final TopicToTopic topicToTopic : this.getParentTopicToTopics())
            retValue.add(topicToTopic.getRelatedTopic().getTopicId());
        return retValue;
    }

    @Transient
    public List<Integer> getTagIDs() {
        final List<Integer> retValue = new ArrayList<Integer>();
        for (final TopicToTag topicToTag : this.topicToTags) {
            final Integer tagId = topicToTag.getTag().getTagId();
            retValue.add(tagId);
        }

        return retValue;
    }

    @Transient
    public List<Tag> getTags() {
        final List<Tag> retValue = new ArrayList<Tag>();
        for (final TopicToTag topicToTag : this.topicToTags) {
            final Tag tag = topicToTag.getTag();
            retValue.add(tag);
        }

        Collections.sort(retValue, new TagIDComparator());

        return retValue;
    }

    /**
     * This is necessary because a4j:repeat does not work with a Set
     */
    @Transient
    public ArrayList<Tag> getTagsArray() {
        final ArrayList<Tag> retValue = new ArrayList<Tag>();
        for (final TopicToTag topicToTag : this.topicToTags)
            retValue.add(topicToTag.getTag());
        return retValue;
    }

    @Transient
    public List<Tag> getTagsInCategories(final List<Category> categories) {
        final List<Integer> catgeoriesByID = new ArrayList<Integer>();
        for (final Category category : categories)
            catgeoriesByID.add(category.getCategoryId());
        return getTagsInCategoriesByID(catgeoriesByID);
    }

    @Transient
    public List<Tag> getTagsInCategoriesByID(final List<Integer> categories) {
        final List<Tag> retValue = new ArrayList<Tag>();

        for (final Integer categoryId : categories) {
            for (final TopicToTag topicToTag : this.topicToTags) {
                final Tag tag = topicToTag.getTag();

                if (topicToTag.getTag().isInCategory(categoryId)) {
                    if (!retValue.contains(tag))
                        retValue.add(tag);
                }
            }
        }

        return retValue;
    }

    public boolean removeRelationshipTo(final Topic topic, final RelationshipTag relationshipTag) {
        return removeRelationshipTo(topic.getTopicId(), relationshipTag.getRelationshipTagId());
    }

    public boolean removeRelationshipTo(final Integer relatedTopicId, final Integer relationshipTagId) {
        for (final TopicToTopic topicToTopic : this.getParentTopicToTopics()) {
            final Topic relatedTopic = topicToTopic.getRelatedTopic();
            final RelationshipTag relationshipTag = topicToTopic.getRelationshipTag();

            if (relatedTopic.getTopicId().equals(relatedTopicId)
                    && relationshipTag.getRelationshipTagId().equals(relationshipTagId)) {
                /* remove the relationship from this topic */
                this.getParentTopicToTopics().remove(topicToTopic);

                /* now remove the relationship from the other topic */
                for (final TopicToTopic childTopicToTopic : relatedTopic.getChildTopicToTopics()) {
                    if (childTopicToTopic.getMainTopic().equals(this)) {
                        relatedTopic.getChildTopicToTopics().remove(childTopicToTopic);
                        break;
                    }
                }

                return true;
            }
        }

        return false;
    }

    public void removeTag(final int tagID) {
        final List<TopicToTag> mappingEntities = filter(having(on(TopicToTag.class).getTag().getTagId(), equalTo(tagID)),
                this.getTopicToTags());
        if (mappingEntities.size() != 0) {
            for (final TopicToTag mapping : mappingEntities) {
                this.topicToTags.remove(mapping);
                mapping.getTag().getTopicToTags().remove(mapping);
            }
        }
    }

    public void removeTag(final Tag tag) {
        removeTag(tag.getTagId());
    }

    public void removeTopicSourceUrl(final int id) {
        final List<TopicToTopicSourceUrl> mappingEntities = filter(
                having(on(TopicToTopicSourceUrl.class).getTopicSourceUrl().getTopicSourceUrlid(), equalTo(id)),
                this.getTopicToTopicSourceUrls());
        if (mappingEntities.size() != 0) {
            for (final TopicToTopicSourceUrl mapping : mappingEntities) {
                this.topicToTopicSourceUrls.remove(mapping);
            }
        }
    }

    public void removeBugzillaBug(final int id) {
        final List<TopicToBugzillaBug> mappingEntities = filter(
                having(on(TopicToBugzillaBug.class).getBugzillaBug().getBugzillaBugId(), equalTo(id)), this.topicToBugzillaBugs);
        if (mappingEntities.size() != 0) {
            for (final TopicToBugzillaBug mapping : mappingEntities) {
                this.topicToBugzillaBugs.remove(mapping);
                mapping.getBugzillaBug().getTopicToBugzillaBugs().remove(mapping);
            }
        }
    }

    @Transient
    public List<TopicToTopic> getParentTopicToTopicsArray() {
        final List<TopicToTopic> retValue = CollectionUtilities.toArrayList(parentTopicToTopics);
        Collections.sort(retValue, new TopicToTopicRelatedTopicIDSort());
        return retValue;
    }

    @Transient
    public List<TopicToTopic> getChildTopicToTopicsArray() {
        final List<TopicToTopic> retValue = CollectionUtilities.toArrayList(childTopicToTopics);
        Collections.sort(retValue, new TopicToTopicMainTopicIDSort());
        return retValue;
    }

    public void changeTopicToTopicRelationshipTag(final RelationshipTag relationshipTag, final Topic existingTopic,
            final RelationshipTag existingRelationshipTag) {
        for (final TopicToTopic topicToTopic : this.parentTopicToTopics) {
            if (topicToTopic.getRelatedTopic().equals(existingTopic)
                    && topicToTopic.getRelationshipTag().equals(existingRelationshipTag)) {
                topicToTopic.setRelationshipTag(relationshipTag);
                break;
            }
        }
    }
    
    @Transient
    public List<TopicToPropertyTag> getTopicToPropertyTagsArray() {
        final List<TopicToPropertyTag> topicToPropertyTags = CollectionUtilities.toArrayList(this.topicToPropertyTags);
        return topicToPropertyTags;
    }

    @Transient
    public List<PropertyTag> getPropertyTagsArray() {
        final List<PropertyTag> retValue = new ArrayList<PropertyTag>();
        for (final TopicToPropertyTag mapping : this.topicToPropertyTags) {
            final PropertyTag entity = mapping.getPropertyTag();
            retValue.add(entity);
        }

        return retValue;
    }

    @Transient
    public List<TopicSourceUrl> getTopicSourceUrls() {
        final List<TopicSourceUrl> retValue = new ArrayList<TopicSourceUrl>();
        for (final TopicToTopicSourceUrl mapping : this.topicToTopicSourceUrls) {
            final TopicSourceUrl entity = mapping.getTopicSourceUrl();
            retValue.add(entity);
        }

        return retValue;
    }

    @Transient
    public List<BugzillaBug> getBugzillaBugs() {
        final List<BugzillaBug> retValue = new ArrayList<BugzillaBug>();
        for (final TopicToBugzillaBug mapping : this.topicToBugzillaBugs) {
            final BugzillaBug entity = mapping.getBugzillaBug();
            retValue.add(entity);
        }

        return retValue;
    }

    @Override
    @Transient
    protected Set<? extends ToPropertyTag<?>> getPropertyTags() {
        return this.topicToPropertyTags;
    }

    public void removePropertyTag(final TopicToPropertyTag topicToPropertyTag) {
        this.topicToPropertyTags.remove(topicToPropertyTag);
        topicToPropertyTag.getPropertyTag().getTopicToPropertyTags().remove(topicToPropertyTag);
    }

    public void addPropertyTag(final TopicToPropertyTag topicToPropertyTag) {
        this.topicToPropertyTags.add(topicToPropertyTag);
        topicToPropertyTag.getPropertyTag().getTopicToPropertyTags().add(topicToPropertyTag);
    }

    @SuppressWarnings("unused")
    @PreRemove
    private void preRemove() {
        for (final TopicToTag mapping : topicToTags)
            mapping.getTag().getTopicToTags().remove(mapping);

        for (final TopicToTopic mapping : childTopicToTopics)
            mapping.getMainTopic().getParentTopicToTopics().remove(mapping);

        for (final TopicToTopic mapping : parentTopicToTopics)
            mapping.getRelatedTopic().getChildTopicToTopics().remove(mapping);

        for (final TopicToPropertyTag mapping : topicToPropertyTags)
            mapping.getPropertyTag().getTopicToPropertyTags().remove(mapping);

        for (final TopicToTopicSourceUrl mapping : topicToTopicSourceUrls)
            mapping.getTopicSourceUrl().getTopicToTopicSourceUrls().remove(mapping);

        for (final TopicToBugzillaBug mapping : this.topicToBugzillaBugs)
            mapping.getBugzillaBug().getTopicToBugzillaBugs().remove(mapping);

        this.topicToTags.clear();
        this.childTopicToTopics.clear();
        this.parentTopicToTopics.clear();
        this.topicToPropertyTags.clear();
        this.topicToTopicSourceUrls.clear();
        this.topicToBugzillaBugs.clear();
    }

    public void addPropertyTag(final PropertyTag propertyTag, final String value) {
        final TopicToPropertyTag mapping = new TopicToPropertyTag();
        mapping.setTopic(this);
        mapping.setPropertyTag(propertyTag);
        mapping.setValue(value);

        this.topicToPropertyTags.add(mapping);
        propertyTag.getTopicToPropertyTags().add(mapping);
    }

    public void removePropertyTag(final PropertyTag propertyTag, final String value) {
        final List<TopicToPropertyTag> removeList = new ArrayList<TopicToPropertyTag>();

        for (final TopicToPropertyTag mapping : this.topicToPropertyTags) {
            final PropertyTag myPropertyTag = mapping.getPropertyTag();
            if (myPropertyTag.equals(propertyTag) && mapping.getValue().equals(value)) {
                removeList.add(mapping);
            }
        }

        for (final TopicToPropertyTag mapping : removeList) {
            this.topicToPropertyTags.remove(mapping);
            mapping.getPropertyTag().getTopicToPropertyTags().remove(mapping);
        }
    }

    @SuppressWarnings("unchecked")
    @Transient
    public List<TranslatedTopicData> getTranslatedTopics(final EntityManager entityManager, final Number revision) {
        final List<TranslatedTopicData> translatedTopicDatas = new ArrayList<TranslatedTopicData>();

        try {
            /*
             * We have to do a query here as a @OneToMany won't work with hibernate envers since the TranslatedTopic entity is
             * audited and we need the latest results. This is because the translated topic will never exist for its matching
             * audited topic.
             */
            final String translatedTopicQuery = TranslatedTopic.SELECT_ALL_QUERY + " WHERE translatedTopic.topicId = "
                    + this.topicId + (revision == null ? "" : (" AND translatedTopic.topicRevision <= " + revision));
            final List<TranslatedTopic> translatedTopics = entityManager.createQuery(translatedTopicQuery).getResultList();

            for (final TranslatedTopic translatedTopic : translatedTopics) {
                translatedTopicDatas.addAll(translatedTopic.getTranslatedTopicDatas());
            }
        } catch (final PersistenceException ex) {
            // Increase the prepared-statement-cache-size if an exception is
            // being thrown here
            throw ex;
        }

        return translatedTopicDatas;
    }
}
