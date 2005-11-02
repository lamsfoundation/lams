package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author conradb
 *
 * @hibernate.class table="tl_lafrum11_message"
 *
 * @hibernate.query name="allMessages" query="from Message m"
 * @hibernate.query name="allMessagesByForum" query="from Message m where forum_uid=? order by create_date"
 * @hibernate.query name="allAuthoredMessagesOfForum" query="from Message m where is_authored=true and forum_uid=? order by create_date"
 */
public class Message {
	private Long uid;
	private String subject;
	private String body;
	private boolean isAuthored;
	private boolean isAnonymous;

	private ToolSession toolSession;
	private Message parent;
	private ForumUser learner;
	private Forum forum;
	private Set replies;
	private Set attachments;
	
	private Date created;
	private Date updated;
	private Long createdBy;
	private Long modifiedBy;

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
	 * Updates the modification data for this entity.
	 */
	public void updateModificationData() {
		long now = System.currentTimeMillis();
		if (created == null) {
			this.setCreated(new Date(now));
		}
		this.setUpdated(new Date(now));
	}

    /**
     * @return Returns the userid of the user who modified the posting.
     *
     *
     * @hibernate.property
     * 		column="modified_by"
     *
     */
    public Long getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy The userid of the user who modified the posting.
     */
    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * @hibernate.id column="uid" generator-class="native"
     */
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uuid) {
		this.uid = uuid;
	}

    /**
     * @return Returns the subject of the Message.
     *
     * @hibernate.property
     * 		column="subject"
     *
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject The subject of the Message to be set.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

	/**
	 * @return Returns the body of the Message.
	 *
	 * @hibernate.property 
	 * 		column="body"
     *      type="text"
	 *  	 
	 */
	public String getBody() {
		return body;
	}
	
	/**
	 * @param body The body of the Message to set.
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return Returns true if the Message was an Authored Message.
	 *
	 * @hibernate.property
	 * 		column="is_authored"
	 *
	 */
	public boolean getIsAuthored() {
		return isAuthored;
	}

	/**
	 * @param isAuthored Set isAuthored to true if Message was authored
	 * 		otherwise set to false.
	 */
	public void setIsAuthored(boolean isAuthored) {
		this.isAuthored = isAuthored;
	}

	/**
	 * @return Returns whether the Message should be shown as an
	 * 		Annonymous message.
	 *
	 * @hibernate.property 
	 * 		column="is_anonymous"
	 *  	 
	 */
	public boolean getIsAnonymous() {
		return isAnonymous;
	}
	
	/**
	 * @param isAnonymous Indicates that the Message is to be shown
	 * 			as an Annonymous message when set to true.
	 */
	public void setIsAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	/**
     * @return a set of relpies to this Message.
     *
     * @hibernate.set table="MESSAGE"
     * inverse="false"
     * lazy="false"
     * cascade="all-delete-orphan"
     * @hibernate.collection-key column="parent_uid"
     * @hibernate.collection-one-to-many 
     * 			class="org.lamsfoundation.lams.tool.forum.persistence.Message"
     *
     */	 
	public Set getReplies() {
		return replies;
	}
	
	/**
	 * @param replies The reply Messages that is associated with this Message.
	 */
	public void setReplies(Set replies) {
		this.replies = replies;
	}

    /**
      * Gets the toolSession
      *
      * @hibernate.many-to-one
      *	class="org.lamsfoundation.lams.tool.forum.persistence.ToolSession"
      *	              column="forum_session_uid"
      *
      */
     public ToolSession getToolSession() {
         return toolSession;
     }


	/**
	 * @param toolSession The toolSession that this Message belongs to
     */
    public void setToolSession(ToolSession forum) {
         this.toolSession = forum;
     }

	/**
	 * @param parent The parent of this Message
     */
    public void setParent(Message parent) {
        this.parent = parent;
    }

    public Message getParent() {
        return parent;
    }
	/**
     * @return a set of Attachments to this Message.
     *
     * @hibernate.set table="ATTACHMENT"
     * inverse="false"
     * lazy="false"
     * cascade="all"
     * @hibernate.collection-key column="message_uid"
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
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Message))
			return false;
		
		final Message genericEntity = (Message) o;

      	return new EqualsBuilder()
      	.append(this.uid,genericEntity.uid)
      	.append(this.subject,genericEntity.subject)
      	.append(this.body,genericEntity.body)
      	.append(this.created,genericEntity.created)
      	.append(this.updated,genericEntity.updated)
      	.append(this.createdBy,genericEntity.createdBy)
      	.append(this.modifiedBy,genericEntity.modifiedBy)
      	.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(uid)
		.append(subject).append(body)
		.append(created)
		.append(updated).append(createdBy)
		.append(modifiedBy)
		.toHashCode();
	}
	/**
	  * @hibernate.many-to-one
      *	class="org.lamsfoundation.lams.tool.forum.persistence.ForumUser"
      *	              column="user_uid"
      *
	 * @return
	 */
	public ForumUser getLearner() {
		return learner;
	}

	public void setLearner(ForumUser learner) {
		this.learner = learner;
	}
	/**
	 * @hibernate.many-to-one column="forum_uid"
	 * 			  cascade="none"
	 * @return
	 */
	public Forum getForum() {
		return forum;
	}

	public void setForum(Forum forum) {
		this.forum = forum;
	}
}
