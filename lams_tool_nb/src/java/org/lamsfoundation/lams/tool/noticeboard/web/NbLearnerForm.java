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
 * Created on Jun 27, 2005
 */


package org.lamsfoundation.lams.tool.noticeboard.web;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;

/**
 * Creation Date: 19-05-05
 *  
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:form name="NbLearnerForm" type="org.lamsfoundation.lams.tool.noticeboard.web.NbLearnerForm"
 *
 * ----------------XDoclet Tags--------------------
 */

public class NbLearnerForm extends ActionForm{
    
    static Logger logger = Logger.getLogger(NbLearnerForm.class.getName());
    
    private String title;
    
    private String content;
    
    private Long userId;
    
    private Long toolSessionId;
    
    private Long toolContentId;
    
    private String method;
    
    private String mode;
    
    

    /**
     * @return Returns the toolContentID.
     */
    public Long getToolContentId() {
        return toolContentId;
    }
    /**
     * @param toolContentID The toolContentID to set.
     */
    public void setToolContentId(Long toolContentID) {
        this.toolContentId = toolContentID;
    }
    /**
     * @return Returns the toolSessionID.
     */
    public Long getToolSessionId() {
        return toolSessionId;
    }
    /**
     * @param toolSessionID The toolSessionID to set.
     */
    public void setToolSessionId(Long toolSessionID) {
        this.toolSessionId = toolSessionID;
    }
    /**
     * @return Returns the userID.
     */
    public Long getUserId() {
        return userId;
    }
    /**
     * @param userID The userID to set.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
    
    public void reset()
	{
		this.content = null;
		this.title = null;
			
	}
    
    public void copyValuesIntoForm(NoticeboardContent content)
    {
        setTitle(content.getTitle());
        setContent(content.getContent());
    }
    
    /**
     * @return Returns the mode.
     */
    public String getMode() {
        return mode;
    }
    /**
     * @param mode The mode to set.
     */
    public void setMode(String mode) {
        this.mode = mode;
    }
}
