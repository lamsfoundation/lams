package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.Date;
import java.util.HashSet;
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
 * @hibernate.query name="forumByContentId" query="from Forum forum where forum.contentId=?"
 */
public class Forum {
	private Long uid;
	//tool contentID
	private Long contentId;
	private String title;
	private boolean lockWhenFinished;
	private boolean runOffline;
	private boolean allowAnonym;
	private String instructions;
	private String onlineInstructions;
	private String offlineInstructions;
	private boolean defineLater;
	private boolean contentInUse;
	private Set messages;
	private Set attachments;
	private Date created;
	private Date updated;
	private Long createdBy;
    
  	public Forum(){
  		attachments = new HashSet();
  		messages = new HashSet();
  	}
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
     * @hibernate.id column="uid" generator-class="native"
     */
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
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
     *
     * @hibernate.set   lazy="true"
     * 					cascade="all"
     * 					inverse="false"
     * 					order-by="create_date desc"
     * @hibernate.collection-key column="forum_uid"
     * @hibernate.collection-one-to-many
     * 			class="org.lamsfoundation.lams.tool.forum.persistence.Attachment"
     *
     * @return a set of Attachments to this Message.
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
	 * 
	 * 
	 * @hibernate.set lazy="false"
	 *                cascade="all"
	 *                inverse="false"
	 *                order-by="create_date desc"
	 * @hibernate.collection-key column="forum_uid"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.forum.persistence.Message"
	 * 
	 * @return
	 */
	public Set getMessages() {
		return messages;
	}
	public void setMessages(Set messages) {
		this.messages = messages;
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

	/**
	 * @hibernate.property  column="content_in_use"
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
	/**
	 * @hibernate.property column="content_id" unique="true" 
	 * @return
	 */
	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Forum))
			return false;

		final Forum genericEntity = (Forum) o;

      	return new EqualsBuilder()
      	.append(this.uid,genericEntity.uid)
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
		return new HashCodeBuilder().append(uid).append(title)
		.append(instructions).append(onlineInstructions)
		.append(offlineInstructions).append(created)
		.append(updated).append(createdBy)
		.toHashCode();
	}
}
