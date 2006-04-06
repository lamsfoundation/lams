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


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * Creation Date: 12-07-05
 *  
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:form name="NbMonitoringForm" type="org.lamsfoundation.lams.tool.noticeboard.web.NbMonitoringForm"
 *
 * ----------------XDoclet Tags--------------------
 */ 
public class NbMonitoringForm extends ActionForm {

	private static final long serialVersionUID = 6958826482877304278L;


	static Logger logger = Logger.getLogger(NbMonitoringForm.class.getName());
    
       
    private String toolContentID;
    
    private String method;
    
    private Map parametersToAppend;
    
	private String currentTab;

	/**
     * @return Returns the parametersToAppend.
     */
    public Map getParametersToAppend() {
        return parametersToAppend;
    }
    /**
     * @param parametersToAppend The parametersToAppend to set.
     */
    public void setParametersToAppend(Map parametersToAppend) {
        this.parametersToAppend = parametersToAppend;
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
    
    public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		this.method = null;
		this.parametersToAppend = null;		
		
	}
	public String getCurrentTab() {
		return currentTab;
	}
	public void setCurrentTab(String currentTab) {
		this.currentTab = currentTab;
	}
 
}
