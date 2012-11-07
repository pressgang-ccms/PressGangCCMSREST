package org.jboss.pressgang.ccms.restserver.entity.contentspec;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
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
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.enums.RESTCSNodeRelationshipTypeV1;
import org.jboss.pressgang.ccms.restserver.entity.base.AuditedEntity;


@Entity
@Audited
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "ContentSpecNodeToContentSpecNode")
public class CSNodeToCSNode extends AuditedEntity<CSNodeToCSNode> implements Serializable {

    private static final long serialVersionUID = 1323433852480196579L;
    private Integer csNodeToCSNodeId = null;
    private CSNode mainNode = null;
    private CSNode relatedNode = null;
    private RESTCSNodeRelationshipTypeV1 relationshipType = null;

    public CSNodeToCSNode()
    {
        super(CSNodeToCSNode.class);
    }

    @Override
    @Transient
    public Integer getId() {
        return csNodeToCSNodeId;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ContentSpecNodeToContentSpecNodeID", unique = true, nullable = false)
    public Integer getCSNodeToCSNodeId() {
        return csNodeToCSNodeId;
    }

    public void setCSNodeToCSNodeId(Integer csNodeToCSNodeId) {
        this.csNodeToCSNodeId = csNodeToCSNodeId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MainNodeID", nullable = false)
    @NotNull
    public CSNode getMainNode() {
        return mainNode;
    }

    public void setMainNode(CSNode mainNode) {
        this.mainNode = mainNode;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RelatedNodeID", nullable = false)
    @NotNull
    public CSNode getRelatedNode() {
        return relatedNode;
    }

    public void setRelatedNode(CSNode relatedNode) {
        this.relatedNode = relatedNode;
    }

    @Enumerated
    @Column(name = "RelationshipType", nullable = false)
    public RESTCSNodeRelationshipTypeV1 getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(RESTCSNodeRelationshipTypeV1 relationshipType) {
        this.relationshipType = relationshipType;
    }
}
