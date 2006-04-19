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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.chat.service;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatAttachment;

/**
 * Defines the services available to the web layer from the Chat Service
 */
public interface IChatService {
	/**
	 * Makes a copy of the default content and assigns it a newContentID
	 * 
	 * @params newContentID
	 * @return
	 */
	public Chat copyDefaultContent(Long newContentID);
	
	/**
	 * Returns an instance of the Chat tools default content.
	 * 
	 * @return
	 */
	public Chat getDefaultContent();
	
	/**
	 * 
	 * @param toolSignature
	 * @return
	 */
	public Long getToolDefaultContentIdBySignature(String toolSignature);
	
	/**
	 * 
	 * @param toolContentID
	 * @return
	 */
	public Chat getChatByContentId(Long toolContentID);
	
	/**
	 * 
	 * 
	 * @param toolContentId
	 * @param file
	 * @param type
	 * @return
	 */
	public ChatAttachment uploadFileToContent(Long toolContentId, FormFile file, String type);
	
	/**
	 * 
	 * @param uuid
	 * @param versionID
	 */
	public void deleteFromRepository(Long uuid, Long versionID);
	
	/**
	 * 
	 * @param contentID
	 * @param uuid
	 * @param versionID
	 * @param type
	 */
	public void deleteInstructionFile(Long contentID, Long uuid, Long versionID, String type);
	
	/*
	 * 
	 */
	public void saveOrUpdateContent(Chat persistContent);
	
	
	
}