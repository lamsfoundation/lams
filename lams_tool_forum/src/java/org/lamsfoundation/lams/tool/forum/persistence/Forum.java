package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.Set;

/**
 * Forum
 * @author conradb
 *
 * @hibernate.joined-subclass table="FORUM"
 * @hibernate.joined-subclass-key column="id"
 *
 * @hibernate.query name="allForums" query="from Forum forum"
 */
public class Forum extends GenericEntity {
	protected String title;
    protected boolean lockWhenFinished;
	protected boolean forceOffline;
	protected boolean allowAnnomity;
    protected String instructions;
    protected String onlineInstructions;
    protected String offlineInstructions;
	protected Set attachments;

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
     * cascade="none"
     * @hibernate.collection-key column="FORUM"
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

}
