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
package org.eucm.lams.tool.eadventure.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.eucm.lams.tool.eadventure.EadventureConstants;
import org.eucm.lams.tool.eadventure.model.Eadventure;
import org.eucm.lams.tool.eadventure.model.EadventureItemVisitLog;
import org.eucm.lams.tool.eadventure.model.EadventureUser;
import org.eucm.lams.tool.eadventure.model.EadventureVars;
import org.eucm.lams.tool.eadventure.service.EadventureServiceProxy;
import org.eucm.lams.tool.eadventure.service.IEadventureService;
import org.eucm.lams.tool.eadventure.util.EadventureToolContentHandler;
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

    private EadventureToolContentHandler handler;
    private IEadventureService service;

    @Override
    public void init() throws ServletException {
	service = EadventureServiceProxy.getEadventureService(getServletContext());
	super.init();
	log.error(
		"CCCCCCCCCCCCCCCCCCCCOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMPPPPPPPPPPPPPPPLLLLLLLLLLLLLLLLLLLLEEEEEEEEEEEEEEEEEEEEEEEEEEETTTTTTTTTTTTTTTTTTTTTTTTTTTTEEEEEEEEEEEEEEEEEEEEEE!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
	String mode = request.getParameter(AttributeNames.ATTR_MODE);
	String sessionMapID = request.getParameter(EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	Long eadventureItemUid = new Long(request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID));

	HttpSession ss = SessionManager.getSession();

	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Long sessionId = (Long) sessionMap.get(EadventureConstants.ATTR_TOOL_SESSION_ID);
	Long toolContentID = (Long) sessionMap.get(EadventureConstants.ATTR_TOOL_CONTENT_ID);
	Eadventure ead = service.getEadventureByContentId(toolContentID);
	boolean completedFromEad = false;
	if (ead.isDefineComplete()) {
	    EadventureItemVisitLog log = service.getEadventureItemLog(ead.getUid(), new Long(user.getUserID()));
	    EadventureVars var = service.getEadventureVars(log.getUid(), EadventureConstants.VAR_NAME_COMPLETED);
	    if (var != null && Boolean.parseBoolean(var.getValue())) {
		completedFromEad = true;
	    }
	}
	if (!ead.isDefineComplete() || completedFromEad) {
	    service.setItemComplete(eadventureItemUid, new Long(user.getUserID().intValue()), sessionId);
	    ead.setComplete(true);
	}

	// set eadventure item complete tag
	/*
	 * SortedSet<EadventureItem> eadventureItemList = getEadventureItemList(sessionMap);
	 * for (EadventureItem item : eadventureItemList) {
	 * if (item.getUid().equals(eadventureItemUid)) {
	 * item.setComplete(true);
	 * break;
	 * }
	 * }
	 */

	EadventureUser rUser = service.getUserByIDAndSession(new Long(user.getUserID()), sessionId);

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
     * List save current eadventure items.
     *
     * @param request
     * @return
     */
    /*
     * private SortedSet<EadventureItem> getEadventureItemList(SessionMap sessionMap) {
     * SortedSet<EadventureItem> list = (SortedSet<EadventureItem>) sessionMap
     * .get(EadventureConstants.ATTR_RESOURCE_ITEM_LIST);
     * if (list == null) {
     * list = new TreeSet<EadventureItem>(new EadventureItemComparator());
     * sessionMap.put(EadventureConstants.ATTR_RESOURCE_ITEM_LIST, list);
     * }
     * return list;
     * }
     */

}
