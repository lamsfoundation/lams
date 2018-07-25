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

package org.lamsfoundation.lams.tool.qa.web.form;

/* ActionForm for the Monitoring environment */

import org.lamsfoundation.lams.tool.qa.QaAppConstants;

/**
 * @author Ozgur Demirtas
 *
 */
public class QaMonitoringForm extends QaAuthoringForm implements QaAppConstants {
    //controls which method is called by the Lookup map */
    protected String method;

    protected String hideResponse;
    protected String showResponse;
    protected String currentUid;

    protected String sessionId;

    /**
     * @return Returns the currentUid.
     */
    public String getCurrentUid() {
	return currentUid;
    }

    /**
     * @param currentUid
     *            The currentUid to set.
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
     * @param hideResponse
     *            The hideResponse to set.
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
     * @param showResponse
     *            The showResponse to set.
     */
    public void setShowResponse(String showResponse) {
	this.showResponse = showResponse;
    }

    /**
     * @return Returns the method.
     */
    @Override
    public String getMethod() {
	return method;
    }

    /**
     * @param method
     *            The method to set.
     */
    @Override
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
     * @param sessionId
     *            The sessionId to set.
     */
    public void setSessionId(String sessionId) {
	this.sessionId = sessionId;
    }
}
