/*
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
*/

package org.lamsfoundation.lams.learning.web.form;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import org.apache.struts.action.ActionErrors;
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
	

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(
		ActionMapping mapping,
		HttpServletRequest request) {

		throw new UnsupportedOperationException("Generated method 'validate(...)' not implemented.");
	}

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
}