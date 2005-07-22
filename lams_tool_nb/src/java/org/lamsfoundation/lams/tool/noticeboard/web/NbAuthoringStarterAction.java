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
 * Created on May 19, 2005
 * @author mtruong
 */
package org.lamsfoundation.lams.tool.noticeboard.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

//import org.apache.struts.action.Action;
import org.lamsfoundation.lams.web.action.LamsAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;

import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;

import org.lamsfoundation.lams.tool.noticeboard.util.NbWebUtil; 
/**TODO: change into one utility class */
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;


/**
 * Creation Date: 19-05-05
 *  
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/starter/authoring" name="NbAuthoringForm" scope="session" type="org.lamsfoundation.lams.tool.noticeboard.web.NbAuthoringStarterAction"
 *                validate="false" 
 * @struts.action-exception key="error.exception.NbApplication" scope="request"
 *                          type="org.lamsfoundation.lams.tool.noticeboard.NbApplicationException"
 *                          path=".error"
 *                          handler="org.lamsfoundation.lams.tool.noticeboard.web.CustomStrutsExceptionHandler"
 * @struts.action-exception key="error.exception.NbApplication" scope="request"
 *                          type="java.lang.NullPointerException"
 *                          path=".error"
 *                          handler="org.lamsfoundation.lams.tool.noticeboard.web.CustomStrutsExceptionHandler"
 * 
 * @struts:action-forward name="basic" path=".nb_basic"
 * @struts:action-forward name="displayMessage" path=".message"
 * ----------------XDoclet Tags--------------------
 */

public class NbAuthoringStarterAction extends LamsAction {
	
	static Logger logger = Logger.getLogger(NbAuthoringAction.class.getName());
	
	/**
	 * This struts actionservlet gets called when the author double clicks
	 * on the tool icon. If the toolContentId already exists in the tool content 
	 * table, the content is then extracted from the content table and is displayed. 
	 * Otherwise, if the toolContentId does not exist in the content table, a new 
	 * record is created with this toolContentId and default content is used.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward, the action path to take once completed.
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws NbApplicationException {
		
	    MessageResources resources = getResources(request);
	    
		NbAuthoringForm nbForm = (NbAuthoringForm)form;
		
		Long contentId = NbWebUtil.convertToLong(request.getParameter(NoticeboardConstants.TOOL_CONTENT_ID));
		
		if(contentId == null)
		{		    
			//String error = "Tool content id missing. Unable to continue.";
		    String error = resources.getMessage(NoticeboardConstants.ERR_MISSING_PARAM, "Tool Content Id");
		    logger.error(error);
			throw new NbApplicationException(error);
		}
		nbForm.setToolContentId(contentId.toString());
		
		/* if there is a defineLater request parameter, set the form value
		 * If a defineLater request parameter is not present, then it is just set to null.
		 * This is used in the basic screen, if defineLater is set, then in the basic page,
		 * the three tabs {Basic, Advanced, Instructions} are not visible.
		 */
		nbForm.setDefineLater((String)request.getParameter(NoticeboardConstants.DEFINE_LATER));

		
		NbWebUtil.cleanAuthoringSession(request); 
		
		request.getSession().setAttribute(NoticeboardConstants.TOOL_CONTENT_ID, contentId);
							
		/*
		 * Retrieve the Service
		 */
		INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
		
		if (!contentExists(nbService, contentId))
		{
			//	Pre-fill the form with the default content
			NoticeboardContent nb =	nbService.retrieveNoticeboard(NoticeboardConstants.DEFAULT_CONTENT_ID);
			
			/** TODO: add a check to see if object is null */
			
			//create a new noticeboard object and prefill with default content, save to database
			NoticeboardContent nbContentNew = new NoticeboardContent(contentId,
			        												nb.getTitle(),
			        												nb.getContent(),
			        												nb.getOnlineInstructions(),
			        												nb.getOfflineInstructions(),
			        												new Date(System.currentTimeMillis()));
			
			nbContentNew = setTrueIfDefineLaterIsSet(nbForm, nbContentNew);
			
			//save new tool content into db
			nbService.saveNoticeboard(nbContentNew);
			
			//initialise the values in the form, so the values will be shown in the jsp
			nbForm.populateFormWithNbContentValues(nbContentNew);
				
		
		}
		else //content already exists on the database
		{
			//get the values from the database
			NoticeboardContent nb = nbService.retrieveNoticeboard(contentId);
			
			/* If retrieving existing content, check whether the contentInUse flag is set, if set, the
			 * author is not allowed to edit content 
			 */
			
			if (NbWebUtil.isContentEditable(nb))
			{
			    /* Define later set to true when the edit activity tab is brought up 
			     * So that users cannot start using the content while the staff member is editing the content */
			    nbForm.populateFormWithNbContentValues(nb);
			    nb = setTrueIfDefineLaterIsSet(nbForm, nb);
			    nbService.updateNoticeboard(nb);
			}
			else
			{
			    //The contentInUse flag is set and a user has already reached this activity.
			    saveMessages(request, null); //ensure there are no existing messages
			    ActionMessages message = new ActionMessages();
			    message.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.contentInUseSet"));
			    saveMessages(request, message);
			    return mapping.findForward(NoticeboardConstants.DISPLAY_MESSAGE);
			    
			}
			
		
		}
		
		return mapping.findForward(NoticeboardConstants.BASIC_PAGE);
	}
	
	/**
	 * Checks the session to see if the title and content session variables exist or not.
	 * 
	 * @param session The HttpSession to check.
	 * @return true if the parameters title and content exists in the session, false otherwise
	 */
	private boolean contentExists(INoticeboardService service, Long id)
	{
		NoticeboardContent nb = service.retrieveNoticeboard(id);
		if (nb == null)
			return false;
		else
			return true;
		
	}
	
	private NoticeboardContent setTrueIfDefineLaterIsSet(NbAuthoringForm form, NoticeboardContent content)
	{
	    if(form.getDefineLater() != null)
	    {
	        if (form.getDefineLater().equals("true"))
	        {
	            //if the defineLater flag is set to true, then set defineLater in the NoticeboardContent object to true too
	            content.setDefineLater(true);
	        }
	    }
	    return content;
	}
	
	
}