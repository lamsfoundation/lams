//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.2/xslt/JavaClass.xsl

package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.*;

import java.util.*;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;

import org.lamsfoundation.lams.learningdesign.*;
import org.lamsfoundation.lams.lesson.*;
import org.lamsfoundation.lams.learning.web.util.ActionMappings;

/** 
 * Action class to forward the user to a Tool.
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/DisplayToolActivity" name="activityForm"
 *                validate="false" scope="request"
 * 
 */
public class DisplayToolActivity extends ActivityAction {

	/**
	 * Gets a tool activity from the request (attribute) and uses a redirect
	 * to forward the user to the tool.
	 */
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response) {
		ActivityForm form = (ActivityForm)actionForm;
		ActionMappings actionMappings = getActionMappings();
		
		LearnerProgress learnerProgress = getLearnerProgress(request, form);
		Activity activity = getActivity(request, form, learnerProgress);
		if (!(activity instanceof ToolActivity)) {
		    log.error(className+": activity not ToolActivity");
			return mapping.findForward(actionMappings.ERROR);
		}
		
		ToolActivity toolActivity = (ToolActivity)activity;

		String url = actionMappings.getToolURL(toolActivity, learnerProgress);
		try {
		    response.sendRedirect(url);
		}
		catch (java.io.IOException e) {
		    return mapping.findForward(actionMappings.ERROR);
		}
		return null;
	}

}