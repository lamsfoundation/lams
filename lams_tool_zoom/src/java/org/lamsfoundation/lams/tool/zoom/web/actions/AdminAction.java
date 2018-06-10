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

package org.lamsfoundation.lams.tool.zoom.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import org.lamsfoundation.lams.tool.zoom.dto.ConfigDTO;
import org.lamsfoundation.lams.tool.zoom.model.ZoomConfig;
import org.lamsfoundation.lams.tool.zoom.service.IZoomService;
import org.lamsfoundation.lams.tool.zoom.service.ZoomServiceProxy;
import org.lamsfoundation.lams.tool.zoom.util.ZoomConstants;
import org.lamsfoundation.lams.tool.zoom.web.forms.AdminForm;

/**
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * @author Ernie Ghiglione
 *
 */
public class AdminAction extends MappingDispatchAction {

    private IZoomService zoomService;

    // private static final Logger logger = Logger.getLogger(AdminAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// set up zoomService
	zoomService = ZoomServiceProxy.getZoomService(this.getServlet().getServletContext());

	return super.execute(mapping, form, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ConfigDTO configDTO = new ConfigDTO();

	request.setAttribute(ZoomConstants.ATTR_CONFIG_DTO, configDTO);
	return mapping.findForward("view-success");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	AdminForm adminForm = (AdminForm) form;

	return mapping.findForward("edit-success");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (!isCancelled(request)) {

	    AdminForm adminForm = (AdminForm) form;

	}

	return mapping.findForward("save-success");
    }

    private void updateConfig(String key, String value) {

	ZoomConfig config = zoomService.getConfig(key);

	if (config == null) {
	    config = new ZoomConfig(key, value);
	} else {
	    config.setValue(value);
	}

	zoomService.saveOrUpdateConfigEntry(config);
    }

}
