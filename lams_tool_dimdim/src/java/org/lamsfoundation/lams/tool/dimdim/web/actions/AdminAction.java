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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */

package org.lamsfoundation.lams.tool.dimdim.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimConfig;
import org.lamsfoundation.lams.tool.dimdim.service.DimdimServiceProxy;
import org.lamsfoundation.lams.tool.dimdim.service.IDimdimService;
import org.lamsfoundation.lams.tool.dimdim.util.Constants;
import org.lamsfoundation.lams.tool.dimdim.web.forms.AdminForm;

/**
 * @struts.action path="/admin/main" name="adminForm" parameter="main"
 *                scope="request" validate="false"
 * @struts.action-forward name="main-success" path="tiles:/admin/main"
 * 
 * @struts.action path="/admin/save" name="adminForm" parameter="save"
 *                scope="request" validate="false"
 * @struts.action-forward name="save-success" redirect="true"
 *                        path="/admin/main.do"
 * 
 * @author Anthony Sukkar
 * 
 */
public class AdminAction extends MappingDispatchAction {

	private IDimdimService dimdimService;

	private static final Logger logger = Logger.getLogger(AdminAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// set up dimdimService
		dimdimService = DimdimServiceProxy.getDimdimService(this.getServlet()
				.getServletContext());

		return super.execute(mapping, form, request, response);
	}

	public ActionForward main(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AdminForm adminForm = (AdminForm) form;
		DimdimConfig serverURL = dimdimService
				.getConfigEntry(Constants.CONFIG_SERVER_URL);

		if (serverURL != null) {
			adminForm.setDimdimServerURL(serverURL.getValue());
		}

		return mapping.findForward("main-success");
	}

	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AdminForm adminForm = (AdminForm) form;

		DimdimConfig dimdimConfig = dimdimService
				.getConfigEntry(Constants.CONFIG_SERVER_URL);

		if (dimdimConfig == null) {
			dimdimConfig = new DimdimConfig();
			dimdimConfig.setKey(Constants.CONFIG_SERVER_URL);
		}

		dimdimConfig.setValue(adminForm.getDimdimServerURL());
		dimdimService.saveOrUpdateConfigEntry(dimdimConfig);

		return mapping.findForward("save-success");
	}
}
