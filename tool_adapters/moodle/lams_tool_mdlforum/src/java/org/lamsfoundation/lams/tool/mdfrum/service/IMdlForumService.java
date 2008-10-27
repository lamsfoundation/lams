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

package org.lamsfoundation.lams.tool.mdfrum.service;

import java.util.HashMap;

import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.tool.mdfrum.model.MdlForum;
import org.lamsfoundation.lams.tool.mdfrum.model.MdlForumConfigItem;
import org.lamsfoundation.lams.tool.mdfrum.model.MdlForumSession;
import org.lamsfoundation.lams.tool.mdfrum.model.MdlForumUser;
import org.lamsfoundation.lams.tool.mdfrum.util.MdlForumException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the MdlForum Service
 */
public interface IMdlForumService {

    public static final String EXT_SERVER_METHOD_CLONE = "clone";
    public static final String EXT_SERVER_METHOD_IMPORT = "import";
    public static final String EXT_SERVER_METHOD_EXPORT = "export";
    public static final String EXT_SERVER_METHOD_OUTPUT = "output";
    public static final String EXT_SERVER_METHOD_EXPORT_PORTFOLIO = "export_portfolio";

    /**
     * Makes a copy of the default content and assigns it a newContentID
     * 
     * @params newContentID
     * @return
     */
    public MdlForum copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the MdlForum tools default content.
     * 
     * @return
     */
    public MdlForum getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public MdlForum getMdlForumByContentId(Long toolContentID);

    /**
     * @param uuid
     * @param versionID
     */
    public void deleteFromRepository(Long uuid, Long versionID) throws MdlForumException;

    /**
     * @param mdlForum
     */
    public void saveOrUpdateMdlForum(MdlForum mdlForum);

    /**
     * @param toolSessionId
     * @return
     */
    public MdlForumSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param mdlForumSession
     */
    public void saveOrUpdateMdlForumSession(MdlForumSession mdlForumSession);

    /**
     * Get the mdlForum config item by key
     * 
     * @param key
     * @return
     */
    public MdlForumConfigItem getConfigItem(String key);

    /**
     * Save a mdl configItem
     * 
     * @param item
     */
    public void saveOrUpdateMdlForumConfigItem(MdlForumConfigItem item);

    /**
     * 
     * @param userId
     * @param toolSessionId
     * @return
     */
    public MdlForumUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     * 
     * @param uid
     * @return
     */
    public MdlForumUser getUserByUID(Long uid);

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
     * @param mdlForumUser
     */
    public void saveOrUpdateMdlForumUser(MdlForumUser mdlForumUser);

    /**
     * 
     * @param user
     * @param mdlForumSession
     * @return
     */
    public MdlForumUser createMdlForumUser(UserDTO user, MdlForumSession mdlForumSession);

    /**
     * 
     * @return toolService
     */
    ILamsToolService getToolService();

    /**
     * gets the tool output for the external server
     * 
     * @param outputName
     * @param mdlForum
     * @param userId
     * @param extToolContentId
     * @param toolSessionId
     * @return
     */
    public int getExternalToolOutputInt(String outputName, MdlForum mdlForum, Long userId, String extToolContentId,
	    Long toolSessionId);

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
     * mdlForum, this constructs the following:
     *  { ["un", username], ["cs", course], ["ts", timestamp], ["hs", hash], }
     * 
     * @param mdlForum
     * @return
     */
    public HashMap<String, String> getRequiredExtServletParams(MdlForum mdlForum);

    /**
     * Constructs a parameter hashmap based off customCSV to be used for the
     * default parameters required by the external LMS tool adapter servlet. For
     * instance in mdlForum, this constructs the following:
     *  { ["un", username], ["cs", course], ["ts", timestamp], ["hs", hash], }
     * 
     * @param user
     * @param course
     * @return
     */
    public HashMap<String, String> getRequiredExtServletParams(String customCSV);
}
