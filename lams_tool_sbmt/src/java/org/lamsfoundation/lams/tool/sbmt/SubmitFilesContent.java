/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	
package org.lamsfoundation.lams.tool.sbmt;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;


/**
 * @hibernate.class table="tl_lasbmt11_content"
 * @serial 9072799761861936838L
 */
public class SubmitFilesContent implements Serializable,Cloneable {

	private static final long serialVersionUID = 9072799761861936838L;

	private static Logger log = Logger.getLogger(SubmitFilesContent.class);
	/** identifier field */
	private Long contentID;

	/** persistent field */
	private String title;

	/** nullable persistent field */
	private String instruction;
	
	/** persistent field */
	private Set toolSession;

	/** persistent field */
	private boolean defineLater;
	/** persistent field */
	private boolean runOffline;

	/** persistent field */
	private String runOfflineInstruction;
	
	/** persistent field */
	private boolean contentInUse;
	
	/** persistent field */
	private String offlineInstruction;

	/** persistent field */
	private String onlineInstruction;

	private Set instructionFiles;
	
	/** persistent field */
	private boolean lockOnFinished;

	private IToolContentHandler toolContentHandler;
	
	/** full constructor */
	public SubmitFilesContent(String title, String instructions,
							  Set toolSession) {
		this.title = title;
		this.instruction = instructions;		
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
		this.instruction = instructions;
		this.toolSession = toolSession;
	}
	
	public SubmitFilesContent(Long contentID, String title, String instructions){
		this.contentID = contentID;
		this.title = title;
		this.instruction = instructions;
	}

	/**
	 * Copy constructor to create a new SubmitFiles tool's content.
	 * 
	 * @param content The original tool content
	 * @param newContentID The new <code>SubmitFiles</code> contentID
	 * @param toolContentHandler 
	 * @return SubmitFilesContent The new SubmitFilesContent object
	 */
	public static SubmitFilesContent newInstance(SubmitFilesContent content,
			Long newContentID, IToolContentHandler toolContentHandler) {
		content.toolContentHandler  = toolContentHandler;
		SubmitFilesContent newContent = (SubmitFilesContent) content.clone();
		newContent.setContentID(newContentID);
		return newContent;
	}

	/**
	 * @hibernate.id generator-class="assigned" 
	 * 				 type="java.lang.Long"
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
	 * @hibernate.property column="instruction" type="text"
	 */
	public String getInstruction() {
		return this.instruction;
	}

	public void setInstruction(String instructions) {
		this.instruction = instructions;
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
						getInstruction()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SubmitFilesContent))
			return false;
		SubmitFilesContent castOther = (SubmitFilesContent) other;
		return new EqualsBuilder().append(this.getContentID(),
				castOther.getContentID()).append(this.getTitle(),
				castOther.getTitle()).append(this.getInstruction(),
				castOther.getInstruction()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getContentID()).append(getTitle())
				.append(getInstruction()).toHashCode();
	}


    /** 
     * @hibernate.property column="define_later" length="1"
     *  not-null="true"
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
     * @hibernate.property column="run_offline" length="1"
     * not-null="true"
     */
    public boolean isRunOffline()
    {
        return this.runOffline;
    }

    public void setRunOffline(boolean runOffline)
    {
        this.runOffline = runOffline;
    }

	/**
	 * @hibernate.property column="offline_instruction" type="text"
	 * @return Returns the offlineInstruction.
	 */
	public String getOfflineInstruction() {
		return offlineInstruction;
	}

	/**
	 * @param offlineInstruction The offlineInstruction to set.
	 */
	public void setOfflineInstruction(String offlineInstruction) {
		this.offlineInstruction = offlineInstruction;
	}

	/**
	 * @hibernate.property column="online_instruction" type="text"
	 * @return Returns the onlineInstruction.
	 */
	public String getOnlineInstruction() {
		return onlineInstruction;
	}

	/**
	 * @param onlineInstruction The onlineInstruction to set.
	 */
	public void setOnlineInstruction(String onlineInstruction) {
		this.onlineInstruction = onlineInstruction;
	}

	/**
	 * @hibernate.property column="run_offline_instruction" type="text"
	 * @return Returns the runOfflineInstruction.
	 */
	public String getRunOfflineInstruction() {
		return runOfflineInstruction;
	}

	/**
	 * @param runOfflineInstruction The runOfflineInstruction to set.
	 */
	public void setRunOfflineInstruction(String runOfflineInstruction) {
		this.runOfflineInstruction = runOfflineInstruction;
	}

	/**
	 * @hibernate.property column="content_in_use" length="1"
	 * @return Returns the contentInUse.
	 */
	public boolean isContentInUse() {
		return contentInUse;
	}

	/**
	 * @param contentInUse The contentInUse to set.
	 */
	public void setContentInUse(boolean contentInUse) {
		this.contentInUse = contentInUse;
	}

	/**
 	 * @hibernate.set lazy="true" inverse="false" cascade="all-delete-orphan"
	 * @hibernate.collection-key column="content_id"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.sbmt.InstructionFiles"
	 * 
	 * @return Returns the instructionFiles.
	 */
	public Set getInstructionFiles() {
		return instructionFiles;
	}

	/**
	 * @param instructionFiles The instructionFiles to set.
	 */
	public void setInstructionFiles(Set instructionFiles) {
		this.instructionFiles = instructionFiles;
	}

	/**
	 * 
	 * @hibernate.property column="lock_on_finished" length="1"
	 * @return Returns the lockOnFinished.
	 */
	public boolean isLockOnFinished() {
		return lockOnFinished;
	}

	/**
	 * @param lockOnFinished The lockOnFinished to set.
	 */
	public void setLockOnFinished(boolean lockOnFinished) {
		this.lockOnFinished = lockOnFinished;
	}

    
    public Object clone(){
		Object obj = null;
		try {
			obj = super.clone();
			//never clone key!
			((SubmitFilesContent)obj).setContentID(null);
			//clone SubmitFIleSession object
			if(toolSession != null ){
				Iterator iter = toolSession.iterator();
				Set set = new HashSet();
				while(iter.hasNext())
					set.add(((SubmitFilesSession)iter.next()).clone());
				((SubmitFilesContent)obj).toolSession = set;
			}
			//clone InstructionFiles object
			if(instructionFiles != null ){
				Iterator iter = instructionFiles.iterator();
				Set set = new HashSet();
				while(iter.hasNext()){
					InstructionFiles file = (InstructionFiles)iter.next();
					//duplicate file node in repository
					NodeKey keys = toolContentHandler.copyFile(file.getUuID());
					InstructionFiles newFile = (InstructionFiles) file.clone();
					newFile.setUuID(keys.getUuid());
					newFile.setVersionID(keys.getVersion());
					set.add(newFile);
				}
				((SubmitFilesContent)obj).instructionFiles= set;
			}
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + SubmitFilesContent.class + " failed");
		} catch (ItemNotFoundException e) {
			log.error("When clone " + SubmitFilesContent.class + " failed");
		} catch (RepositoryCheckedException e) {
			log.error("When clone " + SubmitFilesContent.class + " failed");
		}
		
		return obj;
	}

	public IToolContentHandler getToolContentHandler() {
		return toolContentHandler;
	}

	public void setToolContentHandler(IToolContentHandler toolContentHandler) {
		this.toolContentHandler = toolContentHandler;
	}

}
