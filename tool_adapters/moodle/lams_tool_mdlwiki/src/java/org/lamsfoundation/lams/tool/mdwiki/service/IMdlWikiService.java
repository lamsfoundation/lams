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

package org.lamsfoundation.lams.tool.mdwiki.service;

import java.util.HashMap;

import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.tool.mdwiki.model.MdlWiki;
import org.lamsfoundation.lams.tool.mdwiki.model.MdlWikiConfigItem;
import org.lamsfoundation.lams.tool.mdwiki.model.MdlWikiSession;
import org.lamsfoundation.lams.tool.mdwiki.model.MdlWikiUser;
import org.lamsfoundation.lams.tool.mdwiki.util.MdlWikiException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the MdlWiki Service
 */
public interface IMdlWikiService {

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
    public MdlWiki copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the MdlWiki tools default content.
     * 
     * @return
     */
    public MdlWiki getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public MdlWiki getMdlWikiByContentId(Long toolContentID);

    /**
     * @param uuid
     * @param versionID
     */
    public void deleteFromRepository(Long uuid, Long versionID) throws MdlWikiException;

    /**
     * @param mdlWiki
     */
    public void saveOrUpdateMdlWiki(MdlWiki mdlWiki);

    /**
     * @param toolSessionId
     * @return
     */
    public MdlWikiSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param mdlWikiSession
     */
    public void saveOrUpdateMdlWikiSession(MdlWikiSession mdlWikiSession);

    /**
     * Get the mdlWiki config item by key
     * 
     * @param key
     * @return
     */
    public MdlWikiConfigItem getConfigItem(String key);

    /**
     * Save a mdl configItem
     * 
     * @param item
     */
    public void saveOrUpdateMdlWikiConfigItem(MdlWikiConfigItem item);

    /**
     * 
     * @param userId
     * @param toolSessionId
     * @return
     */
    public MdlWikiUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     * 
     * @param uid
     * @return
     */
    public MdlWikiUser getUserByUID(Long uid);

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
     * @param mdlWikiUser
     */
    public void saveOrUpdateMdlWikiUser(MdlWikiUser mdlWikiUser);

    /**
     * 
     * @param user
     * @param mdlWikiSession
     * @return
     */
    public MdlWikiUser createMdlWikiUser(UserDTO user, MdlWikiSession mdlWikiSession);

    /**
     * 
     * @return toolService
     */
    ILamsToolService getToolService();

    /**
     * gets the tool output for the external server
     * 
     * @param outputName
     * @param mdlWiki
     * @param userId
     * @param extToolContentId
     * @param toolSessionId
     * @return
     */
    public int getExternalToolOutputInt(String outputName, MdlWiki mdlWiki, Long userId, String extToolContentId,
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
     * mdlWiki, this constructs the following:
     *  { ["un", username], ["cs", course], ["ts", timestamp], ["hs", hash], }
     * 
     * @param mdlWiki
     * @return
     */
    public HashMap<String, String> getRequiredExtServletParams(MdlWiki mdlWiki);

    /**
     * Constructs a parameter hashmap based off customCSV to be used for the
     * default parameters required by the external LMS tool adapter servlet. For
     * instance in mdlWiki, this constructs the following:
     *  { ["un", username], ["cs", course], ["ts", timestamp], ["hs", hash], }
     * 
     * @param user
     * @param course
     * @return
     */
    public HashMap<String, String> getRequiredExtServletParams(String customCSV);
}
