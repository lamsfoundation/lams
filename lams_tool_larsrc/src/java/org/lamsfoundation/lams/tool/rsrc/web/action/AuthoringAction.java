/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
package org.lamsfoundation.lams.tool.rsrc.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.rsrc.ResourceConstants;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceAttachment;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItemInstruction;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceUser;
import org.lamsfoundation.lams.tool.rsrc.service.IResourceService;
import org.lamsfoundation.lams.tool.rsrc.service.ResourceApplicationException;
import org.lamsfoundation.lams.tool.rsrc.service.UploadResourceFileException;
import org.lamsfoundation.lams.tool.rsrc.web.form.ResourceForm;
import org.lamsfoundation.lams.tool.rsrc.web.form.ResourceItemForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.tool.rsrc.web.form.InstructionNavForm;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * @author Steve.Ni
 * @version $Revision$
 */
public class AuthoringAction extends Action {
	private static final int INIT_INSTRUCTION_COUNT = 2;
	private static final String INSTRUCTION_ITEM_DESC_PREFIX = "instructionItemDesc";
	private static final String INSTRUCTION_ITEM_COUNT = "instructionCount";
	private static final String ITEM_TYPE = "itemType";
	
	private static Logger log = Logger.getLogger(AuthoringAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String param = mapping.getParameter();
		//-----------------------Resource Author function ---------------------------
		if(param.equals("start")){
			request.getSession().setAttribute(ResourceConstants.MODE,ResourceConstants.AUTHOR_MODE);
			return start(mapping, form, request, response);
		}
	  	if (param.equals("initPage")) {
       		return initPage(mapping, form, request, response);
        }
//	  	if (param.equals("monitoringInitPage")) {
////	  		request.getSession().setAttribute(ForumConstants.MODE,ForumConstants.MONITOR_MODE);
//	  		return initPage(mapping, form, request, response);
//	  	}
        if (param.equals("updateContent")) {
       		return updateContent(mapping, form, request, response);
        }
        if (param.equals("uploadOnlineFile")) {
       		return uploadOnline(mapping, form, request, response);
        }
        if (param.equals("uploadOfflineFile")) {
       		return uploadOffline(mapping, form, request, response);
        }
        if (param.equals("deleteOnlineFile")) {
        	return deleteOnlineFile(mapping, form, request, response);
        }
        if (param.equals("deleteOfflineFile")) {
        	return deleteOfflineFile(mapping, form, request, response);
        }
        //----------------------- Add resource item function ---------------------------
        if (param.equals("newItemInit")) {
        	return newItemlInit(mapping, form, request, response);
        }
        if (param.equals("editItemInit")) {
        	return editItemInit(mapping, form, request, response);
        }
        if (param.equals("saveOrUpdateItem")) {
        	return saveOrUpdateItem(mapping, form, request, response);
        }
        if (param.equals("removeItem")) {
        	return removeItem(mapping, form, request, response);
        }
        //-----------------------Resource Item Instruction function ---------------------------
	  	if (param.equals("newInstruction")) {
       		return newInstruction(mapping, form, request, response);
        }
	  	if (param.equals("removeInstruction")) {
	  		return removeInstruction(mapping, form, request, response);
	  	}
	  	if (param.equals("removeItemAttachment")) {
	  		return removeItemAttachment(mapping, form, request, response);
	  	}
	  	//for preview top frame html page use:
	  	if (param.equals("nextInstruction")) {
	  		return nextInstruction(mapping, form, request, response);
	  	}
	  	
	    //-----------------------Display Learning Object function ---------------------------
	  	if (param.equals("reviewItem")) {
       		return reviewItem(mapping, form, request, response);
        }
        return mapping.findForward(ResourceConstants.ERROR);
	}


	private ActionForward nextInstruction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		InstructionNavForm navForm = (InstructionNavForm) form;
		navForm.setCurrent(navForm.getCurrent()+1);
		return findForward(navForm.getType(),mapping);
	}


	private ActionForward removeItemAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("itemAttachment", null);
    	return mapping.findForward(ResourceConstants.SUCCESS);
    }


	private ActionForward reviewItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		int itemIdx = NumberUtils.stringToInt(request.getParameter(ResourceConstants.PARAM_ITEM_INDEX),-1);
		if(itemIdx != -1){
			List<ResourceItem> resourceList = getResourceItemList(request);
			ResourceItem item = resourceList.get(itemIdx);
			
			Set instructions = item.getItemInstructions();
			InstructionNavForm navForm = (InstructionNavForm) form;
			navForm.setTitle(item.getTitle());
			navForm.setType(item.getType());
			navForm.setCurrent(1);
			navForm.setTotal(instructions.size());
			navForm.setInstructions(instructions);
			if(item.getType() == ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT)
				request.getSession().setAttribute(ResourceConstants.ATT_LEARNING_OBJECT,item);
			//set url to content frame
			request.setAttribute(ResourceConstants.ATTR_RESOURCE_REVIEW_URL,getReviewUrl(item));
			return mapping.findForward(ResourceConstants.SUCCESS);
		}
		return mapping.findForward(ResourceConstants.ERROR);
	}


	private ActionForward removeItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		int itemIdx = NumberUtils.stringToInt(request.getParameter(ResourceConstants.PARAM_ITEM_INDEX),-1);
		if(itemIdx != -1){
			List<ResourceItem> resourceList = getResourceItemList(request);
			ResourceItem item = resourceList.remove(itemIdx);
			List delList = getDeletedResourceItemList(request);
			delList.add(item);
		}		
		return mapping.findForward(ResourceConstants.SUCCESS);
	}

	private ActionForward editItemInit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		int itemIdx = NumberUtils.stringToInt(request.getParameter(ResourceConstants.PARAM_ITEM_INDEX),-1);
		ResourceItem item = null;
		if(itemIdx != -1){
			List<ResourceItem> resourceList = getResourceItemList(request);
			item = resourceList.get(itemIdx);
			if(item != null){
				populateItemToForm(itemIdx, item,(ResourceItemForm) form,request);
			}
		}		
		return findForward(item==null?-1:item.getType(),mapping);
	}
	private ActionForward newItemlInit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		short type = (short) NumberUtils.stringToInt(request.getParameter(ITEM_TYPE));
		List instructionList = new ArrayList(INIT_INSTRUCTION_COUNT);
		for(int idx=0;idx<INIT_INSTRUCTION_COUNT;idx++){
			instructionList.add("");
		}
		request.setAttribute("instructionList",instructionList);
		return findForward(type,mapping);
	}
	/**
	 * This method will get necessary information from resource item form and save or update into 
	 * <code>HttpSession</code> ResourceItemList. Notice, this save is not persist them into database,  
	 * just save <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring 
	 * page is being persisted.
	 *  
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 */
	private ActionForward saveOrUpdateItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
		throws ServletException {
		//get instructions:
		List<String> instructionList = getInstructionsFromRequest(request);
		
		ResourceItemForm itemForm = (ResourceItemForm)form;
		ActionErrors errors = validateResourceItem(itemForm);
		
		if(!errors.isEmpty()){
			this.addErrors(request,errors);
			request.setAttribute(ResourceConstants.ATTR_INSTRUCTION_LIST,instructionList);
			return findForward(itemForm.getItemType(),mapping);
		}
		
		try {
			extractFormToResourceItem(request, instructionList, itemForm);
		} catch (UploadResourceFileException e) {
			log.error("Uploading failed. The exception is " + e.toString());
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(ResourceConstants.ERROR_MSG_UPLOAD_FAILED,e.getMessage()));
			if(!errors.isEmpty()){
				this.addErrors(request,errors);
				request.setAttribute(ResourceConstants.ATTR_INSTRUCTION_LIST,instructionList);
				return findForward(itemForm.getItemType(),mapping);
			}
		}
		
		//return null to close this window
		return mapping.findForward(ResourceConstants.SUCCESS);
	}


	private ActionForward newInstruction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		int count = NumberUtils.stringToInt(request.getParameter(INSTRUCTION_ITEM_COUNT),0);
		List instructionList = new ArrayList(++count);
		for(int idx=0;idx<count;idx++){
			String item = request.getParameter(INSTRUCTION_ITEM_DESC_PREFIX+idx);
			if(item == null)
				instructionList.add("");
			else
				instructionList.add(item);
		}
		request.setAttribute(ResourceConstants.ATTR_INSTRUCTION_LIST,instructionList);
		return mapping.findForward(ResourceConstants.SUCCESS);
	}
	private ActionForward removeInstruction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		int count = NumberUtils.stringToInt(request.getParameter(INSTRUCTION_ITEM_COUNT),0);
		int removeIdx = NumberUtils.stringToInt(request.getParameter("removeIdx"),-1);
		List instructionList = new ArrayList(count-1);
		for(int idx=0;idx<count;idx++){
			String item = request.getParameter(INSTRUCTION_ITEM_DESC_PREFIX+idx);
			if(idx == removeIdx)
				continue;
			if(item == null)
				instructionList.add("");
			else
				instructionList.add(item);
		}
		request.setAttribute(ResourceConstants.ATTR_INSTRUCTION_LIST,instructionList);
		return mapping.findForward(ResourceConstants.SUCCESS);
	}
	//******************************************************************************************************************
	//              Resource Author functions
	//******************************************************************************************************************
	/**
	 * Read resource data from database and put them into HttpSession. It will redirect to init.do directly after this
	 * method run successfully. 
	 *  
	 * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
	 * 
	 */
	private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		//save toolContentID into HTTPSession
		Long contentId = new Long(WebUtil.readLongParam(request,ResourceConstants.PARAM_TOOL_CONTENT_ID));
		request.getSession().setAttribute(ResourceConstants.ATTR_TOOL_CONTENT_ID,contentId);
		
//		get back the resource and item list and display them on page
		IResourceService service = getResourceService();

		List items = null;
		Resource resource = null;
		ResourceForm resourceForm = (ResourceForm)form;
		try {
			resource = service.getResourceByContentId(contentId);
			//if resource does not exist, try to use default content instead.
			if(resource == null){
				resource = service.getDefaultContent(contentId);
				if(resource.getResourceItems() != null){
					items = new ArrayList(resource.getResourceItems());
				}else
					items = null;
			}else
				items = service.getAuthoredItems(resource.getUid());
			
			resourceForm.setResource(resource);

			//initialize instruction attachment list
			List attachmentList = getAttachmentList(request);
			attachmentList.clear();
			attachmentList.addAll(resource.getAttachments());
		} catch (Exception e) {
			log.error(e);
			return mapping.findForward(ResourceConstants.ERROR);
		}
		
		//init it to avoid null exception in following handling
		if(items == null)
			items = new ArrayList();
		
		//init resource item list
		List resourceItemList = getResourceItemList(request);
		resourceItemList.clear();
		resourceItemList.addAll(items);
		
		return mapping.findForward(ResourceConstants.SUCCESS);
	}



	private ActionForward initPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		return mapping.findForward(ResourceConstants.SUCCESS);
	}
	/**
	 * This method will persist all inforamtion in this authoring page, include all resource item, information etc.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ResourceForm resourceForm = (ResourceForm)(form);
		
		Resource resource = resourceForm.getResource();
		try {
			IResourceService service = getResourceService();
			
			//*******************************Handle user*******************
			//try to get form system session
			HttpSession ss = SessionManager.getSession();
			//get back login user DTO
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			ResourceUser resourceUser = service.getUserByID(new Long(user.getUserID().intValue()));
			if(resourceUser == null){
				resourceUser = new ResourceUser(user,null);
				service.createUser(resourceUser);
			}
			
			//**********************************Get Resource PO*********************
			Resource resourcePO = service.getResourceByContentId(resourceForm.getToolContentID());
			if(resourcePO == null || !resourceForm.getToolContentID().equals(resource.getContentId()) ){
				//new Resource, create it.
				resourcePO = resource;
				resourcePO.setContentId(resourceForm.getToolContentID());
				resourcePO.setCreated(new Timestamp(new Date().getTime()));
				resourcePO.setUpdated(new Timestamp(new Date().getTime()));
			}else{
				Long uid = resourcePO.getUid();
				PropertyUtils.copyProperties(resourcePO,resource);
				//get back UID
				resourcePO.setUid(uid);
				resourcePO.setUpdated(new Timestamp(new Date().getTime()));
			}
			resourcePO.setCreatedBy(resourceUser);
			
			//**********************************Handle Authoring Instruction Attachement *********************
	    	//merge attachment info
			Set attPOSet = resourcePO.getAttachments();
			if(attPOSet == null)
				attPOSet = new HashSet();
			List attachmentList = getAttachmentList(request);
			List deleteAttachmentList = getDeletedAttachmentList(request);
			
			//current attachemnt in authoring instruction tab.
			Iterator iter = attachmentList.iterator();
			while(iter.hasNext()){
				ResourceAttachment newAtt = (ResourceAttachment) iter.next();
				//add new attachment if UID is not null
				if(newAtt.getUid() == null)
					attPOSet.add(newAtt);
			}
			attachmentList.clear();
			
			//deleted attachment. 2 possible types: one is persist another is non-persist before.
			iter = deleteAttachmentList.iterator();
			while(iter.hasNext()){
				ResourceAttachment delAtt = (ResourceAttachment) iter.next();
				iter.remove();
				//delete from repository
				service.deleteFromRepository(delAtt.getFileUuid(),delAtt.getFileVersionId());
				//it is an existed att, then delete it from current attachmentPO
				if(delAtt.getUid() != null){
					Iterator attIter = attPOSet.iterator();
					while(attIter.hasNext()){
						ResourceAttachment att = (ResourceAttachment) attIter.next();
						if(delAtt.getUid().equals(att.getUid())){
							attIter.remove();
							break;
						}
					}
					service.deleteResourceAttachment(delAtt.getUid());
				}//end remove from persist value
			}
			
			//copy back
			resourcePO.setAttachments(attPOSet);
			//************************* Handle resource items *******************
			//Handle resource items
			Set itemList = new LinkedHashSet();
			List topics = getResourceItemList(request);
	    	iter = topics.iterator();
	    	while(iter.hasNext()){
	    		ResourceItem item = (ResourceItem) iter.next();
	    		if(item != null){
    				//This flushs user UID info to message if this user is a new user. 
    				item.setCreateBy(resourceUser);
    				item.setCreateDate(new Timestamp(new Date().getTime()));
    				itemList.add(item);
	    		}
	    	}
	    	resourcePO.setResourceItems(itemList);
	    	//delete them from database.
	    	List delResourceItemList = getDeletedResourceItemList(request);
	    	iter = delResourceItemList.iterator();
	    	while(iter.hasNext()){
	    		ResourceItem item = (ResourceItem) iter.next();
	    		iter.remove();
	    		if(item.getUid() != null)
	    			service.deleteResourceItem(item.getUid());
	    		if(item.getFileUuid() != null && item.getFileVersionId() != null)
	    			service.deleteFromRepository(item.getFileUuid(),item.getFileVersionId());
	    	}
	    	//handle resource item attachment file:
	    	List delItemAttList = getDeletedItemAttachmentList(request);
			iter = delItemAttList.iterator();
			while(iter.hasNext()){
				ResourceItem delAtt = (ResourceItem) iter.next();
				iter.remove();
				//delete from repository
				service.deleteFromRepository(delAtt.getFileUuid(),delAtt.getFileVersionId());
			}
			
			//**********************************************
			//finally persist resourcePO again
			service.saveOrUpdateResource(resourcePO);
			
			//initialize attachmentList again
			attachmentList = getAttachmentList(request);
			attachmentList.addAll(resource.getAttachments());
			resourceForm.setResource(resourcePO);
			
		} catch (Exception e) {
			log.error(e);
		}

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("authoring.save.success"));
		this.addMessages(request,messages);
    	String mode = (String) request.getSession().getAttribute(ResourceConstants.MODE);
    	if(StringUtils.equals(mode,ResourceConstants.AUTHOR_MODE))
    		return mapping.findForward("author");
    	else
    		return mapping.findForward("monitor");
	}
	
	/**
	 * Handle upload online instruction files request. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward uploadOnline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return uploadFile(mapping, form, IToolContentHandler.TYPE_ONLINE,request);
	}
	/**
	 * Handle upload offline instruction files request.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward uploadOffline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return uploadFile(mapping, form, IToolContentHandler.TYPE_OFFLINE,request);
	}
	/**
	 * Common method to upload online or offline instruction files request.
	 * @param mapping
	 * @param form
	 * @param type
	 * @param request
	 * @return
	 */
	private ActionForward uploadFile(ActionMapping mapping, ActionForm form,
			String type,HttpServletRequest request) {

		ResourceForm resourceForm = (ResourceForm) form;
		
		FormFile file;
		if(StringUtils.equals(IToolContentHandler.TYPE_OFFLINE,type))
			file = (FormFile) resourceForm.getOfflineFile();
		else
			file = (FormFile) resourceForm.getOnlineFile();
		
		ActionErrors errors = new ActionErrors();
		try {
			IResourceService service = getResourceService();
			//upload to repository
			ResourceAttachment  att = service.uploadInstructionFile(file, type);
			//handle session value
			List attachmentList = getAttachmentList(request);
			List deleteAttachmentList = getDeletedAttachmentList(request);
			//first check exist attachment and delete old one (if exist) to deletedAttachmentList
			Iterator iter = attachmentList.iterator();
			ResourceAttachment existAtt;
			while(iter.hasNext()){
				existAtt = (ResourceAttachment) iter.next();
				if(StringUtils.equals(existAtt.getFileName(),att.getFileName())){
					//if there is same name attachment, delete old one
					deleteAttachmentList.add(existAtt);
					iter.remove();
					break;
				}
			}
			//add to attachmentList
			attachmentList.add(att);
			
			//update Html FORM, this will echo back to web page for display
			List list;
			if(StringUtils.equals(IToolContentHandler.TYPE_OFFLINE,type)){
				list = resourceForm.getOfflineFileList();
				if(list == null){
					list = new ArrayList();
					resourceForm.setOfflineFileList(list);
				}
			}else{
				list = resourceForm.getOnlineFileList();
				if(list == null){
					list = new ArrayList();
					resourceForm.setOnlineFileList(list);
				}
			}
			list.add(att);
		} catch (UploadResourceFileException e) {
			log.error("Upload instruction attachment failed:" + e.getMessage());
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(ResourceConstants.ERROR_MSG_UPLOAD_FAILED,e.getMessage()));
		}
		
		if(!errors.isEmpty())
			this.addErrors(request,errors);
		
		return mapping.findForward(ResourceConstants.SUCCESS);

	}
	/**
	 * Delete offline instruction file from current Resource authoring page.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward deleteOfflineFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return deleteFile(mapping,request, response,form, IToolContentHandler.TYPE_OFFLINE);
	}
	/**
	 * Delete online instruction file from current Resource authoring page.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward deleteOnlineFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return deleteFile(mapping, request, response,form, IToolContentHandler.TYPE_ONLINE);
	}

	/**
	 * @param mapping 
	 * @param request
	 * @param response
	 * @param form 
	 * @param type 
	 * @return
	 */
	private ActionForward deleteFile(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, ActionForm form, String type) {
		Long versionID = new Long(WebUtil.readLongParam(request,ResourceConstants.PARAM_FILE_VERSION_ID));
		Long uuID = new Long(WebUtil.readLongParam(request,ResourceConstants.PARAM_FILE_UUID));
		
		//handle session value
		List attachmentList = getAttachmentList(request);
		List deleteAttachmentList = getDeletedAttachmentList(request);
		//first check exist attachment and delete old one (if exist) to deletedAttachmentList
		Iterator iter = attachmentList.iterator();
		ResourceAttachment existAtt;
		while(iter.hasNext()){
			existAtt = (ResourceAttachment) iter.next();
			if(existAtt.getFileUuid().equals(uuID) && existAtt.getFileVersionId().equals(versionID)){
				//if there is same name attachment, delete old one
				deleteAttachmentList.add(existAtt);
				//remove from attachemnt
				iter.remove();
				break;
			}
		}
		
		//handle web page display
		List leftAttachments;
		if(StringUtils.equals(IToolContentHandler.TYPE_OFFLINE,type)){
			leftAttachments = ((ResourceForm)form).getOfflineFileList();
		}else{
			leftAttachments = ((ResourceForm)form).getOnlineFileList();
		}
		iter = leftAttachments.iterator();
		while(iter.hasNext()){
			ResourceAttachment att = (ResourceAttachment) iter.next();
			if(versionID.equals(att.getFileVersionId()) && uuID.equals(att.getFileUuid())){
				iter.remove();
				break;
			}
		}
		if(StringUtils.equals(IToolContentHandler.TYPE_OFFLINE,type)){
			request.setAttribute("offlineFileList",leftAttachments);
		}else{
			request.setAttribute("onlineFileList",leftAttachments);
		}
		return mapping.findForward(ResourceConstants.SUCCESS);

	}
	//*************************************************************************************
	// Private method 
	//*************************************************************************************
	private IResourceService getResourceService() {
	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
	      return (IResourceService) wac.getBean(ResourceConstants.RESOURCE_SERVICE);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getAttachmentList(HttpServletRequest request) {
		return getListFromSession(request,ResourceConstants.ATT_ATTACHMENT_LIST);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getDeletedAttachmentList(HttpServletRequest request) {
		return getListFromSession(request,ResourceConstants.ATTR_DELETED_ATTACHMENT_LIST);
	}
	/**
	 * List save current resource items.
	 * @param request
	 * @return
	 */
	private List getResourceItemList(HttpServletRequest request) {
		return getListFromSession(request,ResourceConstants.ATTR_RESOURCE_ITEM_LIST);
	}	
	/**
	 * List save deleted resource items, which could be persisted or non-persisted items. 
	 * @param request
	 * @return
	 */
	private List getDeletedResourceItemList(HttpServletRequest request) {
		return getListFromSession(request,ResourceConstants.ATTR_DELETED_RESOURCE_ITEM_LIST);
	}
	/**
	 * If a resource item has attahment file, and the user edit this item and change the attachment
	 * to new file, then the old file need be deleted when submitting the whole authoring page.
	 * Save the file uuid and version id into ResourceItem object for temporarily use.
	 * @param request
	 * @return
	 */
	private List getDeletedItemAttachmentList(HttpServletRequest request) {
		return getListFromSession(request,ResourceConstants.ATTR_DELETED_RESOURCE_ITEM_ATTACHMENT_LIST);
	}


	/**
	 * Get <code>java.util.List</code> from HttpSession by given name.
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	private List getListFromSession(HttpServletRequest request,String name) {
		List list = (List) request.getSession().getAttribute(name);
		if(list == null){
			list = new ArrayList();
			request.getSession().setAttribute(name,list);
		}
		return list;
	}
	
	
	/**
	 * @param request
	 */
	private List<String> getInstructionsFromRequest(HttpServletRequest request) {
		String list = request.getParameter("instructionList");
		String[] params = list.split("&");
		Map<String,String> paramMap = new HashMap<String,String>();
		String[] pair;
		for (String item: params) {
			pair = item.split("=");
			if(pair == null || pair.length != 2)
				continue;
			try {
				paramMap.put(pair[0],URLDecoder.decode(pair[1],"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				log.error("Error occurs when decode instruction string:" + e.toString());
			}
		}
		
		int count = NumberUtils.stringToInt(paramMap.get(INSTRUCTION_ITEM_COUNT));
		List<String> instructionList = new ArrayList<String>();
		for(int idx=0;idx<count;idx++){
			String item = paramMap.get(INSTRUCTION_ITEM_DESC_PREFIX+idx);
			if(item == null)
				continue;
			instructionList.add(item);
		}
		return instructionList;
	}
	private ActionForward findForward(short type, ActionMapping mapping) {
		ActionForward forward;
		switch (type) {
		case ResourceConstants.RESOURCE_TYPE_URL:
			forward = mapping.findForward("url");
			break;
		case ResourceConstants.RESOURCE_TYPE_FILE:
			forward = mapping.findForward("file");
			break;
		case ResourceConstants.RESOURCE_TYPE_WEBSITE:
			forward = mapping.findForward("website");
			break;
		case ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT:
			forward = mapping.findForward("learningobject");
			break;
		default:
			forward = null;
			break;
		}
		return forward;
	}

	private Object getReviewUrl(ResourceItem item) {
		short type = item.getType();
		String url = null;
		switch (type) {
		case ResourceConstants.RESOURCE_TYPE_URL:
			url = item.getUrl();
			break;
		case ResourceConstants.RESOURCE_TYPE_FILE:
			url = "/download/?uuid="+item.getFileUuid()+"&preferDownload=false";
			break;
		case ResourceConstants.RESOURCE_TYPE_WEBSITE:
			url = "/download/?uuid="+item.getFileUuid()+"&preferDownload=false";
			break;
		case ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT:
			url = "/pages/learningobject/mainframe.jsp";
			break;
		}
		return url;
	}

	/**
	 * This method will populate resource item information to its form for edit use.
	 * @param itemIdx
	 * @param item
	 * @param form
	 * @param request
	 */
	private void populateItemToForm(int itemIdx, ResourceItem item, ResourceItemForm form, HttpServletRequest request) {
		form.setDescription(item.getDescription());
		form.setTitle(item.getTitle());
		form.setUrl(item.getUrl());
		if(itemIdx >=0)
			form.setItemIndex(new Integer(itemIdx).toString());
		
		Set<ResourceItemInstruction> instructionList = item.getItemInstructions();
		List instructions = new ArrayList();
		for(ResourceItemInstruction in : instructionList){
			instructions.add(in.getDescription());
		}
		//add extra blank line for instructions
		for(int idx=0;idx<INIT_INSTRUCTION_COUNT;idx++){
			instructions.add("");
		}
		if(item.getFileUuid() != null){
			form.setFileUuid(item.getFileUuid());
			form.setFileVersionId(item.getFileVersionId());
			form.setFileName(item.getFileName());
			form.setHasFile(true);
		}else
			form.setHasFile(false);
		
		request.setAttribute(ResourceConstants.ATTR_INSTRUCTION_LIST,instructions);
		
	}
	/**
	 *
	 * @param request
	 * @param instructionList
	 * @param itemForm
	 * @throws ResourceApplicationException 
	 */
	private void extractFormToResourceItem(HttpServletRequest request, List<String> instructionList, ResourceItemForm itemForm) 
		throws UploadResourceFileException {
		/* BE CAREFUL: This method will copy nessary info from request form to a old or new ResourceItem instance.
		 * It gets all info EXCEPT ResourceItem.createDate and ResourceItem.createBy, which need be set when persisting 
		 * this resource item.
		 */
		
		//check whether it is "edit(old item)" or "add(new item)"
		List resourceList = getResourceItemList(request);
		int itemIdx = NumberUtils.stringToInt(itemForm.getItemIndex(),-1);
		ResourceItem item;
		
		if(itemIdx == -1){ //add
			item = new ResourceItem();
			resourceList.add(item);
		}else //edit
			item = (ResourceItem) resourceList.get(itemIdx);
		
		short type = itemForm.getItemType();	
		item.setType(itemForm.getItemType());
		/* Set following fields regards to the type:
	    item.setFileUuid();
		item.setFileVersionId();
		item.setFileType();
		item.setFileName();
		
		item.getInitialItem()
		item.setImsSchema()
		item.setOrganizationXml()
		 */
		//if the item is edit (not new add) then the getFile may return null
		//it may throw exception, so put it as first, to avoid other invlidate update: 
		if(itemForm.getFile() != null){
			if(type == ResourceConstants.RESOURCE_TYPE_WEBSITE 
					||type == ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT
					||type == ResourceConstants.RESOURCE_TYPE_FILE){
				//if it has old file, and upload a new, then save old to deleteList
				ResourceItem delAttItem = new ResourceItem();
				boolean hasOld = false;
				if(item.getFileUuid() != null){
					hasOld = true;
					//be careful, This new ResourceItem object never be save into database
					//just temporarily use for saving fileUuid and versionID use:
					delAttItem.setFileUuid(item.getFileUuid());
					delAttItem.setFileVersionId(item.getFileVersionId());
				}
				IResourceService service = getResourceService();
				try {
					service.uploadResourceItemFile(item, itemForm.getFile());
				} catch (UploadResourceFileException e) {
					//if it is new add , then remove it!
					if(itemIdx == -1){ 
						resourceList.remove(item);
					}
					throw e;
				}
				//put it after "upload" to ensure deleted file added into list only no exception happens during upload 
				if(hasOld){
					List delAtt = getDeletedItemAttachmentList(request);
					delAtt.add(delAttItem);
				}
			}
		}
		item.setTitle(itemForm.getTitle());
		item.setCreateByAuthor(true);
		item.setHide(false);
		//set instrcutions
		Set instructions = new LinkedHashSet();
		int idx=0;
		for (String ins : instructionList) {
			ResourceItemInstruction rii = new ResourceItemInstruction();
			rii.setDescription(ins);
			rii.setSequenceId(idx++);
			instructions.add(rii);
		}
		item.setItemInstructions(instructions);

		if(type == ResourceConstants.RESOURCE_TYPE_URL){
			item.setUrl(itemForm.getUrl());
		}
//		if(type == ResourceConstants.RESOURCE_TYPE_WEBSITE 
//				||itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT){
			item.setDescription(itemForm.getDescription());
//		}
		
	}

	/**
	 * @param itemForm
	 * @return
	 */
	private ActionErrors validateResourceItem(ResourceItemForm itemForm) {
		ActionErrors errors = new ActionErrors();
		if(StringUtils.isBlank(itemForm.getTitle()))
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(ResourceConstants.ERROR_MSG_TITLE_BLANK));
		
		if(itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_URL){
			if(StringUtils.isBlank(itemForm.getUrl()))
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(ResourceConstants.ERROR_MSG_URL_BLANK));
			//URL validation: Commom URL validate(1.3.0) work not very well: it can not support http://address:port format!!!
//			UrlValidator validator = new UrlValidator();
//			if(!validator.isValid(itemForm.getUrl()))
//				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(ResourceConstants.ERROR_MSG_INVALID_URL));
		}
//		if(itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_WEBSITE 
//				||itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT){
			if(StringUtils.isBlank(itemForm.getDescription()))
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(ResourceConstants.ERROR_MSG_DESC_BLANK));
//		}
		if(itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_WEBSITE 
				||itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT
				||itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_FILE){
			//for edit validate: file already exist
			if(!itemForm.isHasFile() &&
				(itemForm.getFile() == null || StringUtils.isEmpty(itemForm.getFile().getFileName())))
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(ResourceConstants.ERROR_MSG_FILE_BLANK));
		}
		return errors;
	}


}
