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

/* $Id$ */
package org.lamsfoundation.lams.tool.rsrc.web.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.rsrc.ResourceConstants;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceSession;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceUser;
import org.lamsfoundation.lams.tool.rsrc.service.IResourceService;
import org.lamsfoundation.lams.tool.rsrc.service.ResourceApplicationException;
import org.lamsfoundation.lams.tool.rsrc.service.UploadResourceFileException;
import org.lamsfoundation.lams.tool.rsrc.web.form.ResourceItemForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 * 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class LearningAction extends Action {

	private static Logger log = Logger.getLogger(LearningAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String param = mapping.getParameter();
		request.getSession().setAttribute(AttributeNames.ATTR_MODE,ToolAccessMode.LEARNER);
		//-----------------------Resource Learner function ---------------------------
		if(param.equals("start")){
			return start(mapping, form, request, response);
		}
		if(param.equals("complete")){
			return complete(mapping, form, request, response);
		}

		if(param.equals("finish")){
			return finish(mapping, form, request, response);
		}
        if (param.equals("saveOrUpdateItem")) {
        	return saveOrUpdateItem(mapping, form, request, response);
        }
		return  mapping.findForward(ResourceConstants.ERROR);
	}
	
	private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		//auto run mode, when use finish the only one resource item, mark it as complete then finish this activity as well.
		String resourceItemUid = request.getParameter(ResourceConstants.PARAM_RESOURCE_ITEM_UID);
		if(resourceItemUid != null){
			doComplete(request);
			request.setAttribute(ResourceConstants.ATTR_RUN_AUTO,true);
		}else
			request.setAttribute(ResourceConstants.ATTR_RUN_AUTO,false);
		Long sessionId = (Long) request.getSession().getAttribute(
				AttributeNames.PARAM_TOOL_SESSION_ID);
		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		Long userID = new Long(user.getUserID().longValue());
		
		IResourceService service = getResourceService();
		int miniViewFlag = service.checkMiniView(sessionId,userID);
		//if current user view less than reqired view count number, then just return error message.
		if(miniViewFlag > 0){
			ActionErrors errors = new ActionErrors();
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("lable.learning.minimum.view.number.less",miniViewFlag));
			this.addErrors(request,errors);
			return mapping.getInputForward();
			
		}
		ToolAccessMode mode = (ToolAccessMode) request.getSession().getAttribute(AttributeNames.ATTR_MODE);
		if (mode.isLearner()) {
			// get sessionId from HttpServletRequest
			String nextActivityUrl = null ;
			try {
				nextActivityUrl = service.finishToolSession(sessionId,userID);
				request.setAttribute(ResourceConstants.ATTR_NEXT_ACTIVITY_URL,nextActivityUrl);
			} catch (ResourceApplicationException e) {
				log.error("Failed get next activity url:" + e.getMessage());
			}
			return mapping.findForward("finish");
		}else
			return mapping.findForward("previewfinish");
	}

	private ActionForward complete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		doComplete(request);
		return  mapping.findForward(ResourceConstants.SUCCESS);
	}

	
	/**
	 * Save file or url resource item into database.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward saveOrUpdateItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		ResourceItemForm itemForm = (ResourceItemForm)form;
		ActionErrors errors = validateResourceItem(itemForm);
		
		if(!errors.isEmpty()){
			this.addErrors(request,errors);
			return findForward(itemForm.getItemType(),mapping);
		}
		short type = itemForm.getItemType();
		
		//create a new ResourceItem
		ResourceItem item = new ResourceItem(); 
		IResourceService service = getResourceService();
		//try to get form system session
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		ResourceUser resourceUser = service.getUserByID(new Long(user.getUserID().intValue()));
		if(resourceUser == null){
			resourceUser = new ResourceUser(user,null);
			service.createUser(resourceUser);
		}
		item.setType(type);
		item.setTitle(itemForm.getTitle());
		item.setDescription(itemForm.getDescription());
		item.setCreateDate(new Timestamp(new Date().getTime()));
		item.setCreateByAuthor(false);
		item.setCreateBy(resourceUser);
		
		//special attribute for URL or FILE
		if(type == ResourceConstants.RESOURCE_TYPE_FILE){
			try {
				service.uploadResourceItemFile(item, itemForm.getFile());
			} catch (UploadResourceFileException e) {
				log.error("Failed upload Resource File " + e.toString());
				return  mapping.findForward(ResourceConstants.ERROR);
			}
		}else if(type == ResourceConstants.RESOURCE_TYPE_URL){
			item.setUrl(itemForm.getUrl());
		}
		//save and update session
		Long sessionId = (Long) request.getSession().getAttribute(ResourceConstants.ATTR_TOOL_SESSION_ID);
		ResourceSession resSession = service.getResourceSessionBySessionId(sessionId);
		if(resSession == null){
			log.error("Failed update ResourceSession by ID[" + sessionId + "]");
			return  mapping.findForward(ResourceConstants.ERROR);
		}
		Set<ResourceItem> items = resSession.getResourceItems();
		if(items == null){
			items = new HashSet<ResourceItem>();
			resSession.setResourceItems(items);
		}
		items.add(item);
		service.saveOrUpdateResourceSession(resSession);
		
		//update session value
		List<ResourceItem> resourceItemList = getResourceItemList(request);
		resourceItemList.add(item);
		
		//URL or file upload
		request.setAttribute("addType",new Short(type));
		return  mapping.findForward(ResourceConstants.SUCCESS);
	}
	/**
	 * Read resource data from database and put them into HttpSession. It will redirect to init.do directly after this
	 * method run successfully. 
	 *  
	 * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
	 * 
	 */
	private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		//save toolContentID into HTTPSession
		Long sessionId =  new Long(request.getParameter(ResourceConstants.PARAM_TOOL_SESSION_ID));
		request.getSession().setAttribute(ResourceConstants.ATTR_TOOL_SESSION_ID,sessionId);
		
//		get back the resource and item list and display them on page
		IResourceService service = getResourceService();
		//try to get form system session
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		ResourceUser resourceUser = service.getUserByID(new Long(user.getUserID().intValue()));
		if(resourceUser == null){
			resourceUser = new ResourceUser(user,null);
			service.createUser(resourceUser);
		}

		List<ResourceItem> items = null;
		Resource resource;
		try {
			items = service.getResourceItemsBySessionId(sessionId);
			resource = service.getResourceBySessionId(sessionId);
		} catch (Exception e) {
			log.error(e);
			return mapping.findForward(ResourceConstants.ERROR);
		}
		//init resource item list
		List<ResourceItem> resourceItemList = getResourceItemList(request);
		resourceItemList.clear();
		if(items != null){
			//remove hidden items.
			for(ResourceItem item : items){
				if(!item.isHide()){
					resourceItemList.add(item);
				}
			}
		}
		
		//set complete flag for display purpose
		service.retrieveComplete(resourceItemList, resourceUser);
		
		request.getSession().setAttribute(ResourceConstants.ATTR_RESOURCE,resource);
		
		//check whether there is only one resource item and run auto flag is true or not.
		boolean runAuto = false;
		int itemsNumber = 0;
		if(resource.getResourceItems() != null){
			itemsNumber = resource.getResourceItems().size();
			if(resource.isRunAuto() && itemsNumber == 1){
				ResourceItem item = (ResourceItem) resource.getResourceItems().iterator().next();
				//only visible item can be run auto.
				if(!item.isHide()){
					runAuto = true;
					request.setAttribute(ResourceConstants.ATTR_RESOURCE_ITEM_UID,item.getUid());
				}
			}
		}
		request.setAttribute(ResourceConstants.ATTR_RUN_AUTO,new Boolean(runAuto));
		
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
	 * List save current resource items.
	 * @param request
	 * @return
	 */
	private List getResourceItemList(HttpServletRequest request) {
		return getListFromSession(request,ResourceConstants.ATTR_RESOURCE_ITEM_LIST);
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
	 * Return <code>ActionForward</code> according to resource item type.
	 * @param type
	 * @param mapping
	 * @return
	 */
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
	/**
	 * Set complete flag for given resource item.
	 * @param request
	 */
	private void doComplete(HttpServletRequest request) {
		Long resourceItemUid = new Long(request.getParameter(ResourceConstants.PARAM_RESOURCE_ITEM_UID));
		IResourceService service = getResourceService();
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		Long sessionId =  (Long) request.getSession().getAttribute(ResourceConstants.ATTR_TOOL_SESSION_ID);
		service.setItemComplete(resourceItemUid,new Long(user.getUserID().intValue()),sessionId);
		
		//set resource item complete tag
		List<ResourceItem> resourceItemList = getResourceItemList(request);
		for(ResourceItem item:resourceItemList){
			if(item.getUid().equals(resourceItemUid)){
				item.setComplete(true);
				break;
			}
		}
	}

}
