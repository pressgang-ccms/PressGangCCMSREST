package org.jboss.pressgangccms.restserver.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.swing.ImageIcon;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import com.redhat.topicindex.entity.base.AuditedEntity;
import com.redhat.topicindex.exceptions.CustomConstraintViolationException;
import com.redhat.topicindex.utils.SkynetExceptionUtilities;
import com.redhat.topicindex.utils.SVGIcon;

@Entity
@Audited
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "LanguageImage", uniqueConstraints = @UniqueConstraint(columnNames = { "ImageFileID", "Locale" }))
public class LanguageImage extends AuditedEntity<LanguageImage> implements java.io.Serializable
{
	private static final long serialVersionUID = 1585978752264763594L;
	private static String SVG_MIME_TYPE = "image/svg+xml";
	private static String JPG_MIME_TYPE = "image/jpeg";
	private static String GIF_MIME_TYPE = "image/gif";
	private static String PNG_MIME_TYPE = "image/png";

	/** The dimensions of the generated thumbnail */
	private static final int THUMBNAIL_SIZE = 64;
	private Integer languageImageId;
	private ImageFile imageFile;
	private byte[] imageData;
	private byte[] thumbnail;
	private byte[] imageDataBase64;
	private String locale;
	private String originalFileName;

	private byte[] uiImageData;
	private String uiOriginalFileName;

	public LanguageImage()
	{
		super(LanguageImage.class);
	}

	public LanguageImage(final Integer imageFileId, final String originalFileName, final byte[] imageData)
	{
		super(LanguageImage.class);
		this.imageData = imageData;
		this.originalFileName = originalFileName;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "LanguageImageID", unique = true, nullable = false)
	public Integer getLanguageImageId()
	{
		return this.languageImageId;
	}

	public void setLanguageImageId(final Integer languageImageId)
	{
		this.languageImageId = languageImageId;
	}

	@Column(name = "OriginalFileName", length = 2048)
	@Length(max = 512)
	public String getOriginalFileName()
	{
		return this.originalFileName;
	}

	public void setOriginalFileName(final String originalFileName)
	{
		this.originalFileName = originalFileName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ImageFileID", nullable = false)
	@NotNull
	public ImageFile getImageFile()
	{
		return imageFile;
	}

	public void setImageFile(ImageFile imageFile)
	{
		this.imageFile = imageFile;
	}

	@Column(name = "ImageDataBase64")
	public byte[] getImageDataBase64()
	{
		return this.imageDataBase64;
	}

	public void setImageDataBase64(final byte[] imageDataBase64)
	{
		this.imageDataBase64 = imageDataBase64;
	}

	@Transient
	public String getImageDataBase64String()
	{
		return this.imageDataBase64 == null ? "" : new String(this.imageDataBase64);
	}

	@Column(name = "ImageData")
	public byte[] getImageData()
	{
		return this.imageData;
	}

	public void setImageData(final byte[] imageData)
	{
		this.imageData = imageData;
	}

	@Column(name = "ThumbnailData")
	public byte[] getThumbnailData()
	{
		return this.thumbnail;
	}

	public void setThumbnailData(final byte[] thumbnail)
	{
		this.thumbnail = thumbnail;
	}

	@Transient
	public String getThumbnailDataString()
	{
		return this.thumbnail == null ? "" : new String(this.thumbnail);
	}

	private byte[] createImage(final boolean resize)
	{
		if (this.imageData == null)
			return null;

		try
		{
			BufferedImage outImage = null;

			if (getMimeType().equals(SVG_MIME_TYPE))
			{
				SVGIcon svgIcon = null;
				if (resize)
					svgIcon = new SVGIcon(new ByteArrayInputStream(this.imageData), THUMBNAIL_SIZE, THUMBNAIL_SIZE);
				else
					svgIcon = new SVGIcon(new ByteArrayInputStream(this.imageData));

				outImage = new BufferedImage(svgIcon.getIconWidth(), svgIcon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
				final Graphics2D g2d = outImage.createGraphics();

				svgIcon.setBackgroundColour(Color.WHITE);
				svgIcon.paintIcon(null, g2d, 0, 0);
				g2d.dispose();
			}
			else
			{
				final ImageIcon imageIcon = new ImageIcon(this.imageData);
				final Image inImage = imageIcon.getImage();

				double scale = 1.0d;
				if (resize)
				{
					/*
					 * the final image will be at most THUMBNAIL_SIZE pixels high and/or wide
					 */
					final double heightScale = (double) THUMBNAIL_SIZE / (double) inImage.getHeight(null);
					final double widthScale = (double) THUMBNAIL_SIZE / (double) inImage.getWidth(null);
					scale = Math.min(heightScale, widthScale);
				}

				final int newWidth = (int) (imageIcon.getIconWidth() * scale);
				final int newHeight = (int) (imageIcon.getIconHeight() * scale);
				outImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
				final Graphics2D g2d = outImage.createGraphics();

				final AffineTransform tx = new AffineTransform();

				if (scale < 1.0d)
					tx.scale(scale, scale);

				g2d.drawImage(inImage, tx, null);
				g2d.dispose();
			}

			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(outImage, "JPG", baos);
			final byte[] bytesOut = baos.toByteArray();

			return Base64.encodeBase64(bytesOut);

		}
		catch (final Exception ex)
		{
			SkynetExceptionUtilities.handleException(ex, false, "An error creating an image thumbnail");
		}

		return null;
	}

	@Transient
	public String getImageDataString()
	{
		if (imageData == null)
			return "";
		return new String(imageData);
	}

	@Override
	@Transient
	public Integer getId()
	{
		return this.languageImageId;
	}

	/**
	 * Create the thumbnails, and make sure the parent imagefile is valid.
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	@PrePersist
	@PreUpdate
	private void updateImageData() throws CustomConstraintViolationException
	{
		this.thumbnail = createImage(true);
		this.imageDataBase64 = createImage(false);
		
		this.imageFile.validate();
	}

	@Column(name = "Locale", nullable = false)
	@NotNull
	public String getLocale()
	{
		return locale;
	}

	public void setLocale(String locale)
	{
		this.locale = locale;
	}

	@Transient
	public String getMimeType()
	{
		final int lastPeriodIndex = this.originalFileName.lastIndexOf(".");
		if (lastPeriodIndex != -1 && lastPeriodIndex < this.originalFileName.length() - 1)
		{
			final String extension = this.originalFileName.substring(lastPeriodIndex + 1);
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

	@Transient
	public byte[] getUiImageData()
	{
		return uiImageData;
	}

	/**
	 * The UI will attempt to assign a null value if the file upload box does not have a file selected. This method is used to ignore any null values, while
	 * passing through any legitimate file uploads. This means that the absence of a file in the upload box does not indicate that no file should be assigned to
	 * the entity.
	 * 
	 * @param uiImageData
	 *            The image data uploaded through the UI
	 */
	public void setUiImageData(byte[] uiImageData)
	{
		this.uiImageData = uiImageData;
		if (this.uiImageData != null)
			this.imageData = this.uiImageData;
	}

	@Transient
	public String getUiOriginalFileName()
	{
		return uiOriginalFileName;
	}

	/**
	 * The UI will attempt to assign an empty value if the file upload box does not have a file selected. This method is used to ignore any empty values, while
	 * passing through any legitimate file uploads. This means that the absence of a file in the upload box does not indicate that no file should be assigned to
	 * the entity.
	 * 
	 * @param uiImageData
	 *            The image file name uploaded through the UI
	 */
	public void setUiOriginalFileName(final String uiOriginalFileName)
	{
		this.uiOriginalFileName = uiOriginalFileName;
		if (this.uiOriginalFileName != null && !this.uiOriginalFileName.isEmpty())
			this.originalFileName = this.uiOriginalFileName;
	}
}
