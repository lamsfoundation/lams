package org.lamsfoundation.lams.tool.forum.persistence;

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
	//tool contentID
	private Long uuid;
	private String title;
	private boolean lockWhenFinished;
	private boolean runOffline;
	private boolean allowAnonym;
	private String instructions;
	private String onlineInstructions;
	private String offlineInstructions;
	private boolean defineLater;
	private boolean contentInUse;
	private Set attachments;
	protected Date created;
	protected Date updated;
  	protected Long createdBy;
    
	/**
	 * Returns the object's creation date
	 *
	 * @return date
	 * @hibernate.property column="create_date"
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
	 * @hibernate.property column="update_date"
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
     * 		column="create_by"
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
     * @hibernate.id column="uuid" generator-class="native"
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
	 * 		column="title"
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
	 * @return Returns the allowAnonym.
	 *
	 * @hibernate.property 
	 * 		column="allow_anonym"
	 *  
	 */
	public boolean getAllowAnonym() {
		return allowAnonym;
	}
	
	/**
	 * @param allowAnonym The allowAnonym to set.
	 *
	 */
	public void setAllowAnonym(boolean allowAnnomity) {
		this.allowAnonym = allowAnnomity;
	}

	/**
	 * @return Returns the runOffline.
	 *
	 * @hibernate.property 
	 * 		column="run_offline"
	 *
	 */
	public boolean getRunOffline() {
		return runOffline;
	}
    
	/**
	 * @param runOffline The forceOffLine to set.
	 *
	 *
	 */
	public void setRunOffline(boolean forceOffline) {
		this.runOffline = forceOffline;
	}

    /**
     * @return Returns the lockWhenFinish.
     *
     * @hibernate.property
     * 		column="lock_on_finished"
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
     * 		column="instructions"
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
     * 		column="online_instructions"
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
     * 		column="offline_instructions"
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
      	.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(uuid).append(title)
		.append(instructions).append(onlineInstructions)
		.append(offlineInstructions).append(created)
		.append(updated).append(createdBy)
		.toHashCode();
	}
	/**
	 * @hibernate.property column="content_in_use"
	 * @return
	 */
	public boolean isContentInUse() {
		return contentInUse;
	}

	public void setContentInUse(boolean contentInUse) {
		this.contentInUse = contentInUse;
	}
	/**
	 * @hibernate.property column="define_later"
	 * @return
	 */
	public boolean isDefineLater() {
		return defineLater;
	}

	public void setDefineLater(boolean defineLater) {
		this.defineLater = defineLater;
	}

}
