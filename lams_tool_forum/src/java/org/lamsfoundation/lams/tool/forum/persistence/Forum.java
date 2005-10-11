package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Forum
 * @author conradb
 *
 * @hibernate.class  table="tl_lafrum11_forum"
 *
 * @hibernate.query name="allForums" query="from Forum forum"
 */
public class Forum {
	
	private Long uuid;
	private String title;
	private boolean lockWhenFinished;
	private boolean forceOffline;
	private boolean allowAnnomity;
	private String instructions;
	private String onlineInstructions;
	private String offlineInstructions;
	private Set attachments;
	protected Date created;
	protected Date updated;
  	protected Long createdBy;
    protected Long modifiedBy;
    
	/**
	 * Returns the object's creation date
	 *
	 * @return date
	 * @hibernate.property column="CREATED"
	 */
	public Date getCreated() {
      return created;
	}

	/**
	 * Sets the object's creation date
	 *
	 * @param created
	 */
	public void setCreated(Date created) {
	    this.created = created;
	}

	/**
	 * Returns the object's date of last update
	 *
	 * @return date updated
	 * @hibernate.property column="UPDATED"
	 */
	public Date getUpdated() {
        return updated;
	}

	/**
	 * Sets the object's date of last update
	 *
	 * @param updated
	 */
	public void setUpdated(Date updated) {
        this.updated = updated;
	}

    /**
     * @return Returns the userid of the user who created the Forum.
     *
     * @hibernate.property
     * 		column="CREATEDBY"
     *
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy The userid of the user who created this Forum.
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

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
	 * @return Returns the title.
	 *
	 * @hibernate.property
	 * 		column="TITLE"
	 *
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the allowAnnomity.
	 *
	 * @hibernate.property 
	 * 		column="ALLOWANNOMITY"
	 *  
	 */
	public boolean getAllowAnnomity() {
		return allowAnnomity;
	}
	
	/**
	 * @param allowAnnomity The allowAnnomity to set.
	 *
	 */
	public void setAllowAnnomity(boolean allowAnnomity) {
		this.allowAnnomity = allowAnnomity;
	}

	/**
	 * @return Returns the forceOffline.
	 *
	 * @hibernate.property 
	 * 		column="FORCEOFFLINE"
	 *
	 */
	public boolean getForceOffline() {
		return forceOffline;
	}
    
	/**
	 * @param forceOffline The forceOffLine to set.
	 *
	 *
	 */
	public void setForceOffline(boolean forceOffline) {
		this.forceOffline = forceOffline;
	}

    /**
     * @return Returns the lockWhenFinish.
     *
     * @hibernate.property
     * 		column="LOCKWHENFINISHED"
     *
     */
    public boolean getLockWhenFinished() {
        return lockWhenFinished;
    }

    /**
     * @param lockWhenFinished Set to true to lock the forum for finished users.
     */
    public void setLockWhenFinished(boolean lockWhenFinished) {
        this.lockWhenFinished = lockWhenFinished;
    }

    /**
     * @return Returns the instructions set by the teacher.
     *
     * @hibernate.property
     * 		column="INSTRUCTIONS"
     *      //type="text"
     */
    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * @return Returns the onlineInstructions set by the teacher.
     *
     * @hibernate.property
     * 		column="ONLINEINSTRUCTIONS"
     *      //type="text"
     */
    public String getOnlineInstructions() {
        return onlineInstructions;
    }

    public void setOnlineInstructions(String onlineInstructions) {
        this.onlineInstructions = onlineInstructions;
    }

    /**
     * @return Returns the onlineInstructions set by the teacher.
     *
     * @hibernate.property
     * 		column="OFFLINEINSTRUCTIONS"
     *      //type="text"
     */
    public String getOfflineInstructions() {
        return offlineInstructions;
    }

    public void setOfflineInstructions(String offlineInstructions) {
        this.offlineInstructions = offlineInstructions;
    }

	/**
     * @return a set of Attachments to this Message.
     *
     * @hibernate.set table="ATTACHMENT"
     * inverse="false"
     * lazy="false"
     * cascade="all"
     * @hibernate.collection-key column="forum"
     * @hibernate.collection-one-to-many
     * 			class="org.lamsfoundation.lams.tool.forum.persistence.Attachment"
     *
     */
	public Set getAttachments() {
		return attachments;
	}

    /*
	 * @param attachments The attachments to set.
     */
    public void setAttachments(Set attachments) {
		this.attachments = attachments;
	}
	/**
	 * Updates the modification data for this entity.
	 */
	public void updateModificationData() {
	
		long now = System.currentTimeMillis();
		if (created == null) {
			this.setCreated (new Date(now));
		}
		this.setUpdated(new Date(now));
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Forum))
			return false;

		final Forum genericEntity = (Forum) o;

      	return new EqualsBuilder()
      	.append(this.uuid,genericEntity.uuid)
      	.append(this.title,genericEntity.title)
      	.append(this.instructions,genericEntity.instructions)
      	.append(this.onlineInstructions,genericEntity.onlineInstructions)
      	.append(this.offlineInstructions,genericEntity.offlineInstructions)
      	.append(this.created,genericEntity.created)
      	.append(this.updated,genericEntity.updated)
      	.append(this.createdBy,genericEntity.createdBy)
      	.append(this.modifiedBy,genericEntity.modifiedBy)
      	.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(uuid).append(title)
		.append(instructions).append(onlineInstructions)
		.append(offlineInstructions).append(created)
		.append(updated).append(createdBy)
		.append(modifiedBy)
		.toHashCode();
	}
}
