package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.tool.forum.util.ForumToolContentHandler;

/**
 * Forum
 * @author conradb
 *
 * @hibernate.class  table="tl_lafrum11_forum"
 *
 */
public class Forum implements Cloneable{
	
	private static final Logger log = Logger.getLogger(Forum.class);
	
	//key 
	private Long uid;
	//tool contentID
	private Long contentId;
	private String title;
	private boolean lockWhenFinished;
	private boolean runOffline;
	private boolean allowAnonym;
	private boolean allowEdit;
	private boolean allowRichEditor;
	private String instructions;
	private String onlineInstructions;
	private String offlineInstructions;
	private boolean defineLater;
	private boolean contentInUse;
	private Date created;
	private Date updated;
	private ForumUser createdBy;
	
	private Set messages;
	private Set attachments;

	private ForumToolContentHandler toolContentHandler;
    
	/**
	 * Default contruction method. 
	 *
	 */
  	public Forum(){
  		attachments = new HashSet();
  		messages = new HashSet();
  	}
//  **********************************************************
  	//		Function method for Forum
//  **********************************************************
  	public Object clone(){
  		
  		Forum forum = null;
  		try{
  			forum = (Forum) super.clone();
  			forum.setUid(null);
  			//clone message
  			if(messages != null){
				Iterator iter = messages.iterator();
				Set set = new HashSet();
				while(iter.hasNext()){
					set.add(Message.newInstance((Message)iter.next(),toolContentHandler));
				}
				forum.messages = set;
  			}
  			//clone attachment
  			if(attachments != null){
  				Iterator iter = attachments.iterator();
  				Set set = new HashSet();
  				while(iter.hasNext()){
  					Attachment file = (Attachment)iter.next(); 
  					Attachment newFile = (Attachment) file.clone();
  					//if toolContentHandle is null, just clone old file without duplicate it in repository
  					if(toolContentHandler != null){
						//duplicate file node in repository
						NodeKey keys = toolContentHandler.copyFile(file.getFileUuid());
						newFile.setFileUuid(keys.getUuid());
						newFile.setFileVersionId(keys.getVersion());
  					}
					set.add(newFile);
  				}
  				forum.attachments = set;
  			}
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + Forum.class + " failed");
		} catch (ItemNotFoundException e) {
			log.error("When clone " + Forum.class + " failed");
		} catch (RepositoryCheckedException e) {
			log.error("When clone " + Forum.class + " failed");
		}
  		
  		return forum;
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
	
	//**********************************************************
	// get/set methods
	//**********************************************************
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
     * @hibernate.many-to-one
     *      cascade="none"
     * 		column="create_by"
     *
     */
    public ForumUser getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy The userid of the user who created this Forum.
     */
    public void setCreatedBy(ForumUser createdBy) {
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
     *      type="text"
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
     *      type="text"
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
     *      type="text"
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
	 * @hibernate.set lazy="true"
	 *                inverse="true"
	 *                cascade="none"
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

	/**
	 * @hibernate.property column="allow_edit"
	 * @return
	 */
	public boolean isAllowEdit() {
		return allowEdit;
	}
	public void setAllowEdit(boolean allowEdit) {
		this.allowEdit = allowEdit;
	}
	/**
	 * @hibernate.property column="allow_rich_editor"
	 * @return
	 */
	public boolean isAllowRichEditor() {
		return allowRichEditor;
	}
	public void setAllowRichEditor(boolean allowRichEditor) {
		this.allowRichEditor = allowRichEditor;
	}
	
	public static Forum newInstance(Forum fromContent, Long contentId, ForumToolContentHandler forumToolContentHandler){
		Forum toContent = new Forum();
		fromContent.toolContentHandler = forumToolContentHandler;
		toContent = (Forum) fromContent.clone();
		toContent.setContentId(contentId);
		return toContent;
	}
}
