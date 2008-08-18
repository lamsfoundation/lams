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

package org.lamsfoundation.lams.tool.dlfrum.service;

import java.util.HashMap;

import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.tool.dlfrum.model.DotLRNForum;
import org.lamsfoundation.lams.tool.dlfrum.model.DotLRNForumConfigItem;
import org.lamsfoundation.lams.tool.dlfrum.model.DotLRNForumSession;
import org.lamsfoundation.lams.tool.dlfrum.model.DotLRNForumUser;
import org.lamsfoundation.lams.tool.dlfrum.util.DotLRNForumException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the DotLRNForum Service
 */
public interface IDotLRNForumService {
	
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
	public DotLRNForum copyDefaultContent(Long newContentID);

	/**
	 * Returns an instance of the DotLRNForum tools default content.
	 * 
	 * @return
	 */
	public DotLRNForum getDefaultContent();

	/**
	 * @param toolSignature
	 * @return
	 */
	public Long getDefaultContentIdBySignature(String toolSignature);

	/**
	 * @param toolContentID
	 * @return
	 */
	public DotLRNForum getDotLRNForumByContentId(Long toolContentID);


	/**
	 * @param uuid
	 * @param versionID
	 */
	public void deleteFromRepository(Long uuid, Long versionID)
			throws DotLRNForumException;


	/**
	 * @param dotLRNForum
	 */
	public void saveOrUpdateDotLRNForum(DotLRNForum dotLRNForum);

	/**
	 * @param toolSessionId
	 * @return
	 */
	public DotLRNForumSession getSessionBySessionId(Long toolSessionId);

	/**
	 * @param dotLRNForumSession
	 */
	public void saveOrUpdateDotLRNForumSession(DotLRNForumSession dotLRNForumSession);

	
	/**
	 * Get the dotLRNForum config item by key
	 * @param key
	 * @return
	 */
	public DotLRNForumConfigItem getConfigItem(String key);
	
	/**
	 * Save a dotLRN configItem
	 * @param item
	 */
	public void saveOrUpdateDotLRNForumConfigItem(DotLRNForumConfigItem item);
	
	/**
	 * 
	 * @param userId
	 * @param toolSessionId
	 * @return
	 */
	public DotLRNForumUser getUserByUserIdAndSessionId(Long userId,
			Long toolSessionId);

	/**
	 * 
	 * @param uid
	 * @return
	 */
	public DotLRNForumUser getUserByUID(Long uid);
	
	/**
	 * Gets the external organisation map for this tool adapter
	 * @return
	 */
	public ExtServerOrgMap getExtServerOrgMap();
	
	/**
	 * Creates a hash for talking to the external server
	 * @param serverMap
	 * @param extUsername
	 * @param timestamp
	 * @return
	 */
	public String hash(ExtServerOrgMap serverMap, String extUsername, String timestamp);

	/**
	 * 
	 * @param dotLRNForumUser
	 */
	public void saveOrUpdateDotLRNForumUser(DotLRNForumUser dotLRNForumUser);

	/**
	 * 
	 * @param user
	 * @param dotLRNForumSession
	 * @return
	 */
	public DotLRNForumUser createDotLRNForumUser(UserDTO user,DotLRNForumSession dotLRNForumSession);
	
	
	/**
	 * 
	 * @return toolService
	 */
	ILamsToolService getToolService();
	
	/**
	 * gets the tool output for the external server
	 * @param outputName
	 * @param extCourseId
	 * @param userId
	 * @param extToolContentId
	 * @param toolSessionId
	 * @return
	 */
	public int getExternalToolOutputInt(String outputName, String extCourseId, Long userId, String extToolContentId, Long toolSessionId);

	
	/**
	 * Converts the customCSV parameter into a hashmap
	 * @param customCSV
	 * @return
	 */
	public HashMap<String,String> decodeCustomCSV(String customCSV);
	
	/**
	 * Constructs a parameter hashmap to be used for the default parameters
	 * required by the external LMS tool adapter servlet. For instance in dotLRNForum,
	 * this constructs the following:
	 * 
	 * {
	 * 		["un", username],
	 * 		["cs", course],
	 * 		["ts", timestamp],
	 * 		["hs", hash],
	 * }
	 * 
	 * @param user
	 * @param course
	 * @return
	 */
	public HashMap<String, String> getRequiredExtServletParams(String user, String course);
	
	
	/**
	 * Constructs a parameter hashmap based off customCSV to be used for the default parameters
	 * required by the external LMS tool adapter servlet. For instance in dotLRNForum,
	 * this constructs the following:
	 * 
	 * {
	 * 		["un", username],
	 * 		["cs", course],
	 * 		["ts", timestamp],
	 * 		["hs", hash],
	 * }
	 * 
	 * @param user
	 * @param course
	 * @return
	 */
	public HashMap<String, String> getRequiredExtServletParams(String customCSV);
}
