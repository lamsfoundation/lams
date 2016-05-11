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

package org.lamsfoundation.lams.notebook.web;

import java.util.Date;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.usermanagement.User;
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
 * Store a Notebook entry (private).
 *
 * @author Mitchell Seaton
 *
 * @web:servlet name="storeNotebookEntry"
 * @web:servlet-mapping url-pattern="/servlet/notebook/storeNotebookEntry"
 */
public class StoreNBEntryServlet extends AbstractStoreWDDXPacketServlet {

    private static Logger log = Logger.getLogger(StoreNBEntryServlet.class);
    public static final String STORE_NBENTRY_MESSAGE_KEY = "storeNotebookEntry";

    public ICoreNotebookService getNotebookService() {
	WebApplicationContext webContext = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext());
	return (ICoreNotebookService) webContext.getBean("coreNotebookService");
    }

    /**
     * Helper method to retrieve the user data. Gets the id from the user details
     * in the shared session
     * 
     * @return the user id
     */
    public static Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO learner = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return learner != null ? learner.getUserID() : null;
    }

    @Override
    protected String process(String entryDetails, HttpServletRequest request) throws Exception {

	ICoreNotebookService notebookService = getNotebookService();
	FlashMessage flashMessage = null;

	Hashtable table = (Hashtable) WDDXProcessor.deserialize(entryDetails);
	NotebookEntry notebookEntry = new NotebookEntry();

	try {

	    if (keyExists(table, WDDXTAGS.EXTERNAL_ID)) {
		notebookEntry.setExternalID(WDDXProcessor.convertToLong(table, WDDXTAGS.EXTERNAL_ID));
	    }
	    if (keyExists(table, WDDXTAGS.EXTERNAL_ID_TYPE)) {
		notebookEntry.setExternalIDType(WDDXProcessor.convertToInteger(table, WDDXTAGS.EXTERNAL_ID_TYPE));
	    }
	    if (keyExists(table, WDDXTAGS.EXTERNAL_SIG)) {
		notebookEntry.setExternalSignature(WDDXProcessor.convertToString(table, WDDXTAGS.EXTERNAL_SIG));
	    }
	    Integer userID = StoreNBEntryServlet.getUserId();
	    if (userID != null) {

		User user = (User) notebookService.getUserManagementService().findById(User.class, userID);

		notebookEntry.setUser(user);
	    }
	    if (keyExists(table, WDDXTAGS.TITLE)) {
		notebookEntry.setTitle(WDDXProcessor.convertToString(table, WDDXTAGS.TITLE));
	    }
	    if (keyExists(table, WDDXTAGS.ENTRY)) {
		notebookEntry.setEntry(WDDXProcessor.convertToString(table, WDDXTAGS.ENTRY));
	    }

	    // set date fields
	    notebookEntry.setCreateDate(new Date());

	    notebookService.saveOrUpdateNotebookEntry(notebookEntry);

	} catch (Exception e) {
	    flashMessage = new FlashMessage(STORE_NBENTRY_MESSAGE_KEY, notebookService.getMessageService()
		    .getMessage("invalid.wddx.packet", new Object[] { e.getMessage() }), FlashMessage.ERROR);
	}

	flashMessage = new FlashMessage(STORE_NBENTRY_MESSAGE_KEY, notebookEntry.getUid());

	return flashMessage.serializeMessage();

    }

    @Override
    protected String getMessageKey(String designDetails, HttpServletRequest request) {
	return STORE_NBENTRY_MESSAGE_KEY;
    }

    /**
     * Checks whether the hashtable contains the key specified by <code>key</code>
     * If the key exists, returns true, otherwise return false.
     * 
     * @param table
     *            The hashtable to check
     * @param key
     *            The key to find
     * @return
     */
    private boolean keyExists(Hashtable table, String key) {
	if (table.containsKey(key)) {
	    return true;
	} else {
	    return false;
	}
    }

}
