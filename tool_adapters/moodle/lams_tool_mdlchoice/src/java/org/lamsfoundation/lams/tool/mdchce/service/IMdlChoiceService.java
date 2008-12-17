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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.mdchce.service;

import java.util.HashMap;

import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.tool.mdchce.model.MdlChoice;
import org.lamsfoundation.lams.tool.mdchce.model.MdlChoiceConfigItem;
import org.lamsfoundation.lams.tool.mdchce.model.MdlChoiceSession;
import org.lamsfoundation.lams.tool.mdchce.model.MdlChoiceUser;
import org.lamsfoundation.lams.tool.mdchce.util.MdlChoiceException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the MdlChoice Service
 */
public interface IMdlChoiceService {

    public static final String EXT_SERVER_METHOD_CLONE = "clone";
    public static final String EXT_SERVER_METHOD_IMPORT = "import";
    public static final String EXT_SERVER_METHOD_EXPORT = "export";
    public static final String EXT_SERVER_METHOD_OUTPUT = "output";
    public static final String EXT_SERVER_METHOD_EXPORT_PORTFOLIO = "export_portfolio";
    public static final String EXT_SERVER_METHOD_EXPORT_GET_CHOICES = "getoptions";

    /**
     * Makes a copy of the default content and assigns it a newContentID
     * 
     * @params newContentID
     * @return
     */
    public MdlChoice copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the MdlChoice tools default content.
     * 
     * @return
     */
    public MdlChoice getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public MdlChoice getMdlChoiceByContentId(Long toolContentID);

    /**
     * @param uuid
     * @param versionID
     */
    public void deleteFromRepository(Long uuid, Long versionID) throws MdlChoiceException;

    /**
     * @param mdlChoice
     */
    public void saveOrUpdateMdlChoice(MdlChoice mdlChoice);

    /**
     * @param toolSessionId
     * @return
     */
    public MdlChoiceSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param mdlChoiceSession
     */
    public void saveOrUpdateMdlChoiceSession(MdlChoiceSession mdlChoiceSession);

    /**
     * Get the mdlChoice config item by key
     * 
     * @param key
     * @return
     */
    public MdlChoiceConfigItem getConfigItem(String key);

    /**
     * Save a mdl configItem
     * 
     * @param item
     */
    public void saveOrUpdateMdlChoiceConfigItem(MdlChoiceConfigItem item);

    /**
     * 
     * @param userId
     * @param toolSessionId
     * @return
     */
    public MdlChoiceUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     * 
     * @param uid
     * @return
     */
    public MdlChoiceUser getUserByUID(Long uid);

    /**
     * Gets the external organisation map for this tool adapter
     * 
     * @return
     */
    public ExtServerOrgMap getExtServerOrgMap();

    /**
     * Creates a hash for talking to the external server
     * 
     * @param serverMap
     * @param extUsername
     * @param timestamp
     * @return
     */
    public String hash(ExtServerOrgMap serverMap, String extUsername, String timestamp);

    /**
     * 
     * @param mdlChoiceUser
     */
    public void saveOrUpdateMdlChoiceUser(MdlChoiceUser mdlChoiceUser);

    /**
     * 
     * @param user
     * @param mdlChoiceSession
     * @return
     */
    public MdlChoiceUser createMdlChoiceUser(UserDTO user, MdlChoiceSession mdlChoiceSession);

    /**
     * 
     * @return toolService
     */
    ILamsToolService getToolService();

    /**
     * gets the tool output for the external server
     * 
     * @param outputName
     * @param mdlChoice
     * @param userId
     * @param extToolContentId
     * @param toolSessionId
     * @return
     */
    public boolean getExternalToolOutputBoolean(String outputName, MdlChoice mdlChoice, Long userId, String extToolContentId,
	    Long toolSessionId, String choiceID);

    /**
     * Converts the customCSV parameter into a hashmap
     * 
     * @param customCSV
     * @return
     */
    public HashMap<String, String> decodeCustomCSV(String customCSV);

    /**
     * Constructs a parameter hashmap to be used for the default parameters
     * required by the external LMS tool adapter servlet. For instance in
     * mdlChoice, this constructs the following:
     *  { ["un", username], ["cs", course], ["ts", timestamp], ["hs", hash], }
     * 
     * @param mdlChoice
     * @return
     */
    public HashMap<String, String> getRequiredExtServletParams(MdlChoice mdlChoice);

    /**
     * Constructs a parameter hashmap based off customCSV to be used for the
     * default parameters required by the external LMS tool adapter servlet. For
     * instance in mdlChoice, this constructs the following:
     *  { ["un", username], ["cs", course], ["ts", timestamp], ["hs", hash], }
     * 
     * @param user
     * @param course
     * @return
     */
    public HashMap<String, String> getRequiredExtServletParams(String customCSV);
}
