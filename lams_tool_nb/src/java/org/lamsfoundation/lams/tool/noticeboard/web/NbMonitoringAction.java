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
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardAttachment;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.util.NbWebUtil;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;

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
 * @struts:action path="/monitoring" name="NbMonitoringForm" scope="session" type="org.lamsfoundation.lams.tool.noticeboard.web.NbMonitoringAction"
 *                input=".monitoringContent" validate="false" parameter="method"
 * @struts.action-exception key="error.exception.NbApplication" scope="request"
 *                          type="org.lamsfoundation.lams.tool.noticeboard.NbApplicationException"
 *                          path=".error"
 *                          handler="org.lamsfoundation.lams.tool.noticeboard.web.CustomStrutsExceptionHandler"
 * @struts.action-exception key="error.exception.NbApplication" scope="request"
 *                          type="java.lang.NullPointerException"
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
     * If no method parameter, or an unknown key, it will default to displaying
     * the summary page.
     */
    public ActionForward unspecified(
    		ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response) throws NbApplicationException
    {
        return summary(mapping, form, request, response);
    }
  
    /**
     * Will forward to the jsp
	 * and will display the edit activity page, which shows the content of the noticeboard
	 * and will show an edit button which allows an author to modify the noticeboard content.
	 * When this edit button is clicked, it appends defineLater=true to the authoring URL.
	 * However, if the contents is not editable (ie. the contents are in use which means a learner 
	 * already reached the activity) it will display a message saying so.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward editActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        
        NbMonitoringForm monitorForm = (NbMonitoringForm)form;                
      
        INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
        Long toolContentId = NbWebUtil.convertToLong(monitorForm.getToolContentId());
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
     * Will forward to the jsp
	 * and will display the instructions page, which will just show the online and 
	 * offline instructions and also the files that have been uploaded (view only mode).
	 * The attachment map is setup again in case there were changes made from the 
	 * first time the monitoring url was called.
	 * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward instructions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
     
        //Long toolContentId = (Long)request.getSession().getAttribute(NoticeboardConstants.TOOL_CONTENT_ID_INMONITORMODE);
       // Long toolContentId = getToolContentId(request);
        
        INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
        NbMonitoringForm monitorForm = (NbMonitoringForm)form;
        Long toolContentId = NbWebUtil.convertToLong(monitorForm.getToolContentId());
		NoticeboardContent content = nbService.retrieveNoticeboard(toolContentId);
		NbWebUtil.copyValuesIntoSession(request, content);
		
		request.setAttribute(NoticeboardConstants.ONLINE_INSTRUCTIONS, content.getOnlineInstructions());
		
		Map attachmentMap = monitorForm.getAttachments();
	        
	        List attachmentIdList = nbService.getAttachmentIdsFromContent(content);
			for (int i=0; i<attachmentIdList.size(); i++)
			{
			    NoticeboardAttachment file = nbService.retrieveAttachment((Long)attachmentIdList.get(i));
			    String fileType = file.returnFileType();
			    String keyName = file.getFilename() + "-" + fileType;
			    attachmentMap.put(keyName, file);
			}
			monitorForm.setAttachments(attachmentMap);
			NbWebUtil.addUploadsToSession(request, attachmentMap);
		
		
        return mapping.findForward(NoticeboardConstants.MONITOR_PAGE);
    }
    
    /**
	 * Will forward to the jsp
	 * and will display the summary page, which will show the contents of 
	 * noticeboard.
	 * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
       // Long toolContentId = (Long)request.getSession().getAttribute(NoticeboardConstants.TOOL_CONTENT_ID_INMONITORMODE);
       // Long toolContentId = getToolContentId(request);
        INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
        NbMonitoringForm monitorForm = (NbMonitoringForm)form;
        Long toolContentId = NbWebUtil.convertToLong(monitorForm.getToolContentId());
   		NoticeboardContent content = nbService.retrieveNoticeboard(toolContentId);
   		NbWebUtil.copyValuesIntoSession(request, content);
        
   		return mapping.findForward(NoticeboardConstants.MONITOR_PAGE);
    }
    
    /**
     * Will forward to the jsp
	 * and will display the statistics page which shows the number of users that
	 * have viewed this noticeboard. If grouping is applied, then the number of learners
	 * will also be sorted into groups.
	 * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward statistics(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        
        INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
        Map map = new HashMap();
       // Long toolContentId = getToolContentId(request);
        NbMonitoringForm monitorForm = (NbMonitoringForm)form;
        Long toolContentId = NbWebUtil.convertToLong(monitorForm.getToolContentId());
        
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
    
   
   
}
