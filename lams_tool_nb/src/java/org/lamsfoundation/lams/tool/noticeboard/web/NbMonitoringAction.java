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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.util.NbMonitoringUtil;
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
 * @struts:action path="/tool/nb/monitoring" name="NbMonitoringForm" scope="session" type="org.lamsfoundation.lams.tool.noticeboard.web.NbMonitoringAction"
 *                input=".monitoringContent" validate="false" parameter="method"
 * @struts:action-forward name="monitorPage" path=".monitoringContent"
 * ----------------XDoclet Tags--------------------
 */
public class NbMonitoringAction extends LookupDispatchAction {
    
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
                
        Long toolContentId = (Long)request.getSession().getAttribute(NoticeboardConstants.TOOL_CONTENT_ID_INMONITORMODE);
        INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
		NoticeboardContent content = nbService.retrieveNoticeboard(toolContentId);
		
		if (isContentEditable(content))
		{
		    request.getSession().removeAttribute(NoticeboardConstants.CONTENT_IN_USE);
		    
		    request.getSession().setAttribute(NoticeboardConstants.TITLE, content.getTitle());
	        request.getSession().setAttribute(NoticeboardConstants.CONTENT, content.getContent());
		}
		else
		{
		   request.getSession().setAttribute(NoticeboardConstants.CONTENT_IN_USE, "true");
		}
		//copyValuesIntoSession(request, content);
		
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
        NbMonitoringForm monitorForm = (NbMonitoringForm)form;
       // NbMonitoringUtil.cleanSession(request);
        Long toolContentId = (Long)request.getSession().getAttribute(NoticeboardConstants.TOOL_CONTENT_ID_INMONITORMODE);
        INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
		NoticeboardContent content = nbService.retrieveNoticeboard(toolContentId);
		//copyValuesIntoSession(request, content);
		
		request.getSession().setAttribute(NoticeboardConstants.ONLINE_INSTRUCTIONS, content.getOnlineInstructions());
        request.getSession().setAttribute(NoticeboardConstants.OFFLINE_INSTRUCTIONS, content.getOfflineInstructions());
        
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
    private void copyValuesIntoSession(HttpServletRequest request, NoticeboardContent content)
    {
        request.getSession().setAttribute(NoticeboardConstants.TITLE, content.getTitle());
        request.getSession().setAttribute(NoticeboardConstants.CONTENT, content.getContent());
        request.getSession().setAttribute(NoticeboardConstants.ONLINE_INSTRUCTIONS, content.getOnlineInstructions());
        request.getSession().setAttribute(NoticeboardConstants.OFFLINE_INSTRUCTIONS, content.getOfflineInstructions());
    }
    
    /**
     * <p> This method checks the two tool content flags, defineLater and contentInUse
     * to determine whether the tool content is modifiable or not. Returns true is content is
     * modifiable and false otherwise 
     * <br>Tool content is modifiable if:
     * <li>defineLater is set to true</li>
     * <li>defineLater is set to false and contentInUse is set to false</li>
     * <br>Tool content is not modifiable if:
     * <li>contentInUse is set to true</li></p>
     * @param content The instance of NoticeboardContent to check
     * @return true if content is modifiable and false otherwise
     * @throws NbApplicationException
     */
    private boolean isContentEditable(NoticeboardContent content) throws NbApplicationException
    {
        if ( (content.isDefineLater() == true) && (content.isContentInUse()==true) )
        {
            throw new NbApplicationException("An exception has occurred: There is a bug in this tool, conflicting flags are set");
                    //return false;
        }
        else if ( (content.isDefineLater() == true) && (content.isContentInUse() == false))
            return true;
        else if ( (content.isDefineLater() == false) && (content.isContentInUse() == false))
            return true;
        else //  (content.isContentInUse()==true && content.isDefineLater() == false)
            return false;
        
     }

}
