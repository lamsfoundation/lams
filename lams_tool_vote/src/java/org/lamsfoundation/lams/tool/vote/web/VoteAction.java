/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.web;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteAttachmentDTO;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * * @author Ozgur Demirtas
 * 
 * <p>Action class that controls the logic of tool behavior. </p>
 * 
 * <p>Note that Struts action class only has the responsibility to navigate 
 * page flow. All database operation should go to service layer and data 
 * transformation from domain model to struts form bean should go to form 
 * bean class. This ensure clean and maintainable code.
 * </p>
 * 
 * <code>SystemException</code> is thrown whenever an known error condition is
 * identified. No system exception error handling code should appear in the 
 * Struts action class as all of them are handled in 
 * <code>CustomStrutsExceptionHandler<code>.
 * 
 * 
	<!--Authoring Main Action : interacts with the authoring module user-->
	<action path="/authoring"
	      	type="org.lamsfoundation.lams.tool.vote.web.VoteAction"
    	  	name="VoteAuthoringForm"
	      	scope="request"
    	  	input="/authoring/AuthoringMaincontent.jsp"
      		parameter="dispatch">

	      <forward
	        name="load"
	        path="/authoring/AuthoringMaincontent.jsp"
		    redirect="false"
	      />
	    
	      <forward
	        name="starter"
	        path="/index.jsp"
		    redirect="false"
	      />
	      
	  	<forward
		    name="loadMonitoring"
		    path="/monitoring/MonitoringMaincontent.jsp"
		    redirect="false"
	  	/>
	  	
	     <forward
	        name="preview"
	        path="/learning/Preview.jsp"
		    redirect="false"
	     />
	  	
	  	<forward
		    name="errorList"
		    path="/VoteErrorBox.jsp"
		    redirect="false"
	  	/>
    </action>

 * 
*/
public class VoteAction extends LamsDispatchAction implements VoteAppConstants
{
	/*
	 * when to reset define later and synchin monitor etc..
	 * make sure the tool gets called on:
	 * setAsForceComplete(Long userId) throws VoteApplicationException 
	 */
	static Logger logger = Logger.getLogger(VoteAction.class.getName());
	
	 /** 
     * <p>Default struts dispatch method.</p> 
     * 
     * <p>It is assuming that progress engine should pass in the tool access
     * mode and the tool session id as http parameters.</p>
     * 
     * @param mapping An ActionMapping class that will be used by the Action class to tell
     * the ActionServlet where to send the end-user.
     *
     * @param form The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param request A standard Servlet HttpServletRequest class.
     * @param response A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where
     *         the user is to go next.
     * @throws IOException
     * @throws ServletException
     * @throws VoteApplicationException the known runtime exception 
     * 
	 * unspecified(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                            ServletException
                                                            
	 * main content/question content management and workflow logic
	 * 
	 * if the passed toolContentID exists in the db, we need to get the relevant data into the Map 
	 * if not, create the default Map 
	*/
    public ActionForward unspecified(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                            ServletException
    {
    	VoteUtils.cleanUpUserExceptions(request);
	 	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	 	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	 	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	 	VoteUtils.saveRichText(request, voteGeneralAuthoringDTO);	 	
	 	voteAuthoringForm.resetUserAction();
	 	return null;
    }
    
    
    public boolean isNewNominationAdded(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response)
    {
		logger.debug("doing isNewNominationAdded");
		
		IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	    logger.debug("voteService :" +voteService);
	    
		VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	    logger.debug("voteAuthoringForm :" +voteAuthoringForm);
	    
	    VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	    
	    repopulateRequestParameters(request, voteAuthoringForm,voteGeneralAuthoringDTO);
	    
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

	    voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
	    voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());
		
	    AuthoringUtil authoringUtil= new AuthoringUtil();
	    Map mapOptionsContent=(Map)sessionMap.get(MAP_OPTIONS_CONTENT_KEY);
	    logger.debug("mapOptionsContent: " + mapOptionsContent);
	
	    VoteUtils.saveRichText(request, voteGeneralAuthoringDTO);
	    
	    mapOptionsContent=authoringUtil.reconstructOptionContentMapForAdd(mapOptionsContent, request);
	    logger.debug("final mapOptionsContent: " + mapOptionsContent);

	    int maxIndex=mapOptionsContent.size();
	    logger.debug("maxIndex: " + maxIndex);
    	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);
    	
    	String firstEntry=(String)mapOptionsContent.get("1");
    	logger.debug("firstEntry: " +  firstEntry);
    	voteGeneralAuthoringDTO.setDefaultOptionContent(firstEntry);

	    String toolContentID=voteAuthoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);
	    
    	VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
	    logger.debug("voteContent: " + voteContent);
	    
		/*true means there is at least 1 response*/
    	if (voteContent != null)
    	{
    		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
    		{
    		    	voteGeneralAuthoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
    				logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
    		}
    		else
    		{
    		    voteGeneralAuthoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
    			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
    		}
    	}
    	
		logger.debug("active module is: " + voteAuthoringForm.getActiveModule());
		logger.debug("before fwd: mapOptionsContent: " + mapOptionsContent);
		voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);
		
		sessionMap.put(MAP_OPTIONS_CONTENT_KEY, mapOptionsContent);
		
		
 		List listOnlineFilesMetaData =(List)sessionMap.get(LIST_ONLINEFILES_METADATA_KEY);
 		logger.debug("listOnlineFilesMetaData:" + listOnlineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);

 		List listOfflineFilesMetaData =(List)sessionMap.get(LIST_OFFLINEFILES_METADATA_KEY);
 		logger.debug("listOfflineFilesMetaData:" + listOfflineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);
		
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		logger.debug("persisting sessionMap into session: " + sessionMap);
		request.getSession().setAttribute(httpSessionID, sessionMap);

	    return true;
        
    }
    
    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward addNewNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
		logger.debug("dispathcing addNewNomination");
		boolean isNewNominationAdded=isNewNominationAdded(mapping, form, request, response);
		logger.debug("isNewNominationAdded:" + isNewNominationAdded);
		
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);

	    return (mapping.findForward(destination));
    }


    public boolean isNominationRemoved(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    {
        logger.debug("starting isNominationRemoved");
        
    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	    logger.debug("voteService :" +voteService);

		VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	    logger.debug("voteAuthoringForm :" +voteAuthoringForm);
	    
	    VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
	    
	    repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	    
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

	    voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
	    voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());
		
		VoteUtils.saveRichText(request, voteGeneralAuthoringDTO);
	    
		AuthoringUtil authoringUtil= new AuthoringUtil();
	    //Map mapOptionsContent=(Map)request.getSession().getAttribute(MAP_OPTIONS_CONTENT);
	    Map mapOptionsContent=(Map)sessionMap.get(MAP_OPTIONS_CONTENT_KEY);
	    logger.debug("mapOptionsContent: " + mapOptionsContent);
	    
	    authoringUtil.reconstructOptionContentMapForRemove(mapOptionsContent, request, voteAuthoringForm);
	    
        int maxIndex=mapOptionsContent.size();
    	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);
    	
    	String firstEntry=(String)mapOptionsContent.get("1");
    	logger.debug("firstEntry: " +  firstEntry);
    	voteGeneralAuthoringDTO.setDefaultOptionContent(firstEntry);
    	
    	String toolContentID =voteAuthoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);

    	VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		/*true means there is at least 1 response*/
    	if (voteContent != null)
    	{
    		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
    		{
    		    	voteGeneralAuthoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
    				logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
    		}
    		else
    		{
    			voteGeneralAuthoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
    			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
    		}
    	}

		logger.debug("active module is: " + voteAuthoringForm.getActiveModule());
		logger.debug("before fwd: mapOptionsContent: " + mapOptionsContent);
		voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);
		
		sessionMap.put(MAP_OPTIONS_CONTENT_KEY, mapOptionsContent);
		
 		List listOnlineFilesMetaData =(List)sessionMap.get(LIST_ONLINEFILES_METADATA_KEY);
 		logger.debug("listOnlineFilesMetaData:" + listOnlineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);

 		List listOfflineFilesMetaData =(List)sessionMap.get(LIST_OFFLINEFILES_METADATA_KEY);
 		logger.debug("listOfflineFilesMetaData:" + listOfflineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);

		
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		logger.debug("persisting sessionMap into session: " + sessionMap);
		request.getSession().setAttribute(httpSessionID, sessionMap);

    	
    	return true;
    }

    
    public ActionForward removeNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
		logger.debug("starting removeNomination ");
		boolean isNominationRemoved=isNominationRemoved(mapping, form, request, response);
		logger.debug("isNominationRemoved:" + isNominationRemoved);
		
	    /* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);
		
	    return (mapping.findForward(destination));
    }


    /**
     * persists the content into the database
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public boolean submitContent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    {
        logger.debug("doing submitContent..");

    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	    logger.debug("voteService :" +voteService);

	    VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	    logger.debug("voteAuthoringForm :" +voteAuthoringForm);
	    
	    VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
	    
	    repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	    
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

	    
	    voteAuthoringForm.setSubmissionAttempt(new Boolean(true).toString());
	    voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(true).toString());
	    
	    request.setAttribute(VALIDATION_ERROR, new Boolean(false).toString());
	    voteGeneralAuthoringDTO.setValidationError(new Boolean(false).toString());
	    
	    String toolContentID=voteAuthoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);

    	VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
	    logger.debug("voteContent: " + voteContent);
	    
		/*true means there is at least 1 response*/
    	if (voteContent != null)
    	{
    		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
    		{
   				voteGeneralAuthoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
   				logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
    		}
    		else
    		{

    			voteGeneralAuthoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
    			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
    		}
    	}
	    
		VoteUtils.saveRichText(request, voteGeneralAuthoringDTO);
	           	
	    ActionMessages errors= new ActionMessages();
	    //errors=validateSubmit(request, errors, voteAuthoringForm);
	    /*
	    if (errors.size() > 0)  
	    {
	        logger.debug("returning back to from to fix errors:");
	        voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	        request.setAttribute(VALIDATION_ERROR, new Boolean(true).toString());
	        voteGeneralAuthoringDTO.setValidationError(new Boolean(true).toString());
	        return false;
	    }
	    */
	    
	    List attachmentList=(List)sessionMap.get(ATTACHMENT_LIST_KEY);
	    logger.debug("attachmentList :" +attachmentList);
	    
	    List deletedAttachmentList = (List)sessionMap.get(DELETED_ATTACHMENT_LIST_KEY);
	    logger.debug("deletedAttachmentList :" +deletedAttachmentList);
	    
	    Map mapOptionsContent=(Map)sessionMap.get(MAP_OPTIONS_CONTENT_KEY);
	    logger.debug("mapOptionsContent: " + mapOptionsContent);
	
	    if (mapOptionsContent == null)
	        mapOptionsContent= new TreeMap(new VoteComparator());

	    logger.debug("mapOptionsContent :" +mapOptionsContent);
	    boolean nominationsDuplicate=AuthoringUtil.verifyDuplicateNominations(mapOptionsContent);
	    logger.debug("nominationsDuplicate :" +nominationsDuplicate);
	    
	    if (nominationsDuplicate == true)
	    {
	        logger.debug("back to user, nominationsDuplicate :" +nominationsDuplicate);
	        request.setAttribute(USER_EXCEPTION_OPTIONS_DUPLICATE, new Boolean(true).toString());
	        voteGeneralAuthoringDTO.setUserExceptionOptionsDuplicate(new Boolean(true).toString());
	        return false;
	    }
	    

        int maxIndex=mapOptionsContent.size();
        logger.debug("maxIndex :" +maxIndex);
    	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);
    	
    	
    	String firstEntry=(String)mapOptionsContent.get("1");
    	logger.debug("firstEntry: " +  firstEntry);
    	voteGeneralAuthoringDTO.setDefaultOptionContent(firstEntry);

    	AuthoringUtil authoringUtil= new AuthoringUtil();
    	mapOptionsContent=authoringUtil.reconstructOptionsContentMapForSubmit(mapOptionsContent, request);
	    logger.debug("before saveOrUpdateVoteContent, mapOptionsContent" + mapOptionsContent);
	    
	 	/*to remove deleted entries in the questions table based on mapQuestionContent */
	    authoringUtil.removeRedundantOptions(mapOptionsContent, voteService, voteAuthoringForm, request);
	    logger.debug("end of removing unused entries... ");
	    
	    voteContent=authoringUtil.saveOrUpdateVoteContent(mapOptionsContent, voteService, voteAuthoringForm, request, sessionMap);
	    logger.debug("voteContent: " + voteContent);
		
	    String maxNomCount=voteAuthoringForm.getMaxNominationCount();
	    logger.debug("maxNomCount:" + maxNomCount);
	    
	    String activeModule=voteAuthoringForm.getActiveModule();
	    logger.debug("activeModule:" + activeModule);
	    
	    if (activeModule != null)
	    {
		    if (activeModule.equals(AUTHORING))
		    {
			    if (maxNomCount != null)
			    {
				    if (maxNomCount.equals("0"))
				    {
				        voteGeneralAuthoringDTO.setUserExceptionMaxNominationInvalid(new Boolean(true).toString());
						return false;
				    }
				    
			    	try
					{
			    		int intMaxNomCount=new Integer(maxNomCount).intValue();
				    	logger.debug("intMaxNomCount : " +intMaxNomCount);
					}
			    	catch(NumberFormatException e)
					{
				        voteGeneralAuthoringDTO.setUserExceptionMaxNominationInvalid(new Boolean(true).toString());
			    		return false;
					}
			    }

			
				logger.debug("start persisting offline files metadata");
				AuthoringUtil.persistFilesMetaData(request, voteService, true, voteContent, sessionMap);
				logger.debug("start persisting online files metadata");
				AuthoringUtil.persistFilesMetaData(request, voteService, false, voteContent, sessionMap );
		        
			
			/* making sure only the filenames in the session cache are persisted and the others in the db are removed*/ 
			logger.debug("start removing redundant offline files metadata");
			AuthoringUtil.removeRedundantOfflineFileItems(request, voteService,  voteContent, sessionMap);
			
			logger.debug("start removing redundant online files metadata");
			AuthoringUtil.removeRedundantOnlineFileItems(request, voteService, voteContent, sessionMap);
	 		logger.debug("done removing redundant files");
		    }
	    }
	
	    errors.clear();
	    errors.add(Globals.ERROR_KEY, new ActionMessage("sbmt.successful"));
	    
	    logger.debug("toolContentID: " + toolContentID);
	    VoteUtils.setDefineLater(request, false, voteService, toolContentID);
	    
	    saveErrors(request,errors);
	    logger.debug("define later set to false");
	    
	    voteAuthoringForm.resetUserAction();
	    voteAuthoringForm.setSbmtSuccess(new Boolean(true).toString());
	    voteGeneralAuthoringDTO.setSbmtSuccess(new Boolean(true).toString());
	    logger.debug("setting SUBMIT_SUCCESS to 1.");
	    
	    
		logger.debug("active module is: " + voteAuthoringForm.getActiveModule());
		logger.debug("before fwd: mapOptionsContent: " + mapOptionsContent);
		voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);
		
		sessionMap.put(MAP_OPTIONS_CONTENT_KEY, mapOptionsContent);
		
 		List listOnlineFilesMetaData =(List)sessionMap.get(LIST_ONLINEFILES_METADATA_KEY);
 		logger.debug("listOnlineFilesMetaData:" + listOnlineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);

 		List listOfflineFilesMetaData =(List)sessionMap.get(LIST_OFFLINEFILES_METADATA_KEY);
 		logger.debug("listOfflineFilesMetaData:" + listOfflineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);

		voteAuthoringForm.setDefineLaterInEditMode(new Boolean(false).toString());
		voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());

		
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		logger.debug("persisting sessionMap into session: " + sessionMap);
		request.getSession().setAttribute(httpSessionID, sessionMap);

	    
	    return true;
    }
    
    
    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
	
		logger.debug("starting submitAllContent :" +form);
		
	    VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	    logger.debug("voteAuthoringForm :" +voteAuthoringForm);
		
	    VoteGeneralAuthoringDTO voteGeneralAuthoringDTO=new VoteGeneralAuthoringDTO();
	    repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	    
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);

		
		boolean isContentSubmitted=submitContent(mapping, form, request, response);
		logger.debug("isContentSubmitted :" +isContentSubmitted);
		
		if (isContentSubmitted == true)
		{
		    voteAuthoringForm.setSbmtSuccess(new Boolean(true).toString());
		}
		    
		    
		logger.debug("final submit status :" +voteAuthoringForm.getSbmtSuccess());
		logger.debug("fwding to destination :" +destination);
		
        request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG,Boolean.TRUE);
        return (mapping.findForward(destination));	
    }


    public boolean isMoveNominationDown(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) 
    {
    	logger.debug("starting isMoveNominationDown...");
    	
		IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);

    	VoteUtils.cleanUpUserExceptions(request);
    	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	    logger.debug("voteAuthoringForm :" +voteAuthoringForm);
	    
	    VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	    repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	    
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

		voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
	    voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());

		VoteUtils.saveRichText(request, voteGeneralAuthoringDTO);
	 	
    	Map mapOptionsContent=AuthoringUtil.repopulateMap(request, "optionContent");
     	logger.debug("mapOptionsContent before move down: " + mapOptionsContent);
     	logger.debug("mapOptionsContent size move down: " + mapOptionsContent.size());
     	

     	/* perform a move down if there are at least 2 nominations*/
     	if (mapOptionsContent.size() > 1)
     	{
     		String optIndex =voteAuthoringForm.getOptIndex();
        	logger.debug("optIndex:" + optIndex);
        	String movableOptionEntry=(String)mapOptionsContent.get(optIndex);
        	logger.debug("movableOptionEntry:" + movableOptionEntry);
        	
        	if (movableOptionEntry != null && (!movableOptionEntry.equals("")))
        	{
        	    mapOptionsContent= AuthoringUtil.shiftMap(mapOptionsContent, optIndex,movableOptionEntry,  "down");
            	logger.debug("mapOptionsContent after move down: " + mapOptionsContent);
            	//request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
            	sessionMap.put(MAP_OPTIONS_CONTENT_KEY, mapOptionsContent);
            	//logger.debug("updated Options Map: " + request.getSession().getAttribute(MAP_OPTIONS_CONTENT));
        	}
     	}
    	
    	voteAuthoringForm.resetUserAction();
        
    	mapOptionsContent=(Map)sessionMap.get(MAP_OPTIONS_CONTENT_KEY);
    	logger.debug("mapOptionsContent: " + mapOptionsContent);
    	
        int maxIndex=mapOptionsContent.size();
    	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);
    	
    	String firstEntry=(String)mapOptionsContent.get("1");
    	logger.debug("firstEntry: " +  firstEntry);
    	voteGeneralAuthoringDTO.setDefaultOptionContent(firstEntry);
    	
    	String toolContentID=voteAuthoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);

    	VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
    	logger.debug("voteContent: " + voteContent);
		
    	/*true means there is at least 1 response*/
    	if (voteContent != null)
    	{
    		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
    		{
    		    voteGeneralAuthoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
    			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
    		}
    		else
    		{
    			voteGeneralAuthoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
    			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
    		}
    	}

	    
		logger.debug("active module is: " + voteAuthoringForm.getActiveModule());
		logger.debug("before fwd: mapOptionsContent: " + mapOptionsContent);
		voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);
		
		sessionMap.put(MAP_OPTIONS_CONTENT_KEY, mapOptionsContent);
		
 		List listOnlineFilesMetaData =(List)sessionMap.get(LIST_ONLINEFILES_METADATA_KEY);
 		logger.debug("listOnlineFilesMetaData:" + listOnlineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);

 		List listOfflineFilesMetaData =(List)sessionMap.get(LIST_OFFLINEFILES_METADATA_KEY);
 		logger.debug("listOfflineFilesMetaData:" + listOfflineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);
		
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		logger.debug("persisting sessionMap into session: " + sessionMap);
		request.getSession().setAttribute(httpSessionID, sessionMap);
    	
    	return true;
    }
    
    
    /**
     * shifts the nominations map for moving down
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveNominationDown(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);

		boolean isMoveNominationDown=isMoveNominationDown(mapping, form, request, response);
		logger.debug("isMoveNominationDown:" + isMoveNominationDown);
		
        return (mapping.findForward(destination));	
    }


    /**
     * shifts the nominations map for moving up
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public boolean isMoveNominationUp(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        logger.debug("starting isMoveNominationUp...");

        IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);

    	VoteUtils.cleanUpUserExceptions(request);
    	
    	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
    	logger.debug("voteAuthoringForm :" +voteAuthoringForm);
    	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
    	
    	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

    	
    	voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
    	voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());
    	
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);

		VoteUtils.saveRichText(request, voteGeneralAuthoringDTO);
	 	
    	Map mapOptionsContent=AuthoringUtil.repopulateMap(request, "optionContent");
     	logger.debug("mapOptionsContent before move down: " + mapOptionsContent);
     	logger.debug("mapOptionsContent size move down: " + mapOptionsContent.size());

     	/* perform a move down if there are at least 2 nominations */
     	if (mapOptionsContent.size() > 1)
     	{
     		String optIndex =voteAuthoringForm.getOptIndex();
        	logger.debug("optIndex:" + optIndex);
        	String movableOptionEntry=(String)mapOptionsContent.get(optIndex);
        	logger.debug("movableOptionEntry:" + movableOptionEntry);
        	
        	if (movableOptionEntry != null && (!movableOptionEntry.equals("")))
        	{
        	    mapOptionsContent= AuthoringUtil.shiftMap(mapOptionsContent, optIndex,movableOptionEntry,  "up");
            	logger.debug("mapOptionsContent after move down: " + mapOptionsContent);
            	sessionMap.put(MAP_OPTIONS_CONTENT_KEY, mapOptionsContent);
        	}
     	}
    	
    	voteAuthoringForm.resetUserAction();

    	mapOptionsContent=(Map)sessionMap.get(MAP_OPTIONS_CONTENT_KEY);
    	logger.debug("mapOptionsContent: " + mapOptionsContent);
    	
        int maxIndex=mapOptionsContent.size();
        voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);

    	String firstEntry=(String)mapOptionsContent.get("1");
    	logger.debug("firstEntry: " +  firstEntry);
    	voteGeneralAuthoringDTO.setDefaultOptionContent(firstEntry);
    	
    	String toolContentID=voteAuthoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);

    	VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
    	logger.debug("voteContent: " + voteContent);
		/*true means there is at least 1 response*/
    	if (voteContent != null)
    	{
    		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
    		{
   		    	voteGeneralAuthoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
   				logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
    		}
    		else
    		{
    		    voteGeneralAuthoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
    			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
    		}
    	}

		logger.debug("active module is: " + voteAuthoringForm.getActiveModule());
		logger.debug("before fwd: mapOptionsContent: " + mapOptionsContent);
		voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);
		
		sessionMap.put(MAP_OPTIONS_CONTENT_KEY, mapOptionsContent);
		
 		List listOnlineFilesMetaData =(List)sessionMap.get(LIST_ONLINEFILES_METADATA_KEY);
 		logger.debug("listOnlineFilesMetaData:" + listOnlineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);

 		List listOfflineFilesMetaData =(List)sessionMap.get(LIST_OFFLINEFILES_METADATA_KEY);
 		logger.debug("listOfflineFilesMetaData:" + listOfflineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);

		
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		logger.debug("persisting sessionMap into session: " + sessionMap);
		request.getSession().setAttribute(httpSessionID, sessionMap);

        return true;
    }

    
    public ActionForward moveNominationUp(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);

		boolean isMoveNominationUp=isMoveNominationUp(mapping, form, request, response);
		logger.debug("isMoveNominationUp:" + isMoveNominationUp);
		
        return (mapping.findForward(destination));	
    }
    
    
    /**
     * checks the user entries before submit is performed
     * @param request
     * @param errors
     * @param voteAuthoringForm
     * @return
     */
    protected ActionMessages validateSubmit(HttpServletRequest request, ActionMessages errors, VoteAuthoringForm voteAuthoringForm)
    {
        logger.debug("starting validateSubmit");
        String title = voteAuthoringForm.getTitle();
        logger.debug("title: " + title);

        String instructions = voteAuthoringForm.getInstructions();
        logger.debug("instructions: " + instructions);
        
        boolean validateSuccess=true;
        if ((title == null) || (title.trim().length() == 0) || title.equalsIgnoreCase(RICHTEXT_BLANK))
        {
            validateSuccess=false;
        }

        if ((instructions == null) || (instructions.trim().length() == 0) || instructions.equalsIgnoreCase(RICHTEXT_BLANK))
        {
            validateSuccess=false;
        }

        /*
         * enforce that the first (default) question entry is not empty
         */
        String defaultOptionEntry =request.getParameter("optionContent0");
        if ((defaultOptionEntry == null) || (defaultOptionEntry.length() == 0))
        {
            validateSuccess=false;        }
        
        if (validateSuccess == false)
        {
            errors.add(Globals.ERROR_KEY, new ActionMessage("error.fields.mandatory"));
            logger.debug("validate success is false");
        }
        
        saveErrors(request,errors);
        return errors;
    }
    
    
    /**
     * persists offline files
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws RepositoryCheckedException
     */
    public ActionForward submitOfflineFiles(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         RepositoryCheckedException
    {
    	VoteUtils.cleanUpUserExceptions(request);
    	logger.debug("dispatching submitOfflineFile...");
    	
    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);

    	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
    	logger.debug("voteAuthoringForm :" +voteAuthoringForm);
    	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
    	
    	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
    	
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
	    Map mapOptionsContent=(Map)sessionMap.get(MAP_OPTIONS_CONTENT_KEY);
	    logger.debug("mapOptionsContent: " + mapOptionsContent);
	    voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);
	    
	    int maxIndex=mapOptionsContent.size();
	    logger.debug("maxIndex: " + maxIndex);
    	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);
    	
    	String firstEntry=(String)mapOptionsContent.get("1");
    	logger.debug("firstEntry: " +  firstEntry);
    	voteGeneralAuthoringDTO.setDefaultOptionContent(firstEntry);

    	voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
    	voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());
    	

		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);

		VoteUtils.saveRichText(request, voteGeneralAuthoringDTO);
	 	
		logger.debug("will uploadFile for offline file:");
 		VoteAttachmentDTO voteAttachmentDTO=AuthoringUtil.uploadFile(request, voteService, voteAuthoringForm, true, sessionMap);
 		logger.debug("returned voteAttachmentDTO:" + voteAttachmentDTO);
 		logger.debug("returned sessionMap:" + sessionMap);

 		List listOfflineFilesMetaData =(List)sessionMap.get(LIST_OFFLINEFILES_METADATA_KEY);
 		logger.debug("listOfflineFilesMetaData:" + listOfflineFilesMetaData);
 		
 		if (voteAttachmentDTO != null)
 		{
 		   listOfflineFilesMetaData.add(voteAttachmentDTO);    
 		}
 		
 		logger.debug("listOfflineFilesMetaData after add:" + listOfflineFilesMetaData);
 		sessionMap.put(LIST_OFFLINEFILES_METADATA_KEY, listOfflineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);
 		
		logger.debug("active module is: " + voteAuthoringForm.getActiveModule());
		
		
 		List listOnlineFilesMetaData =(List)sessionMap.get(LIST_ONLINEFILES_METADATA_KEY);
 		logger.debug("listOnlineFilesMetaData:" + listOnlineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);

 		if (voteAttachmentDTO == null)
 		{
 			ActionMessages errors= new ActionMessages();
 			errors= new ActionMessages();
 			voteGeneralAuthoringDTO.setUserExceptionFilenameEmpty(new Boolean(true).toString());
 			errors.add(Globals.ERROR_KEY,new ActionMessage("error.fileName.empty"));
 			saveErrors(request,errors);
 			voteAuthoringForm.resetUserAction();
 			persistInRequestError(request,"error.fileName.empty");

 			logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
 			request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

 	   	    return (mapping.findForward(destination));	
 		}
 		
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		logger.debug("persisting sessionMap into session: " + sessionMap);
		request.getSession().setAttribute(httpSessionID, sessionMap);

 		
 		voteAuthoringForm.resetUserAction();
   	    return (mapping.findForward(destination));    
    }

    
    /**
     * persists online files
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws RepositoryCheckedException
     */
    public ActionForward submitOnlineFiles(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         RepositoryCheckedException
    {
    	VoteUtils.cleanUpUserExceptions(request);
    	logger.debug("dispatching submitOnlineFiles...");
    	
    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);

    	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
    	logger.debug("voteAuthoringForm :" +voteAuthoringForm);
    	
    	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
    	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
    	
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
	    Map mapOptionsContent=(Map)sessionMap.get(MAP_OPTIONS_CONTENT_KEY);
	    logger.debug("mapOptionsContent: " + mapOptionsContent);
	    voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);

	    int maxIndex=mapOptionsContent.size();
	    logger.debug("maxIndex: " + maxIndex);
    	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);
    	
    	String firstEntry=(String)mapOptionsContent.get("1");
    	logger.debug("firstEntry: " +  firstEntry);
    	voteGeneralAuthoringDTO.setDefaultOptionContent(firstEntry);

    	
    	voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
    	voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());
    	

		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);

		VoteUtils.saveRichText(request, voteGeneralAuthoringDTO);
	 	
		logger.debug("will uploadFile for online file:");
 		VoteAttachmentDTO voteAttachmentDTO=AuthoringUtil.uploadFile(request, voteService, voteAuthoringForm, false, sessionMap);
 		logger.debug("returned voteAttachmentDTO:" + voteAttachmentDTO);
 		logger.debug("returned sessionMap:" + sessionMap);

 		List listOnlineFilesMetaData =(List)sessionMap.get(LIST_ONLINEFILES_METADATA_KEY);
 		logger.debug("listOnlineFilesMetaData:" + listOnlineFilesMetaData);
 		
 		if (voteAttachmentDTO != null)
 		{
 		   listOnlineFilesMetaData.add(voteAttachmentDTO);    
 		}
 		logger.debug("listOnlineFilesMetaData after add:" + listOnlineFilesMetaData);
 		
 		sessionMap.put(LIST_ONLINEFILES_METADATA_KEY, listOnlineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);
 		
		logger.debug("active module is: " + voteAuthoringForm.getActiveModule());
		
 		List listOfflineFilesMetaData =(List)sessionMap.get(LIST_OFFLINEFILES_METADATA_KEY);
 		logger.debug("listOfflineFilesMetaData:" + listOfflineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);

 		if (voteAttachmentDTO == null)
 		{
 			ActionMessages errors= new ActionMessages();
 			errors= new ActionMessages();
 			voteGeneralAuthoringDTO.setUserExceptionFilenameEmpty(new Boolean(true).toString());
 			errors.add(Globals.ERROR_KEY,new ActionMessage("error.fileName.empty"));
 			saveErrors(request,errors);
 			voteAuthoringForm.resetUserAction();
 			persistInRequestError(request,"error.fileName.empty");

 			logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
 			request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

 		    return (mapping.findForward(destination));	
 		}

		
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		logger.debug("persisting sessionMap into session: " + sessionMap);
		request.getSession().setAttribute(httpSessionID, sessionMap);

        voteAuthoringForm.resetUserAction();
   	    return (mapping.findForward(destination));
    }


    /**
     * removes an offline file from the jsp
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward deleteOfflineFile(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException

    {
    	VoteUtils.cleanUpUserExceptions(request);
    	logger.debug("dispatching deleteOfflineFile...");
    	
    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);

    	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
    	logger.debug("voteAuthoringForm :" +voteAuthoringForm);
    	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
    	
    	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
    	
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

	    Map mapOptionsContent=(Map)sessionMap.get(MAP_OPTIONS_CONTENT_KEY);
	    logger.debug("mapOptionsContent: " + mapOptionsContent);
	    voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);
	    
	    int maxIndex=mapOptionsContent.size();
	    logger.debug("maxIndex: " + maxIndex);
    	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);
    	
    	String firstEntry=(String)mapOptionsContent.get("1");
    	logger.debug("firstEntry: " +  firstEntry);
    	voteGeneralAuthoringDTO.setDefaultOptionContent(firstEntry);
	    
    	
    	voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
    	voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());
	 	
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);
	 	
		VoteUtils.saveRichText(request, voteGeneralAuthoringDTO);
	 	
	 	String uuid =voteAuthoringForm.getUuid();
	 	logger.debug("uuid:" + uuid);
	 	
	 	List listOfflineFilesMetaData =(List)sessionMap.get(LIST_OFFLINEFILES_METADATA_KEY);
 		logger.debug("listOfflineFilesMetaData:" + listOfflineFilesMetaData);
 		listOfflineFilesMetaData=AuthoringUtil.removeFileItem(listOfflineFilesMetaData, uuid);
 		logger.debug("listOfflineFilesMetaData after remove:" + listOfflineFilesMetaData);
 		sessionMap.put(LIST_OFFLINEFILES_METADATA_KEY, listOfflineFilesMetaData);
 		
 		voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);

		logger.debug("active module is: " + voteAuthoringForm.getActiveModule());
		
 		List listOnlineFilesMetaData =(List)sessionMap.get(LIST_ONLINEFILES_METADATA_KEY);
 		logger.debug("listOnlineFilesMetaData:" + listOnlineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);

		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		logger.debug("persisting sessionMap into session: " + sessionMap);
		request.getSession().setAttribute(httpSessionID, sessionMap);

 		
        voteAuthoringForm.resetUserAction();
    	
        return (mapping.findForward(destination));
    }

	/**
	 * deletes an online file from the jsp
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
    public ActionForward deleteOnlineFile(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException

    {
    	VoteUtils.cleanUpUserExceptions(request);
    	logger.debug("dispatching deleteOnlineFile...");
    	
    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);

    	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
    	logger.debug("voteAuthoringForm :" +voteAuthoringForm);
    	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
    	
    	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
    	
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
	    Map mapOptionsContent=(Map)sessionMap.get(MAP_OPTIONS_CONTENT_KEY);
	    logger.debug("mapOptionsContent: " + mapOptionsContent);
	    voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);
	    
	    int maxIndex=mapOptionsContent.size();
	    logger.debug("maxIndex: " + maxIndex);
    	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);
    	
    	String firstEntry=(String)mapOptionsContent.get("1");
    	logger.debug("firstEntry: " +  firstEntry);
    	voteGeneralAuthoringDTO.setDefaultOptionContent(firstEntry);
	    

    	voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
    	voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());

		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);

		VoteUtils.saveRichText(request, voteGeneralAuthoringDTO);
	 	
	 	String uuid =voteAuthoringForm.getUuid();
	 	logger.debug("uuid:" + uuid);
	 	
	 	List listOnlineFilesMetaData =(List)sessionMap.get(LIST_ONLINEFILES_METADATA_KEY);
 		logger.debug("listOnlineFilesMetaData:" + listOnlineFilesMetaData);
 		listOnlineFilesMetaData=AuthoringUtil.removeFileItem(listOnlineFilesMetaData, uuid);
 		logger.debug("listOnlineFilesMetaData after remove:" + listOnlineFilesMetaData);
 		sessionMap.put(LIST_ONLINEFILES_METADATA_KEY, listOnlineFilesMetaData);
 		
 		voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);

		logger.debug("active module is: " + voteAuthoringForm.getActiveModule());
		
 		List listOfflineFilesMetaData =(List)sessionMap.get(LIST_OFFLINEFILES_METADATA_KEY);
 		logger.debug("listOfflineFilesMetaData:" + listOfflineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);

		
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		logger.debug("persisting sessionMap into session: " + sessionMap);
		request.getSession().setAttribute(httpSessionID, sessionMap);
 		
        voteAuthoringForm.resetUserAction();
        
        return (mapping.findForward(destination));
    }

    /**
     * used in define later to switch from view-only to editable mode
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws ToolException
     */
    public ActionForward editActivityQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         ToolException
    {
    	logger.debug("dispatching editActivityQuestions...");

    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	    logger.debug("voteService :" +voteService);
    	
    	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
    	logger.debug("voteAuthoringForm :" +voteAuthoringForm);
    	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
    	
    	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

    	voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
    	voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());
    	
    	
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);

		voteAuthoringForm.setDefineLaterInEditMode(new Boolean(true).toString());
		voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
     	String toolContentID=voteAuthoringForm.getToolContentID();
     	logger.debug("toolContentID: " + toolContentID);
     	
     	VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		logger.debug("existing voteContent:" + voteContent);

		VoteUtils.readContentValues(request, voteContent, voteAuthoringForm, voteGeneralAuthoringDTO);
		logger.debug("form title is: : " + voteAuthoringForm.getTitle());
		
	    /*
		 * get the nominations 
		 */
		Map mapOptionsContent= new TreeMap(new VoteComparator());
		logger.debug("setting existing content data from the db");
		Iterator queIterator=voteContent.getVoteQueContents().iterator();
		Long mapIndex=new Long(1);
		logger.debug("mapOptionsContent: " + mapOptionsContent);
		while (queIterator.hasNext())
		{
			VoteQueContent voteQueContent=(VoteQueContent) queIterator.next();
			logger.debug("voteQueContent : " + voteQueContent);
			if (voteQueContent != null)
			{
				logger.debug("question: " + voteQueContent.getQuestion());
				mapOptionsContent.put(mapIndex.toString(),voteQueContent.getQuestion());
	    		/**
	    		 * make the first entry the default(first) one for jsp
	    		 */
	    		if (mapIndex.longValue() == 1)
	    		{
	    		    voteGeneralAuthoringDTO.setDefaultOptionContent(voteQueContent.getQuestion());
	    		}
	    		
	    		mapIndex=new Long(mapIndex.longValue()+1);
			}
		}
		logger.debug("Map initialized with existing contentid to: " + mapOptionsContent);
		voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);
		
		int maxIndex=mapOptionsContent.size();
		logger.debug("maxIndex: " + maxIndex);
		voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);
		
		boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
		logger.debug("isContentInUse:" + isContentInUse);
		
		voteGeneralAuthoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
		if (isContentInUse == true)
		{
			logger.debug("monitoring url does not allow editActivity since the content is in use.");
	    	persistInRequestError(request,"error.content.inUse");
	    	voteGeneralAuthoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
		}
     	
		VoteUtils.setDefineLater(request, true, voteService, toolContentID);

		logger.debug("active module is: " + voteAuthoringForm.getActiveModule());
		
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		logger.debug("forwarding to : " + destination);
		return mapping.findForward(destination);
    }


    protected void repopulateRequestParameters(HttpServletRequest request, VoteAuthoringForm voteAuthoringForm, 
            VoteGeneralAuthoringDTO voteGeneralAuthoringDTO)
    {
        logger.debug("starting repopulateRequestParameters");
        
        String toolContentID=request.getParameter(TOOL_CONTENT_ID);
        logger.debug("toolContentID: " + toolContentID);
        voteAuthoringForm.setToolContentID(toolContentID);
        voteGeneralAuthoringDTO.setToolContentID(toolContentID);
        
        String activeModule=request.getParameter(ACTIVE_MODULE);
        logger.debug("activeModule: " + activeModule);
        voteAuthoringForm.setActiveModule(activeModule);
        voteGeneralAuthoringDTO.setActiveModule(activeModule);
        
        String httpSessionID=request.getParameter(HTTP_SESSION_ID);
        logger.debug("httpSessionID: " + httpSessionID);
        voteAuthoringForm.setHttpSessionID(httpSessionID);
        voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

        String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
        logger.debug("defaultContentIdStr: " + defaultContentIdStr);
        voteAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
        voteGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
        
        String defineLaterInEditMode=request.getParameter(DEFINE_LATER_IN_EDIT_MODE);
        logger.debug("defineLaterInEditMode: " + defineLaterInEditMode);
        voteAuthoringForm.setDefineLaterInEditMode(defineLaterInEditMode);
        voteGeneralAuthoringDTO.setDefineLaterInEditMode(defineLaterInEditMode);
        
        String voteChangable=request.getParameter(VOTE_CHANGABLE);
        logger.debug("voteChangable: " + voteChangable);
        voteAuthoringForm.setVoteChangable(voteChangable);
        voteGeneralAuthoringDTO.setVoteChangable(voteChangable);
        
        String lockOnFinish=request.getParameter(LOCK_ON_FINISH);
        logger.debug("lockOnFinish: " + lockOnFinish);
        voteAuthoringForm.setLockOnFinish(lockOnFinish);
        voteGeneralAuthoringDTO.setLockOnFinish(lockOnFinish);
        
        String allowText=request.getParameter(ALLOW_TEXT);
        logger.debug("allowText: " + allowText);
        voteAuthoringForm.setAllowText(allowText);
        voteGeneralAuthoringDTO.setAllowText(allowText);
        
        String maxNominationCount=request.getParameter(MAX_NOMINATION_COUNT);
        logger.debug("maxNominationCount: " + maxNominationCount);
        voteAuthoringForm.setMaxNominationCount(maxNominationCount);
        voteGeneralAuthoringDTO.setMaxNominationCount(maxNominationCount);
    }    
    
            
    /**
     * persists error messages to request scope
     * @param request
     * @param message
     */
    public void persistInRequestError(HttpServletRequest request, String message)
	{
		ActionMessages errors= new ActionMessages();
		errors.add(Globals.ERROR_KEY, new ActionMessage(message));
		logger.debug("add " + message +"  to ActionMessages:");
		saveErrors(request,errors);	    	    
	}
}
    
