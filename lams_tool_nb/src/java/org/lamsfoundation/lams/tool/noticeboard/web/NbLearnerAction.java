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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import org.apache.struts.actions.LookupDispatchAction;
import org.lamsfoundation.lams.web.action.LamsLookupDispatchAction;
import org.apache.log4j.Logger;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.tool.noticeboard.util.NbWebUtil;
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
    
    /**
     * Indicates that the user has finished viewing the noticeboard.
     * The session is set to complete and leaveToolSession is called.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws NbApplicationException, ToolException, DataMissingException {
		
	  NbLearnerForm learnerForm = (NbLearnerForm)form;
	  Long toolSessionID = NbWebUtil.convertToLong(learnerForm.getToolSessionId());
	  Long userID = NbWebUtil.convertToLong(learnerForm.getUserId());
	  
	  if (toolSessionID == null || userID == null)
	  {
	      String error = "Unable to continue. The parameters tool session id or user id is missing";
	      logger.error(error);
	      throw new NbApplicationException(error);
	  }
	  INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
	  ToolSessionManager sessionMgrService = NoticeboardServiceProxy.getNbSessionManager(getServlet().getServletContext());
		  
	 	  
	  if (learnerForm.getMode().equals(NoticeboardConstants.TOOL_ACCESS_MODE_LEARNER))
	  {
		  NoticeboardSession nbSession = nbService.retrieveNoticeboardSession(toolSessionID);
		  NoticeboardUser nbUser = nbService.retrieveNoticeboardUser(userID);
		  
		  nbUser.setUserStatus(NoticeboardUser.COMPLETED);
		  nbService.updateNoticeboardSession(nbSession);
		  nbService.updateNoticeboardUser(nbUser);
		  
		  //Notify the progress engine of the user's completion
		  /**
		   * TODO: Find out how to construct the User object that is passed to the method leaveToolSession() 
		   * currently only the userId is set. There is no username or any other information
		   */
		  User user = new User();
		  user.setUserId(new Integer(learnerForm.getUserId().toString()));
		 
		 /**
		  * TODO: when this method is called, it throws a NullPointerException.
		  * This is an error due to the learner service method completeToolSession(). 
		  * It is not tested yet, however it is left in the code, to indicate that a learner has completed an activity.
		  */
		  sessionMgrService.leaveToolSession(NbWebUtil.convertToLong(learnerForm.getToolSessionId()), user);
		  
		  
	  }
	  request.getSession().setAttribute(NoticeboardConstants.READ_ONLY_MODE, "true");
	  
	  return mapping.findForward(NoticeboardConstants.DISPLAY_LEARNER_CONTENT);
	  
	}
}
