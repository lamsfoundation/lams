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

package org.lamsfoundation.lams.tool.mdquiz.service;

import java.util.HashMap;

import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.tool.mdquiz.model.MdlQuiz;
import org.lamsfoundation.lams.tool.mdquiz.model.MdlQuizConfigItem;
import org.lamsfoundation.lams.tool.mdquiz.model.MdlQuizSession;
import org.lamsfoundation.lams.tool.mdquiz.model.MdlQuizUser;
import org.lamsfoundation.lams.tool.mdquiz.util.MdlQuizException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the MdlQuiz Service
 */
public interface IMdlQuizService {

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
    public MdlQuiz copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the MdlQuiz tools default content.
     * 
     * @return
     */
    public MdlQuiz getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public MdlQuiz getMdlQuizByContentId(Long toolContentID);

    /**
     * @param uuid
     * @param versionID
     */
    public void deleteFromRepository(Long uuid, Long versionID) throws MdlQuizException;

    /**
     * @param mdlQuiz
     */
    public void saveOrUpdateMdlQuiz(MdlQuiz mdlQuiz);

    /**
     * @param toolSessionId
     * @return
     */
    public MdlQuizSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param mdlQuizSession
     */
    public void saveOrUpdateMdlQuizSession(MdlQuizSession mdlQuizSession);

    /**
     * Get the mdlQuiz config item by key
     * 
     * @param key
     * @return
     */
    public MdlQuizConfigItem getConfigItem(String key);

    /**
     * Save a mdl configItem
     * 
     * @param item
     */
    public void saveOrUpdateMdlQuizConfigItem(MdlQuizConfigItem item);

    /**
     * 
     * @param userId
     * @param toolSessionId
     * @return
     */
    public MdlQuizUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     * 
     * @param uid
     * @return
     */
    public MdlQuizUser getUserByUID(Long uid);

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
     * @param mdlQuizUser
     */
    public void saveOrUpdateMdlQuizUser(MdlQuizUser mdlQuizUser);

    /**
     * 
     * @param user
     * @param mdlQuizSession
     * @return
     */
    public MdlQuizUser createMdlQuizUser(UserDTO user, MdlQuizSession mdlQuizSession);

    /**
     * 
     * @return toolService
     */
    ILamsToolService getToolService();

    /**
     * gets the tool output for the external server
     * 
     * @param outputName
     * @param mdlQuiz
     * @param userId
     * @param extToolContentId
     * @param toolSessionId
     * @return
     */
    public int getExternalToolOutputInt(String outputName, MdlQuiz mdlQuiz, Long userId, String extToolContentId,
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
     * mdlQuiz, this constructs the following:
     *  { ["un", username], ["cs", course], ["ts", timestamp], ["hs", hash], }
     * 
     * @param mdlQuiz
     * @return
     */
    public HashMap<String, String> getRequiredExtServletParams(MdlQuiz mdlQuiz);

    /**
     * Constructs a parameter hashmap based off customCSV to be used for the
     * default parameters required by the external LMS tool adapter servlet. For
     * instance in mdlQuiz, this constructs the following:
     *  { ["un", username], ["cs", course], ["ts", timestamp], ["hs", hash], }
     * 
     * @param user
     * @param course
     * @return
     */
    public HashMap<String, String> getRequiredExtServletParams(String customCSV);
}
