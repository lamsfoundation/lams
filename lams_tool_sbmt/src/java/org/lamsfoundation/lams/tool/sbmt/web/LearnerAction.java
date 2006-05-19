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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	

package org.lamsfoundation.lams.tool.sbmt.web;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.sbmt.Learner;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.LearnerDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.exception.SubmitFilesException;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author Manpreet Minhas
 * @author Steve.Ni
 * @struts.action 
 * 			path="/learner"
 * 			parameter="method"
 * 			name="SbmtLearnerForm"
 * 			input="/learner/sbmtLearner.jsp"
 * 			scope="request"
 * 			validate="false"
 * 
 * @struts.action-forward name="upload" path="/learner/sbmtLearner.jsp"
 * @struts.action-forward name="contentInUse" path="/learner/contentinuse.jsp"
 * 
 */
public class LearnerAction extends DispatchAction {
    
    private static final boolean MODE_OPTIONAL = false;
    
	public ISubmitFilesService submitFilesService;
	public static Logger logger = Logger.getLogger(LearnerAction.class);
    
    
    public ActionForward unspecified(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        
        //set the mode into http session 
        ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE,MODE_OPTIONAL);
        request.getSession().setAttribute(AttributeNames.ATTR_MODE, mode);
                
        if(mode.equals(ToolAccessMode.LEARNER)){
            return listFiles(mapping, form, request, response);
        }
        else if(mode.equals(ToolAccessMode.AUTHOR) || mode.equals(ToolAccessMode.TEACHER)){
        	return listFiles(mapping, form, request, response);
        }
        logger.error("Requested mode + '" + mode.toString() + "' not supported");
        return returnErrors(mapping,request,"submit.modenotsupported","upload");
    }
    
	/**
	 * The initial page of learner in Submission tool. This page will list all uploaded files and learn 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward listFiles(ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response){
		
		DynaActionForm authForm= (DynaActionForm)form;
		Long sessionID =(Long) authForm.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		//get session from shared session.
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		Long userID = new Long(user.getUserID().longValue());
		
		//get submission information from content table.E.g., title, instruction
		submitFilesService = SubmitFilesServiceProxy.getSubmitFilesService(this.getServlet().getServletContext());
		SubmitFilesSession session = submitFilesService.getSessionById(sessionID);
		SubmitFilesContent content = session.getContent();
		//if content in use, return special page.
		if(content.isContentInUse())
			return mapping.findForward("contentInUse");
		
		List filesUploaded = submitFilesService.getFilesUploadedByUser(userID,sessionID);
		Learner learner = submitFilesService.getLearner(sessionID,userID);
		setLearnerDTO(request, sessionID,user,learner, content, filesUploaded);
		//to avoid user without patience click "upload" button too fast
		saveToken(request);
		return mapping.getInputForward();
		
	}
	/**
	 * Implements learner upload submission function. This function also display the page again for learner uploading
	 * more submission use. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward uploadFile(ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response){
		
		DynaActionForm authForm= (DynaActionForm)form;
		if(!isTokenValid(request,true)){
			Long sessionID =(Long) authForm.get(AttributeNames.PARAM_TOOL_SESSION_ID);
			//get session from shared session.
			HttpSession ss = SessionManager.getSession();
			//get back login user DTO
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			Long userID = new Long(user.getUserID().longValue());
			
			submitFilesService = SubmitFilesServiceProxy.getSubmitFilesService(this.getServlet().getServletContext());
			List filesUploaded = submitFilesService.getFilesUploadedByUser(userID,sessionID);

			submitFilesService = SubmitFilesServiceProxy.getSubmitFilesService(this.getServlet().getServletContext());
			SubmitFilesSession session = submitFilesService.getSessionById(sessionID);
			SubmitFilesContent content = session.getContent();
			Learner learner = submitFilesService.getLearner(sessionID,userID);
			setLearnerDTO(request, sessionID,user,learner, content, filesUploaded);
			return returnErrors(mapping,request,"submit.upload.twice","upload");
		}
		
		Long sessionID =(Long) authForm.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		//get session from shared session.
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		Long userID = new Long(user.getUserID().longValue());
		
		FormFile uploadedFile= (FormFile) authForm.get("filePath");
		String fileDescription = (String) authForm.get("fileDescription");
		authForm.set("fileDescription","");
		
		submitFilesService = SubmitFilesServiceProxy.getSubmitFilesService(this.getServlet().getServletContext());
		//to avoid user without patience click "upload" button too fast 
		saveToken(request);
		try{
			submitFilesService.uploadFileToSession(sessionID,uploadedFile,fileDescription,userID);
			List filesUploaded = submitFilesService.getFilesUploadedByUser(userID,sessionID);
			SubmitFilesSession session = submitFilesService.getSessionById(sessionID);
			SubmitFilesContent content = session.getContent();
			Learner learner = submitFilesService.getLearner(sessionID,userID);
			setLearnerDTO(request,sessionID,user, learner, content, filesUploaded);
			return mapping.getInputForward();			
		}catch(SubmitFilesException se){
			logger.error("uploadFile: Submit Files Exception has occured" + se.getMessage());
			return returnErrors(mapping,request,se.getMessage(),"upload");
		}
	}
	/**
	 * Learner choose finish upload button, will invoke this function. This function will mark the <code>finished</code> 
	 * field by special toolSessionID and userID.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward finish(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response){
		
		DynaActionForm authForm = (DynaActionForm) form;
		ToolAccessMode mode = (ToolAccessMode) request.getSession().getAttribute(AttributeNames.ATTR_MODE);
		if (mode == ToolAccessMode.LEARNER) {
			ToolSessionManager sessionMgrService = SubmitFilesServiceProxy.getToolSessionManager(getServlet().getServletContext());
			submitFilesService = SubmitFilesServiceProxy.getSubmitFilesService(this.getServlet().getServletContext());

			Long sessionID = (Long) authForm.get(AttributeNames.PARAM_TOOL_SESSION_ID);
			//get back login user DTO
			//get session from shared session.
			HttpSession ss = SessionManager.getSession();
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			Long userID = new Long(user.getUserID().longValue());
			submitFilesService.finishSubmission(sessionID, userID);

			String nextActivityUrl;
			try {
				nextActivityUrl = sessionMgrService.leaveToolSession(sessionID, userID);
				response.sendRedirect(nextActivityUrl);
			} catch (DataMissingException e) {
				throw new SubmitFilesException(e);
			} catch (ToolException e) {
				throw new SubmitFilesException(e);
			} catch (IOException e) {
				throw new SubmitFilesException(e);
			}
			return null;
		}
		
		request.getSession().setAttribute(SbmtConstants.READ_ONLY_MODE, "true");
		return returnErrors(mapping,request,"error.read.only.mode","upload");
		
	}
	//**********************************************************************************************
	//		Private mehtods
	//**********************************************************************************************
	/**
	 * This is a utily function for forwarding the errors to the respective JSP
	 * page indicated by <code>forward</code>
	 * 
	 * @param mapping
	 * @param request
	 * @param errorMessage
	 *            The error message to be displayed
	 * @param forward
	 *            The JSP page to which the errors would be forwarded
	 * @return ActionForward
	 */
	private ActionForward returnErrors(ActionMapping mapping, 
							  HttpServletRequest request, 
							  String errorMessage,String forward){
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,
					 new ActionMessage(errorMessage));
		saveErrors(request,messages);
		return mapping.findForward(forward);
	}
	

	/**
	 * 
	 * Set information into learner DTO object for page display.
	 * Fill file list uploaded by the special user into web form. Remove the unauthorized mark and comments.
	 * @param request
	 * @param sessionID
	 * @param userID
	 * @param content
	 * @param filesUploaded 
	 */
	private void setLearnerDTO(HttpServletRequest request, Long sessionID,
			UserDTO userDto, Learner learner, SubmitFilesContent content,
			List filesUploaded) {
		
		LearnerDetailsDTO dto = new LearnerDetailsDTO();
		//don't use learner data, becuase these 2 values come from web page. Learner maybe empty.
		dto.setToolSessionID(sessionID);
		dto.setUserDto(userDto);
		
		if(learner != null){
			//only content is lock-on-finished, then check whehter learner choose "finish"
			if(content.isLockOnFinished())
				//if learner already finished and lcokonfinished is true, the lock the page, learner
				//can not "finish" again.
				dto.setLocked(learner.isFinished());
			else
				dto.setLocked(false);
			//if Monitoring does not release marks, then skip this mark and comment content.
			if(filesUploaded != null){
				Iterator iter = filesUploaded.iterator();
				while(iter.hasNext()){
					FileDetailsDTO filedto = (FileDetailsDTO) iter.next();
					if(filedto .getDateMarksReleased() == null){
						filedto .setComments(null);
						filedto .setMarks(null);
					}
				}
			}
			dto.setFilesUploaded(filesUploaded);
		}
		if(content != null){
			dto.setContentInstruction(content.getInstruction());
			dto.setContentLockOnFinished(content.isLockOnFinished());
			dto.setContentTitle(content.getTitle());
		}
		request.setAttribute("learner",dto);
	}
}
