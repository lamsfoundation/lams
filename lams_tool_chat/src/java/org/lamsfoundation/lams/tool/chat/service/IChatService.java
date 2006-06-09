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

import java.util.List;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatAttachment;
import org.lamsfoundation.lams.tool.chat.model.ChatMessage;
import org.lamsfoundation.lams.tool.chat.model.ChatSession;
import org.lamsfoundation.lams.tool.chat.model.ChatUser;
import org.lamsfoundation.lams.tool.chat.util.ChatException;
import org.lamsfoundation.lams.tool.chat.util.ChatMessageFilter;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	 * @return
	 */
	public Chat getDefaultContent();
	
	/**
	 * @param toolSignature
	 * @return
	 */
	public Long getDefaultContentIdBySignature(String toolSignature);
	
	/**
	 * @param toolContentID
	 * @return
	 */
	public Chat getChatByContentId(Long toolContentID);
	
	/**
	 * @param toolContentId
	 * @param file
	 * @param type
	 * @return
	 */
	public ChatAttachment uploadFileToContent(Long toolContentId, FormFile file, String type);
	
	/**
	 * @param uuid
	 * @param versionID
	 */
	public void deleteFromRepository(Long uuid, Long versionID) throws ChatException;
	
	/**
	 * @param contentID
	 * @param uuid
	 * @param versionID
	 * @param type
	 */
	public void deleteInstructionFile(Long contentID, Long uuid, Long versionID, String type);
	
	/**
	 * @param chat
	 */
	public void saveOrUpdateChat(Chat chat);
	

	/**
	 * @param toolSessionId
	 * @return
	 */
	public ChatSession getSessionBySessionId(Long toolSessionId);
	
	/**
	 * 
	 * @param jabberRoom
	 * @return
	 */
	public ChatSession getSessionByJabberRoom(String jabberRoom);
	
	/**
	 * @param chatSession
	 */
	public void saveOrUpdateChatSession(ChatSession chatSession);
	
	/**
	 * 
	 * @param userId
	 * @param toolSessionId
	 * @return
	 */
	public ChatUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

	/**
	 * 
	 * @param loginName
	 * @param sessionID
	 * @return
	 */
	public ChatUser getUserByLoginNameAndSessionId(String loginName, Long sessionId);
	
	/**
	 * 
	 * @param jabberID
	 * @param jabberRoom
	 * @return
	 */
	public ChatUser getUserByJabberIDAndJabberRoom(String jabberID, String jabberRoom);
	
	/**
	 * 
	 * @param uid
	 * @return
	 */
	public ChatUser getUserByUID(Long uid);
	
	/**
	 * 
	 * @param jabberNickname
	 * @param sessionID
	 * @return
	 */
	public ChatUser getUserByJabberNicknameAndSessionID(String jabberNickname, Long sessionID);
	
	/**
	 * 
	 * @param chatUser
	 */
	public void saveOrUpdateChatUser(ChatUser chatUser);
	
	/**
	 * 
	 * @param chatUser
	 * @return
	 */
	public List getMessagesForUser(ChatUser chatUser);
	
	/**
	 * 
	 * @param chatMessage
	 */
	public void saveOrUpdateChatMessage(ChatMessage chatMessage);	
	
	/**
	 * 
	 * @param user
	 * @param chatSession
	 * @return
	 */
	public ChatUser createChatUser(UserDTO user, ChatSession chatSession);
	
	/**
	 * 
	 * @param chatSession
	 */
	public void createJabberRoom(ChatSession chatSession);

	/**
	 * 
	 * @param messageElems
	 */
	public void processIncomingMessages(NodeList messageElems);

	/**
	 * 
	 * @param presenceElems
	 */
	public List<Node> processIncomingPresence(Node presence);
	
	/**
	 * 
	 * @param toolContentID
	 * @param pattern
	 */
	public ChatMessageFilter updateMessageFilters(Chat chat); 
		
	/**
	 * 
	 * @param node
	 */
	public void filterMessage(Node message);
	
	/**
	 * 
	 * @param message
	 * @param chat
	 */
	public void filterMessage(Node message, Chat chat);
	
	/**
	 * 
	 * @param messageUID
	 * @return
	 */
	public ChatMessage getMessageByUID(Long messageUID);
	
	public List getLastestMessages(ChatSession chatSession, int max);

	public void auditEditMessage(ChatMessage chatMessage, String messageBody);

	public void auditHideShowMessage(ChatMessage chatMessage, boolean messageHidden );
}