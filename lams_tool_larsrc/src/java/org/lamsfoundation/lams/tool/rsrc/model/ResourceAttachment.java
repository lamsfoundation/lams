package org.lamsfoundation.lams.tool.rsrc.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * @author Dapeng Ni
 *
 * A Wrapper class for uploaded files. An Attachment cannot exist independently
 * and must belong to a Resource.
 * 
 *
 * @hibernate.class table="tl_larsrc11_attachment"
 *
 */
public class ResourceAttachment implements Cloneable{
	private static final Logger log = Logger.getLogger(ResourceAttachment.class);
	
    private Long uid;
    private Long fileUuid;
    private Long fileVersionId;
    private String fileType;
    private String fileName;
    private Date created;
    
    //Default contruction method
    public ResourceAttachment(){
    	
    }
//  **********************************************************
  	//		Function method for Attachment
//  **********************************************************
    public Object clone(){
		Object obj = null;
		try {
			obj = super.clone();
			((ResourceAttachment)obj).setUid(null);
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + ResourceAttachment.class + " failed");
		}
		
		return obj;
	}
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ResourceAttachment))
			return false;

		final ResourceAttachment genericEntity = (ResourceAttachment) o;

      	return new EqualsBuilder()
      	.append(this.uid,genericEntity.uid)
      	.append(this.fileVersionId,genericEntity.fileVersionId)
      	.append(this.fileName,genericEntity.fileName)
      	.append(this.fileType,genericEntity.fileType)
      	.append(this.created,genericEntity.created)
      	.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(uid).append(fileVersionId).append(fileName).append(fileType).append(created)
				.toHashCode();
	}
	
//  **********************************************************
  	//		get/set methods
//  **********************************************************
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
