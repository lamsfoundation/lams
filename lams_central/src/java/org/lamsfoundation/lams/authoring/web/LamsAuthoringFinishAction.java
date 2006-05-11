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

/* $Id$ */
package org.lamsfoundation.lams.authoring.web;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This action class does some process when author try to save/cancel/close authoring tool pages.
 * If author try to save tool page, this action will redirct tool page to confirm page and execute clearSession() method.
 * If author try to cancel/close window, this action will execute clearSession().
 *  
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public abstract class LamsAuthoringFinishAction extends Action {
	 private static Logger log = Logger.getLogger(LamsAuthoringFinishAction.class);
	 
	private static final String ACTION_NAME = "action";
	private static final String ACTION_MODE = "mode";
	private static final String TOOL_SIGNATURE = "signature";
	
	private static final String CONFIRM_ACTION = "confirm";
	private static final String CANCEL_ACTION = "cancel";

	private static final String RE_EDIT_URL = "reEditUrl";

	
	/**
	 * Action method, will handle save/cancel action.
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String action = request.getParameter(ACTION_NAME);
		String modeStr = request.getParameter(ACTION_MODE);
		if(StringUtils.equals(ToolAccessMode.AUTHOR.toString(),modeStr))
			clearSession(request.getSession(),ToolAccessMode.AUTHOR);
		if(StringUtils.equals(ToolAccessMode.LEARNER.toString(),modeStr))
			clearSession(request.getSession(),ToolAccessMode.LEARNER);
		if(StringUtils.equals(ToolAccessMode.TEACHER.toString(),modeStr))
			clearSession(request.getSession(),ToolAccessMode.TEACHER);
			
		if(StringUtils.equals(action,CONFIRM_ACTION)){
			String nextUrl = getLamsUrl() + "authoringConfirm.jsp";
			
			String signature = request.getParameter(TOOL_SIGNATURE);
			Long toolContentId = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
			IToolVO tool = getToolService().getToolBySignature(signature);
			String reeditUrl = WebUtil.appendParameterToURL(getLamsUrl()+tool.getAuthorUrl(), AttributeNames.PARAM_TOOL_CONTENT_ID,
					toolContentId.toString());
			
			nextUrl = WebUtil.appendParameterToURL(nextUrl,RE_EDIT_URL,URLEncoder.encode(reeditUrl,"UTF-8"));
			response.sendRedirect(nextUrl);
		}
		if(StringUtils.equals(action,CANCEL_ACTION)){
		}
		return null;
	}
	/**
	 * All subclass will implements this method and execute clear <code>HttpSession</code> action to 
	 * remove obsolete session values.
	 * 
	 * @param session
	 * @param mode ToolAccessMode to decide which role's session will be clear.
	 */
	abstract public void clearSession(HttpSession session, ToolAccessMode mode);
	
	private String getLamsUrl(){
		String serverURL = Configuration.get(ConfigurationKeys.SERVER_URL);
		
   		if ( StringUtils.isBlank(serverURL) ) {
	   		log.warn("ServerURLTag unable to write out server URL as it is missing from the configuration file.");
   		}
   		
   		return serverURL;
	}
	
	
	public ILamsToolService getToolService(){
		WebApplicationContext webContext = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServlet().getServletContext());
		return (ILamsToolService) webContext.getBean(AuthoringConstants.TOOL_SERVICE_BEAN_NAME);		
	}
}
