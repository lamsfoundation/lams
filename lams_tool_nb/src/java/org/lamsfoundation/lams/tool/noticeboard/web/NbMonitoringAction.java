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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.tool.noticeboard.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;
import org.lamsfoundation.lams.tool.noticeboard.util.NbWebUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * @author mtruong
 * 
 * The buttons are a switch between tabs and will forward to a jsp and display
 * the appropriate page.
 * 
 *
 */

/**
 * Creation Date: 14-07-05
 *  
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/monitoring" name="NbMonitoringForm" scope="request" type="org.lamsfoundation.lams.tool.noticeboard.web.NbMonitoringAction"
 *                input=".monitoringContent" validate="false" parameter="method"
 * @struts:action-forward name="monitorPage" path="/monitoring/monitoring.jsp"
 * ----------------XDoclet Tags--------------------
 */
public class NbMonitoringAction extends LamsDispatchAction {
    
    static Logger logger = Logger.getLogger(NbMonitoringAction.class.getName());
    
    public final static String FORM="NbMonitoringForm";
    
    public static final String SUMMARY_TABID = "1";
    public static final String INSTRUCTIONS_TABID = "2";
    public static final String EDITACTIVITY_TABID = "3";
    public static final String STATISTICS_TABID = "4";
   
    public ActionForward unspecified(
    		ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response) throws NbApplicationException
    {
    	Long toolContentId = NbWebUtil.convertToLong(request.getParameter(NoticeboardConstants.TOOL_CONTENT_ID));
        String contentFolderID = WebUtil.readStrParam(request, NoticeboardConstants.CONTENT_FOLDER_ID);
    	if (toolContentId == null)
 		{
 		    String error = "Unable to continue. Tool content id missing";
 		    logger.error(error);
 			throw new NbApplicationException(error);
 		}
         
        NbMonitoringForm monitorForm = new NbMonitoringForm();

        INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
		NoticeboardContent content = nbService.retrieveNoticeboard(toolContentId);

    	monitorForm.setTitle(content.getTitle());
    	monitorForm.setContent(content.getContent());
    	monitorForm.setOnlineInstructions(content.getOnlineInstructions());
    	monitorForm.setOfflineInstructions(content.getOfflineInstructions());
		monitorForm.setAttachmentsList(NbWebUtil.setupAttachmentList(nbService, content));

		if ( NbWebUtil.isContentEditable(content) ) {
			monitorForm.setContentEditable("true");
		    //set up the request parameters to append to the URL
		    Map<String,Object> map = new HashMap<String,Object>();
		    map.put(NoticeboardConstants.TOOL_CONTENT_ID, toolContentId.toString());
		    map.put(NoticeboardConstants.DEFINE_LATER, "true");
		    map.put(NoticeboardConstants.CONTENT_FOLDER_ID, contentFolderID);
		    monitorForm.setParametersToAppend(map);
		} else {
			monitorForm.setContentEditable("false");
		}
		
        //Get the total number of learners that have participated in this tool activity
        monitorForm.setTotalLearners(nbService.calculateTotalNumberOfUsers(toolContentId));
        
        Set sessions = content.getNbSessions();
        Iterator i = sessions.iterator();
        Map map = new HashMap();
        while (i.hasNext())
        {
        	NoticeboardSession session = (NoticeboardSession) i.next();
            int numUsersInSession = nbService.getNumberOfUsersInSession(session);
            map.put(session.getNbSessionName(), new Integer(numUsersInSession));
        }
        monitorForm.setGroupStatsMap(map);
        
   		monitorForm.setCurrentTab(SUMMARY_TABID);
        request.setAttribute(FORM, monitorForm);
   		return mapping.findForward(NoticeboardConstants.MONITOR_PAGE);
    }
	    
   
   
}
