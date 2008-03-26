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


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lams.lams.tool.wiki.WikiApplicationException;
import org.lams.lams.tool.wiki.WikiConstants;
import org.lams.lams.tool.wiki.WikiContent;
import org.lams.lams.tool.wiki.WikiSession;
import org.lams.lams.tool.wiki.WikiUser;
import org.lams.lams.tool.wiki.service.IWikiService;
import org.lams.lams.tool.wiki.service.WikiServiceProxy;
import org.lams.lams.tool.wiki.util.WikiWebUtil;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;


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
 * @struts:action path="/learner" name="WikiLearnerForm" scope="request" type="org.lams.lams.tool.wiki.web.WikiLearnerAction"
 *                input=".learnerContent" validate="false" parameter="method"
 * @struts:action-forward name="displayLearnerContent" path=".learnerContent"
 * @struts:action-forward name="reflectOnActivity" path=".reflectOnActivity"
 * ----------------XDoclet Tags--------------------
 */
public class WikiLearnerAction extends LamsDispatchAction {

    static Logger logger = Logger.getLogger(WikiLearnerAction.class.getName());

    /** Get the user id from the shared session */
	public Long getUserID(HttpServletRequest request) {
		// set up the user details
		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		if ( user == null )
		{
		    MessageResources resources = getResources(request);
		    String error = resources.getMessage(WikiConstants.ERR_MISSING_PARAM, "User");
		    logger.error(error);
			throw new WikiApplicationException(error);
		}
        return new Long(user.getUserID().longValue());
	}

    /**
     * Indicates that the user has finished viewing the wiki.
     * The session is set to complete and leaveToolSession is called.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws WikiApplicationException, ToolException, DataMissingException, ServletException, IOException {
		
	  WikiLearnerForm learnerForm = (WikiLearnerForm)form;
	  Long userID = getUserID(request);
	  
	  Long toolSessionID = WikiWebUtil.convertToLong(learnerForm.getToolSessionID());
	  if (toolSessionID == null)
	  {
	      String error = "Unable to continue. The parameters tool session id is missing";
	      logger.error(error);
	      throw new WikiApplicationException(error);
	  }
	  
	  IWikiService wikiService = WikiServiceProxy.getWikiService(getServlet().getServletContext());
	  ToolSessionManager sessionMgrService = WikiServiceProxy.getWikiSessionManager(getServlet().getServletContext());
		  
      ToolAccessMode mode = WebUtil.getToolAccessMode(learnerForm.getMode());
      if (mode == ToolAccessMode.LEARNER || mode == ToolAccessMode.AUTHOR)
	  {
		  WikiSession wikiSession = wikiService.retrieveWikiSession(toolSessionID);
		  WikiUser wikiUser = wikiService.retrieveWikiUserBySession(userID,toolSessionID);
		  
		  wikiUser.setUserStatus(WikiUser.COMPLETED);
		  wikiService.updateWikiSession(wikiSession);
		  wikiService.updateWikiUser(wikiUser);
		  
		  // Create the notebook entry if reflection is set.
		  WikiContent wikiContent = wikiService.retrieveWikiBySessionID(toolSessionID);
		  if (wikiContent.getReflectOnActivity()) {
				// check for existing notebook entry
				NotebookEntry entry = wikiService.getEntry(toolSessionID,
						CoreNotebookConstants.NOTEBOOK_TOOL,
						WikiConstants.TOOL_SIGNATURE, userID.intValue());

				if (entry == null) {
					// create new entry
					wikiService.createNotebookEntry(toolSessionID,
							CoreNotebookConstants.NOTEBOOK_TOOL,
							WikiConstants.TOOL_SIGNATURE, userID
									.intValue(), learnerForm
									.getReflectionText());
				} else {
					// update existing entry
					entry.setEntry(learnerForm.getReflectionText());
					entry.setLastModified(new Date());
					wikiService.updateEntry(entry);
				}
			}
		  
		  String nextActivityUrl;
			try
			{
				nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, getUserID(request));
			}
			catch (DataMissingException e)
			{
				log.error(e);
				throw new ServletException(e);
			}
			catch (ToolException e)
			{
				log.error(e);
				throw new ServletException(e);
			}
	        
			response.sendRedirect(nextActivityUrl);
			
	        return null;
		  
		  
	  }
	  request.setAttribute(WikiConstants.READ_ONLY_MODE, "true");
	  
	  return mapping.findForward(WikiConstants.DISPLAY_LEARNER_CONTENT);
	  
	}
    
    /**
     * Indicates that the user has finished viewing the wiki, and will be
     * passed onto the Notebook reflection screen.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward reflect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws WikiApplicationException {
    	
    	IWikiService wikiService = WikiServiceProxy.getWikiService(getServlet().getServletContext());
    	
    	WikiLearnerForm learnerForm = (WikiLearnerForm)form;
    	Long toolSessionID = WikiWebUtil.convertToLong(learnerForm.getToolSessionID());
    	WikiContent wikiContent = wikiService.retrieveWikiBySessionID(toolSessionID);
    	request.setAttribute("reflectInstructions", wikiContent.getReflectInstructions());
    	request.setAttribute("title", wikiContent.getTitle());
    	
        // get the existing reflection entry
    	NotebookEntry entry = wikiService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL, WikiConstants.TOOL_SIGNATURE, getUserID(request).intValue());
        if (entry != null) {
        	request.setAttribute("reflectEntry", entry.getEntry());
        }
	  
    	return mapping.findForward(WikiConstants.REFLECT_ON_ACTIVITY);
    }
}
