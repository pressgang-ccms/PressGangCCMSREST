package org.jboss.pressgang.ccms.restserver.entity.contentspec;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
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
import org.jboss.pressgang.ccms.restserver.entity.base.BaseTranslatedString;
import org.jboss.pressgang.ccms.restserver.utils.Constants;

@Entity
@Audited
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "ContentSpecTranslatedString")
public class CSTranslatedString extends BaseTranslatedString<CSTranslatedString> implements java.io.Serializable
{
    private static final long serialVersionUID = 5185674451816385008L;
    private Integer contentspecTranslatedStringID;
    private String locale;
    private Set<CSNodeToCSTranslatedString> csNodeToCSTranslatedStrings = new HashSet<CSNodeToCSTranslatedString>(0);
    private Set<CSMetaDataToCSTranslatedString> csMetaDataToCSTranslatedStrings = new HashSet<CSMetaDataToCSTranslatedString>(0);
    
    public CSTranslatedString()
    {
        super(CSTranslatedString.class);
    }
    
    @Transient
    public Integer getId()
    {
        return this.contentspecTranslatedStringID;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ContentSpecTranslatedStringID", unique = true, nullable = false)
    public Integer getCSTranslatedStringID()
    {
        return contentspecTranslatedStringID;
    }

    public void setCSTranslatedStringID(final Integer translatedTopicStringID)
    {
        this.contentspecTranslatedStringID = translatedTopicStringID;
    }

    @Column(name = "Locale", nullable = false)
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "CSTranslatedString", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<CSNodeToCSTranslatedString> getCSNodeToCSTranslatedStrings()
    {
        return csNodeToCSTranslatedStrings;
    }

    public void setCSNodeToCSTranslatedStrings(final Set<CSNodeToCSTranslatedString> csNodeToCSTranslatedStrings)
    {
        this.csNodeToCSTranslatedStrings = csNodeToCSTranslatedStrings;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "CSTranslatedString", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
    public Set<CSMetaDataToCSTranslatedString> getCSMetaDataToCSTranslatedStrings()
    {
        return csMetaDataToCSTranslatedStrings;
    }

    public void setCSMetaDataToCSTranslatedStrings(final Set<CSMetaDataToCSTranslatedString> csMetaDataToCSTranslatedStrings)
    {
        this.csMetaDataToCSTranslatedStrings = csMetaDataToCSTranslatedStrings;
    }
}