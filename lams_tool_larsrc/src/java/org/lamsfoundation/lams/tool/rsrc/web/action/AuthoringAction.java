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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.rsrc.service.IResourceService;

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
        return mapping.findForward("error");
	}
	//******************************************************************************************************************
	//              Forum Author functions
	//******************************************************************************************************************

	private ActionForward initPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	
}
