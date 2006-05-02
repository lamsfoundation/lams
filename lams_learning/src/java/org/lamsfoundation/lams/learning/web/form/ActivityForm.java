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
package org.lamsfoundation.lams.learning.web.form;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;

/** 
 * @author daveg
 * 
 * XDoclet definition:
 * @struts:form name="activityForm"
 */
public class ActivityForm extends ActionForm {

	/** Unique identifier specifying the session for this activity, maps back to
	 * LearnerProgress (or Learner) and Activity. Note that the activity may already
	 * be complete.
	 */
	private Long activityId;
		
	/** List of ActivityURL, will only contain one if a simple activity */
	private List activityURLs;
	
	/** Progress summary suitable for Flash. In the form attempted=13_14&completed=10_11&current=12.*/
	private String progressSummary;

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		activityURLs = null;
	}

	public List getActivityURLs() {
		return activityURLs;
	}
	public void setActivityURLs(List activityURLs) {
		this.activityURLs = activityURLs;
	}
	public Long getActivityId() {
		return activityId;
	}
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	
	public void addActivityURL(ActivityURL activityUrl)
	{
	    if(this.activityURLs ==null)
	        this.activityURLs = new ArrayList();
	    this.activityURLs.add(activityUrl);	    
	}

	public String getProgressSummary() {
		return progressSummary;
	}

	public void setProgressSummary(String progressSummary) {
		this.progressSummary = progressSummary;
	}
}