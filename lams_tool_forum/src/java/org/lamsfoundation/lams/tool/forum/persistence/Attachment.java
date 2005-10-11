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
    private Long uuid;
    private Long version;
    private String type;
    private String name;
    private InputStream inputStream;
    private String contentType;
    
    public final static String TYPE_ONLINE = "ONLINE";
    public final static String TYPE_OFFLINE = "OFFLINE";

    /**
     * @hibernate.id column="UUID" generator-class="native"
     */
    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    /**
     * @hibernate.property column="VERSION"
     *
     */
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
     * @hibernate.property column="NAME"
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
      	.append(this.uuid,genericEntity.uuid)
      	.append(this.version,genericEntity.version)
      	.append(this.name,genericEntity.name)
      	.append(this.type,genericEntity.type)
      	.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(uuid).append(version).append(name).append(type).toHashCode();
	}
}
