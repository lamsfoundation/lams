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

package org.lamsfoundation.lams.tool.mdlesn.service;

import java.util.HashMap;

import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.tool.mdlesn.model.MdlLesson;
import org.lamsfoundation.lams.tool.mdlesn.model.MdlLessonConfigItem;
import org.lamsfoundation.lams.tool.mdlesn.model.MdlLessonSession;
import org.lamsfoundation.lams.tool.mdlesn.model.MdlLessonUser;
import org.lamsfoundation.lams.tool.mdlesn.util.MdlLessonException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the MdlLesson Service
 */
public interface IMdlLessonService {

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
    public MdlLesson copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the MdlLesson tools default content.
     * 
     * @return
     */
    public MdlLesson getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public MdlLesson getMdlLessonByContentId(Long toolContentID);

    /**
     * @param uuid
     * @param versionID
     */
    public void deleteFromRepository(Long uuid, Long versionID) throws MdlLessonException;

    /**
     * @param mdlLesson
     */
    public void saveOrUpdateMdlLesson(MdlLesson mdlLesson);

    /**
     * @param toolSessionId
     * @return
     */
    public MdlLessonSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param mdlLessonSession
     */
    public void saveOrUpdateMdlLessonSession(MdlLessonSession mdlLessonSession);

    /**
     * Get the mdlLesson config item by key
     * 
     * @param key
     * @return
     */
    public MdlLessonConfigItem getConfigItem(String key);

    /**
     * Save a mdl configItem
     * 
     * @param item
     */
    public void saveOrUpdateMdlLessonConfigItem(MdlLessonConfigItem item);

    /**
     * 
     * @param userId
     * @param toolSessionId
     * @return
     */
    public MdlLessonUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     * 
     * @param uid
     * @return
     */
    public MdlLessonUser getUserByUID(Long uid);

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
     * @param mdlLessonUser
     */
    public void saveOrUpdateMdlLessonUser(MdlLessonUser mdlLessonUser);

    /**
     * 
     * @param user
     * @param mdlLessonSession
     * @return
     */
    public MdlLessonUser createMdlLessonUser(UserDTO user, MdlLessonSession mdlLessonSession);

    /**
     * 
     * @return toolService
     */
    ILamsToolService getToolService();

    /**
     * gets the tool output for the external server
     * 
     * @param outputName
     * @param mdlLesson
     * @param userId
     * @param extToolContentId
     * @param toolSessionId
     * @return
     */
    public int getExternalToolOutputInt(String outputName, MdlLesson mdlLesson, Long userId, String extToolContentId,
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
     * mdlLesson, this constructs the following:
     *  { ["un", username], ["cs", course], ["ts", timestamp], ["hs", hash], }
     * 
     * @param mdlLesson
     * @return
     */
    public HashMap<String, String> getRequiredExtServletParams(MdlLesson mdlLesson);

    /**
     * Constructs a parameter hashmap based off customCSV to be used for the
     * default parameters required by the external LMS tool adapter servlet. For
     * instance in mdlLesson, this constructs the following:
     *  { ["un", username], ["cs", course], ["ts", timestamp], ["hs", hash], }
     * 
     * @param user
     * @param course
     * @return
     */
    public HashMap<String, String> getRequiredExtServletParams(String customCSV);
}
