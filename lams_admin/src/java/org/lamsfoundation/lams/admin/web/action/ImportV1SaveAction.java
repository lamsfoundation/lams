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

/* $$Id$$ */
package org.lamsfoundation.lams.admin.web.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.service.IImportService;
import org.lamsfoundation.lams.admin.web.dto.V1OrganisationDTO;
import org.lamsfoundation.lams.admin.web.dto.V1UserDTO;
import org.lamsfoundation.lams.admin.web.form.ImportV1Form;

/**
 * @author jliew
 *
 * @struts:action path="/importv1save"
 *              name="ImportV1Form"
 *              input=".importv1"
 *              scope="request"
 * 				validate="false"
 * 
 * @struts:action-forward name="importv1contents" path="/importv1contents.do"
 * @struts:action-forward name="sysadmin" path="/sysadminstart.do"
 * @struts:action-forward name="importv1" path="/importv1.do"
 */
public class ImportV1SaveAction extends Action {
	
	private static Logger log = Logger.getLogger(ImportV1SaveAction.class);
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		if (isCancelled(request)) {
			//return mapping.getInputForward();
			return mapping.findForward("sysadmin");
		}
		
		//MessageService messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
		IImportService importService = AdminServiceProxy.getImportService(getServlet().getServletContext());
		ImportV1Form importV1Form = (ImportV1Form)form;
		FormFile file = importV1Form.getFile();
		boolean includeIntegrated = importV1Form.getIntegrated();
		
		// validation
		if (file==null || file.getFileSize()<=0) {
			return mapping.findForward("importv1");
		}
		
		List<List> results = importService.parseV1UsersFile(file, includeIntegrated);
		List<V1UserDTO> users = results.get(0);
		List<V1OrganisationDTO> orgs = results.get(1);
		
		request.setAttribute("users", users);
		request.setAttribute("orgs", orgs);
		
		return mapping.findForward("importv1contents");
	}

}
