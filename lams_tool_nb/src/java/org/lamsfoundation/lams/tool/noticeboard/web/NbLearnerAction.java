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
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.log4j.Logger;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.usermanagement.User;


/**
 * Creation Date: 29-06-05
 *  
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/tool/nb/learner" name="NbLearnerForm" scope="session" type="org.lamsfoundation.lams.tool.noticeboard.web.NbLearnerAction"
 *                input=".learnerContent" validate="false" parameter="method"
 * @struts:action-forward name="displayLearnerContent" path=".learnerContent"
 * ----------------XDoclet Tags--------------------
 */
public class NbLearnerAction extends LookupDispatchAction {

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
    public ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
	  NbLearnerForm learnerForm = (NbLearnerForm)form;
	  Long toolSessionID = learnerForm.getToolSessionID();
	  Long userID = learnerForm.getUserID();
	  INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
	 /** TODO: learnerServiceProxy causes an exception, fix this up later */
	  // ILearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
	  
	  if (learnerForm.getMode().equals(NoticeboardConstants.TOOL_ACCESS_MODE_LEARNER))
	  {
		  NoticeboardSession nbSession = nbService.retrieveNoticeboardSession(toolSessionID);
		  NoticeboardUser nbUser = nbService.retrieveNoticeboardUser(userID);
		  //do not set session status to complete, as it might be a grouping activity and so you dont know when all the users are finished or not
		  //nbSession.setSessionStatus(NoticeboardSession.COMPLETED);
		  //nbSession.setSessionEndDate(new Date(System.currentTimeMillis()));
		  nbUser.setUserStatus(NoticeboardUser.COMPLETED);
		  nbService.updateNoticeboardSession(nbSession);
		  nbService.updateNoticeboardUser(nbUser);
		  
		  //Notify the progress engine of the user's completion
		  User user = new User();
		  user.setUserId(new Integer(learnerForm.getUserID().toString()));
		  /** TODO: cannot use this yet as learnerservice causes an exception, fix this up later */
		 // learnerService.completeToolSession(learnerForm.getToolSessionID(), user);
		  
	  }
	  request.getSession().setAttribute(NoticeboardConstants.READ_ONLY_MODE, "true");
	  
	  return mapping.findForward(NoticeboardConstants.DISPLAY_LEARNER_CONTENT);
	  
	}
}
