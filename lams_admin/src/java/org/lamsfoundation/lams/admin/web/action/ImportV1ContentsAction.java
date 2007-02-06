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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.admin.web.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.dto.V1OrganisationDTO;
import org.lamsfoundation.lams.admin.web.dto.V1UserDTO;
import org.lamsfoundation.lams.admin.web.form.ImportV1ContentsForm;
import org.lamsfoundation.lams.util.MessageService;

/**
 * @author jliew
 *
 * @struts:action path="/importv1contents"
 *              name="ImportV1ContentsForm"
 *              scope="session"
 * 				validate="false"
 * 
 * @struts:action-forward name="importv1contents" path=".importv1contents"
 * @struts:action-forward name="sysadmin" path="/sysadminstart.do"
 */
public class ImportV1ContentsAction extends Action {
	
	private static Logger log = Logger.getLogger(ImportV1ContentsAction.class);
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		ImportV1ContentsForm importV1ContentsForm = (ImportV1ContentsForm)form;
		importV1ContentsForm.setOnlyMembers(true);
		List<V1UserDTO> users = (List)request.getAttribute("users");
		List<V1OrganisationDTO> orgs = (List)request.getAttribute("orgs");
		
		// user and org data kept in session so we don't send it to the client
		// unnecessarily as it can get quite large; danger is, if the client
		// doesn't complete the form, i.e. click save or cancel, then these
		// remain in session
		request.getSession().setAttribute("users", users);
		request.getSession().setAttribute("orgs", orgs);
		
		MessageService messageService = AdminServiceProxy
			.getMessageService(getServlet().getServletContext());
		String[] args = new String[1];
		args[0] = String.valueOf(users.size());
		request.setAttribute("msgNumUsers", messageService
				.getMessage("msg.importv1.found.users", args));
		request.setAttribute("numOrgs", orgs.size());
		
		return mapping.findForward("importv1contents");
	}

}
