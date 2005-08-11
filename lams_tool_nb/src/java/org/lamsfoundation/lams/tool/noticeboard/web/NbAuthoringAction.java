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

package org.lamsfoundation.lams.tool.noticeboard.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
//import org.apache.struts.actions.LookupDispatchAction;
import org.lamsfoundation.lams.web.action.LamsLookupDispatchAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardAttachment;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.util.NbWebUtil;
import org.lamsfoundation.lams.tool.noticeboard.util.NbToolContentHandler;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.util.WebUtil;

//import org.lamsfoundation.lams.contentrepository.CrNodeVersionProperty;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Creation Date: 20-05-05
 * Modified Date: 03-06-05
 * 
 * <p>This Action class process any actions that are made by the user.</p>
 * <br>
 * <p>When the following buttons are pressed:<br>
 * NoticeboardConstants.BUTTON_BASIC, NoticeboardConstants.BUTTON_ADVANCED, 
 * NoticeboardConstants.BUTTON_INSTRUCTIONS or NoticeboardConstants.BUTTON_DONE, <br>
 * they switch between the tabs, but before doing so, they copy the values
 * in the form (only if they are not null) and place them in the
 * equivalent formbean properties (session scope). This is done so because the form
 * is spread across two pages (basic and instructions, in Noticeboard tool, there is 
 * no Advanced page). It might be strange that all buttons forward to the same path,
 * but the formbean property "method" keeps a track of what button was pressed.
 * So when control returns to the jsp, it will determine the value of method and 
 * show the appropriate page. </p> <br>
 * 
 * <p> The button NoticeboardConstants.BUTTON_UPLOAD triggers an upload and will 
 * upload either a file of type "ONLINE" or "OFFLINE", to the content repository
 * and will save the file details (filename, toolcontent id, uuid, and filetype).
 * It will also conduct checks
 * to see whether the same file (and same type) has been uploaded or not. 
 * If the same file has been uploaded (to the same noticeboard instance, ie. same
 * tool content id) then, it will delete the file from the content respository
 * and delete that particular entry in the table, and then it the new version
 * will be uploaded to the content repository and a new entry will be saved into 
 * the database. It then updates the attachment map that was initially setup 
 * by NbAuthoringStarterAction. </p> <br>
 * 
 * <p> The button NoticeboardConstants.LINK_DELETE will trigger the deleteAttachment
 * action which will delete a file from the content repository, and the entry from
 * the attachment table. </p>
 * 
 * <p> The save method persist the values of the formbean properties 
 * title, instructions, onlineInstructions and offlineInstructions to 
 * the database </p>
 * 
 * 
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/authoring" name="NbAuthoringForm" scope="session"
 * 				  type="org.lamsfoundation.lams.tool.noticeboard.web.NbAuthoringAction"
 *                parameter="method" validate="true" input=".authoringContent"
 * 
 * @struts.action-exception key="error.exception.NbApplication" scope="request"
 *                          type="org.lamsfoundation.lams.tool.noticeboard.NbApplicationException"
 *                          path=".error"
 *                          handler="org.lamsfoundation.lams.tool.noticeboard.web.CustomStrutsExceptionHandler"
 * @struts.action-exception key="error.exception.NbApplication" scope="request"
 *                          type="java.lang.NullPointerException"
 *                          path=".error"
 *                          handler="org.lamsfoundation.lams.tool.noticeboard.web.CustomStrutsExceptionHandler"
 *
 * @struts:action-forward name="authoringContent" path=".authoringContent"
 * 
 * ----------------XDoclet Tags--------------------
 */

public class NbAuthoringAction extends LamsLookupDispatchAction {
	
    static Logger logger = Logger.getLogger(NbAuthoringAction.class.getName());
    private NbToolContentHandler toolContentHandler;
    
    private NbToolContentHandler getToolContentHandler()
    {
        if ( toolContentHandler == null ) {
    	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
    	    toolContentHandler = (NbToolContentHandler) wac.getBean(NbToolContentHandler.SPRING_BEAN_NAME);
    	    }
    	    return toolContentHandler;
   	} 
   
	
    protected Map getKeyMethodMap()
	{
		Map map = new HashMap();
		map.put(NoticeboardConstants.BUTTON_BASIC, "basic" );
		map.put(NoticeboardConstants.BUTTON_ADVANCED, "advanced" );
		map.put(NoticeboardConstants.BUTTON_INSTRUCTIONS, "instructions");
		map.put(NoticeboardConstants.BUTTON_DONE, "done");
		map.put(NoticeboardConstants.BUTTON_SAVE, "save");
		map.put(NoticeboardConstants.BUTTON_UPLOAD, "upload");
		map.put(NoticeboardConstants.LINK_DELETE, "deleteAttachment");
		
		return map;
	}
    
    /**
     * 
     */
    public ActionForward unspecified(
    		ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response)
    {
        NbAuthoringForm nbForm = (NbAuthoringForm)form;
        copyAuthoringFormValuesIntoFormBean(request, nbForm);

        return mapping.findForward(NoticeboardConstants.AUTHOR_PAGE);
    }

	/**
	 * Forwards to the basic page.
	 */
	public ActionForward basic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
	    NbAuthoringForm nbForm = (NbAuthoringForm)form;
	    copyAuthoringFormValuesIntoFormBean(request, nbForm);
	  //  return mapping.findForward(NoticeboardConstants.BASIC_PAGE);
	    return mapping.findForward(NoticeboardConstants.AUTHOR_PAGE);
	}
	
	/**
	 * Forwards to the advanced page.
	 */
	
	public ActionForward advanced(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	    return mapping.findForward(NoticeboardConstants.AUTHOR_PAGE);
	}
	
	/**
	 * Forwards to the instructions page.
	 */
	
	public ActionForward instructions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	    NbAuthoringForm nbForm = (NbAuthoringForm)form;
	    copyAuthoringFormValuesIntoFormBean(request, nbForm);
	 
	    return mapping.findForward(NoticeboardConstants.AUTHOR_PAGE);
	}	
	
	/**
	 * Online/Offline instructions entered, form values saved and forward to the basic page.
	 */
	
	public ActionForward done(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	    NbAuthoringForm nbForm = (NbAuthoringForm)form;
	    copyAuthoringFormValuesIntoFormBean(request, nbForm);
	  
	    return mapping.findForward(NoticeboardConstants.AUTHOR_PAGE);
	}
	
	/**
	 * Persist the values of the noticeboard content (title, content, online and offline instructions)
	 * into the database. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws NbApplicationException
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws NbApplicationException {
		
		NbAuthoringForm nbForm = (NbAuthoringForm)form;
		copyAuthoringFormValuesIntoFormBean(request, nbForm);
		
		INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
		String idAsString = nbForm.getToolContentId();
		if (idAsString == null)
		{
		    String error = "Unable to continue. Tool content id missing";
		    logger.error(error);
			throw new NbApplicationException(error);
		}
		Long content_id = NbWebUtil.convertToLong(nbForm.getToolContentId());
		
		//throws exception if the content id does not exist
		checkContentId(content_id);
		
		NoticeboardContent nbContent = nbService.retrieveNoticeboard(content_id);
		nbForm.copyValuesIntoNbContent(nbContent);
		/* Author has finished editing the content and mark the defineLater flag to false */
		nbContent.setDefineLater(false);
		nbService.updateNoticeboard(nbContent);
			
		return mapping.findForward(NoticeboardConstants.AUTHOR_PAGE);
	}
	
	/**
	 * This method will either upload an online instructions file or an offline instructions file. 
	 * It will upload an online file if the bean property onlineFile is not null and similarly,
	 * will upload an offline file if the bean property offlineFile is not null.
	 * By using the term "upload", we are saving the file information on the local database (?)
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws NbApplicationException
	 */
	public ActionForward upload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws InvalidParameterException, FileNotFoundException, RepositoryCheckedException, IOException, NbApplicationException {
	    		
	    
	    	//set up the values in the map
	    	//call the uploadFile method from toolContentHandler
	    	NbAuthoringForm nbForm = (NbAuthoringForm)form;
	    	copyAuthoringFormValuesIntoFormBean(request, nbForm);
	    	FormFile theFile;
	    	INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
			
	    	Long content_id = NbWebUtil.convertToLong(nbForm.getToolContentId());
			//throws exception if the content id does not exist
			checkContentId(content_id);
			NoticeboardContent nbContent = nbService.retrieveNoticeboard(content_id);
	    	
	    	//check if the file uploaded is an online instructions file or offline instructions file.
	    	//if one of the types is null, then the other one must have been uploaded. 
	    	//here we check if the file is an online one
			
			if ((nbForm.getOnlineFile() != null && (nbForm.getOnlineFile().getFileName().trim().length() != 0)) ||
			        (nbForm.getOfflineFile() != null && (nbForm.getOfflineFile().getFileName().trim().length() != 0)))
			{
		    	boolean isOnlineFile = ((nbForm.getOnlineFile() != null && (nbForm.getOnlineFile().getFileName().trim().length() != 0)) ? true: false );
		    	theFile = (isOnlineFile ? nbForm.getOnlineFile() : nbForm.getOfflineFile());
		    	String fileType = isOnlineFile ? NoticeboardAttachment.TYPE_ONLINE : NoticeboardAttachment.TYPE_OFFLINE;
		    	
		    	//check to see if FileExists
		    	NoticeboardAttachment file = nbService.retrieveAttachmentByFilename(theFile.getFileName());
		    	
		    		    			    	
		    	try
		    	{
			    	
		    	    if (fileExists(content_id, file, isOnlineFile))
		    		{
		    	        /**
		    	         * The same file belonging to the same toolcontent id already exists.
		    	         * The old version of this file is deleted from the content repository 
		    	         * and the new one is saved in the content repository.
		    	         * 
		    	         * The entry in the database is then updated with the new uuid and version
		    	         */
		    	        getToolContentHandler().deleteFile(file.getUuid());
		    	        
		    	        nbService.removeAttachment(file);
		    	        
		    	      /*  NodeKey node = getToolContentHandler().uploadFile(theFile.getInputStream(), theFile.getFileName(), 
		                        theFile.getContentType(), fileType); 
		    	        
		    	        file.setUuid(node.getUuid()); //only need to update the uuid, the rest of the info is the same
		    	        file.setVersionId(node.getVersion());
		    	        nbService.saveAttachment(file); */
		    	        
		    		}
		    	        /**
		    	         * This is a new file and so is saved to the content repository.
		    	         * 
		    	         * A new entry is added to the database.
		    	         */
		    	        NodeKey node = getToolContentHandler().uploadFile(theFile.getInputStream(), theFile.getFileName(), 
		                        theFile.getContentType(), fileType); 
		    	        file = new NoticeboardAttachment();
			    	    file.setFilename(theFile.getFileName());
			    	   	file.setOnlineFile(isOnlineFile);
				    	file.setNbContent(nbContent);
				    	file.setUuid(node.getUuid());
				    	file.setVersionId(node.getVersion()); /** TODO: change versionId to version */
				    	nbService.saveAttachment(file);
		    	    
		    	    
			    	String keyName = file.returnKeyName();
				    
			    	//add the files to the map
			    	Map attachmentMap = nbForm.getAttachments();
			    	if (!attachmentMap.containsKey(keyName))
			    	{
			    	    attachmentMap.put(keyName, file);
			    	}
			    	
			    	NbWebUtil.addUploadsToSession(request, attachmentMap);
			    	//reset the fields so that more files can be uploaded
			    	nbForm.setOfflineFile(null);
			    	nbForm.setOnlineFile(null);
		    	}
		    	catch (FileNotFoundException e) {
		            logger.error("Unable to uploadfile",e);
		            throw new NbApplicationException("Unable to upload file, exception was "+e.getMessage());
		    	} catch (IOException e) {
		            logger.error("Unable to uploadfile",e);
		            throw new NbApplicationException("Unable to upload file, exception was "+e.getMessage());
		    	} catch (RepositoryCheckedException e) {
		            logger.error("Unable to uploadfile",e);
		            throw new NbApplicationException("Unable to upload file, exception was "+e.getMessage());
		    	}			    	
			}
	  
			nbForm.setMethod(NoticeboardConstants.INSTRUCTIONS);
			
			return mapping.findForward(NoticeboardConstants.AUTHOR_PAGE);
	}
	
	
	public ActionForward deleteAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws InvalidParameterException, RepositoryCheckedException, NbApplicationException {
	
	    Long uuid = NbWebUtil.convertToLong(request.getParameter(NoticeboardConstants.UUID));
	    
	    NbAuthoringForm nbForm = (NbAuthoringForm)form;
	   // copyAuthoringFormValuesIntoFormBean(request, nbForm);
	    
    	INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
	    
    	if (uuid == null)
    	{
    	    String error = "Unable to continue. The file uuid is missing.";
			logger.error(error);
			throw new NbApplicationException(error);
    	}
    	NoticeboardAttachment attachment = nbService.retrieveAttachmentByUuid(uuid);
       	String keyName = attachment.returnKeyName();
    	
    	//remove entry from map
    	Map attachmentMap = nbForm.getAttachments();
    	attachmentMap.remove(keyName);
    	NbWebUtil.addUploadsToSession(request, attachmentMap);
    	
    	//remove entry from content repository
    	try
    	{
    	    getToolContentHandler().deleteFile(uuid);
    	}
    	catch (RepositoryCheckedException e) {
            logger.error("Unable to delete file",e);
            throw new NbApplicationException("Unable to delete file, exception was "+e.getMessage());
    	}		
    	
    	//remove entry from db
    	nbService.removeAttachment(attachment);
    	
    	nbForm.setMethod(NoticeboardConstants.INSTRUCTIONS);
	   
	   	return mapping.findForward(NoticeboardConstants.AUTHOR_PAGE);
	}
	
	
	
	/**
	 * It is assumed that the contentId is passed as a http parameter
	 * if the contentId is null, an exception is thrown, otherwise proceed as normal
	 * 
	 * @param contentId the <code>toolContentId</code> to check
	 */
	private void checkContentId(Long contentId)
	{
	    if (contentId == null)
		{
			String error = "Unable to continue. Tool content id missing.";
			
			throw new NbApplicationException(error);
		}
	}
	
	/**
	 * This method copies the values of the request parameters <code>richTextOnlineInstructions</code>
	 * <code>richTextOfflineInstructions</code> <code>richTextContent</code> into the form properties
	 * onlineInstructions, offlineInstructions and content respectively.
	 * If a null value is returned for the request parameter, the form value is not modified.
	 * The request parameters are set as optional because the form spans amongst two pages. 
	 * 
	 * @param request HttpServlet request
	 * @param form The ActionForm class containing data submitted by the forms.
	 */
	private void copyAuthoringFormValuesIntoFormBean(HttpServletRequest request, NbAuthoringForm form)
	{
	    String onlineInstruction = WebUtil.readStrParam(request, NoticeboardConstants.RICH_TEXT_ONLINE_INSTRN, true);
	    String offlineInstruction = WebUtil.readStrParam(request, NoticeboardConstants.RICH_TEXT_OFFLINE_INSTRN, true);
	  	String content = WebUtil.readStrParam(request, NoticeboardConstants.RICH_TEXT_CONTENT, true);
	  	String title = WebUtil.readStrParam(request, NoticeboardConstants.RICH_TEXT_TITLE, true);

	  	if(title != null)
	  	    form.setTitle(title);
	  	if(content != null)
	  	    form.setContent(content);
	  	if(onlineInstruction != null)
	        form.setOnlineInstructions(onlineInstruction);
	  	if(offlineInstruction != null)
	  	    form.setOfflineInstructions(offlineInstruction);
	  
	}
	/**
	 * This method checks whether a file
	 * already exists in the database. If this file already exists, then the 
	 * type of file is checked, along with the associated toolContentId. 
	 * If both files are also of the same type, ie.
	 * both are offline files or both are online files and the tool content Id matches
	 * <code>toolContentId</code>, then this method will 
	 * return true, to indicate that the file already exists in the database. 
	 * Otherwise false is returned. 
	 * @param filename The filename of the attachment to check
	 * @param isOnlineFile A boolean to indicate whether it is an online File. 1 indicates an online file. The value 0 indicates an offline file.
	 * @return
	 */
	private boolean fileExists(Long toolContentId, NoticeboardAttachment fileFromDatabase, boolean isFileUploadedAnOnlineFile)
	{
	    /**
	     * TODO: check whether they refer to the same contentIdk
	     */
	   if (fileFromDatabase == null)
	   {
	       return false;
	   }
	   else
	   {
	       /**
	        * true && true = true <-- both files are both online files
	        * true && false = false <-- the files are different types
	        * false && true = false <-- the files are different types
	        * false && false = true <-- both files are offline files
	        */
	       if (fileFromDatabase.isOnlineFile() && isFileUploadedAnOnlineFile && fileFromDatabase.getNbContent().getNbContentId().equals(toolContentId))
	       {
	           return true;
	       }
	       else
	           return false;
	   }
	}

	
	
	
	
	
	
	
	
	
	
}