/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
package org.lamsfoundation.lams.tool.qa.web;

/* ActionForm for the Monitoring environment */

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.service.IQaService;

/**
 * @author Ozgur Demirtas
 *
 */
public class QaMonitoringForm extends QaAuthoringForm implements QaAppConstants {
	//controls which method is called by the Lookup map */
	protected String method;
	protected String selectedToolSessionId;
	protected String isToolSessionChanged;
	
	protected String editResponse;
	protected String hideResponse;
	protected String showResponse;
	protected String currentUid;
	
	protected String sessionId;
	
	protected IQaService qaService;
	
	
	public String toString() {
	    super.toString();
        return new ToStringBuilder(this)
        	.append("Listing current QaMonitoringForm properties: ")
            .append("method: ", method)
            .append("selectedToolSessionId: ", selectedToolSessionId)
            .append("isToolSessionChanged: ", isToolSessionChanged)
            .append("editResponse: ", editResponse)
            .append("hideResponse: ", hideResponse)
            .append("showResponse: ", showResponse)
            .append("currentUid: ", currentUid)
            .toString();
    }
	
    /**
     * @return Returns the editResponse.
     */
    public String getEditResponse() {
        return editResponse;
    }
    /**
     * @param editResponse The editResponse to set.
     */
    public void setEditResponse(String editResponse) {
        this.editResponse = editResponse;
    }
    /**
     * @return Returns the qaService.
     */
    public IQaService getQaService() {
        return qaService;
    }
    /**
     * @param qaService The qaService to set.
     */
    public void setQaService(IQaService qaService) {
        this.qaService = qaService;
    }
    /**
     * @return Returns the currentUid.
     */
    public String getCurrentUid() {
        return currentUid;
    }
    /**
     * @param currentUid The currentUid to set.
     */
    public void setCurrentUid(String currentUid) {
        this.currentUid = currentUid;
    }
    /**
     * @return Returns the hideResponse.
     */
    public String getHideResponse() {
        return hideResponse;
    }
    /**
     * @param hideResponse The hideResponse to set.
     */
    public void setHideResponse(String hideResponse) {
        this.hideResponse = hideResponse;
    }
    /**
     * @return Returns the showResponse.
     */
    public String getShowResponse() {
        return showResponse;
    }
    /**
     * @param showResponse The showResponse to set.
     */
    public void setShowResponse(String showResponse) {
        this.showResponse = showResponse;
    }
	/**
	 * @return Returns the isToolSessionChanged.
	 */
	public String getIsToolSessionChanged() {
		return isToolSessionChanged;
	}
	/**
	 * @param isToolSessionChanged The isToolSessionChanged to set.
	 */
	public void setIsToolSessionChanged(String isToolSessionChanged) {
		this.isToolSessionChanged = isToolSessionChanged;
	}
	/**
	 * @return Returns the selectedToolSessionId.
	 */
	public String getSelectedToolSessionId() {
		return selectedToolSessionId;
	}
	/**
	 * @param selectedToolSessionId The selectedToolSessionId to set.
	 */
	public void setSelectedToolSessionId(String selectedToolSessionId) {
		this.selectedToolSessionId = selectedToolSessionId;
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
     * @return Returns the sessionId.
     */
    public String getSessionId() {
        return sessionId;
    }
    /**
     * @param sessionId The sessionId to set.
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
