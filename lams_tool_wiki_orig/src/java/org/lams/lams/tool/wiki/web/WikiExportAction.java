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
import org.lamsfoundation.lams.web.action.LamsDispatchAction;


/**
 * @author mtruong
 *
 * Export Portfolio functionality.
 * 
 * With this wiki tool,
 * both the learner and teacher will export the contents of the wiki.
 * 
 */

/**
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/exportPortfolio" name="WikiExportForm" scope="request" type="org.lams.lams.tool.wiki.web.WikiExportAction"
 *                validate="false" parameter="mode"
 * @struts:action-forward name="exportPortfolio" path="/exportPortfolio.jsp"
 * ----------------XDoclet Tags--------------------
 */
public class WikiExportAction extends LamsDispatchAction {
    
    static Logger logger = Logger.getLogger(WikiExportForm.class.getName());

    public ActionForward unspecified(
    		ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response)
    {
        return mapping.findForward(WikiConstants.EXPORT_PORTFOLIO);
    }
    
    public ActionForward learner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    {
       //parameters given are the toolSessionId and userId
        WikiExportForm exportForm = (WikiExportForm)form;
        Long toolSessionId = WikiWebUtil.convertToLong(request.getParameter(WikiConstants.TOOL_SESSION_ID));
        Long userId = WikiWebUtil.convertToLong(request.getParameter(WikiConstants.USER_ID));
      
        
        IWikiService wikiService = WikiServiceProxy.getWikiService(getServlet().getServletContext());
        
        if (userId == null || toolSessionId == null)
        {
            String error = "Tool session Id or user Id is null. Unable to continue";
            logger.error(error);
            throw new WikiApplicationException(error);
        }
        
        WikiUser userInThisSession = wikiService.retrieveWikiUserBySession(userId, toolSessionId);
        
        if (userInThisSession == null)
        {
            String error="The user with user id " + userId + " does not exist in this session or session may not exist.";
            logger.error(error);
            throw new WikiApplicationException(error);
        }
        
       WikiContent content = wikiService.retrieveWikiBySessionID(toolSessionId);
        
        if (content == null)
        {
            String error="The content for this activity has not been defined yet.";
            logger.error(error);
            throw new WikiApplicationException(error);
        }
        
        // Get user's reflection if exists
        if (content.getReflectOnActivity()) {
        	log.debug(content.getReflectOnActivity());
        	request.setAttribute("learner", true);
        	NotebookEntry wikiEntry = wikiService.getEntry(userInThisSession.getWikiSession().getWikiSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL, 
    			WikiConstants.TOOL_SIGNATURE, userId.intValue());
        	log.debug(wikiEntry);
        	if (wikiEntry!=null) {
        		request.setAttribute("wikiEntry", wikiEntry.getEntry());
        	}
        }
               
        exportForm.populateForm(content);
       
        return mapping.findForward(WikiConstants.EXPORT_PORTFOLIO);
    }
    
    public ActionForward teacher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    {
        //given the toolcontentId as a parameter
        WikiExportForm exportForm = (WikiExportForm)form;
        IWikiService wikiService = WikiServiceProxy.getWikiService(getServlet().getServletContext());
        
        Long toolContentId = WikiWebUtil.convertToLong(request.getParameter(WikiConstants.TOOL_CONTENT_ID));
       
        //check if toolContentId exists in db or not
        if (toolContentId==null)
        {
            String error="Tool Content Id is missing. Unable to continue";
            logger.error(error);
            throw new WikiApplicationException(error);
        }
        
        WikiContent content = wikiService.retrieveWiki(toolContentId);
        
        if (content == null)
        {
            String error="Data is missing from the database. Unable to Continue";
            logger.error(error);
            throw new WikiApplicationException(error);
        }
        
        // Get class's reflections if exists
        if (content.getReflectOnActivity()) {
        	Set sessions = content.getWikiSessions();
            Iterator i = sessions.iterator();
            List<ReflectionDTO> reflections = new ArrayList<ReflectionDTO>();
            while (i.hasNext())
            {
            	WikiSession session = (WikiSession) i.next();
                List sessionUsers = wikiService.getUsersBySession(session.getWikiSessionId());
                for (int j=0; j<sessionUsers.size(); j++) {
                	WikiUser wikiUser = (WikiUser)sessionUsers.get(j);
                	NotebookEntry wikiEntry = wikiService.getEntry(wikiUser.getWikiSession().getWikiSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL, 
                   			WikiConstants.TOOL_SIGNATURE, wikiUser.getUserId().intValue());
                	log.debug(wikiEntry);
                	if (wikiEntry!=null) {
                		ReflectionDTO dto = new ReflectionDTO(wikiEntry);
                		dto.setFullName(wikiUser.getFullname());
                		reflections.add(dto);
                	}
                }
            }
            request.setAttribute("reflections", reflections);
        }
        
        exportForm.populateForm(content);
  
   		return mapping.findForward(WikiConstants.EXPORT_PORTFOLIO);
    }
    
    
}
