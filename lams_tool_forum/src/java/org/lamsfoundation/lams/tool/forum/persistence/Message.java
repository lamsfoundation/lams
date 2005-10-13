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
 * @hibernate.query name="allMessages" query="from Message message"
 * @hibernate.query name="allAuthoredMessagesOfForum" query="from Message message where message.forum.id = ? AND message.isAuthored = true"
 * @hibernate.query name="allMessagesByForum" query="from Message message where message.forum = ?"
 */
public class Message {
	private Long uuid;
	private String subject;
	private String body;
	private boolean isAuthored;
	private boolean isAnonymous;
	private Message parent;
	private Set replies;
	private Forum forum;
	
	protected Date created;
	protected Date updated;
  	protected Long createdBy;
    protected Long modifiedBy;
    
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
     * @hibernate.id column="uuid" generator-class="native"
     */
	public Long getUuid() {
		return uuid;
	}

	public void setUuid(Long uuid) {
		this.uuid = uuid;
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
	public void setIsAnnonymous(boolean isAnnonymous) {
		this.isAnonymous = isAnnonymous;
	}

	/**
     * @return a set of relpies to this Message.
     *
     * @hibernate.set table="MESSAGE"
     * inverse="false"
     * lazy="false"
     * cascade="all-delete-orphan"
     * @hibernate.collection-key column="parent"
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
      * Gets the forum
      *
      * @hibernate.many-to-one
      *	class="org.lamsfoundation.lams.tool.forum.persistence.Forum"
      *	              column="FORUM"
      *
      */
     public Forum getForum() {
         return forum;
     }


	/**
	 * @param forum The forum that this Message belongs to
     */
    public void setForum(Forum forum) {
         this.forum = forum;
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
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Message))
			return false;
		
		final Message genericEntity = (Message) o;

      	return new EqualsBuilder()
      	.append(this.uuid,genericEntity.uuid)
      	.append(this.subject,genericEntity.subject)
      	.append(this.body,genericEntity.body)
      	.append(this.created,genericEntity.created)
      	.append(this.updated,genericEntity.updated)
      	.append(this.createdBy,genericEntity.createdBy)
      	.append(this.modifiedBy,genericEntity.modifiedBy)
      	.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(uuid)
		.append(subject).append(body)
		.append(created)
		.append(updated).append(createdBy)
		.append(modifiedBy)
		.toHashCode();
	}
}
