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

package org.lamsfoundation.lams.tool.zoom.web.controller;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.zoom.model.ZoomApi;
import org.lamsfoundation.lams.tool.zoom.service.IZoomService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger = Logger.getLogger(AdminController.class);

    @Autowired
    private IZoomService zoomService;

    @Autowired
    @Qualifier("zoomMessageService")
    private MessageService messageService;

    @RequestMapping("/start")
    public String start(HttpServletRequest request) throws Exception {
	List<ZoomApi> apis = zoomService.getApis();
	ArrayNode apisJSON = JsonNodeFactory.instance.arrayNode();
	for (ZoomApi api : apis) {
	    apisJSON.add(api.toJSON());
	}
	request.setAttribute("apis", apisJSON);
	return "pages/admin/view";
    }

    @RequestMapping("/save")
    public String save(HttpServletRequest request) throws Exception {
	String apisJSONString = request.getParameter("apisJSON");
	ArrayNode apisJSON = JsonUtil.readArray(apisJSONString);
	List<ZoomApi> apis = new LinkedList<>();
	for (int index = 0; index < apisJSON.size(); index++) {
	    ObjectNode apiJSON = (ObjectNode) apisJSON.get(index);
	    ZoomApi api = new ZoomApi(apiJSON);
	    apis.add(api);
	}
	zoomService.saveApis(apis);
	request.setAttribute("saveOK", true);
	if (logger.isDebugEnabled()) {
	    logger.debug("Saved " + apis.size() + " Zoom APIs");
	}

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	apis = zoomService.getApis();
	for (ZoomApi api : apis) {
	    if (!zoomService.pingZoomApi(api.getUid())) {
		errorMap.add("GLOBAL", messageService.getMessage("error.api.ping", new Object[] { api.getClientId() }));
	    }
	}
	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	}

	return start(request);
    }
}