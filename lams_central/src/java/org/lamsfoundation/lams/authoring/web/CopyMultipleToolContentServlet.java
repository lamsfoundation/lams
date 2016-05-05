/***************************************************************************
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.authoring.web;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;
import org.lamsfoundation.lams.web.servlet.AbstractStoreWDDXPacketServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Copy all the tool content entries for the given ids. This is a batch version of AuthoringAction.copyToolContent.
 * It is used when a complex object is copied in the Flash authoring client.
 *
 * Input: wddx packet containing tool content ids
 * Output: wddx packet containing list of old a new ids, in the format oldId=newId,oldId=newId, etc
 *
 * @author Fiona Malikoff
 *
 * @web:servlet name="copyMultipleToolContent"
 * @web:servlet-mapping url-pattern="/servlet/authoring/copyMultipleToolContent"
 */
public class CopyMultipleToolContentServlet extends AbstractStoreWDDXPacketServlet {

    private static Logger log = Logger.getLogger(CopyMultipleToolContentServlet.class);

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }

    public IAuthoringService getAuthoringService() {
	WebApplicationContext webContext = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext());
	return (IAuthoringService) webContext.getBean(AuthoringConstants.AUTHORING_SERVICE_BEAN_NAME);
    }

    @Override
    protected String process(String wddxPacket, HttpServletRequest request) throws Exception {
	String returnPacket = null;
	IAuthoringService authoringService = getAuthoringService();

	try {
	    Hashtable table = (Hashtable) WDDXProcessor.deserialize(wddxPacket);

	    String customCSV = WDDXProcessor.convertToString(table, WDDXTAGS.CUSTOM_CSV);
	    String toolContentIdList = WDDXProcessor.convertToString(table, WDDXTAGS.TOOL_CONTENT_IDS);
	    if (toolContentIdList != null) {
		String[] stringIds = toolContentIdList.split(",");
		ArrayList<Long> longIds = new ArrayList<Long>();
		for (int i = 0; i < stringIds.length; i++) {
		    String id = stringIds[i].trim();
		    if (id.length() > 0) {
			longIds.add(new Long(stringIds[i]));
		    }
		}
		String idMap = authoringService.copyMultipleToolContent(getUserId(), longIds, customCSV);
		return new FlashMessage(getMessageKey(wddxPacket, request), idMap).serializeMessage();
	    }

	    return new FlashMessage(getMessageKey(wddxPacket, request), "").serializeMessage();

	} catch (Exception e) {
	    log.error("Authoring error. input packet was " + wddxPacket, e);
	    FlashMessage flashMessage = new FlashMessage(getMessageKey(wddxPacket, request), authoringService
		    .getMessageService().getMessage("invalid.wddx.packet", new Object[] { e.getMessage() }),
		    FlashMessage.ERROR);
	    returnPacket = flashMessage.serializeMessage();
	}
	return returnPacket;
    }

    @Override
    protected String getMessageKey(String designDetails, HttpServletRequest request) {
	return IAuthoringService.COPY_TOOL_CONTENT_MESSAGE_KEY;
    }

}
