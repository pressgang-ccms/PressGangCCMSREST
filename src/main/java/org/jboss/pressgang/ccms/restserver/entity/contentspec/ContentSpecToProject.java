package org.jboss.pressgang.ccms.restserver.entity.contentspec;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import org.jboss.pressgang.ccms.restserver.entity.Project;
import org.jboss.pressgang.ccms.restserver.entity.base.AuditedEntity;


@Entity
@Audited
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "ContentSpecToProject")
public class ContentSpecToProject extends AuditedEntity<ContentSpecToProject> implements Serializable
{
    private static final long serialVersionUID = -739535710737597988L;

    private Integer contentSpecToProjectId = null;
    private ContentSpec contentSpec = null;
    private Project project = null;
    
    @Override
    @Transient
    public Integer getId()
    {
        return this.contentSpecToProjectId;
    }
    
    public ContentSpecToProject()
    {
        super(ContentSpecToProject.class);
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ContentSpecToProjectID", unique = true, nullable = false)
    public Integer getContentSpecToProjectId()
    {
        return contentSpecToProjectId;
    }

    public void setContentSpecToProjectId(Integer contentSpecToProjectId)
    {
        this.contentSpecToProjectId = contentSpecToProjectId;
    }

    @JoinColumn(name = "ContentSpecID")
    @ManyToOne
    @NotNull
    public ContentSpec getContentSpec()
    {
        return contentSpec;
    }

    public void setContentSpec(ContentSpec contentSpec)
    {
        this.contentSpec = contentSpec;
    }

    @JoinColumn(name = "ProjectID")
    @ManyToOne
    @NotNull
    public Project getProject()
    {
        return project;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

}
