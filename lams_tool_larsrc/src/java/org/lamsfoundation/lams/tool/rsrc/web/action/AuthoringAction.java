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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.rsrc.ResourceConstants;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.service.IResourceService;
import org.lamsfoundation.lams.tool.rsrc.web.form.ResourceForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Steve.Ni
 * @version $Revision$
 */
public class AuthoringAction extends Action {
	private static Logger log = Logger.getLogger(AuthoringAction.class);
	private IResourceService rsrcService;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String param = mapping.getParameter();
		//-----------------------Forum Author function ---------------------------
	  	if (param.equals("initPage")) {
//	  		request.getSession().setAttribute(ForumConstants.MODE,ForumConstants.AUTHOR_MODE);
       		return initPage(mapping, form, request, response);
        }
//	  	if (param.equals("monitoringInitPage")) {
////	  		request.getSession().setAttribute(ForumConstants.MODE,ForumConstants.MONITOR_MODE);
//	  		return initPage(mapping, form, request, response);
//	  	}
//        if (param.equals("updateContent")) {
//       		return updateContent(mapping, form, request, response);
//        }
//        if (param.equals("uploadOnlineFile")) {
//       		return uploadOnline(mapping, form, request, response);
//        }
//        if (param.equals("uploadOfflineFile")) {
//       		return uploadOffline(mapping, form, request, response);
//        }
//        if (param.equals("deleteOnlineFile")) {
//        	return deleteOnlineFile(mapping, form, request, response);
//        }
//        if (param.equals("deleteOfflineFile")) {
//        	return deleteOfflineFile(mapping, form, request, response);
//        }
//        //-----------------------Topic function ---------------------------
//	  	if (param.equals("newTopic")) {
//       		return newTopic(mapping, form, request, response);
//        }
//	  	if (param.equals("createTopic")) {
//       		return createTopic(mapping, form, request, response);
//        }
//	  	if (param.equals("editTopic")) {
//	  		return editTopic(mapping, form, request, response);
//	  	}
//	  	if (param.equals("updateTopic")) {
//	  		return updateTopic(mapping, form, request, response);
//	  	}
//        if (param.equals("viewTopic")) {
//       		return viewTopic(mapping, form, request, response);
//        }
//        if (param.equals("deleteTopic")) {
//        	return deleteTopic(mapping, form, request, response);
//        }
//        if (param.equals("deleteAttachment")) {
//        	return deleteAttachment(mapping, form, request, response);
//        }
//        if (param.equals("refreshTopic")) {
//        	return refreshTopic(mapping, form, request, response);
//        }
//        if (param.equals("finishTopic")) {
//       		return finishTopic(mapping, form, request, response);
//        }
        return mapping.findForward(ResourceConstants.ERROR);
	}
	//******************************************************************************************************************
	//              Forum Author functions
	//******************************************************************************************************************

	private ActionForward initPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		Long contentId = new Long(WebUtil.readLongParam(request,ResourceConstants.PARAM_TOOL_CONTENT_ID));
		ResourceForm forumForm = (ResourceForm)form;
		//get back the topic list and display them on page
		IResourceService service = getResourceService();

		Set topics = null;
		Resource resource = null;
		try {
			resource = service.getResourceByContentId(contentId);
			//if forum does not exist, try to use default content instead.
			if(resource == null){
				resource = service.getDefaultContent(contentId);
				if(resource.getResourceItems() != null){
					topics = resource.getResourceItems();
				}else
					topics = null;
			}else
				topics = service.getAuthoredItems(resource.getUid());
			//initialize instruction attachment list
			List attachmentList = getAttachmentList(request);
			attachmentList.addAll(resource.getAttachments());

			forumForm.setResource(resource);
		} catch (Exception e) {
			log.error(e);
			return mapping.findForward("error");
		}
		
		//set back STRUTS component value
		//init it to avoid null exception in following handling
		if(topics == null)
			topics = new HashSet();
		request.getSession().setAttribute(ResourceConstants.AUTHORING_RESOURCE_LIST, topics);
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
		return getListFromSession(request,ResourceConstants.INSTRUCTION_ATTACHMENT_LIST);
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
