/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.qa;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.qa.QaQueContent;


/**
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * QaContent Value Object
 * The value object that maps to our model database table: tl_laqa11_content
 * The relevant hibernate mapping resides in: QaContent.hbm.xml
 * 
 * Holds content representation for the tool.
 * Default content is made available to the tool by the database. 
 */
public class QaContent implements Serializable {
	static Logger logger = Logger.getLogger(QaContent.class.getName());
	
	/** identifier field */
    private Long uid;
    
	/** identifier field */
    private Long qaContentId;
	
    /** nullable persistent field */
    private String title;
    
    /** nullable persistent field */
    private String instructions;
    
    /** persistent field, used for export portfolio */
    private String content;
    
    /** nullable persistent field */
    private String reportTitle;
    
    /** nullable persistent field */
    private String monitoringReportTitle;
    
    /** nullable persistent field */
    private String offlineInstructions;
    
    /** nullable persistent field */
    private String onlineInstructions;
    
    /** nullable persistent field */
    private long createdBy;
    
    /** nullable persistent field */
    private boolean defineLater;
    
    /** nullable persistent field */
    private boolean runOffline;
    
    private boolean questionsSequenced;
    
    /** nullable persistent field */
    private boolean usernameVisible;
    
    /** nullable persistent field */
    private boolean synchInMonitor;
    
    /** nullable persistent field */
    private boolean contentLocked;
    
    /** nullable persistent field */
    private String endLearningMessage;
    
    /** nullable persistent field */
    private String creationDate;
	
    /** nullable persistent field */
    private Date updateDate;
    
	
	/** persistent field */
    private Set qaQueContents;
    
    /** persistent field */
    private Set qaSessions;
    
    /** persistent field */
    private Set qaUploadedFiles;

    public QaContent(){};
    
    /** full constructor */
    public QaContent(Long 		qaContentId,
    				 String 	content,
                     String 	title,
	                 String 	instructions,
	                 String 	reportTitle,
	                 String 	monitoringReportTitle,
	                 String 	offlineInstructions,
	                 String 	onlineInstructions,
	                 String 	endLearningMessage,
	                 long		createdBy,
	                 boolean 	defineLater,
					 boolean	runOffline,
					 boolean 	questionsSequenced,
	                 boolean 	usernameVisible,
	                 boolean 	synchInMonitor,
	                 boolean 	contentLocked,
	                 String		creationDate,
	                 Date 		updateDate,
	                 Set 		qaQueContents,
	                 Set 		qaSessions,
					 Set 		qaUploadedFiles)
    {
        this.qaContentId 		 = qaContentId;
        this.content 		 	 = content;
        this.title 				 = title;
        this.instructions 		 = instructions;
        this.reportTitle 		 = reportTitle;
        this.monitoringReportTitle=monitoringReportTitle;
        this.offlineInstructions = offlineInstructions;
        this.onlineInstructions  = onlineInstructions;
        this.endLearningMessage	 = endLearningMessage;
        this.createdBy 			 = createdBy;
        this.defineLater 		 = defineLater;
        this.runOffline 		 = runOffline;
        this.questionsSequenced	 = questionsSequenced;
        this.usernameVisible 	 = usernameVisible;
        this.synchInMonitor 	 = synchInMonitor;
        this.contentLocked		 = contentLocked;
        this.creationDate 		 = creationDate;
        this.updateDate 		 = updateDate;
        this.qaQueContents 		 = qaQueContents;
        this.qaSessions 		 = qaSessions;
        this.qaUploadedFiles	 = qaUploadedFiles;
        logger.debug(logger + " " + this.getClass().getName() +  "in full constructor: QaContent()");
    }
    
    /**
     * Copy Construtor to create a new qa content instance. Note that we
     * don't copy the qa session data here because the qa session 
     * will be created after we copied tool content.
     * @param qa the original qa content.
     * @param newContentId the new qa content id.
     * @return the new qa content object.
     */
    public static QaContent newInstance(IToolContentHandler toolContentHandler, QaContent qa,
            Long newContentId)
    	throws ItemNotFoundException, RepositoryCheckedException
    {
    	QaContent newContent = new QaContent(newContentId,
    				 qa.getContent(),
                     qa.getTitle(),
                     qa.getInstructions(),
                     qa.getReportTitle(),
					 qa.getMonitoringReportTitle(),
                     qa.getOfflineInstructions(),
                     qa.getOnlineInstructions(),
					 qa.getEndLearningMessage(),
                     qa.getCreatedBy(),
                     qa.isDefineLater(),
					 qa.isRunOffline(),
					 qa.isQuestionsSequenced(),
                     qa.isUsernameVisible(),
                     qa.isSynchInMonitor(),
					 qa.isContentLocked(),
                     qa.getCreationDate(),
                     qa.getUpdateDate(),
					 new TreeSet(),
					 new TreeSet(),
                     new TreeSet());
    	logger.debug(logger + " " + "QaContent" +  " " + "before doing deepCopyQaQueContent");
    	newContent.setQaQueContents(qa.deepCopyQaQueContent(newContent));
    	logger.debug(logger + " " + "QaContent" +  " " + "after doing deepCopyQaQueContent");

    	newContent.setQaUploadedFiles(qa.deepCopyQaAttachments(toolContentHandler, newContent));	
		
    	return newContent;
	}

    public Set deepCopyQaQueContent(QaContent newQaContent)
    {
    	logger.debug(logger + " " + "QaContent" +  " " + "start of deepCopyQaQueContent");
    	Set newQaQueContent = new TreeSet();
        for (Iterator i = this.getQaQueContents().iterator(); i.hasNext();)
        {
            QaQueContent queContent = (QaQueContent) i.next();
            newQaQueContent.add(QaQueContent.newInstance(queContent,
            											newQaContent,
														null));
        }
        logger.debug(logger + " " + "QaContent" +  " " + "returning newQaQueContent: " + newQaQueContent);
        return newQaQueContent;
    }
    
    
    public Set deepCopyQaAttachments(IToolContentHandler toolContentHandler,QaContent newQaContent)
    throws ItemNotFoundException, RepositoryCheckedException
    {
    	Set attachments = new TreeSet();
        for (Iterator i = this.getQaUploadedFiles().iterator(); i.hasNext();)
        {
        	QaUploadedFile qaUploadedFile = (QaUploadedFile) i.next();
            if (qaUploadedFile.getQaContent() != null)
            {
            	QaUploadedFile newQaUploadedFile=QaUploadedFile.newInstance(toolContentHandler, qaUploadedFile,
            			newQaContent);
            	attachments.add(newQaUploadedFile);
            }
        }
        return attachments;
    }
    
    
    
    public Set deepCopyQaSession(QaContent newQaSession)
    {
        return new TreeSet();
    }
    
    
    public Set getQaQueContents()
    {
        if (this.qaQueContents == null)
            setQaQueContents(new TreeSet());
        return this.qaQueContents;
    }

    public void setQaQueContents(Set qaQueContents)
    {
        this.qaQueContents = qaQueContents;
    }

    public Set getQaSessions()
    {
        if (this.qaSessions == null)
            setQaSessions(new TreeSet());
        return this.qaSessions;
    }

    public void setQaSessions(Set qaSessions)
    {
        this.qaSessions = qaSessions;
    }

    
   	/**
	 * @return Returns the qaContentId.
	 */
	public Long getQaContentId() {
		return qaContentId;
	}
	/**
	 * @param qaContentId The qaContentId to set.
	 */
	public void setQaContentId(Long qaContentId) {
		this.qaContentId = qaContentId;
	}
	/**
	 * @return Returns the title.
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
	
	
	public String toString()
    {
        return new ToStringBuilder(this).append("qaContentId:",
                                                getQaContentId())
                                        .append("qa title:", getTitle())
                                        .append("qa instructions:",
                                                getInstructions())
                                        .append("date created:",
                                        		getCreationDate())
										.append("update date:",
												getUpdateDate())												
                                        .append("creator user id",
                                        		getCreatedBy())
                                        .append("username_visible:", isUsernameVisible())
                                        .append("defineLater", isDefineLater())
                                        .append("offline_instructions:", getOfflineInstructions())
										.append("online_instructions:", getOnlineInstructions())
                                        .append("report_title: ", getReportTitle())
										.append("synch_in_monitor: ", isSynchInMonitor())
                                        .toString();
    }


    public boolean equals(Object other)
    {
        if (!(other instanceof QaContent))
            return false;
        QaContent castOther = (QaContent) other;
        return new EqualsBuilder().append(this.getQaContentId(),
                                          castOther.getQaContentId())
										  .isEquals();
    }


    public int hashCode()
    {
        return new HashCodeBuilder().append(getQaContentId()).toHashCode();
    }


	/**
	 * @return Returns the createdBy.
	 */
	public long getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return Returns the creationDate.
	 */
	public String getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate The creationDate to set.
	 */
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return Returns the defineLater.
	 */
	public boolean isDefineLater() {
		return defineLater;
	}
	/**
	 * @param defineLater The defineLater to set.
	 */
	public void setDefineLater(boolean defineLater) {
		this.defineLater = defineLater;
	}
	/**
	 * @return Returns the instructions.
	 */
	public String getInstructions() {
		return instructions;
	}
	/**
	 * @param instructions The instructions to set.
	 */
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	/**
	 * @return Returns the offlineInstructions.
	 */
	public String getOfflineInstructions() {
		return offlineInstructions;
	}
	/**
	 * @param offlineInstructions The offlineInstructions to set.
	 */
	public void setOfflineInstructions(String offlineInstructions) {
		this.offlineInstructions = offlineInstructions;
	}
	/**
	 * @return Returns the onlineInstructions.
	 */
	public String getOnlineInstructions() {
		return onlineInstructions;
	}
	/**
	 * @param onlineInstructions The onlineInstructions to set.
	 */
	public void setOnlineInstructions(String onlineInstructions) {
		this.onlineInstructions = onlineInstructions;
	}
	/**
	 * @return Returns the reportTitle.
	 */
	public String getReportTitle() {
		return reportTitle;
	}
	/**
	 * @param reportTitle The reportTitle to set.
	 */
	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}
	/**
	 * @return Returns the synchInMonitor.
	 */
	public boolean isSynchInMonitor() {
		return synchInMonitor;
	}
	/**
	 * @param synchInMonitor The synchInMonitor to set.
	 */
	public void setSynchInMonitor(boolean synchInMonitor) {
		this.synchInMonitor = synchInMonitor;
	}
	/**
	 * @return Returns the updateDate.
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
	/**
	 * @param updateDate The updateDate to set.
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * @return Returns the usernameVisible.
	 */
	public boolean isUsernameVisible() {
		return usernameVisible;
	}
	/**
	 * @param usernameVisible The usernameVisible to set.
	 */
	public void setUsernameVisible(boolean usernameVisible) {
		this.usernameVisible = usernameVisible;
	}
	 	
	/**
	 * @return Returns the questionsSequenced.
	 */
	public boolean isQuestionsSequenced() {
		return questionsSequenced;
	}
	/**
	 * @param questionsSequenced The questionsSequenced to set.
	 */
	public void setQuestionsSequenced(boolean questionsSequenced) {
		this.questionsSequenced = questionsSequenced;
	}
	/**
	 * @return Returns the endLearningMessage.
	 */
	public String getEndLearningMessage() {
		return endLearningMessage;
	}
	/**
	 * @param endLearningMessage The endLearningMessage to set.
	 */
	public void setEndLearningMessage(String endLearningMessage) {
		this.endLearningMessage = endLearningMessage;
	}
	/**
	 * @return Returns the runOffline.
	 */
	public boolean isRunOffline() {
		return runOffline;
	}
	/**
	 * @param runOffline The runOffline to set.
	 */
	public void setRunOffline(boolean runOffline) {
		this.runOffline = runOffline;
	}
	/**
	 * @return Returns the monitoringReportTitle.
	 */
	public String getMonitoringReportTitle() {
		return monitoringReportTitle;
	}
	/**
	 * @param monitoringReportTitle The monitoringReportTitle to set.
	 */
	public void setMonitoringReportTitle(String monitoringReportTitle) {
		this.monitoringReportTitle = monitoringReportTitle;
	}
	/**
	 * @return Returns the contentLocked.
	 */
	public boolean isContentLocked() {
		return contentLocked;
	}
	/**
	 * @param contentLocked The contentLocked to set.
	 */
	public void setContentLocked(boolean contentLocked) {
		this.contentLocked = contentLocked;
	}
	/**
	 * @return Returns the qaUploadedFiles.
	 */
	public Set getQaUploadedFiles() {
        if(qaUploadedFiles == null)
            qaUploadedFiles = new TreeSet();
		return qaUploadedFiles;
	}
	/**
	 * @param qaUploadedFiles The qaUploadedFiles to set.
	 */
	public void setQaUploadedFiles(Set qaUploadedFiles) {
		this.qaUploadedFiles = qaUploadedFiles;
	}
	/**
	 * @return Returns the uid.
	 */
	public Long getUid() {
		return uid;
	}
	/**
	 * @param uid The uid to set.
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}
	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}
}
