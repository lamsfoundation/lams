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

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.tool.zoom.model.ZoomApi;
import org.lamsfoundation.lams.tool.zoom.service.IZoomService;
import org.lamsfoundation.lams.tool.zoom.service.ZoomServiceProxy;

public class AdminAction extends DispatchAction {

    private IZoomService zoomService;

    private static final Logger logger = Logger.getLogger(AdminAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// set up zoomService
	zoomService = ZoomServiceProxy.getZoomService(this.getServlet().getServletContext());
	return super.execute(mapping, form, request, response);
    }

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	List<ZoomApi> apis = zoomService.getApis();
	JSONArray apisJSON = new JSONArray();
	for (ZoomApi api : apis) {
	    apisJSON.put(api.toJSON());
	}
	request.setAttribute("apis", apisJSON);
	return mapping.findForward("success");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String apisJSONString = request.getParameter("apisJSON");
	JSONArray apisJSON = new JSONArray(apisJSONString);
	List<ZoomApi> apis = new LinkedList<ZoomApi>();
	for (int index = 0; index < apisJSON.length(); index++) {
	    JSONObject apiJSON = apisJSON.getJSONObject(index);
	    ZoomApi api = new ZoomApi(apiJSON);
	    apis.add(api);
	}
	zoomService.saveApis(apis);
	request.setAttribute("saveOK", true);
	if (logger.isDebugEnabled()) {
	    logger.debug("Saved " + apis.size() + " Zoom APIs");
	}

	ActionErrors errors = new ActionErrors();
	apis = zoomService.getApis();
	for (ZoomApi api : apis) {
	    if (!zoomService.pingZoomApi(api.getUid())) {
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.api.ping", api.getEmail()));
	    }
	}
	if (!errors.isEmpty()) {
	    this.addErrors(request, errors);
	}

	return unspecified(mapping, form, request, response);
    }
}