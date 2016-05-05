/**
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 */
package org.lamsfoundation.lams.tool.commonCartridge.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.commonCartridge.CommonCartridgeConstants;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeItem;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeUser;
import org.lamsfoundation.lams.tool.commonCartridge.service.CommonCartridgeServiceProxy;
import org.lamsfoundation.lams.tool.commonCartridge.service.ICommonCartridgeService;
import org.lamsfoundation.lams.tool.commonCartridge.util.CommonCartridgeItemComparator;
import org.lamsfoundation.lams.tool.commonCartridge.util.CommonCartridgeToolContentHandler;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 *
 *
 * @author mseaton
 */
@SuppressWarnings("serial")
public class CompleteItemServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(CompleteItemServlet.class);

    private CommonCartridgeToolContentHandler handler;
    private ICommonCartridgeService service;

    @Override
    public void init() throws ServletException {
	service = CommonCartridgeServiceProxy.getCommonCartridgeService(getServletContext());
	super.init();
    }

    /**
     * The doGet method of the servlet. <br>
     *
     *
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String sessionMapID = request.getParameter(CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	Long commonCartridgeItemUid = new Long(request.getParameter(CommonCartridgeConstants.PARAM_RESOURCE_ITEM_UID));

	HttpSession ss = SessionManager.getSession();

	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Long sessionId = (Long) sessionMap.get(CommonCartridgeConstants.ATTR_TOOL_SESSION_ID);
	service.setItemComplete(commonCartridgeItemUid, new Long(user.getUserID().intValue()), sessionId);

	// set commonCartridge item complete tag
	SortedSet<CommonCartridgeItem> commonCartridgeItemList = getCommonCartridgeItemList(sessionMap);
	for (CommonCartridgeItem item : commonCartridgeItemList) {
	    if (item.getUid().equals(commonCartridgeItemUid)) {
		item.setComplete(true);
		break;
	    }
	}

	CommonCartridgeUser rUser = service.getUserByIDAndSession(new Long(user.getUserID()), sessionId);

	response.setContentType("text/javascript");
	PrintWriter out = response.getWriter();

	if (!rUser.isSessionFinished()) {
	    out.println("window.parent.opener.checkNew(false);");
	    out.println("window.parent.opener=null;");
	    out.println("window.parent.close();");
	} else {
	    out.println("window.parent.opener.checkNew(true);");
	    out.println("window.parent.opener=null;");
	    out.println("window.parent.close();");
	}

	out.println();
	out.flush();
	out.close();
    }

    /**
     * List save current commonCartridge items.
     *
     * @param request
     * @return
     */
    private SortedSet<CommonCartridgeItem> getCommonCartridgeItemList(SessionMap sessionMap) {
	SortedSet<CommonCartridgeItem> list = (SortedSet<CommonCartridgeItem>) sessionMap
		.get(CommonCartridgeConstants.ATTR_RESOURCE_ITEM_LIST);
	if (list == null) {
	    list = new TreeSet<CommonCartridgeItem>(new CommonCartridgeItemComparator());
	    sessionMap.put(CommonCartridgeConstants.ATTR_RESOURCE_ITEM_LIST, list);
	}
	return list;
    }

}
