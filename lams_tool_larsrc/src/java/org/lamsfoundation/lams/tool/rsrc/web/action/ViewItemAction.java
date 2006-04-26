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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.rsrc.ResourceConstants;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItemInstruction;
import org.lamsfoundation.lams.tool.rsrc.service.IResourceService;
import org.lamsfoundation.lams.tool.rsrc.web.form.InstructionNavForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ViewItemAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String param = mapping.getParameter();
	    //-----------------------Display Learning Object function ---------------------------
	  	if (param.equals("reviewItem")) {
       		return reviewItem(mapping, form, request, response);
        }
	  	//for preview top frame html page use:
	  	if (param.equals("nextInstruction")) {
	  		return nextInstruction(mapping, form, request, response);
	  	}

        return mapping.findForward(ResourceConstants.ERROR);
	}

	private ActionForward nextInstruction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		InstructionNavForm navForm = (InstructionNavForm) form;
		List list = navForm.getAllInstructions();
		if(list != null && navForm.getCurrent() < list.size()){
			//current is start from 1, so, this will return next.
			navForm.setInstruction((ResourceItemInstruction) list.get(navForm.getCurrent()));
			navForm.setCurrent(navForm.getCurrent()+1);
		}
		
		return mapping.findForward(ResourceConstants.SUCCESS);
	}

	private ActionForward reviewItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		int itemIdx = NumberUtils.stringToInt(request.getParameter(ResourceConstants.PARAM_ITEM_INDEX),-1);
		Long itemUid = NumberUtils.createLong(request.getParameter(ResourceConstants.PARAM_RESOURCE_ITEM_UID));
		ResourceItem item = null;
		if(itemIdx != -1){
			//authoring: does not save item yet, so only has ItemList from session and identity by Index
			List<ResourceItem> resourceList = getResourceItemList(request);
			item = resourceList.get(itemIdx);
		}else if(itemUid != null){
			//learning, list from database, so get item by Uid
//			get back the resource and item list and display them on page
			IResourceService service = getResourceService();			
			item = service.getResourceItemByUid(itemUid);
			HttpSession ss = SessionManager.getSession();
			//get back login user DTO
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			service.setItemAccess(itemUid,new Long(user.getUserID().intValue()));
		}
		if(item != null){
			Set instructions = item.getItemInstructions();
			InstructionNavForm navForm = (InstructionNavForm) form;
			navForm.setAllInstructions(new ArrayList(instructions));
			navForm.setTitle(item.getTitle());
			navForm.setType(item.getType());
			navForm.setTotal(instructions.size());
			if(instructions.size() > 0){
				navForm.setCurrent(1);
				navForm.setInstruction((ResourceItemInstruction) instructions.iterator().next());
			}else{
				navForm.setCurrent(0);
				navForm.setInstruction(null);
			}
			if(item.getType() == ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT)
				request.getSession().setAttribute(ResourceConstants.ATT_LEARNING_OBJECT,item);
			//set url to content frame
			request.setAttribute(ResourceConstants.ATTR_RESOURCE_REVIEW_URL,getReviewUrl(item));
			return mapping.findForward(ResourceConstants.SUCCESS);
		}
		
		return mapping.findForward(ResourceConstants.ERROR);
	}
	//*************************************************************************************
	// Private method 
	//*************************************************************************************
	private IResourceService getResourceService() {
	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
	      return (IResourceService) wac.getBean(ResourceConstants.RESOURCE_SERVICE);
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
			url = "/pages/learningobj/mainframe.jsp";
			break;
		}
		return url;
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
	
}
