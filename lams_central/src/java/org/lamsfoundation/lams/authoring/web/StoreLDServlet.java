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

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.dto.StoreLearningDesignResultsDTO;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.web.servlet.AbstractStoreWDDXPacketServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Store a learning design.
 *
 * @author Fiona Malikoff
 *
 * @web:servlet name="storeLD"
 * @web:servlet-mapping url-pattern="/servlet/authoring/storeLearningDesignDetails"
 */
public class StoreLDServlet extends AbstractStoreWDDXPacketServlet {

    private static final long serialVersionUID = -2298959991408815691L;
    private static Logger log = Logger.getLogger(StoreLDServlet.class);

    private String getUserLanguage() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getLocaleLanguage() : "";
    }

    public IAuthoringService getAuthoringService() {
	WebApplicationContext webContext = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext());
	return (IAuthoringService) webContext.getBean(AuthoringConstants.AUTHORING_SERVICE_BEAN_NAME);
    }

    @Override
    protected String process(String designDetails, HttpServletRequest request) throws Exception {
	String returnPacket = null;
	IAuthoringService authoringService = getAuthoringService();

	try {

	    Long learningDesignID = authoringService.storeLearningDesignDetails(designDetails);
	    Vector<AuthoringActivityDTO> activityDTOS = authoringService.getToolActivities(learningDesignID,
		    getUserLanguage());
	    Vector<ValidationErrorDTO> validationDTOS = authoringService.validateLearningDesign(learningDesignID);
	    FlashMessage flashMessage = null;
	    if (validationDTOS != null && validationDTOS.size() > 0) {
		flashMessage = new FlashMessage(getMessageKey(designDetails, request),
			new StoreLearningDesignResultsDTO(Boolean.FALSE, validationDTOS, activityDTOS,
				learningDesignID),
			FlashMessage.OBJECT_MESSAGE);
	    } else {
		flashMessage = new FlashMessage(getMessageKey(designDetails, request),
			new StoreLearningDesignResultsDTO(Boolean.TRUE, activityDTOS, learningDesignID));
	    }
	    returnPacket = flashMessage.serializeMessage();

	} catch (Exception e) {
	    log.error("Authoring error. input packet was " + designDetails, e);
	    FlashMessage flashMessage = new FlashMessage(getMessageKey(designDetails, request), authoringService
		    .getMessageService().getMessage("invalid.wddx.packet", new Object[] { e.getMessage() }),
		    FlashMessage.ERROR);
	    returnPacket = flashMessage.serializeMessage();
	}
	return returnPacket;
    }

    @Override
    protected String getMessageKey(String designDetails, HttpServletRequest request) {
	return IAuthoringService.STORE_LD_MESSAGE_KEY;
    }

}
