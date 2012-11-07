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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.jboss.pressgang.ccms.restserver.entity.base.AuditedEntity;
import org.jboss.pressgang.ccms.restserver.utils.Constants;


@Entity
@Audited
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "ContentSpecMetaData")
public class CSMetaData extends AuditedEntity<CSMetaData> implements Serializable {

    private static final long serialVersionUID = 5183201301752071357L;
    public static final String SELECT_ALL_QUERY = "select csMetaData FROM CSMetaData AS csMetaData";
    
    private Integer csMetaDataId = null;
    private String csMetaDataTitle = null;
    private String csMetaDataDescription = null;
    private Set<ContentSpecToCSMetaData> contentSpecToCSMetaData = new HashSet<ContentSpecToCSMetaData>(0);

    public CSMetaData() {
        super(CSMetaData.class);
    }

    @Override
    @Transient
    public Integer getId() {
        return this.csMetaDataId;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ContentSpecMetaDataID", unique = true, nullable = false)
    public Integer getCSMetaDataId() {
        return csMetaDataId;
    }

    public void setCSMetaDataId(Integer csMetaDataId) {
        this.csMetaDataId = csMetaDataId;
    }

    @Column(name = "ContentSpecMetaDataTitle", nullable = false, length = 512)
    public String getCSMetaDataTitle() {
        return csMetaDataTitle;
    }

    public void setCSMetaDataTitle(String csMetaDataTitle) {
        this.csMetaDataTitle = csMetaDataTitle;
    }
    
    @Column(name = "ContentSpecMetaDataDescription", columnDefinition = "TEXT")
    public String getCSMetaDataDescription() {
        return csMetaDataDescription;
    }

    public void setCSMetaDataDescription(String csMetaDataDescription) {
        this.csMetaDataDescription = csMetaDataDescription;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "CSMetaData", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<ContentSpecToCSMetaData> getContentSpecToCSMetaData() {
        return contentSpecToCSMetaData;
    }

    public void setContentSpecToCSMetaData(Set<ContentSpecToCSMetaData> contentSpecToCSMetaData) {
        this.contentSpecToCSMetaData = contentSpecToCSMetaData;
    }
    
    @Transient
    public List<ContentSpec> getContentSpecsList()
    {
        final List<ContentSpec> contentSpecs = new ArrayList<ContentSpec>();
        
        for (final ContentSpecToCSMetaData mapping : this.contentSpecToCSMetaData) {
            contentSpecs.add(mapping.getContentSpec());
        }
        
        return contentSpecs;
    }
}
