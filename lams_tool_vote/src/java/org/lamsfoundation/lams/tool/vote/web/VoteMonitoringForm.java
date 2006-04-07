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
package org.lamsfoundation.lams.tool.vote.web;


import org.lamsfoundation.lams.tool.vote.VoteAppConstants;

/**
 * @author Ozgur Demirtas
 *
 * ActionForm for the Monitoring environment
 */
public class VoteMonitoringForm extends VoteAuthoringForm implements VoteAppConstants {
	// controls which method is called by the Lookup map */
	protected String method;
	
	protected String selectedToolSessionId;
	
	protected String isToolSessionChanged;
	
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
}
