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
 * Created on Jul 14, 2005
 *
 */
package org.lamsfoundation.lams.tool.noticeboard.web;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
//import org.apache.struts.actions.LookupDispatchAction;
import org.lamsfoundation.lams.web.action.LamsLookupDispatchAction;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.util.NbWebUtil;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;

/**
 * @author mtruong
 *
 */

/**
 * Creation Date: 14-07-05
 *  
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/monitoring" name="NbMonitoringForm" scope="session" type="org.lamsfoundation.lams.tool.noticeboard.web.NbMonitoringAction"
 *                input=".monitoringContent" validate="false" parameter="method"
 * @struts.action-exception key="error.exception.NbApplication" scope="request"
 *                          type="org.lamsfoundation.lams.tool.noticeboard.NbApplicationException"
 *                          path=".error"
 *                          handler="org.lamsfoundation.lams.tool.noticeboard.web.CustomStrutsExceptionHandler"
 * @struts:action-forward name="monitorPage" path=".monitoringContent"
 * ----------------XDoclet Tags--------------------
 */
public class NbMonitoringAction extends LamsLookupDispatchAction {
    
    static Logger logger = Logger.getLogger(NbMonitoringAction.class.getName());
    
    protected Map getKeyMethodMap()
	{
		Map map = new HashMap();
		map.put(NoticeboardConstants.BUTTON_EDIT_ACTIVITY, "editActivity");
		map.put(NoticeboardConstants.BUTTON_INSTRUCTIONS, "instructions");
		map.put(NoticeboardConstants.BUTTON_STATISTICS, "statistics");
		map.put(NoticeboardConstants.BUTTON_SUMMARY, "summary");
		return map;
	}
    
   /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward editActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        NbMonitoringForm monitorForm = (NbMonitoringForm)form;                
       // Long toolContentId = (Long)request.getSession().getAttribute(NoticeboardConstants.TOOL_CONTENT_ID_INMONITORMODE);
        Long toolContentId = getToolContentId(request);
        INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
		NoticeboardContent content = nbService.retrieveNoticeboard(toolContentId);
		NbWebUtil.copyValuesIntoSession(request, content);
		if (NbWebUtil.isContentEditable(content))
		{
		  //  request.getSession().setAttribute(NoticeboardConstants.CONTENT_IN_USE, "false"); //used in jsp page to allow the edit button to show, so that author can edit page
		    // request.getSession().setAttribute(NoticeboardConstants.DEFINE_LATER, "true");
		    request.setAttribute(NoticeboardConstants.PAGE_EDITABLE, "true");
		    
		    //set up the request parameters to append to the URL
		    Map map = new HashMap();
		    map.put(NoticeboardConstants.TOOL_CONTENT_ID, monitorForm.getToolContentId());
		    map.put(NoticeboardConstants.DEFINE_LATER, "true");
		    
		    monitorForm.setParametersToAppend(map);
		    
		  
		}
		else
		{
		   request.setAttribute(NoticeboardConstants.PAGE_EDITABLE, "false");
		}
				
		return mapping.findForward(NoticeboardConstants.MONITOR_PAGE);
		
    }
    
    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward instructions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
     
        //Long toolContentId = (Long)request.getSession().getAttribute(NoticeboardConstants.TOOL_CONTENT_ID_INMONITORMODE);
        Long toolContentId = getToolContentId(request);
        INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
		NoticeboardContent content = nbService.retrieveNoticeboard(toolContentId);
		NbWebUtil.copyValuesIntoSession(request, content);
		
        return mapping.findForward(NoticeboardConstants.MONITOR_PAGE);
    }
    
    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
       // Long toolContentId = (Long)request.getSession().getAttribute(NoticeboardConstants.TOOL_CONTENT_ID_INMONITORMODE);
        Long toolContentId = getToolContentId(request);
        INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
   		NoticeboardContent content = nbService.retrieveNoticeboard(toolContentId);
   		NbWebUtil.copyValuesIntoSession(request, content);
        
   		return mapping.findForward(NoticeboardConstants.MONITOR_PAGE);
    }
    
    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward statistics(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        
        INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
        Map map = new HashMap();
        Long toolContentId = getToolContentId(request);
        NoticeboardContent content = nbService.retrieveNoticeboard(toolContentId);
        
        //Get the total number of learners that have participated in this tool activity
        int totalNumberOfUsers = nbService.calculateTotalNumberOfUsers(toolContentId);
        request.setAttribute(NoticeboardConstants.TOTAL_LEARNERS, new Integer(totalNumberOfUsers));    
        
        //Now get the number of learners for each individual group (if any)
        List listOfSessionIds = nbService.getSessionIdsFromContent(content);
        Iterator i = listOfSessionIds.iterator();
        int groupNum = 1;
        while (i.hasNext())
        {
           
            Long sessionId = (Long)i.next();
            int numUsersInSession = nbService.getNumberOfUsersInSession(nbService.retrieveNoticeboardSession(sessionId));
            map.put(new Integer(groupNum), new Integer(numUsersInSession));
            groupNum++;
        }
        request.setAttribute(NoticeboardConstants.GROUP_STATS_MAP, map);
        
        return mapping.findForward(NoticeboardConstants.MONITOR_PAGE);
    }
    
    private Long getToolContentId(HttpServletRequest request)
    {
        Long toolContentId = (Long)request.getSession().getAttribute(NoticeboardConstants.TOOL_CONTENT_ID_INMONITORMODE);
        return toolContentId;
    }
    
   
}
