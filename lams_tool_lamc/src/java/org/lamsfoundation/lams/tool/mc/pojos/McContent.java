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

package org.lamsfoundation.lams.tool.mc.pojos;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;

/**
 * <p>Persistent  object/bean that defines the content for the MCQ tool.
 * Provides accessors and mutators to get/set attributes
 * It maps to database table: tl_lamc11_content
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class McContent implements Serializable {
	static Logger logger = Logger.getLogger(McContent.class.getName());

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long mcContentId;

    /** nullable persistent field */
    private String title;

    /** nullable persistent field */
    private String instructions;

    /** nullable persistent field */
    private boolean defineLater;

    /** nullable persistent field */
    private boolean runOffline;

    /** nullable persistent field */
    private String creationDate;

    /** nullable persistent field */
    private Date updateDate;

    /** nullable persistent field */
    private boolean questionsSequenced;

    /** nullable persistent field */
    private boolean usernameVisible;

    /** nullable persistent field */
    private String reportTitle;

    /** nullable persistent field */
    private String monitoringReportTitle;

    /** nullable persistent field */
    private long createdBy;

    /** nullable persistent field */
    private boolean synchInMonitor;

    /** nullable persistent field */
    private boolean contentInUse;

    /** nullable persistent field */
    private String offlineInstructions;

    /** nullable persistent field */
    private String onlineInstructions;

    /** nullable persistent field */
    private String endLearningMessage;

    /** nullable persistent field */
    private boolean retries;
    
    private boolean showReport;
    
	
    /** nullable persistent field */
    private Integer passMark;

    /** nullable persistent field */
    private boolean showFeedback;

    
    /** persistent field */
    private Set mcQueContents;

    /** persistent field */
    private Set mcSessions;
    
    /** persistent field */
    private Set mcAttachments;

    /** full constructor */
    public McContent(Long mcContentId, String title, String instructions, boolean defineLater, boolean runOffline, String creationDate, 
    		Date updateDate, boolean questionsSequenced, boolean usernameVisible, String reportTitle, String monitoringReportTitle, 
			long createdBy, boolean synchInMonitor, boolean contentInUse, String offlineInstructions, String onlineInstructions, 
			String endLearningMessage, Integer passMark, boolean showReport, boolean showFeedback, boolean retries, Set mcQueContents, Set mcSessions, 
			Set mcAttachments) {
        this.mcContentId = mcContentId;
        this.title = title;
        this.instructions = instructions;
        this.defineLater = defineLater;
        this.runOffline = runOffline;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.questionsSequenced = questionsSequenced;
        this.usernameVisible = usernameVisible;
        this.reportTitle = reportTitle;
        this.monitoringReportTitle = monitoringReportTitle;
        this.createdBy = createdBy;
        this.synchInMonitor = synchInMonitor;
        this.contentInUse = contentInUse;
        this.offlineInstructions = offlineInstructions;
        this.onlineInstructions = onlineInstructions;
        this.endLearningMessage = endLearningMessage;
        this.passMark = passMark;
        this.showReport = showReport;
        this.retries=retries;
        this.showFeedback = showFeedback;
        this.mcQueContents = mcQueContents;
        this.mcSessions = mcSessions;
        this.mcAttachments = mcAttachments;
    }

    /** default constructor */
    public McContent() {
    }

    /** minimal constructor */
    public McContent(Long mcContentId, Set mcQueContents, Set mcSessions) {
        this.mcContentId = mcContentId;
        this.mcQueContents = mcQueContents;
        this.mcSessions = mcSessions;
    }
    
    
    /**
     *  gets called as part of the copyToolContent
     *  
     * Copy Construtor to create a new mc content instance. Note that we
     * don't copy the mc session data here because the mc session 
     * will be created after we copied tool content.
     * @param mc the original mc content.
     * @param newContentId the new mc content id.
     * @return the new mc content object.
     */
    public static McContent newInstance(IToolContentHandler toolContentHandler, McContent mc,
            Long newContentId)
    throws ItemNotFoundException, RepositoryCheckedException
    {
    	McContent newContent = new McContent(
    				 newContentId,
                     mc.getTitle(),
                     mc.getInstructions(),
                     mc.isDefineLater(),
					 mc.isRunOffline(),
					 mc.getCreationDate(),
			         mc.getUpdateDate(),
					 mc.isQuestionsSequenced(),
                     mc.isUsernameVisible(),
                     mc.getReportTitle(),
					 mc.getMonitoringReportTitle(),
					 mc.getCreatedBy(),				 
					 mc.isSynchInMonitor(),
					 mc.isContentInUse(),
					 mc.getOfflineInstructions(),
					 mc.getOnlineInstructions(),
					 mc.getEndLearningMessage(),
					 mc.getPassMark(),
					 mc.isShowReport(),
					 mc.isRetries(),
					 mc.isShowFeedback(),
         			 new TreeSet(),
                     new TreeSet(),
                     new TreeSet()
					 );
    	newContent.setMcQueContents(mc.deepCopyMcQueContent(newContent));
    	newContent.setMcAttachments(mc.deepCopyMcAttachments(toolContentHandler, newContent));
    	
    	 
    	
    	return newContent;
	}
    
    /**
     * gets called as part of the copyToolContent
     * 
     * @param newQaContent
     * @return Set
     */
    public Set deepCopyMcQueContent(McContent newMcContent)
    {
    	Set newMcQueContent = new TreeSet();
        for (Iterator i = this.getMcQueContents().iterator(); i.hasNext();)
        {
            McQueContent queContent = (McQueContent) i.next();
            if (queContent.getMcContent() != null)
            {
            	McQueContent mcQueContent=McQueContent.newInstance(queContent,
															newMcContent);
            	newMcQueContent.add(mcQueContent);
            }
        }
        return newMcQueContent;
    }
    
    /**
     * gets called as part of the copyToolContent
     * 
     * @param newMcContent
     * @return Set
     */
    public Set deepCopyMcAttachments(IToolContentHandler toolContentHandler,McContent newMcContent)
    throws ItemNotFoundException, RepositoryCheckedException
    {
    	Set newMcQueContent = new TreeSet();
        for (Iterator i = this.getMcAttachments().iterator(); i.hasNext();)
        {
        	McUploadedFile mcUploadedFile = (McUploadedFile) i.next();
            if (mcUploadedFile.getMcContent() != null)
            {
            	McUploadedFile newMcUploadedFile=McUploadedFile.newInstance(toolContentHandler, mcUploadedFile,
															newMcContent);
            	newMcQueContent.add(newMcUploadedFile);
            }
        }
        return newMcQueContent;
    }
    

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getMcContentId() {
        return this.mcContentId;
    }

    public void setMcContentId(Long mcContentId) {
        this.mcContentId = mcContentId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isDefineLater() {
        return this.defineLater;
    }

    public void setDefineLater(boolean defineLater) {
        this.defineLater = defineLater;
    }

    public boolean isRunOffline() {
        return this.runOffline;
    }

    public void setRunOffline(boolean runOffline) {
        this.runOffline = runOffline;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public boolean isQuestionsSequenced() {
        return this.questionsSequenced;
    }

    public void setQuestionsSequenced(boolean questionsSequenced) {
        this.questionsSequenced = questionsSequenced;
    }

    public boolean isUsernameVisible() {
        return this.usernameVisible;
    }

    public void setUsernameVisible(boolean usernameVisible) {
        this.usernameVisible = usernameVisible;
    }

    public String getReportTitle() {
        return this.reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getMonitoringReportTitle() {
        return this.monitoringReportTitle;
    }

    public void setMonitoringReportTitle(String monitoringReportTitle) {
        this.monitoringReportTitle = monitoringReportTitle;
    }

    public long getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isSynchInMonitor() {
        return this.synchInMonitor;
    }

    public void setSynchInMonitor(boolean synchInMonitor) {
        this.synchInMonitor = synchInMonitor;
    }

    public boolean isContentInUse() {
        return this.contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
        this.contentInUse = contentInUse;
    }

    public String getOfflineInstructions() {
        return this.offlineInstructions;
    }

    public void setOfflineInstructions(String offlineInstructions) {
        this.offlineInstructions = offlineInstructions;
    }

    public String getOnlineInstructions() {
        return this.onlineInstructions;
    }

    public void setOnlineInstructions(String onlineInstructions) {
        this.onlineInstructions = onlineInstructions;
    }

    public String getEndLearningMessage() {
        return this.endLearningMessage;
    }

    public void setEndLearningMessage(String endLearningMessage) {
        this.endLearningMessage = endLearningMessage;
    }

    public Integer getPassMark() {
        return this.passMark;
    }

    public void setPassMark(Integer passMark) {
        this.passMark = passMark;
    }

    public boolean isShowFeedback() {
        return this.showFeedback;
    }

    public void setShowFeedback(boolean showFeedback) {
        this.showFeedback = showFeedback;
    }

        
    public Set getMcQueContents() {
    	if (this.mcQueContents == null)
        	setMcQueContents(new HashSet());
        return this.mcQueContents;
    }

    
    public void setMcQueContents(Set mcQueContents) {
        this.mcQueContents = mcQueContents;
    }

    public Set getMcSessions() {
    	if (this.mcSessions == null)
        	setMcSessions(new HashSet());
        return this.mcSessions;
    }

    public void setMcSessions(Set mcSessions) {
        this.mcSessions = mcSessions;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("uid", getUid())
            .toString();
    }
    
    /**
	 * @return Returns the retries.
	 */
	public boolean isRetries() {
		return retries;
	}
	/**
	 * @param retries The retries to set.
	 */
	public void setRetries(boolean retries) {
		this.retries = retries;
	}

	/**
	 * @return Returns the showReport.
	 */
	public boolean isShowReport() {
		return showReport;
	}
	/**
	 * @param showReport The showReport to set.
	 */
	public void setShowReport(boolean showReport) {
		this.showReport = showReport;
	}
	/**
	 * @return Returns the mcAttachments.
	 */
	public Set getMcAttachments() {
		return mcAttachments;
	}
	/**
	 * @param mcAttachments The mcAttachments to set.
	 */
	public void setMcAttachments(Set mcAttachments) {
		this.mcAttachments = mcAttachments;
	}
}
