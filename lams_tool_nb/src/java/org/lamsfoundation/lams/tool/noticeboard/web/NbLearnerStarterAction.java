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
 * Created on Jun 27, 2005
 *
 */

/**
 * TODO: internationalisation of error messages.
 */

package org.lamsfoundation.lams.tool.noticeboard.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
//import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;
import org.lamsfoundation.lams.tool.noticeboard.web.NbLearnerForm;

import org.lamsfoundation.lams.tool.noticeboard.util.NbWebUtil;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessage;
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
 * the contents of the noticeboard.
 * If none of the flags are set, then the learner is able to see the noticeboard. 
 * </p>
 * <p>The difference between author mode (which is basically the preview)
 * is that if the defineLater flag is set, it will not be able to see the noticeboard screen
 * </p>
 * 
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/starter/learner" name="NbLearnerForm" scope="session" type="org.lamsfoundation.lams.tool.noticeboard.web.NbLearnerStarterAction"
 *               validate="false" parameter="mode"
 * @struts.action-exception key="error.exception.NbApplication" scope="request"
 *                          type="org.lamsfoundation.lams.tool.noticeboard.NbApplicationException"
 *                          path=".error"
 *                          handler="org.lamsfoundation.lams.tool.noticeboard.web.CustomStrutsExceptionHandler"
 * @struts:action-forward name="displayLearnerContent" path=".learnerContent"
 * @struts:action-forward name="displayMessage" path=".message"
 * ----------------XDoclet Tags--------------------
 */

public class NbLearnerStarterAction extends LamsDispatchAction {
    
    static Logger logger = Logger.getLogger(NbLearnerStarterAction.class.getName());
       
    public ActionForward unspecified(
    		ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response)
    {
        
        return learner(mapping, form, request, response);
    }
   
    public ActionForward learner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws NbApplicationException {

        NoticeboardContent nbContent = null;
        NoticeboardUser nbUser = null;
        NbWebUtil.cleanLearnerSession(request);
        saveMessages(request, null);
        
        NbLearnerForm learnerForm = (NbLearnerForm)form;
      
        ActionMessages message = new ActionMessages();
        INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
        
        Long userId = NbWebUtil.convertToLong(learnerForm.getUserID());
        Long toolSessionId = NbWebUtil.convertToLong(learnerForm.getToolSessionID());
       // Long toolContentId = NbWebUtil.convertToLong(learnerForm.getToolContentId());
        
        if(userId == null || toolSessionId ==null)
		{
			String error = "Unable to continue. The user ID or tool session ID";
			logger.error(error);
			throw new NbApplicationException(error);
		}

        nbContent = nbService.retrieveNoticeboardBySessionID(toolSessionId);
	       // nbSession = nbService.retrieveNoticeboardSession(toolSessionId);
	   
	    
	    if(nbContent == null)
		{
			String error = "An Internal error has occurred. Please exit and retry this sequence";
			logger.error(error);				
			throw new NbApplicationException(error);
		}   

	    nbUser = nbService.retrieveNbUserBySession(userId, toolSessionId);
	    
	    /*
         * Checks to see if the defineLater or runOffline flag is set.
         * If the particular flag is set, control is forwarded to jsp page
         * displaying to the user the message according to what flag is set.
         */
        if (displayMessageToUser(nbContent, message))
        {
            saveMessages(request, message);
            return mapping.findForward(NoticeboardConstants.DISPLAY_MESSAGE);
        }
        if (learnerForm.getMode().equals(NoticeboardConstants.TOOL_ACCESS_MODE_LEARNER))
        {
            /* Set the ContentInUse flag to true, and defineLater flag to false */
            nbContent.setContentInUse(true);
          //  nbContent.setDefineLater(false); /* defineLater should be false anyway */
            nbService.updateNoticeboard(nbContent);
                     
            if (nbUser != null)
	        {
            	if (nbUser.getUserStatus().equals(NoticeboardUser.COMPLETED))
	                   request.getSession().setAttribute(NoticeboardConstants.READ_ONLY_MODE, "true");	           
	        }
            else
            {
            	//create a new user with this session id
            	NoticeboardUser newUser = new NoticeboardUser(userId);
            	nbService.addUser(toolSessionId, newUser);
            }
            
            
        } 
        learnerForm.copyValuesIntoForm(nbContent);
        return mapping.findForward(NoticeboardConstants.DISPLAY_LEARNER_CONTENT);
    
    }
    
    public ActionForward teacher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws NbApplicationException {
        return learner(mapping, form, request, response);
    }
    
    public ActionForward author(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws NbApplicationException {
        
        /* will show a different screen if defineLater flag is set and running in preview mode */
    	NoticeboardContent nbContent = null;
        NoticeboardUser nbUser = null;
        NbWebUtil.cleanLearnerSession(request);
        saveMessages(request, null);
        
        NbLearnerForm learnerForm = (NbLearnerForm)form;
      
        ActionMessages message = new ActionMessages();
        INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
        
        Long userId = NbWebUtil.convertToLong(learnerForm.getUserID());
        Long toolSessionId = NbWebUtil.convertToLong(learnerForm.getToolSessionID());
        
        if(userId == null || toolSessionId ==null)
		{
			String error = "Unable to continue. The user ID, tool session ID or tool contentID is missing.";
			logger.error(error);
			throw new NbApplicationException(error);
		}

        nbContent = nbService.retrieveNoticeboardBySessionID(toolSessionId);
	      
	    if(nbContent == null)
		{
			String error = "An Internal error has occurred. Please exit and retry this sequence";
			logger.error(error);				
			throw new NbApplicationException(error);
		}   
	    
	    nbUser = nbService.retrieveNbUserBySession(userId, toolSessionId);	    
       
        if (displayMessageToAuthor(nbContent, message))
        {
            saveMessages(request, message);
            return mapping.findForward(NoticeboardConstants.DISPLAY_MESSAGE);
        }
                
        learnerForm.copyValuesIntoForm(nbContent);       
        return mapping.findForward(NoticeboardConstants.DISPLAY_LEARNER_CONTENT);
    }
    
    
    
    
    /**
     * <p>Performs a check on the flag indicated by <code>flag</code>
     * In this noticeboard tool, there are 3 possible flags:
     * <li>defineLater</li>
     * <li>contentInUse</li>
     * <li>runOffline</li>
     * <br>Returns true if the flag is set, false otherwise <p>
     * 
     * @param content The instance of NoticeboardContent
     * @param flag The flag to check, can take the following set of values (defineLater, contentInUse, runOffline)
     * @return Returns true if flag is set, false otherwise
     */
	private boolean isFlagSet(NoticeboardContent content, int flag) throws NbApplicationException
	{
	    switch (flag)
	    {
	    	case NoticeboardConstants.FLAG_DEFINE_LATER:
	    	    return (content.isDefineLater());
	    	  //  break;
	    	case NoticeboardConstants.FLAG_CONTENT_IN_USE:
	    		return (content.isContentInUse());
	    	//	break;
	    	case NoticeboardConstants.FLAG_RUN_OFFLINE:
	    		return(content.isForceOffline()); 
	    //		break;
	    	default:
	    	    throw new NbApplicationException("Invalid flag");
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
	 * @param content The instance of NoticeboardContent
	 * @param message the instance of ActtionMessages
	 * @return true if any of the flags are set, false otherwise
	 */
	private boolean displayMessageToUser(NoticeboardContent content, ActionMessages message)
	{
	    boolean isDefineLaterSet = isFlagSet(content, NoticeboardConstants.FLAG_DEFINE_LATER);
        boolean isRunOfflineSet = isFlagSet(content, NoticeboardConstants.FLAG_RUN_OFFLINE);
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
	
	private boolean displayMessageToAuthor(NoticeboardContent content, ActionMessages message)
	{
	    boolean isDefineLaterSet = isFlagSet(content, NoticeboardConstants.FLAG_DEFINE_LATER);
        boolean isRunOfflineSet = isFlagSet(content, NoticeboardConstants.FLAG_RUN_OFFLINE);
        if(isDefineLaterSet || isRunOfflineSet)
        {
            if (isDefineLaterSet)
            {
                message.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.author.defineLaterSet1"));
                message.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.author.defineLaterSet2"));
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