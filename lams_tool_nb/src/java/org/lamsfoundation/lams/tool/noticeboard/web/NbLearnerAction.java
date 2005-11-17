/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */



/*
 * Created on Jun 29, 2005
 *
 */
package org.lamsfoundation.lams.tool.noticeboard.web;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.web.action.LamsLookupDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.tool.noticeboard.util.NbWebUtil;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;


/**
 * Creation Date: 29-06-05
 *  
 * This class has been created so that when a learner finishes an activity,
 * leaveToolSession() will be called to inform the progress engine
 * that the user has completed this activity.
 * 
 * A note: at the time of writing (11-08-05) a null pointer exception
 * occurs when making a call to leaveToolSession(). Will have to wait until
 * the learner side of things is tested first.
 * 
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/learner" name="NbLearnerForm" scope="session" type="org.lamsfoundation.lams.tool.noticeboard.web.NbLearnerAction"
 *                input=".learnerContent" validate="false" parameter="method"
 * @struts.action-exception key="error.exception.NbApplication" scope="request"
 *                type="org.lamsfoundation.lams.tool.noticeboard.NbApplicationException"
 *                path=".error"
 *                handler="org.lamsfoundation.lams.tool.noticeboard.web.CustomStrutsExceptionHandler"
 * @struts:action-forward name="displayLearnerContent" path=".learnerContent"
 * ----------------XDoclet Tags--------------------
 */
public class NbLearnerAction extends LamsLookupDispatchAction {

    static Logger logger = Logger.getLogger(NbLearnerAction.class.getName());
    
    protected Map getKeyMethodMap()
	{
		Map map = new HashMap();
		map.put(NoticeboardConstants.BUTTON_FINISH, "finish");
		return map;
	}
    
    /** Get the user id from the shared session */
	public Long getUserID(HttpServletRequest request) {
		// set up the user details
		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		if ( user == null )
		{
		    MessageResources resources = getResources(request);
		    String error = resources.getMessage(NoticeboardConstants.ERR_MISSING_PARAM, "User");
		    logger.error(error);
			throw new NbApplicationException(error);
		}
        return new Long(user.getUserID().longValue());
	}

    /**
     * Indicates that the user has finished viewing the noticeboard.
     * The session is set to complete and leaveToolSession is called.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws NbApplicationException, ToolException, DataMissingException, ServletException, IOException {
		
	  NbLearnerForm learnerForm = (NbLearnerForm)form;
	  Long userID = getUserID(request);
	  
	  Long toolSessionID = NbWebUtil.convertToLong(learnerForm.getToolSessionID());
	  if (toolSessionID == null)
	  {
	      String error = "Unable to continue. The parameters tool session id is missing";
	      logger.error(error);
	      throw new NbApplicationException(error);
	  }
	  
	  INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
	  ToolSessionManager sessionMgrService = NoticeboardServiceProxy.getNbSessionManager(getServlet().getServletContext());
		  
      ToolAccessMode mode = WebUtil.getToolAccessMode(learnerForm.getMode());
      if (mode == ToolAccessMode.LEARNER)
	  {
		  NoticeboardSession nbSession = nbService.retrieveNoticeboardSession(toolSessionID);
		  NoticeboardUser nbUser = nbService.retrieveNoticeboardUser(userID);
		  
		  nbUser.setUserStatus(NoticeboardUser.COMPLETED);
		  nbService.updateNoticeboardSession(nbSession);
		  nbService.updateNoticeboardUser(nbUser);
		  
		 /**
		  * TODO: when this method is called, it throws a NullPointerException.
		  * This is an error due to the learner service method completeToolSession(). 
		  * It is not tested yet, however it is left in the code, to indicate that a learner has completed an activity.
		  * 
		  * get the url that is returned from leavetoolsession and redirect to this url
		  */
		  
		  
		  String nextActivityUrl;
			try
			{
				nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, getUserID(request));
			}
			catch (DataMissingException e)
			{
				// TODO Auto-generated catch block
				throw new ServletException(e);
			}
			catch (ToolException e)
			{
				// TODO Auto-generated catch block
				throw new ServletException(e);
			}
	        
			response.sendRedirect(nextActivityUrl);
			
	        return null;
		  
		  
	  }
	  request.getSession().setAttribute(NoticeboardConstants.READ_ONLY_MODE, "true");
	  
	  return mapping.findForward(NoticeboardConstants.DISPLAY_LEARNER_CONTENT);
	  
	}
}
