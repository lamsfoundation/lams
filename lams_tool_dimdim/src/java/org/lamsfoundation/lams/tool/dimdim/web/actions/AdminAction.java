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
/* $Id$ */

package org.lamsfoundation.lams.tool.dimdim.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import org.lamsfoundation.lams.tool.dimdim.dto.ConfigDTO;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimConfig;
import org.lamsfoundation.lams.tool.dimdim.service.DimdimServiceProxy;
import org.lamsfoundation.lams.tool.dimdim.service.IDimdimService;
import org.lamsfoundation.lams.tool.dimdim.util.Constants;
import org.lamsfoundation.lams.tool.dimdim.web.forms.AdminForm;

/**
 * @struts.action path="/admin/view" name="adminForm" parameter="view" scope="request" validate="false"
 * @struts.action-forward name="view-success" path="tiles:/admin/view"
 * 
 * @struts.action path="/admin/edit" name="adminForm" parameter="edit" scope="request" validate="false"
 * @struts.action-forward name="edit-success" path="tiles:/admin/edit"
 * 
 * @struts.action path="/admin/save" name="adminForm" parameter="save" scope="request" validate="true"
 *                input="tiles:/admin/edit"
 * @struts.action-forward name="save-success" redirect="true" path="/admin/view.do"
 * 
 * @author Anthony Sukkar
 * 
 */
public class AdminAction extends MappingDispatchAction {

    private IDimdimService dimdimService;

    // private static final Logger logger = Logger.getLogger(AdminAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// set up dimdimService
	dimdimService = DimdimServiceProxy.getDimdimService(this.getServlet().getServletContext());

	return super.execute(mapping, form, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ConfigDTO configDTO = new ConfigDTO();

	configDTO.setServerURL(dimdimService.getConfigValue(Constants.CFG_SERVER_URL));
	configDTO.setVersion(dimdimService.getConfigValue(Constants.CFG_VERSION));

	request.setAttribute(Constants.ATTR_CONFIG_DTO, configDTO);
	return mapping.findForward("view-success");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	AdminForm adminForm = (AdminForm) form;

	adminForm.setServerURL(dimdimService.getConfigValue(Constants.CFG_SERVER_URL));
	adminForm.setVersion(dimdimService.getConfigValue(Constants.CFG_VERSION));

	String version = adminForm.getVersion();
	boolean allowVersionChange = true;
	if (isVersionSet(version)) {
	    allowVersionChange = false;
	}
	request.setAttribute(Constants.ATTR_ALLOW_VERSION_CHANGE, allowVersionChange);

	return mapping.findForward("edit-success");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (!isCancelled(request)) {

	    AdminForm adminForm = (AdminForm) form;

	    // if version is already set, do not update
	    String version = dimdimService.getConfigValue(Constants.CFG_VERSION);
	    if (!isVersionSet(version)) {
		updateConfig(Constants.CFG_VERSION, adminForm.getVersion());
	    }

	    updateConfig(Constants.CFG_SERVER_URL, adminForm.getServerURL().trim());

	}

	return mapping.findForward("save-success");
    }

    private void updateConfig(String key, String value) {

	DimdimConfig config = dimdimService.getConfig(key);

	if (config == null) {
	    config = new DimdimConfig(key, value);
	} else {
	    config.setValue(value);
	}

	dimdimService.saveOrUpdateConfigEntry(config);
    }

    private boolean isVersionSet(String version) {
	return version != null
		&& (version.equals(Constants.CFG_VERSION_STANDARD) || version.equals(Constants.CFG_VERSION_ENTERPRISE));
    }
}
