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
    
	private static final long serialVersionUID = -669035956406254875L;

	static Logger logger = Logger.getLogger(NbLearnerForm.class.getName());
    
    private String title;
    
    private String content;
    
    private String toolSessionID;
    
    private String method;
    
    private String mode;
    
    
    /**
     * @return Returns the toolSessionID.
     */
    public String getToolSessionID() {
        return toolSessionID;
    }
    /**
     * @param toolSessionID The toolSessionID to set.
     */
    public void setToolSessionID(String toolSessionID) {
        this.toolSessionID = toolSessionID;
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
		this.toolSessionID = null;
		this.method = null;
		this.mode = null;
			
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
