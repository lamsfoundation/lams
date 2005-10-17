package org.lamsfoundation.lams.tool.forum.persistence;

import java.io.InputStream;

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
 * @hibernate.query name="getAttachmentbyType" query="from Attachment attachment where attachment.type = ?"
 */
public class Attachment {
    private Long uid;
    private Long uuid;
    private Long versionId;
    private String type;
    private String fileName;
    private InputStream inputStream;
    private String contentType;
    
    public final static String TYPE_ONLINE = "ONLINE";
    public final static String TYPE_OFFLINE = "OFFLINE";

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
     * @hibernate.property column="version_id"
     *
     */
    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long version) {
        this.versionId = version;
    }

    /**
     * @hibernate.property column="type"
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
      	.append(this.versionId,genericEntity.versionId)
      	.append(this.fileName,genericEntity.fileName)
      	.append(this.type,genericEntity.type)
      	.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(uid).append(versionId).append(fileName).append(type).toHashCode();
	}
	/**
	 * @hibernate.property column="uuid"
	 * @return
	 */
	public Long getUuid() {
		return uuid;
	}

	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}
}
