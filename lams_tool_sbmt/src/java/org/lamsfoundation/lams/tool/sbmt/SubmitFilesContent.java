package org.lamsfoundation.lams.tool.sbmt;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;


/**
 * @hibernate.class table="tl_lasbmt11_content"
 */
public class SubmitFilesContent implements Serializable,Cloneable {
	private static Logger log = Logger.getLogger(SubmitFilesContent.class);
	/** identifier field */
	private Long contentID;

	/** persistent field */
	private String title;

	/** nullable persistent field */
	private String instructions;
	
	/** persistent field */
	private Set toolSession;

	/** persistent field */
	private boolean defineLater;
	/** persistent field */
	private boolean runOffline;


	/** full constructor */
	public SubmitFilesContent(String title, String instructions,
							  Set toolSession) {
		this.title = title;
		this.instructions = instructions;		
		this.toolSession = toolSession;
	}

	/** default constructor */
	public SubmitFilesContent() {
	}

	/** minimal constructor */
	public SubmitFilesContent(Long contentID, 
							  String title,
							  String instructions,
							  Set toolSession
							  ) {
		super();
		this.contentID = contentID;
		this.title = title;
		this.instructions = instructions;
		this.toolSession = toolSession;
	}
	
	public SubmitFilesContent(Long contentID, String title, String instructions){
		this.contentID = contentID;
		this.title = title;
		this.instructions = instructions;
	}

	/**
	 * Copy constructor to create a new SubmitFiles tool's content.
	 * 
	 * @param content The original tool content
	 * @param newContentID The new <code>SubmitFiles</code> contentID
	 * @return SubmitFilesContent The new SubmitFilesContent object
	 */
	public static SubmitFilesContent newInstance(SubmitFilesContent content,
			Long newContentID) {

		SubmitFilesContent newContent = new SubmitFilesContent(newContentID,
															   content.getTitle(), 
															   content.getInstructions(),
															   new TreeSet());		
		return newContent;
	}

	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long"
	 *               column="content_id"
	 */
	public Long getContentID() {
		return this.contentID;
	}

	public void setContentID(Long contentID) {
		this.contentID = contentID;
	}

	/**
	 * @hibernate.property column="title" length="64" not-null="true"
	 */
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @hibernate.property column="instructions" length="64"
	 */
	public String getInstructions() {
		return this.instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}	

	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="all-delete-orphan"
	 * @hibernate.collection-key column="content_id"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession"
	 *  
	 */
	public Set getToolSession() {
		return this.toolSession;
	}

	public void setToolSession(Set toolSession) {
		this.toolSession = toolSession;
	}

	public String toString() {
		return new ToStringBuilder(this).append("contentID", getContentID())
				.append("title", getTitle()).append("instructions",
						getInstructions()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SubmitFilesContent))
			return false;
		SubmitFilesContent castOther = (SubmitFilesContent) other;
		return new EqualsBuilder().append(this.getContentID(),
				castOther.getContentID()).append(this.getTitle(),
				castOther.getTitle()).append(this.getInstructions(),
				castOther.getInstructions()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getContentID()).append(getTitle())
				.append(getInstructions()).toHashCode();
	}


    /** 
     * @hibernate.property column="defineLater" length="1"
     *         
     */
    public boolean isDefineLater()
    {
        return this.defineLater;
    }

    public void setDefineLater(boolean defineLater)
    {
        this.defineLater = defineLater;
    }

    /** 
     * @hibernate.property column="runOffline" length="1"
     *         
     */
    public boolean isRunOffline()
    {
        return this.runOffline;
    }

    public void setRunOffline(boolean runOffline)
    {
        this.runOffline = runOffline;
    }
    
    public Object clone(){
		Object obj = null;
		try {
			obj = super.clone();
			//clone SubmitFIleSession object
			if(toolSession != null ){
				Iterator iter = toolSession.iterator();
				Set set = new TreeSet();
				while(iter.hasNext())
					set.add(((SubmitFilesSession)iter.next()).clone());
				((SubmitFilesContent)obj).toolSession = set;
			}
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + SubmissionDetails.class + " failed");
		}
		
		return obj;
	}

}
