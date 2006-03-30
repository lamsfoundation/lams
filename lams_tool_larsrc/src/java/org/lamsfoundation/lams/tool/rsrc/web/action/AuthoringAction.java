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
package org.lamsfoundation.lams.tool.rsrc.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.validator.UrlValidator;
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
import org.lamsfoundation.lams.tool.rsrc.service.IResourceService;
import org.lamsfoundation.lams.tool.rsrc.service.ResourceApplicationException;
import org.lamsfoundation.lams.tool.rsrc.web.form.ResourceForm;
import org.lamsfoundation.lams.tool.rsrc.web.form.ResourceItemForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sun.org.apache.bcel.internal.generic.Instruction;

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
	  	if (param.equals("initPage")) {
//	  		request.getSession().setAttribute(ForumConstants.MODE,ForumConstants.AUTHOR_MODE);
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
        return mapping.findForward(ResourceConstants.ERROR);
	}
	
	private ActionForward removeItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		int itemIdx = NumberUtils.stringToInt(request.getParameter(ResourceConstants.PARAM_ITEM_INDEX),-1);
		if(itemIdx != -1){
			List<ResourceItem> resourceList = getResourceList(request);
			ResourceItem item = resourceList.remove(itemIdx);
			List delList = getDeletedTopicList(request);
			delList.add(item);
		}		
		return mapping.findForward(ResourceConstants.SUCCESS);
	}

	private ActionForward editItemInit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		int itemIdx = NumberUtils.stringToInt(request.getParameter(ResourceConstants.PARAM_ITEM_INDEX),-1);
		ResourceItem item = null;
		if(itemIdx != -1){
			List<ResourceItem> resourceList = getResourceList(request);
			item = resourceList.get(itemIdx);
			if(item != null){
				popuplateItemToForm(itemIdx, item,(ResourceItemForm) form,request);
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
	private ActionForward saveOrUpdateItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		//get instructions:
		List<String> instructionList = getInstructionsFromRequest(request);
		
		ResourceItemForm itemForm = (ResourceItemForm)form;
		ActionErrors errors = validateResourceItem(itemForm);
			
		if(!errors.isEmpty()){
			this.addErrors(request,errors);
			request.setAttribute(ResourceConstants.ATTR_INSTRUCTION_LIST,instructionList);
			return findForward(itemForm.getItemType(),mapping);
		}
		
		//check whether it is "edit(old item)" or "add(new item)"
		List resourceList = getResourceList(request);
		int itemIdx = NumberUtils.stringToInt(itemForm.getItemIndex(),-1);
		ResourceItem item;
		if(itemIdx == -1){
			item = new ResourceItem();
			resourceList.add(item);
		}else
			item = (ResourceItem) resourceList.get(itemIdx);
		item.setType(ResourceConstants.RESOURCE_TYPE_URL);
		item.setTitle(itemForm.getTitle());
		item.setUrl(itemForm.getUrl());
//		TODO: set author when persist:need persit the author as well if required
//		HttpSession ss = SessionManager.getSession();
//		//get back login user DTO
//		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
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

	private ActionForward initPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		Long contentId = new Long(WebUtil.readLongParam(request,ResourceConstants.PARAM_TOOL_CONTENT_ID));
		ResourceForm resourceForm = (ResourceForm)form;
		//get back the topic list and display them on page
		IResourceService service = getResourceService();

		Set item = null;
		Resource resource = null;
		try {
			resource = service.getResourceByContentId(contentId);
			//if resource does not exist, try to use default content instead.
			if(resource == null){
				resource = service.getDefaultContent(contentId);
				if(resource.getResourceItems() != null){
					item = resource.getResourceItems();
				}else
					item = null;
			}else
				item = service.getAuthoredItems(resource.getUid());
			//initialize instruction attachment list
			List attachmentList = getAttachmentList(request);
			attachmentList.addAll(resource.getAttachments());

			resourceForm.setResource(resource);
		} catch (Exception e) {
			log.error(e);
			return mapping.findForward("error");
		}
		
		//set back STRUTS component value
		//init it to avoid null exception in following handling
		if(item == null)
			item = new HashSet();
		request.getSession().setAttribute(ResourceConstants.ATTR_RESOURCE_LIST, new ArrayList(item));
		return mapping.findForward(ResourceConstants.SUCCESS);
	}

	private ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return null;
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
		} catch (ResourceApplicationException e) {
			log.error("Upload instruction attachment failed:" + e.getMessage());
		}
		
		return mapping.findForward("success");

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
		return deleteFile(request, response,form, IToolContentHandler.TYPE_OFFLINE);
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
		return deleteFile(request, response,form, IToolContentHandler.TYPE_ONLINE);
	}

	/**
	 * @param request
	 * @param response
	 * @param form 
	 * @param type 
	 * @return
	 */
	private ActionForward deleteFile(HttpServletRequest request, HttpServletResponse response, ActionForm form, String type) {
		Long contentID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		Long versionID = new Long(WebUtil.readLongParam(request,"versionID"));
		Long uuID = new Long(WebUtil.readLongParam(request,"uuID"));
		
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
		StringBuffer sb = new StringBuffer();
		iter = leftAttachments.iterator();
		while(iter.hasNext()){
//			ResourceAttachment file = (ResourceAttachment) iter.next();
//			sb.append("<li>").append(file.getFileName()).append("\r\n");
//			sb.append(" <a href=\"javascript:launchInstructionsPopup('download/?uuid=").append(file.getFileUuid()).append("&preferDownload=false')\">");
//			sb.append(this.getResources(request).getMessage("label.view"));
//			sb.append("</a>\r\n");
//			sb.append(" <a href=\"../download/?uuid=").append(file.getFileUuid()).append("&preferDownload=true\">");
//			sb.append(this.getResources(request).getMessage("label.download"));
//			sb.append("</a>\r\n");
//			sb.append("<a href=\"javascript:loadDoc('");
//			sb.append(ForumConstants.TOOL_URL_BASE);
//			sb.append("deletefile.do?method=");
//			if(StringUtils.equals(type,IToolContentHandler.TYPE_OFFLINE))
//				sb.append("deleteOffline");
//			else
//				sb.append("deleteOnline");
//			sb.append("File&toolContentID=").append(contentID);
//			sb.append("&uuID=").append(file.getFileUuid()).append("&versionID=").append(file.getFileVersionId()).append("','");
//			if(StringUtils.equals(type,IToolContentHandler.TYPE_OFFLINE))
//				sb.append("offlinefile");
//			else
//				sb.append("onlinefile");
//			sb.append("')\">");
//			
//			if(StringUtils.equals(type,IToolContentHandler.TYPE_OFFLINE))
//				sb.append(this.getResources(request).getMessage("label.authoring.offline.delete"));
//			else
//				sb.append(this.getResources(request).getMessage("label.authoring.online.delete"));
//			sb.append("</a></li>\r\n");
		}
		try {
			PrintWriter out = response.getWriter();
			out.print(sb.toString());
			out.flush();
		} catch (IOException e) {
			log.error(e);
		}
		return null;
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
	private List getDeletedAttachmentList(HttpServletRequest request) {
		return getListFromSession(request,ResourceConstants.ATTR_DELETED_ATTACHMENT_LIST);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getDeletedTopicList(HttpServletRequest request) {
		return getListFromSession(request,ResourceConstants.ATTR_DELETED_RESOURCE_LIST);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getResourceList(HttpServletRequest request) {
		return getListFromSession(request,ResourceConstants.ATTR_RESOURCE_LIST);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getAttachmentList(HttpServletRequest request) {
		return getListFromSession(request,ResourceConstants.ATT_ATTACHMENT_LIST);
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
	private void popuplateItemToForm(int itemIdx, ResourceItem item, ResourceItemForm form, HttpServletRequest request) {
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
		request.setAttribute(ResourceConstants.ATTR_INSTRUCTION_LIST,instructions);
		
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
		if(itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_WEBSITE 
				||itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT){
			if(StringUtils.isBlank(itemForm.getDescription()))
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(ResourceConstants.ERROR_MSG_DESC_BLANK));
		}
		if(itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_WEBSITE 
				||itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT
				||itemForm.getItemType() == ResourceConstants.RESOURCE_TYPE_FILE){
			if(itemForm.getFile() == null)
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(ResourceConstants.ERROR_MSG_FILE_BLANK));
		}
		return errors;
	}


}
