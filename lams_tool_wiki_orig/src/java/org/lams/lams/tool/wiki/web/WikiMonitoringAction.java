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
package org.lams.lams.tool.wiki.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lams.lams.tool.wiki.WikiApplicationException;
import org.lams.lams.tool.wiki.WikiConstants;
import org.lams.lams.tool.wiki.WikiContent;
import org.lams.lams.tool.wiki.WikiSession;
import org.lams.lams.tool.wiki.WikiUser;
import org.lams.lams.tool.wiki.dto.ReflectionDTO;
import org.lams.lams.tool.wiki.service.IWikiService;
import org.lams.lams.tool.wiki.service.WikiServiceProxy;
import org.lams.lams.tool.wiki.util.WikiWebUtil;
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
 * @struts:action path="/monitoring" name="WikiMonitoringForm" scope="request" type="org.lams.lams.tool.wiki.web.WikiMonitoringAction"
 *                input=".monitoringContent" validate="false" parameter="method"
 * @struts:action-forward name="monitorPage" path="/monitoring/monitoring.jsp"
 * @struts:action-forward name="monitorReflectionPage" path=".monitorReflectionPage"
 * ----------------XDoclet Tags--------------------
 */
public class WikiMonitoringAction extends LamsDispatchAction {
    
    static Logger logger = Logger.getLogger(WikiMonitoringAction.class.getName());
    
    public final static String FORM="WikiMonitoringForm";
    
    public static final String SUMMARY_TABID = "1";
    public static final String INSTRUCTIONS_TABID = "2";
    public static final String EDITACTIVITY_TABID = "3";
    public static final String STATISTICS_TABID = "4";
   
    public ActionForward unspecified(
    		ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response) throws WikiApplicationException
    {
    	Long toolContentId = WikiWebUtil.convertToLong(request.getParameter(WikiConstants.TOOL_CONTENT_ID));
        String contentFolderID = WebUtil.readStrParam(request, WikiConstants.CONTENT_FOLDER_ID);
    	if (toolContentId == null)
 		{
 		    String error = "Unable to continue. Tool content id missing";
 		    logger.error(error);
 			throw new WikiApplicationException(error);
 		}
         
        WikiMonitoringForm monitorForm = new WikiMonitoringForm();

        IWikiService wikiService = WikiServiceProxy.getWikiService(getServlet().getServletContext());
		WikiContent content = wikiService.retrieveWiki(toolContentId);

    	monitorForm.setTitle(content.getTitle());
    	monitorForm.setContent(content.getContent());
    	monitorForm.setOnlineInstructions(content.getOnlineInstructions());
    	monitorForm.setOfflineInstructions(content.getOfflineInstructions());
		monitorForm.setAttachmentsList(WikiWebUtil.setupAttachmentList(wikiService, content));

		monitorForm.setContentEditable("true");
	    //set up the request parameters to append to the URL
	    Map<String,Object> mapParameters = new HashMap<String,Object>();
	    mapParameters.put(WikiConstants.TOOL_CONTENT_ID, toolContentId.toString());
	    mapParameters.put(WikiConstants.DEFINE_LATER, "true");
	    mapParameters.put(WikiConstants.CONTENT_FOLDER_ID, contentFolderID);
	    monitorForm.setParametersToAppend(mapParameters);
		
        //Get the total number of learners that have participated in this tool activity
        monitorForm.setTotalLearners(wikiService.calculateTotalNumberOfUsers(toolContentId));
        
        Set sessions = content.getWikiSessions();
        Iterator i = sessions.iterator();
        Map map = new HashMap();
        List<ReflectionDTO> reflections = new ArrayList<ReflectionDTO>();
        while (i.hasNext())
        {
        	WikiSession session = (WikiSession) i.next();
            int numUsersInSession = wikiService.getNumberOfUsersInSession(session);
            map.put(session.getWikiSessionName(), new Integer(numUsersInSession));
            // Get list of users that have made a reflection entry 
            if (content.getReflectOnActivity()) {
            	List sessionUsers = wikiService.getUsersBySession(session.getWikiSessionId());
            	for (int j=0; j<sessionUsers.size(); j++) {
            		WikiUser wikiUser = (WikiUser)sessionUsers.get(j);
            		NotebookEntry wikiEntry = wikiService.getEntry(session.getWikiSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL, 
                			WikiConstants.TOOL_SIGNATURE, wikiUser.getUserId().intValue());
            		if (wikiEntry!=null) {
            			ReflectionDTO dto = new ReflectionDTO(wikiEntry);
            			dto.setExternalId(session.getWikiSessionId());
            			dto.setUserId(wikiUser.getUserId());
            			dto.setUsername(wikiUser.getUsername());
            			reflections.add(dto);
            		}
            	}
            }
        }
        monitorForm.setGroupStatsMap(map);
        
        // Set reflection statistics, if reflection is set
        request.setAttribute("reflectOnActivity", content.getReflectOnActivity());
        request.setAttribute("reflections", reflections);
        
   		monitorForm.setCurrentTab(SUMMARY_TABID);
        request.setAttribute(FORM, monitorForm);
   		return mapping.findForward(WikiConstants.MONITOR_PAGE);
    }
	    
    public ActionForward viewReflection (
    		ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response) throws WikiApplicationException
    {
    	Long userId = WikiWebUtil.convertToLong(request.getParameter(WikiConstants.USER_ID));
    	Long toolSessionId = WikiWebUtil.convertToLong(request.getParameter(WikiConstants.TOOL_SESSION_ID));
    	IWikiService wikiService = WikiServiceProxy.getWikiService(getServlet().getServletContext());
    	WikiUser wikiUser = wikiService.retrieveWikiUser(userId, toolSessionId);
    	NotebookEntry wikiEntry = wikiService.getEntry(wikiUser.getWikiSession().getWikiSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL, 
    			WikiConstants.TOOL_SIGNATURE, userId.intValue());
    	if (wikiEntry!=null) {
    		request.setAttribute("wikiEntry", wikiEntry.getEntry());
    		request.setAttribute("name", wikiUser.getFullname());
    	}
    	
    	return mapping.findForward(WikiConstants.MONITOR_REFLECTION_PAGE);
    }
   
}
