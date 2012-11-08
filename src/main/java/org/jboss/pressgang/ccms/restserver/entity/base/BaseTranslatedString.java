package org.jboss.pressgang.ccms.restserver.entity.base;

import javax.persistence.Column;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

public abstract class BaseTranslatedString<T extends AuditedEntity<T>> extends AuditedEntity<T> {

    private String originalString;
    private String translatedString;
    private Boolean fuzzyTranslation = false;
    
    public BaseTranslatedString(Class<T> classType) {
        super(classType);
    }
    
    @Column(name = "OriginalString", columnDefinition = "TEXT")
    @Size(max = 65535)
    public String getOriginalString()
    {
        return originalString;
    }

    public void setOriginalString(final String originalString)
    {
        this.originalString = originalString;
    }

    @Column(name = "TranslatedString", columnDefinition = "TEXT")
    @Size(max = 65535)
    public String getTranslatedString()
    {
        return translatedString;
    }

    public void setTranslatedString(final String translatedString)
    {
        this.translatedString = translatedString;
    }

    @Column(name = "FuzzyTranslation", nullable = false, columnDefinition = "BIT", length = 1)
    @NotNull
    public Boolean getFuzzyTranslation()
    {
        return fuzzyTranslation;
    }

    public void setFuzzyTranslation(final Boolean fuzzyTranslation)
    {
        this.fuzzyTranslation = fuzzyTranslation;
    }
}
