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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.base.ToCSMetaData;
import org.jboss.pressgang.ccms.restserver.utils.Constants;


@Entity
@Audited
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "ContentSpecToContentSpecMetaData")
public class ContentSpecToCSMetaData extends ToCSMetaData<ContentSpecToCSMetaData> implements Serializable {

    private static final long serialVersionUID = -6739315876639954128L;
    private Integer contentSpecToCSMetaDataId = null;
    private ContentSpec contentSpec = null;
    private Set<CSMetaDataToCSTranslatedString> csMetaDataToCSTranslatedStrings = new HashSet<CSMetaDataToCSTranslatedString>(0);

    public ContentSpecToCSMetaData() {
        super(ContentSpecToCSMetaData.class);
    }

    @Override
    @Transient
    public Integer getId() {
        return contentSpecToCSMetaDataId;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ContentSpecToContentSpecMetaDataID", unique = true, nullable = false)
    public Integer getContentSpecToCSMetaDataId() {
        return contentSpecToCSMetaDataId;
    }

    public void setContentSpecToCSMetaDataId(Integer contentSpecToCSMetaDataId) {
        this.contentSpecToCSMetaDataId = contentSpecToCSMetaDataId;
    }

    @ManyToOne
    @JoinColumn(name = "ContentSpecID", nullable = false)
    @NotNull
    public ContentSpec getContentSpec() {
        return contentSpec;
    }

    public void setContentSpec(ContentSpec contentSpec) {
        this.contentSpec = contentSpec;
    }

    @Override
    @ManyToOne
    @JoinColumn(name = "ContentSpecMetaDataID", nullable = false)
    @NotNull
    public CSMetaData getCSMetaData() {
        return this.csMetaData;
    }

    @Override
    public void setCSMetaData(CSMetaData csMetaData) {
        this.csMetaData = csMetaData;
    }

    @Override
    @Column(name = "Value", columnDefinition = "TEXT")
    @Size(max = 65535)
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contentSpecToCSMetaData", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<CSMetaDataToCSTranslatedString> getCSMetaDataToCSTranslatedStrings() {
        return csMetaDataToCSTranslatedStrings;
    }

    public void setCSMetaDataToCSTranslatedStrings(Set<CSMetaDataToCSTranslatedString> csMetaDataToCSTranslatedStrings) {
        this.csMetaDataToCSTranslatedStrings = csMetaDataToCSTranslatedStrings;
    }

    @Transient
    public List<CSTranslatedString> getCSTranslatedStringsList()
    {
        final List<CSTranslatedString> translatedStrings = new ArrayList<CSTranslatedString>();
        
        for (final CSMetaDataToCSTranslatedString mapping : this.csMetaDataToCSTranslatedStrings) {
            translatedStrings.add(mapping.getCSTranslatedString());
        }
        
        return translatedStrings;
    }
    
    public void addTranslatedString(final CSTranslatedString translatedString) {
        final CSMetaDataToCSTranslatedString mapping = new CSMetaDataToCSTranslatedString();
        
        mapping.setContentSpecToCSMetaData(this);
        mapping.setCSTranslatedString(translatedString);
        
        this.csMetaDataToCSTranslatedStrings.add(mapping);
        translatedString.getCSMetaDataToCSTranslatedStrings().add(mapping);
    }
    
    public void removeTranslatedString(final CSTranslatedString translatedString)
    {
        final List<CSMetaDataToCSTranslatedString> removeList = new ArrayList<CSMetaDataToCSTranslatedString>();

        for (final CSMetaDataToCSTranslatedString mapping : this.csMetaDataToCSTranslatedStrings)
        {
            if (mapping.getCSTranslatedString().equals(translatedString))
            {
                removeList.add(mapping);
            }
        }

        for (final CSMetaDataToCSTranslatedString mapping : removeList)
        {
            this.csMetaDataToCSTranslatedStrings.remove(mapping);
            mapping.getCSTranslatedString().getCSMetaDataToCSTranslatedStrings().remove(mapping);
        }
    }
}
