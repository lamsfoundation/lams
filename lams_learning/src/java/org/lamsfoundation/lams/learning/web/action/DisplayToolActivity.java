//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.2/xslt/JavaClass.xsl

package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.*;
import java.util.*;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
import org.lamsfoundation.lams.learning.web.util.Utils;

import org.lamsfoundation.lams.learningdesign.*;
import org.lamsfoundation.lams.lesson.*;

/** 
 * MyEclipse Struts
 * Creation date: 01-12-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/DisplayToolActivity" name="activityForm"
 *                validate="false" scope="request"
 * 
 * -- Load one or more URLs to display an activity
 * @struts:action-forward name="displayTool" path=".toolActivity"
 * 
 */
public class DisplayToolActivity extends DisplayActivity {
		
	/**
	 * Returns an ActionForward to display an activity based on its type. The form bean
	 * also has its values set for display. Note that this method is over-ridden by the
	 * DisplayOptionsActivity sub-class.
	 */
	protected ActionForward displayActivity(Activity activity, LearnerProgress progress,
			ActionMapping mapping, ActivityForm form, HttpServletRequest request, HttpServletResponse response) {

		if (!(activity instanceof ToolActivity)) {
			// error
			return mapping.findForward("error");
		}
		ToolActivity toolActivity = (ToolActivity)activity;
		
		List activityURLs = new ArrayList();
		String forward = null;

		forward = "displayTool";

		form.setActivityId(activity.getActivityId());
		//ActivityURL url = Utils.generateActivityURL(activity, progress);
		ActivityURL url = Utils.getToolURL(toolActivity, progress);
		activityURLs.add(url);
		form.setActivityURLs(activityURLs);
		
		return mapping.findForward(forward);
	}

}