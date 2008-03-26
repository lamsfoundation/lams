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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lams.lams.tool.wiki.WikiApplicationException;
import org.lams.lams.tool.wiki.WikiConstants;
import org.lams.lams.tool.wiki.WikiContent;
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
 * Creation Date: 27-06-05
 * 
 * The learner url can be of three modes learner, teacher or author. Depending on 
 * what mode was set, it will trigger the corresponding action. If the mode parameter
 * is missing or a key is not found in the keymap, it will call the unspecified method
 * which defaults to the learner action. 
 *  
 * <p>The learner action, checks the defineLater and runOffline flags, and if required
 * it will show the learner the message screen indicating a reason why they cant see
 * the contents of the wiki.
 * If none of the flags are set, then the learner is able to see the wiki. 
 * </p>
 * <p>The difference between author mode (which is basically the preview)
 * is that if the defineLater flag is set, it will not be able to see the wiki screen
 * </p>
 * 
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/starter/learner" name="WikiLearnerForm" scope="request" type="org.lams.lams.tool.wiki.web.WikiLearnerStarterAction"
 *               validate="false" parameter="mode"
 * @struts:action-forward name="displayLearnerContent" path=".learnerContent"
 * @struts:action-forward name="displayMessage" path=".message"
 * @struts:action-forward name="defineLater" path=".defineLater"
 * ----------------XDoclet Tags--------------------
 */

public class WikiLearnerStarterAction extends LamsDispatchAction {
    
    static Logger logger = Logger.getLogger(WikiLearnerStarterAction.class.getName());
   
	private UserDTO getUserDTO(HttpServletRequest request) {
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
		return user;
    }
    
    /** Get the user id from the shared session */
	public Long getUserID(HttpServletRequest request) {
		UserDTO user = getUserDTO(request);
        return new Long(user.getUserID().longValue());
	}

    /** Get the user id from the url - needed for the monitoring mode */
	public Long getUserIDFromURLCall(HttpServletRequest request) {
		return WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
    }

    public ActionForward unspecified(
    		ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response)
    {
        
        return learner(mapping, form, request, response);
    }
   
    public ActionForward learner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws WikiApplicationException {

        WikiContent wikiContent = null;
        WikiUser wikiUser = null;
        saveMessages(request, null);
        
        WikiLearnerForm learnerForm = (WikiLearnerForm)form;
      
        ActionMessages message = new ActionMessages();
        IWikiService wikiService = WikiServiceProxy.getWikiService(getServlet().getServletContext());
        
        Long toolSessionID = WikiWebUtil.convertToLong(learnerForm.getToolSessionID());
        
        if (toolSessionID == null)
		{
        	String error = "Unable to continue. The parameters tool session id is missing";
		    logger.error(error);
		    throw new WikiApplicationException(error);
		}

        wikiContent = wikiService.retrieveWikiBySessionID(toolSessionID);
	       // wikiSession = wikiService.retrieveWikiSession(toolSessionId);
	   
	    
	    if(wikiContent == null)
		{
			String error = "An Internal error has occurred. Please exit and retry this sequence";
			logger.error(error);				
			throw new WikiApplicationException(error);
		}   

        if ( isFlagSet(wikiContent, WikiConstants.FLAG_DEFINE_LATER) ) {
            return mapping.findForward(WikiConstants.DEFINE_LATER);
	    }
	    

	    boolean readOnly = false;
        ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE,false);
        Long userID = null;
        if (mode == ToolAccessMode.LEARNER || mode == ToolAccessMode.AUTHOR )
        {
            userID = getUserID(request);
    	    wikiUser = wikiService.retrieveWikiUserBySession(userID, toolSessionID);
            
            if ( ! wikiContent.isContentInUse() ) {
	            /* Set the ContentInUse flag to true, and defineLater flag to false */
	            wikiContent.setContentInUse(true);
	            wikiService.saveWiki(wikiContent);
        	}
        	
            if (wikiUser == null)
            {
            	//create a new user with this session id
            	wikiUser = new WikiUser(userID);
            	UserDTO user = getUserDTO(request);
      		  	wikiUser.setUsername(user.getLogin());
      		  	wikiUser.setFullname(user.getFirstName()+" "+user.getLastName());
            	wikiService.addUser(toolSessionID, wikiUser);
            }
        } else {
        	// user will not exist if force completed.
            userID = getUserIDFromURLCall(request);
    	    wikiUser = wikiService.retrieveWikiUserBySession(userID, toolSessionID);
     		readOnly = true;
        }
        
        learnerForm.copyValuesIntoForm(wikiContent, readOnly, mode.toString());
        
        NotebookEntry notebookEntry = wikiService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL, WikiConstants.TOOL_SIGNATURE, userID.intValue());
        if (notebookEntry != null) {
        	request.setAttribute("reflectEntry", notebookEntry.getEntry());
        }
        request.setAttribute("reflectInstructions", wikiContent.getReflectInstructions());
	    request.setAttribute("reflectOnActivity", wikiContent.getReflectOnActivity());
	    
	    Boolean userFinished = (wikiUser!=null && WikiUser.COMPLETED.equals(wikiUser.getUserStatus()));
        request.setAttribute("userFinished", userFinished);
	    
	    /*
         * Checks to see if the runOffline flag is set.
         * If the particular flag is set, control is forwarded to jsp page
         * displaying to the user the message according to what flag is set.
         */
        if (displayMessageToUser(wikiContent, message))
        {
            saveMessages(request, message);
            return mapping.findForward(WikiConstants.DISPLAY_MESSAGE);
        }
        
        return mapping.findForward(WikiConstants.DISPLAY_LEARNER_CONTENT);
    
    }
    
    public ActionForward teacher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws WikiApplicationException {
        return learner(mapping, form, request, response);
    }
    
    public ActionForward author(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws WikiApplicationException {
        
    	return learner(mapping, form, request, response);

    }
    
    
    
    
    /**
     * <p>Performs a check on the flag indicated by <code>flag</code>
     * In this wiki tool, there are 3 possible flags:
     * <li>defineLater</li>
     * <li>contentInUse</li>
     * <li>runOffline</li>
     * <br>Returns true if the flag is set, false otherwise <p>
     * 
     * @param content The instance of WikiContent
     * @param flag The flag to check, can take the following set of values (defineLater, contentInUse, runOffline)
     * @return Returns true if flag is set, false otherwise
     */
	private boolean isFlagSet(WikiContent content, int flag) throws WikiApplicationException
	{
	    switch (flag)
	    {
	    	case WikiConstants.FLAG_DEFINE_LATER:
	    	    return (content.isDefineLater());
	    	  //  break;
	    	case WikiConstants.FLAG_CONTENT_IN_USE:
	    		return (content.isContentInUse());
	    	//	break;
	    	case WikiConstants.FLAG_RUN_OFFLINE:
	    		return(content.isForceOffline()); 
	    //		break;
	    	default:
	    	    throw new WikiApplicationException("Invalid flag");
	    }
	  
	}
	
	/**
	 * <p> This methods checks the defineLater and runOffline flag. 
	 * If defineLater flag is set, then a message is added to an ActionMessages
	 * object saying that the contents have not been defined yet. If the runOffline 
	 * flag is set, a message is added to ActionMessages saying that the contents
	 * are not being run online. 
	 * This method will return true if any one of the defineLater or runOffline flag is set.
	 * Otherwise false will be returned. </p>
	 * 
	 * @param content The instance of WikiContent
	 * @param message the instance of ActtionMessages
	 * @return true if any of the flags are set, false otherwise
	 */
	private boolean displayMessageToUser(WikiContent content, ActionMessages message)
	{
	    boolean isDefineLaterSet = isFlagSet(content, WikiConstants.FLAG_DEFINE_LATER);
        boolean isRunOfflineSet = isFlagSet(content, WikiConstants.FLAG_RUN_OFFLINE);
        if(isDefineLaterSet || isRunOfflineSet)
        {
            if (isDefineLaterSet)
            {
                message.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.defineLaterSet"));
            }
            if (isRunOfflineSet)
            {
                message.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.runOfflineSet"));
            }
            return true;
        }
        else
            return false;
	}
	
	
 }
