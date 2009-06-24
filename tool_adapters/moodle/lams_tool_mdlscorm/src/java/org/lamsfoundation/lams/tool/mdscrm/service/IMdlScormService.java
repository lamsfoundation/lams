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

package org.lamsfoundation.lams.tool.mdscrm.service;

import java.util.HashMap;
import java.util.List;

import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtServerToolAdapterMap;
import org.lamsfoundation.lams.tool.mdscrm.model.MdlScorm;
import org.lamsfoundation.lams.tool.mdscrm.model.MdlScormSession;
import org.lamsfoundation.lams.tool.mdscrm.model.MdlScormUser;
import org.lamsfoundation.lams.tool.mdscrm.util.MdlScormException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the MdlScorm Service
 */
public interface IMdlScormService {

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
    public MdlScorm copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the MdlScorm tools default content.
     * 
     * @return
     */
    public MdlScorm getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public MdlScorm getMdlScormByContentId(Long toolContentID);

    /**
     * @param uuid
     * @param versionID
     */
    public void deleteFromRepository(Long uuid, Long versionID) throws MdlScormException;

    /**
     * @param mdlScorm
     */
    public void saveOrUpdateMdlScorm(MdlScorm mdlScorm);

    /**
     * @param toolSessionId
     * @return
     */
    public MdlScormSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param mdlScormSession
     */
    public void saveOrUpdateMdlScormSession(MdlScormSession mdlScormSession);

    /**
     * 
     * @param userId
     * @param toolSessionId
     * @return
     */
    public MdlScormUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     * 
     * @param uid
     * @return
     */
    public MdlScormUser getUserByUID(Long uid);

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
     * @param mdlScormUser
     */
    public void saveOrUpdateMdlScormUser(MdlScormUser mdlScormUser);

    /**
     * 
     * @param user
     * @param mdlScormSession
     * @return
     */
    public MdlScormUser createMdlScormUser(UserDTO user, MdlScormSession mdlScormSession);

    /**
     * 
     * @return toolService
     */
    ILamsToolService getToolService();

    /**
     * gets the tool output for the external server
     * 
     * @param outputName
     * @param mdlScorm
     * @param userId
     * @param extToolContentId
     * @param toolSessionId
     * @return
     */
    public int getExternalToolOutputInt(String outputName, MdlScorm mdlScorm, Long userId, String extToolContentId,
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
     * mdlScorm, this constructs the following: { ["un", username], ["cs",
     * course], ["ts", timestamp], ["hs", hash], }
     * 
     * @param mdlScorm
     * @return
     */
    public HashMap<String, String> getRequiredExtServletParams(MdlScorm mdlScorm);

    /**
     * Constructs a parameter hashmap based off customCSV to be used for the
     * default parameters required by the external LMS tool adapter servlet. For
     * instance in mdlScorm, this constructs the following: { ["un", username],
     * ["cs", course], ["ts", timestamp], ["hs", hash], }
     * 
     * @param user
     * @param course
     * @return
     */
    public HashMap<String, String> getRequiredExtServletParams(String customCSV);

    /**
     * Gets a list of all external servers
     * 
     * @return
     */
    public List<ExtServerOrgMap> getExtServerList();

    /**
     * Gets a list of servers mapped to this tool adapter
     * 
     * @return
     */
    public List<ExtServerToolAdapterMap> getMappedServers();

    /**
     * Save all the mapped servers
     * 
     * @param mappableServers
     */
    public void saveServerMappings(String[] mappableServers);

    /**
     * Gets an external server url given the extLmsId
     * 
     * @param extLmsId
     * @return
     */
    public String getExtServerUrl(String extLmsId);
}
