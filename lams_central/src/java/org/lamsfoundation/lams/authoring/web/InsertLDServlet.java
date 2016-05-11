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

package org.lamsfoundation.lams.authoring.web;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
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
 * Insert a learning design into another learning design. This is a copy and paste type of copy - it just dumps the
 * contents (with modified
 * activity ui ids) in the main learning design. It doesn't wrap up the contents in a sequence activity. Always sets the
 * type to COPY_TYPE_NONE.
 *
 * @author Fiona Malikoff
 *
 * @web:servlet name="insertLearningDesign"
 * @web:servlet-mapping url-pattern="/servlet/authoring/insertLearningDesign"
 */
public class InsertLDServlet extends AbstractStoreWDDXPacketServlet {

//	private static final long serialVersionUID = -2298959991408815691L;
    private static Logger log = Logger.getLogger(InsertLDServlet.class);

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
	    Long originalLearningDesignID = WDDXProcessor.convertToLong(table, WDDXTAGS.LEARNING_DESIGN_ID);
	    Long designToImportID = WDDXProcessor.convertToLong(table, WDDXTAGS.LEARNING_DESIGN_TO_IMPORT_ID);
	    boolean createNewLearningDesign = WDDXProcessor.convertToBoolean(table,
		    WDDXTAGS.CREATE_NEW_LEARNING_DESIGN);
	    Integer folderID = WDDXProcessor.convertToInteger(table, WDDXTAGS.WORKSPACE_FOLDER_ID);
	    String title = WDDXProcessor.convertToString(table, WDDXTAGS.TITLE);

	    LearningDesign updatedLearningDesign = authoringService.insertLearningDesign(originalLearningDesignID,
		    designToImportID, getUserId(), createNewLearningDesign, title, folderID, customCSV);
	    return new FlashMessage(getMessageKey(wddxPacket, request), updatedLearningDesign.getLearningDesignId())
		    .serializeMessage();

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
	return IAuthoringService.INSERT_LD_MESSAGE_KEY;
    }

}
