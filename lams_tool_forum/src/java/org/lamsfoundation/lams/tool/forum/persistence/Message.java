package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.Set;
import java.util.Date;

/**
 * @author conradb
 *
 * @hibernate.joined-subclass table="tl_lafrum11_message"
 * @hibernate.joined-subclass-key column="id"
 *
 * @hibernate.query name="allMessages" query="from Message message"
 * @hibernate.query name="allAuthoredMessagesOfForum" query="from Message message where message.forum.id = ? AND message.isAuthored = true"
 * @hibernate.query name="allMessagesByForum" query="from Message message where message.forum = ?"
 */
public class Message extends GenericEntity {
	protected String subject;
	protected String body;
    protected boolean isAuthored;
	protected boolean isAnnonymous;
    protected Message parent;
    protected Set replies;
    protected Forum forum;

    /**
     * @return Returns the subject of the Message.
     *
     * @hibernate.property
     * 		column="SUBJECT"
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
	 * 		column="BODY"
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
	 * 		column="ISAUTHORED"
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
	 * 		column="ISANNONYMOUS"
	 *  	 
	 */
	public boolean getIsAnnonymous() {
		return isAnnonymous;
	}
	
	/**
	 * @param isAnnonymous Indicates that the Message is to be shown
	 * 			as an Annonymous message when set to true.
	 */
	public void setIsAnnonymous(boolean isAnnonymous) {
		this.isAnnonymous = isAnnonymous;
	}

	/**
     * @return a set of relpies to this Message.
     *
     * @hibernate.set table="MESSAGE"
     * inverse="false"
     * lazy="false"
     * cascade="all"
     * @hibernate.collection-key column="PARENT"
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

}
