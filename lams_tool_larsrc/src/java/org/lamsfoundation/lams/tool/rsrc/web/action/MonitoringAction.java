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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.rsrc.ResourceConstants;
import org.lamsfoundation.lams.tool.rsrc.dto.Summary;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.service.IResourceService;
import org.lamsfoundation.lams.tool.rsrc.service.ResourceApplicationException;
import org.lamsfoundation.lams.tool.rsrc.util.ResourceWebUtils;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import sun.util.logging.resources.logging;

public class MonitoringAction extends Action {
	public static Logger log = Logger.getLogger(MonitoringAction.class);
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String param = mapping.getParameter();
		// -----------------------Resource Author function
		// ---------------------------
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);

		if (param.equals("summary")) {
			return summary(mapping, form, request, response);
		}

		if (param.equals("listuser")) {
			return listuser(mapping, form, request, response);
		}
		if (param.equals("showitem")) {
			return showitem(mapping, form, request, response);
		}
		if (param.equals("hideitem")) {
			return hideitem(mapping, form, request, response);
		}


		return mapping.findForward(ResourceConstants.ERROR);
	}


	private ActionForward hideitem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long itemUid = WebUtil.readLongParam(request, ResourceConstants.PARAM_RESOURCE_ITEM_UID);
		IResourceService service = getResourceService();
		service.setItemVisible(itemUid,false);
		
		//update session value
		List<List> groupList = (List<List>) request.getSession().getAttribute(ResourceConstants.ATTR_SUMMARY_LIST);
		if(groupList != null)
			for(List<Summary> group : groupList){
				for(Summary sum: group){
					if(itemUid.equals(sum.getItemUid())){
						sum.setItemHide(true);
						break;
					}
				}
			}

		return mapping.findForward(ResourceConstants.SUCCESS);
	}

	private ActionForward showitem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long itemUid = WebUtil.readLongParam(request, ResourceConstants.PARAM_RESOURCE_ITEM_UID);
		IResourceService service = getResourceService();
		service.setItemVisible(itemUid,true);
		
		//update session value
		List<List> groupList = (List<List>) request.getSession().getAttribute(ResourceConstants.ATTR_SUMMARY_LIST);
		if(groupList != null)
			for(List<Summary> group : groupList){
				for(Summary sum: group){
					if(itemUid.equals(sum.getItemUid())){
						sum.setItemHide(false);
						break;
					}
				}
			}
		return mapping.findForward(ResourceConstants.SUCCESS);
	}

	private ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
		IResourceService service = getResourceService();
		List<List<Summary>> groupList = service.getSummary(contentId);
		
		//put it into HTTPSession
		request.getSession().setAttribute(ResourceConstants.ATTR_SUMMARY_LIST, groupList);
		
		Resource resource = service.getResourceByContentId(contentId);
		resource.toDTO();
		request.getSession().setAttribute(ResourceConstants.PAGE_EDITABLE, new Boolean(ResourceWebUtils.isResourceEditable(resource)));
		request.getSession().setAttribute(ResourceConstants.ATTR_RESOURCE, resource);
		request.getSession().setAttribute(ResourceConstants.ATTR_TOOL_CONTENT_ID, contentId);
		return mapping.findForward(ResourceConstants.SUCCESS);
	}

	private ActionForward listuser(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
		Long itemUid = WebUtil.readLongParam(request, ResourceConstants.PARAM_RESOURCE_ITEM_UID);

		//get user list by given item uid
		IResourceService service = getResourceService();
		List list = service.getUserListBySessionItem(sessionId, itemUid);
		
		//set to request
		request.setAttribute(ResourceConstants.ATTR_USER_LIST, list);
		return mapping.findForward(ResourceConstants.SUCCESS);
	}

	// *************************************************************************************
	// Private method
	// *************************************************************************************
	private IResourceService getResourceService() {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
				.getServletContext());
		return (IResourceService) wac.getBean(ResourceConstants.RESOURCE_SERVICE);
	}	
}
