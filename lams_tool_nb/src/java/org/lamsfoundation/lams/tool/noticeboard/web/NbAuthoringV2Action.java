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
 * Created on Aug 8, 2005
 *
 */
package org.lamsfoundation.lams.tool.noticeboard.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
//import org.apache.struts.actions.LookupDispatchAction;
import org.lamsfoundation.lams.web.action.LamsLookupDispatchAction;
//import org.lamsfoundation.lams.web.action.LamsAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardAttachment;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.util.NbWebUtil;
import org.lamsfoundation.lams.tool.noticeboard.util.NbToolContentHandler;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.lamsfoundation.lams.util.WebUtil;

//import org.lamsfoundation.lams.contentrepository.CrNodeVersionProperty;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author mtruong
 * 
 * <p>This class is a simple combination of NbAuthoringStarterAction and NbAuthoringAction.
 * It has been created for the purpose of supporting the new authoring page which is done using 
 * DHTML.</p>
 * 
 * <p>The unspecified method, is the same as the execute method for NbAuthoringStarterAction.
 * It will get called when the method parameter is not specified (that is on first entry
 * into the authoring environment).</p>
 * 
 * <p> The save, upload and delete method is the same as that of NbAuthoringAction, to see its explanation,
 * please see org.lamsfoundation.lams.tool.noticeboard.web.NbAuthoringAction </p>
 *
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/authoringv2" name="NbAuthoringForm" scope="session"
 * 				  type="org.lamsfoundation.lams.tool.noticeboard.web.NbAuthoringV2Action"
 *                parameter="method" validate="true" input="/author_page.jsp"
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
 * @struts:action-forward name="authoringContent2" path="/author_page.jsp"
 * @struts:action-forward name="displayMessage" path=".message"
 * 
 * ----------------XDoclet Tags--------------------
 */

public class NbAuthoringV2Action extends LamsLookupDispatchAction {
    static Logger logger = Logger.getLogger(NbAuthoringV2Action.class.getName());
    public final static String FORM="NbAuthoringForm";
    
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
		map.put(NoticeboardConstants.BUTTON_SAVE, "save");
		map.put(NoticeboardConstants.BUTTON_UPLOAD, "upload");
		map.put(NoticeboardConstants.LINK_DELETE, "deleteAttachment");
		
		return map;
	}

    
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws NbApplicationException {
        
        MessageResources resources = getResources(request);
	    
	    //to ensure that we are working with a new form, not one from previous session
		NbAuthoringForm nbForm = new NbAuthoringForm();		
		NbWebUtil.cleanAuthoringSession(request); 
		
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

		request.getSession().setAttribute(NoticeboardConstants.TOOL_CONTENT_ID, contentId);
							
		/*
		 * Retrieve the Service
		 */
		INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
		Map attachmentMap = nbForm.getAttachments();
		
		if (!contentExists(nbService, contentId))
		{
			//	Pre-fill the form with the default content
			//NoticeboardContent nb =	nbService.retrieveNoticeboard(NoticeboardConstants.DEFAULT_CONTENT_ID);
		    Long defaultToolContentId = nbService.getToolDefaultContentIdBySignature(NoticeboardConstants.TOOL_SIGNATURE);
		  //  logger.debug("Default tool content id is " + defaultToolContentId);
		    NoticeboardContent nb = nbService.retrieveNoticeboard(defaultToolContentId);
			
			/** TODO: add a check to see if object is null */
		    if (nb==null)
		    {
		        String error= "There is data missing in the database";
		        logger.error(error);
		        throw new NbApplicationException(error);
		    }
			
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
			    
			    /** TODO: setup values in the instructions map */
			 
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
			
			//Setup the map containing the files that have been uploaded for this particular tool content id
						
			List attachmentIdList = nbService.getAttachmentIdsFromContent(nb);
			for (int i=0; i<attachmentIdList.size(); i++)
			{
			    NoticeboardAttachment file = nbService.retrieveAttachment((Long)attachmentIdList.get(i));
			    String fileType = file.returnFileType();
			    String keyName = file.getFilename() + "-" + fileType;
			    attachmentMap.put(keyName, file);
			}
			nbForm.setAttachments(attachmentMap);
			
			
		
		}
		NbWebUtil.addUploadsToSession(request, attachmentMap);
		request.getSession().setAttribute(FORM, nbForm);
	
    		return mapping.findForward(NoticeboardConstants.AUTHOR_PAGE2);
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
    				
    			return mapping.findForward(NoticeboardConstants.AUTHOR_PAGE2);
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
    					    	file.setVersionId(node.getVersion()); 
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
    				
    				return mapping.findForward(NoticeboardConstants.AUTHOR_PAGE2);
    		}
    		
    		
    		public ActionForward deleteAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    			throws InvalidParameterException, RepositoryCheckedException, NbApplicationException {
    		
    		    Long uuid = NbWebUtil.convertToLong(request.getParameter(NoticeboardConstants.UUID));
    		    
    		    NbAuthoringForm nbForm = (NbAuthoringForm)form;
    		 //  copyAuthoringFormValuesIntoFormBean(request, nbForm);
    		    
    	    	INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
    		    
    	    	if (uuid == null)
    	    	{
    	    	    String error = "Unable to continue. The file uuid is missing.";
    				logger.error(error);
    				throw new NbApplicationException(error);
    	    	}
    	    	NoticeboardAttachment attachment = nbService.retrieveAttachmentByUuid(uuid);
    	    	if (attachment == null)
    	    	{
    	    	    String error = "Unable to continue. The file does not exist";
					logger.error(error);
					throw new NbApplicationException(error);
	    	    	    
    	    	}
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
    		   
    		   	return mapping.findForward(NoticeboardConstants.AUTHOR_PAGE2);
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

    		  	
    		  	    form.setTitle(title);
    		  	
    		  	    form.setContent(content);
    		  	
    		        form.setOnlineInstructions(onlineInstruction);
    		  	
    		  	    form.setOfflineInstructions(offlineInstruction);
    		  
    		} 
    		
    	/*	private void copyFormValuesIntoNbContent(HttpServletRequest request, NoticeboardContent nbContent)
    		{
    		    nbContent.setTitle((String)request.getParameter(NoticeboardConstants.RICH_TEXT_TITLE));
    		    nbContent.setContent((String)request.getParameter(NoticeboardConstants.RICH_TEXT_CONTENT));
    		    nbContent.setOnlineInstructions((String)request.getParameter(NoticeboardConstants.RICH_TEXT_ONLINE_INSTRN));
    		    nbContent.setOfflineInstructions((String)request.getParameter(NoticeboardConstants.RICH_TEXT_OFFLINE_INSTRN));
    		    
    		} */
    		
    		
    		
    		
    		
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
	
