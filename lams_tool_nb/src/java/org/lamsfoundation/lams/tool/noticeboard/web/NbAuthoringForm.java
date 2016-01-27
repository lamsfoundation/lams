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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.tool.noticeboard.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.util.UploadFileUtil;


//import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;

/**
 * <p>ActionForm which holds the state of the noticeboard form content in the Authoring 
 * environment. Stores all values in the session scope.</p>
 * 
 * <p>The validate method does not check whether any of the input from
 * title, content are empty or not.
 * This is because I have encountered a situation where even though the field is
 * empty, the FCKEditor places a <br/> tag and so the validate method doesnt work.
 * However, the validate method checks the length of the file that has been uploaded,
 * the maximum filesize that can be uploaded is a property that is in the UploadFileUtil. </p>
 * 
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
	
	private static final long serialVersionUID = -8425012664714570196L;

	static Logger logger = Logger.getLogger(NbAuthoringForm.class.getName());

	private String title;
	private String basicContent;
	
	private String method;
	private String toolContentID;
	private String contentFolderID;
	private String defineLater;
	
	private boolean allowComments;
	private boolean commentsLikeAndDislike;
	private boolean reflectOnActivity;
	private String reflectInstructions;

	private String currentTab;

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
    
    public boolean isAllowComments() {
        return allowComments;
    }
    public void setAllowComments(boolean allowComments) {
        this.allowComments = allowComments;
    }
    public boolean isCommentsLikeAndDislike() {
        return commentsLikeAndDislike;
    }
    public void setCommentsLikeAndDislike(boolean commentsLikeAndDislike) {
        this.commentsLikeAndDislike = commentsLikeAndDislike;
    }
    public boolean getReflectOnActivity() {
    	return reflectOnActivity;
    }
    
    public void setReflectOnActivity(boolean reflectOnActivity) {
    	this.reflectOnActivity = reflectOnActivity;
    }
    
    public String getReflectInstructions() {
    	return reflectInstructions;
    }
    
    public void setReflectInstructions(String reflectInstructions) {
    	this.reflectInstructions = reflectInstructions;
    }
    
	/**
	 * @return Returns the content.
	 */
	public String getBasicContent() {
		return basicContent;
	}
	/**
	 * @param basicContent The content to set.
	 */
	public void setBasicContent(String basicContent) {
		this.basicContent = basicContent;
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
	public String getToolContentID() {
		return toolContentID;
	}
	/**
	 * @param toolContentID The toolContentID to set.
	 */
	public void setToolContentID(String toolContentID) {
		this.toolContentID = toolContentID;
	}

	/**
	 * 
	 * @return Returns the contentFolderID
	 */
	public String getContentFolderID() {
		return contentFolderID;
	}
	
	/**
	 * 
	 * @param contentFolderID The contentFolderID is set
	 */
	public void setContentFolderID(String contentFolderID) {
		this.contentFolderID = contentFolderID;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
	//	this.content = null;
		//this.title = null;
	    this.method= null;
	    this.defineLater = null;
	}
	
	/**
	 * This method is to prepopulate the form with values from a noticeboard content
	 * object. Used in AuthoringStarterAction
	 * @param nbContent
	 */
	public void populateFormWithNbContentValues(NoticeboardContent nbContent)
	{
		setTitle(nbContent.getTitle());
		setBasicContent(nbContent.getContent());
		setAllowComments(nbContent.isAllowComments());
		setCommentsLikeAndDislike(nbContent.isCommentsLikeAndDislike());
		setReflectOnActivity(nbContent.getReflectOnActivity());
		setReflectInstructions(nbContent.getReflectInstructions());
	}
	
	public void copyValuesIntoNbContent(NoticeboardContent nbContent)
	{
	    nbContent.setTitle(getTitle());
	    nbContent.setContent(getBasicContent());
	    nbContent.setAllowComments(isAllowComments());
	    nbContent.setCommentsLikeAndDislike(isCommentsLikeAndDislike());
	    nbContent.setReflectOnActivity(getReflectOnActivity());
	    nbContent.setReflectInstructions(getReflectInstructions());
	    nbContent.setDateUpdated(new Date(System.currentTimeMillis()));
	}
	
	public String getCurrentTab() {
		return currentTab;
	}
	public void setCurrentTab(String currentTab) {
		this.currentTab = currentTab;
	}
  
}