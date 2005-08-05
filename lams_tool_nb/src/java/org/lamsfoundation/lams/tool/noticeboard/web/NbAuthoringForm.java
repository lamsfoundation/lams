/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */


/*
 * Created on May 19, 2005
 *
 */



package org.lamsfoundation.lams.tool.noticeboard.web;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.util.UploadFileUtil;


//import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;

/**
 * ActionForm which holds the state of the noticeboard form content in the Authoring 
 * environment. Stores all values in the session scope.
 */

/**
 * Creation Date: 19-05-05
 *  
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:form name="NbAuthoringForm" type="org.lamsfoundation.lams.tool.noticeboard.web.NbAuthoringForm"
 *
 * ----------------XDoclet Tags--------------------
 */

public class NbAuthoringForm extends ActionForm {
	
	static Logger logger = Logger.getLogger(NbAuthoringForm.class.getName());

	private String title;
	private String content;
	private String onlineInstructions;
	private String offlineInstructions;
	
	private String method;
	private String toolContentId;
	private String defineLater;
	
	private FormFile onlineFile;
	private FormFile offlineFile;
	private Map attachments = new HashMap();
	
    
	 /**
     * @return Returns the attachments.
     */
    public Map getAttachments() {
        if (attachments == null)
        {
            return new HashMap();
        }
        return attachments;
    }
    /**
     * @param attachments The attachments to set.
     */
    public void setAttachments(Map attachments) {
        this.attachments = attachments;
    }
	
	/**
     * @return Returns the offlineFile.
     */
    public FormFile getOfflineFile() {
        return offlineFile;
    }
    /**
     * @param offlineFile The offlineFile to set.
     */
    public void setOfflineFile(FormFile offlineFile) {
        this.offlineFile = offlineFile;
    }
    /**
     * @return Returns the onlineFile.
     */
    public FormFile getOnlineFile() {
        return onlineFile;
    }
    /**
     * @param onlineFile The onlineFile to set.
     */
    public void setOnlineFile(FormFile onlineFile) {
        this.onlineFile = onlineFile;
    }
    /**
     * @return Returns the defineLater.
     */
    public String getDefineLater() {
        return defineLater;
    }
    /**
     * @param defineLater The defineLater to set.
     */
    public void setDefineLater(String defineLater) {
        this.defineLater = defineLater;
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
	
	/**
	 * @return Returns the online instructions
	 */
	public String getOnlineInstructions()
	{
		return onlineInstructions;
	}
	
	/**
	 * @param onlineInstructions The online instructions to set
	 */
	public void setOnlineInstructions(String onlineInstructions)
	{
		this.onlineInstructions = onlineInstructions;
	}
	
	/**
	 * @return Returns the offline instructions
	 */
	public String getOfflineInstructions()
	{
		return offlineInstructions;
	}
	
	/**
	 * @param offlineInstructions The offline instructions to set
	 */
	public void setOfflineInstructions(String offlineInstructions)
	{
		this.offlineInstructions = offlineInstructions;
	}
	
	/**
	 * @return Returns the method.
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method The method to set.
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * @return Returns the toolContentID.
	 */
	public String getToolContentId() {
		return toolContentId;
	}
	/**
	 * @param toolContentID The toolContentID to set.
	 */
	public void setToolContentId(String toolContentID) {
		this.toolContentId = toolContentID;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
	//	this.content = null;
		//this.title = null;
		//this.onlineInstructions = null;
		//this.offlineInstructions = null;
	    this.method= null;
	    this.defineLater = null;
	    this.onlineFile = null;
	    this.offlineFile = null;
	}
	
	/**
	 * This method is to prepopulate the form with values from a noticeboard content
	 * object. Used in AuthoringStarterAction
	 * @param nbContent
	 */
	public void populateFormWithNbContentValues(NoticeboardContent nbContent)
	{
		setTitle(nbContent.getTitle());
		setContent(nbContent.getContent());
		setOnlineInstructions(nbContent.getOnlineInstructions());
		setOfflineInstructions(nbContent.getOfflineInstructions());
	}
	
	public void copyValuesIntoNbContent(NoticeboardContent nbContent)
	{
		nbContent.setTitle(getTitle());
	    nbContent.setContent(getContent());
		nbContent.setOnlineInstructions(getOnlineInstructions());
		nbContent.setOfflineInstructions(getOfflineInstructions());
		nbContent.setDateUpdated(new Date(System.currentTimeMillis()));
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
	    ActionErrors errors = new ActionErrors();
	    //check the tool content id
	    //check the title and instructions
	    
	    MessageResources resources =
            (MessageResources) request.getAttribute(Globals.MESSAGES_KEY);
	    
	    float maxFileSize = UploadFileUtil.getMaxFileSize();
	   
	    
	 /*   if (toolContentId == null)
	    {
	        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(NoticeboardConstants.ERROR_MANDATORY, "Tool COntent id"));
	    }
	    if (title==null || title.length()==0)
	    {
	        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(NoticeboardConstants.ERROR_MANDATORY, "Title"));
	    } */
	    
	    //check the file sizes so of the file uploads
	   if (this.offlineFile != null && offlineFile.getFileSize()!= 0) 
	    {
	        float sizeOfOfflineUpload = offlineFile.getFileSize() / 1024 / 1024;
	        //check the file size
	       if (sizeOfOfflineUpload  > maxFileSize)
	        {
	            //show message saying the file is greater than the allowed limit
	            logger.error("the file uploaded exceeds the maximum file size");
	            errors.add("filesize", new ActionMessage("error.exceedMaxFileSize"));
	            this.setMethod("Instructions");
	        }
	       
	    } 
	   else if ( this.onlineFile != null && onlineFile.getFileSize() != 0)
	   {
	       float sizeOfOnlineUpload = onlineFile.getFileSize() / 1024 / 1024; //getFileSize() returns the file size in bytes, but we are comparing the filesize using units MBs
		   
	     if (sizeOfOnlineUpload > maxFileSize) 
	     {
//	       show message saying the file is greater than the allowed limit
	            logger.error("the file uploaded exceeds the maximum file size");
	            errors.add("filesize", new ActionMessage("error.exceedMaxFileSize"));
	            this.setMethod("Instructions");
	     }
	   }
	   
	    return errors; 
	    
	}
	

  
}
