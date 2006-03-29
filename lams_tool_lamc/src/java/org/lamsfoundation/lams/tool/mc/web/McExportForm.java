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
package org.lamsfoundation.lams.tool.mc.web;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;

/**
 * @author Ozgur Demirtas
 */

public class McExportForm extends ActionForm {
    
    static Logger logger = Logger.getLogger(McExportForm.class.getName());
    
    private String title;
	private String content;
	
	private String toolContentID;
	private String toolSessionID;
	private String userID;
	private String mode;
	
    /**
     * @return Returns the logger.
     */
    public static Logger getLogger() {
        return logger;
    }
    /**
     * @param logger The logger to set.
     */
    public static void setLogger(Logger logger) {
    	McExportForm.logger = logger;
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
     * @return Returns the toolContentId.
     */
    public String getToolContentID() {
        return toolContentID;
    }
    /**
     * @param toolContentId The toolContentId to set.
     */
    public void setToolContentID(String toolContentId) {
        this.toolContentID = toolContentId;
    }
    /**
     * @return Returns the toolSessionId.
     */
    public String getToolSessionID() {
        return toolSessionID;
    }
    /**
     * @param toolSessionId The toolSessionId to set.
     */
    public void setToolSessionID(String toolSessionId) {
        this.toolSessionID = toolSessionId;
    }
    /**
     * @return Returns the userId.
     */
    public String getUserID() {
        return userID;
    }
    /**
     * @param userId The userId to set.
     */
    public void setUserID(String userId) {
        this.userID = userId;
    }
    
    public void reset()
    {
        this.mode = null;
        this.title = null;
        this.content = null;
        this.toolContentID = null;
        this.toolSessionID = null;
        this.userID = null;
    }
    
    public void populateForm(McContent content)
    {
    	String strTitle=null;
    	if (content.getTitle() == null)
    		strTitle="";
    	else
    		strTitle=content.getTitle();
        setTitle(strTitle);
        

        String strContent=null;
        if (content.getContent() == null)
        	strContent="";
        else
        	strContent=content.getContent();
        setContent(strContent);
    }
}
