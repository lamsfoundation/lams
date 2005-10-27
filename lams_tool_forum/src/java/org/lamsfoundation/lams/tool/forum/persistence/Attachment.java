package org.lamsfoundation.lams.tool.forum.persistence;

import java.io.InputStream;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author conradb
 *
 * A Wrapper class for uploaded files. An Attachment cannot exist independently
 * and must belong to a Forum.
 * 
 *
 * @hibernate.class table="tl_lafrum11_attachment"
 *
 * @hibernate.query name="allAttachments" query="from Attachment attachment"
 * @hibernate.query name="getAttachmentbyType" query="from Attachment attachment where file_type = ?"
 */
public class Attachment {
    private Long uid;
    private Long fileUuid;
    private Long fileVersionId;
    private String fileType;
    private String fileName;
    private Date created;
    
    //non-persist fields
    private InputStream inputStream;
    private String contentType;

    /**
     * @hibernate.id column="uid" generator-class="native"
     */
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * @hibernate.property column="file_version_id"
     *
     */
    public Long getFileVersionId() {
        return fileVersionId;
    }

    public void setFileVersionId(Long version) {
        this.fileVersionId = version;
    }

    /**
     * @hibernate.property column="file_type"
     */
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String type) {
        this.fileType = type;
    }

    /**
     * @hibernate.property column="file_name"
     */
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String name) {
        this.fileName = name;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Attachment))
			return false;

		final Attachment genericEntity = (Attachment) o;

      	return new EqualsBuilder()
      	.append(this.uid,genericEntity.uid)
      	.append(this.fileVersionId,genericEntity.fileVersionId)
      	.append(this.fileName,genericEntity.fileName)
      	.append(this.fileType,genericEntity.fileType)
      	.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(uid).append(fileVersionId).append(fileName).append(fileType).toHashCode();
	}
	/**
	 * @hibernate.property column="file_uuid"
	 * @return
	 */
	public Long getFileUuid() {
		return fileUuid;
	}

	public void setFileUuid(Long uuid) {
		this.fileUuid = uuid;
	}
	/**
	 * @hibernate.property column="create_date"
	 * @return
	 */
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
