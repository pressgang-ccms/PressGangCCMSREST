package org.jboss.pressgangccms.restserver.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Collections;
import java.util.Comparator;
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
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.jboss.pressgangccms.restserver.constants.Constants;
import org.jboss.pressgangccms.restserver.entities.base.AuditedEntity;
import org.jboss.pressgangccms.restserver.exceptions.CustomConstraintViolationException;
import org.jboss.pressgangccms.utils.common.CollectionUtilities;
import org.jboss.pressgangccms.utils.common.FileUtilities;


@Entity
@Audited
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "ImageFile")
public class ImageFile extends AuditedEntity<ImageFile> implements java.io.Serializable
{
	private static String SVG_MIME_TYPE = "image/svg+xml";
	private static String JPG_MIME_TYPE = "image/jpeg";
	private static String GIF_MIME_TYPE = "image/gif";
	private static String PNG_MIME_TYPE = "image/png";

	private static final long serialVersionUID = -3885332582642450795L;
	private Integer imageFileId;

	private String description;
	private Set<LanguageImage> languageImages = new HashSet<LanguageImage>(0);

	public ImageFile()
	{
		super(ImageFile.class);
	}

	public ImageFile(final Integer imageFileId)
	{
		super(ImageFile.class);
		this.imageFileId = imageFileId;

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ImageFileID", unique = true, nullable = false)
	public Integer getImageFileId()
	{
		return this.imageFileId;
	}

	public void setImageFileId(final Integer imageFileId)
	{
		this.imageFileId = imageFileId;
	}

	// @Column(name = "Description", length = 65535)
	@Column(name = "Description", columnDefinition = "TEXT")
	@Size(max = 65535)
	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	@Transient
	public String getDocbookFileName()
	{
		if (this.languageImages != null && this.languageImages.size() != 0 && this.imageFileId != null)
		{
			for (final LanguageImage firstChild : this.languageImages)
			{
				if (firstChild.getOriginalFileName() != null)
				{
					final int extensionIndex = firstChild.getOriginalFileName().lastIndexOf(".");
					if (extensionIndex != -1)
						return this.imageFileId + firstChild.getOriginalFileName().substring(extensionIndex);
				}
			}
		}

		return "";
	}

	@Transient
	public String getExtension()
	{
		if (this.languageImages.size() != 0)
		{
			for (final LanguageImage firstChild : this.languageImages)
			{
				if (firstChild.getOriginalFileName() != null)
				{
					return FileUtilities.getFileExtension(firstChild.getOriginalFileName());
				}
			}
		}

		return null;
	}

	@Transient
	public String getMimeType()
	{
		final String extension = getExtension();

		if (extension != null)
		{
			if (extension.equalsIgnoreCase("JPG"))
				return JPG_MIME_TYPE;
			if (extension.equalsIgnoreCase("GIF"))
				return GIF_MIME_TYPE;
			if (extension.equalsIgnoreCase("PNG"))
				return PNG_MIME_TYPE;
			if (extension.equalsIgnoreCase("SVG"))
				return SVG_MIME_TYPE;
		}

		return "application/octet-stream";
	}

	@Override
	@Transient
	public Integer getId()
	{
		return this.imageFileId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "imageFile", cascade = CascadeType.ALL, orphanRemoval = true)
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	@BatchSize(size = Constants.DEFAULT_BATCH_SIZE)
	public Set<LanguageImage> getLanguageImages()
	{
		return languageImages;
	}

	@Transient
	public List<LanguageImage> getLanguageImagesArray()
	{
		final List<LanguageImage> retValue = CollectionUtilities.toArrayList(languageImages);
		Collections.sort(retValue, new Comparator<LanguageImage>()
		{
			@Override
			public int compare(final LanguageImage o1, final LanguageImage o2)
			{
				if (o1.getLocale() == null && o2.getLocale() == null)
					return 0;
				if (o1.getLocale() == null)
					return -1;
				if (o2.getLocale() == null)
					return 1;
				return o1.getLocale().compareTo(o2.getLocale());
			}
		});

		return retValue;
	}

	public void setLanguageImages(Set<LanguageImage> languageImages)
	{
		this.languageImages = languageImages;
	}

	/**
	 * An image file can be represented by multiple translated images. These images can all be from originally differently names files, and can have different
	 * dimensions. However, all translated images need to be of the same file file. The getDocbookFileName() method needs to be able to append a fixed extension
	 * to the file name when the image is included in a Docbook XML file.
	 * 
	 * This method will throw an exception if there are inconsistent file extensions on any of the original file names assigned to the translated images.
	 * @throws CustomConstraintViolationException 
	 */
	public void validate() throws CustomConstraintViolationException
	{
		final String extension = this.getExtension();
		if (extension != null)
		{
			for (final LanguageImage langImg : this.languageImages)
			{
				if (langImg.getOriginalFileName() != null && !langImg.getOriginalFileName().isEmpty())
				{
					final String thisExtension = FileUtilities.getFileExtension(langImg.getOriginalFileName());
					if (!thisExtension.toLowerCase().equals(extension.toLowerCase()))
						throw new CustomConstraintViolationException("All LanguageImages contained by an ImageFile need to have the same file extension");
				}
			}
		}
	}
}
