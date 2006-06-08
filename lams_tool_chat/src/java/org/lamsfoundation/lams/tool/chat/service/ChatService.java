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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.RepositoryProxy;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.chat.dao.IChatAttachmentDAO;
import org.lamsfoundation.lams.tool.chat.dao.IChatDAO;
import org.lamsfoundation.lams.tool.chat.dao.IChatMessageDAO;
import org.lamsfoundation.lams.tool.chat.dao.IChatSessionDAO;
import org.lamsfoundation.lams.tool.chat.dao.IChatUserDAO;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatAttachment;
import org.lamsfoundation.lams.tool.chat.model.ChatMessage;
import org.lamsfoundation.lams.tool.chat.model.ChatSession;
import org.lamsfoundation.lams.tool.chat.model.ChatUser;
import org.lamsfoundation.lams.tool.chat.util.ChatConstants;
import org.lamsfoundation.lams.tool.chat.util.ChatException;
import org.lamsfoundation.lams.tool.chat.util.ChatMessageFilter;
import org.lamsfoundation.lams.tool.chat.util.ChatToolContentHandler;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * An implementation of the NoticeboardService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement
 * ToolContentManager and ToolSessionManager.
 */

public class ChatService implements ToolSessionManager, ToolContentManager,
		IChatService {

	static Logger logger = Logger.getLogger(ChatService.class.getName());

	private IChatDAO chatDAO = null;

	private IChatSessionDAO chatSessionDAO = null;

	private IChatUserDAO chatUserDAO = null;

	private IChatMessageDAO chatMessageDAO = null;

	private IChatAttachmentDAO chatAttachmentDAO = null;

	private ILearnerService learnerService;

	private ILamsToolService toolService;

	private IToolContentHandler chatToolContentHandler = null;

	private IRepositoryService repositoryService = null;
	
	private IAuditService auditService = null;

	private IExportToolContentService exportContentService;
	
	public ChatService() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* ************ Methods from ToolSessionManager ************* */
	public void createToolSession(Long toolSessionId, String toolSessionName,
			Long toolContentId) throws ToolException {
		if (logger.isDebugEnabled()) {
			logger.debug("entering method createToolSession:"
					+ " toolSessionId = " + toolSessionId
					+ " toolSessionName = " + toolSessionName
					+ " toolContentId = " + toolContentId);
		}

		ChatSession session = new ChatSession();
		session.setSessionId(toolSessionId);
		session.setSessionName(toolSessionName);
		session.setJabberRoom(null); // we will create this when the first
		// learner starts
		// TODO need to also set other fields.
		Chat chat = chatDAO.getByContentId(toolContentId);
		session.setChat(chat);
		chatSessionDAO.saveOrUpdate(session);
	}

	public String leaveToolSession(Long toolSessionId, Long learnerId)
			throws DataMissingException, ToolException {

		// TODO issues with session status/start date/ end date. Need to
		// reimplement method.

		// if (logger.isDebugEnabled()) {
		// logger.debug("entering method leaveToolSession:"
		// + " toolSessionId=" + toolSessionId + " learnerId="
		// + learnerId);
		// }
		//
		// if (toolSessionId == null) {
		// logger
		// .error("Fail to leave tool Session based on null tool session id.");
		// throw new ToolException(
		// "Fail to remove tool Session based on null tool session id.");
		// }
		// if (learnerId == null) {
		// logger.error("Fail to leave tool Session based on null learner.");
		// throw new ToolException(
		// "Fail to remove tool Session based on null learner.");
		// }
		//
		// ChatSession session = chatSessionDAO.getBySessionId(toolSessionId);
		// if (session != null) {
		// session.setStatus(ChatConstants.SESSION_COMPLETED);
		// chatSessionDAO.saveOrUpdate(session);
		// } else {
		// logger
		// .error("Fail to leave tool Session.Could not find submit file "
		// + "session by given session id: " + toolSessionId);
		// throw new DataMissingException(
		// "Fail to leave tool Session."
		// + "Could not find submit file session by given session id: "
		// + toolSessionId);
		// }
		return learnerService.completeToolSession(toolSessionId, learnerId);
	}

	public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
			throws DataMissingException, ToolException {
		// TODO Auto-generated method stub
		return null;
	}

	public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
			throws DataMissingException, ToolException {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeToolSession(Long toolSessionId)
			throws DataMissingException, ToolException {
		// TODO Auto-generated method stub

	}

	/* ************ Methods from ToolContentManager ************************* */

	public void copyToolContent(Long fromContentId, Long toContentId)
			throws ToolException {

		if (logger.isDebugEnabled()) {
			logger.debug("entering method copyToolContent:" + " fromContentId="
					+ fromContentId + " toContentId=" + toContentId);
		}

		if (fromContentId == null || toContentId == null) {
			String error = "Failed to copy tool content: "
					+ " fromContentID or toContentID is null";
			throw new ToolException(error);
		}

		Chat fromContent = chatDAO.getByContentId(fromContentId);
		if (fromContent == null) {
			// create the fromContent using the default tool content
			fromContent = copyDefaultContent(fromContentId);
		}
		Chat toContent = Chat.newInstance(fromContent, toContentId,
				chatToolContentHandler);
		chatDAO.saveOrUpdate(toContent);
	}

	public void setAsDefineLater(Long toolContentId)
			throws DataMissingException, ToolException {
		// TODO Auto-generated method stub

	}

	public void setAsRunOffline(Long toolContentId)
			throws DataMissingException, ToolException {
		// TODO Auto-generated method stub

	}

	public void removeToolContent(Long toolContentId, boolean removeSessionData)
			throws SessionDataExistsException, ToolException {
		// TODO Auto-generated method stub

	}
	/**
     * Export the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws DataMissingException if no tool content matches the toolSessionId 
     * @throws ToolException if any other error occurs
     */

	public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
		Chat toolContentObj = chatDAO.getByContentId(toolContentId);
 		if(toolContentObj == null)
 			throw new DataMissingException("Unable to find tool content by given id :" + toolContentId);
 		
 		//set ResourceToolContentHandler as null to avoid copy file node in repository again.
 		toolContentObj = Chat.newInstance(toolContentObj,toolContentId,null);
 		toolContentObj.setToolContentHandler(null);
 		toolContentObj.setChatSessions(null);
 		Set<ChatAttachment> atts = toolContentObj.getChatAttachments(); 
 		for(ChatAttachment att: atts){
 			att.setChat(null);
 		}
		try {
			exportContentService.registerFileClassForExport(ChatAttachment.class.getName(),"fileUuid","fileVersionId");
			exportContentService.exportToolContent( toolContentId, toolContentObj,chatToolContentHandler, rootPath);
		} catch (ExportToolContentException e) {
			throw new ToolException(e);
		}
	}


    /**
     * Import the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws ToolException if any other error occurs
     */
	public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath) throws ToolException {
		try {
			exportContentService.registerFileClassForImport(ChatAttachment.class.getName()
					,"fileUuid","fileVersionId","fileName","fileType",null,null);
			
			Object toolPOJO =  exportContentService.importToolContent(toolContentPath,chatToolContentHandler);
			if(!(toolPOJO instanceof Chat))
				throw new ImportToolContentException("Import Chat tool content failed. Deserialized object is " + toolPOJO);
			Chat toolContentObj = (Chat) toolPOJO;
			
//			reset it to new toolContentId
			toolContentObj.setToolContentId(toolContentId);
			toolContentObj.setCreateBy(new Long(newUserUid.longValue()));
			
			chatDAO.saveOrUpdate(toolContentObj);
		} catch (ImportToolContentException e) {
			throw new ToolException(e);
		}
	}
	
	/* ********** IChatService Methods ************************************** */
	public Long getDefaultContentIdBySignature(String toolSignature) {
		Long toolContentId = null;
		toolContentId = new Long(toolService
				.getToolDefaultContentIdBySignature(toolSignature));
		if (toolContentId == null) {
			String error = "Could not retrieve default content id for this tool";
			logger.error(error);
			throw new ChatException(error);
		}
		return toolContentId;
	}

	public Chat getDefaultContent() {
		Long defaultContentID = getDefaultContentIdBySignature(ChatConstants.TOOL_SIGNATURE);
		Chat defaultContent = getChatByContentId(defaultContentID);
		if (defaultContent == null) {
			String error = "Could not retrieve default content record for this tool";
			logger.error(error);
			throw new ChatException(error);
		}
		return defaultContent;
	}

	public Chat copyDefaultContent(Long newContentID) {

		if (newContentID == null) {
			String error = "Cannot copy the Chat tools default content: + "
					+ "newContentID is null";
			logger.error(error);
			throw new ChatException(error);
		}

		Chat defaultContent = getDefaultContent();
		// create new chat using the newContentID
		Chat newContent = new Chat();
		newContent = Chat.newInstance(defaultContent, newContentID,
				chatToolContentHandler);
		chatDAO.saveOrUpdate(newContent);
		return newContent;
	}

	public Chat getChatByContentId(Long toolContentID) {
		Chat chat = (Chat) chatDAO.getByContentId(toolContentID);
		if (chat == null) {
			logger.debug("Could not find the content with toolContentID:"
					+ toolContentID);
		}
		return chat;
	}

	public ChatSession getSessionBySessionId(Long toolSessionId) {
		ChatSession chatSession = chatSessionDAO.getBySessionId(toolSessionId);
		if (chatSession == null) {
			logger.debug("Could not find the chat session with toolSessionID:"
					+ toolSessionId);
		}
		return chatSession;
	}

	public ChatSession getSessionByJabberRoom(String jabberRoom) {
		ChatSession chatSession = chatSessionDAO.getByJabberRoom(jabberRoom);
		if (chatSession == null) {
			logger.debug("Could not find the chat session with jabberRoom:"
					+ jabberRoom);
		}
		return chatSession;
	}

	public ChatUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
		return chatUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
	}

	public ChatUser getUserByLoginNameAndSessionId(String loginName,
			Long toolSessionId) {
		return chatUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
	}

	public ChatUser getUserByJabberIDAndJabberRoom(String jabberID,
			String jabberRoom) {
		return chatUserDAO.getByJabberIDAndJabberRoom(jabberID, jabberRoom);
	}

	public List getMessagesForUser(ChatUser chatUser) {
		return chatMessageDAO.getForUser(chatUser);
	}

	public ChatAttachment uploadFileToContent(Long toolContentId,
			FormFile file, String type) {
		if (file == null || StringUtils.isEmpty(file.getFileName()))
			throw new ChatException("Could not find upload file: " + file);

		NodeKey nodeKey = processFile(file, type);

		ChatAttachment attachment = new ChatAttachment();
		attachment.setFileType(type);
		attachment.setFileUuid(nodeKey.getUuid());
		attachment.setFileVersionId(nodeKey.getVersion());
		attachment.setFileName(file.getFileName());

		return attachment;
	}

	public void deleteFromRepository(Long uuid, Long versionID)
			throws ChatException {
		ITicket ticket = getRepositoryLoginTicket();
		try {
			repositoryService.deleteVersion(ticket, uuid, versionID);
		} catch (Exception e) {
			throw new ChatException(
					"Exception occured while deleting files from"
							+ " the repository " + e.getMessage());
		}
	}

	public void deleteInstructionFile(Long contentID, Long uuid,
			Long versionID, String type) {
		chatDAO.deleteInstructionFile(contentID, uuid, versionID, type);

	}

	public void saveOrUpdateChat(Chat chat) {
		updateMessageFilters(chat);
		chatDAO.saveOrUpdate(chat);
	}

	public void saveOrUpdateChatSession(ChatSession chatSession) {
		chatSessionDAO.saveOrUpdate(chatSession);
	}

	public void saveOrUpdateChatUser(ChatUser chatUser) {
		chatUserDAO.saveOrUpdate(chatUser);
	}

	public void saveOrUpdateChatMessage(ChatMessage chatMessage) {
		chatMessageDAO.saveOrUpdate(chatMessage);
	}

	public ChatUser createChatUser(UserDTO user, ChatSession chatSession) {
		ChatUser chatUser = new ChatUser(user, chatSession);
		chatUser.setJabberId(createJabberId(user));
		// persist chatUser to db
		saveOrUpdateChatUser(chatUser);
		return chatUser;
	}

	public void createJabberRoom(ChatSession chatSession) {
		try {
			XMPPConnection.DEBUG_ENABLED = false;
			XMPPConnection con = new XMPPConnection(ChatConstants.XMPPDOMAIN);

			con.login(ChatConstants.XMPP_ADMIN_USERNAME,
					ChatConstants.XMPP_ADMIN_PASSWORD);

			// Create a MultiUserChat using an XMPPConnection for a room
			String jabberRoom = new Long(System.currentTimeMillis()).toString()
					+ "@" + ChatConstants.XMPPCONFERENCE;

			MultiUserChat muc = new MultiUserChat(con, jabberRoom);

			// Create the room
			muc.create("nick");

			// Get the the room's configuration form
			Form form = muc.getConfigurationForm();

			// Create a new form to submit based on the original form
			Form submitForm = form.createAnswerForm();

			// Add default answers to the form to submit
			for (Iterator fields = form.getFields(); fields.hasNext();) {
				FormField field = (FormField) fields.next();
				if (!FormField.TYPE_HIDDEN.equals(field.getType())
						&& field.getVariable() != null) {
					// Sets the default value as the answer
					submitForm.setDefaultAnswer(field.getVariable());
				}
			}

			// Sets the new owner of the room
			submitForm.setAnswer("muc#roomconfig_persistentroom", true);
			// Send the completed form (with default values) to the server to
			// configure the room
			muc.sendConfigurationForm(submitForm);

			chatSession.setJabberRoom(jabberRoom);
			con.close();
			
		} catch (XMPPException e) {
			logger.error(e);
		}
	}

	public void processIncomingMessages(NodeList messageElems) {
		for (int i = 0; i < messageElems.getLength(); i++) {
			// extract message attributes
			Node message = messageElems.item(i);
			NamedNodeMap nnm = message.getAttributes();

			Node from = nnm.getNamedItem("from");
			Node to = nnm.getNamedItem("to");
			Node type = nnm.getNamedItem("type");

			Node body = getBodyElement(message);

			// save the messages.
			ChatMessage chatMessage = new ChatMessage();
			String jabberRoom;
			String toNick = "";

			// setting to field
			if (type.getNodeValue().equals("chat")) {
				// we are sending to an individual user.
				// extract the jabber room from the to field.
				// format is room@domain/nick
				int index = to.getNodeValue().lastIndexOf("/");
				if (index == -1) {
					logger
							.debug("processIncomingMessages: malformed 'to' attribute :"
									+ to.getNodeValue());
					return; // somethings wrong, ignore packet
				}
				jabberRoom = to.getNodeValue().substring(0, index);
				toNick = to.getNodeValue().substring(index + 1);
			} else if (type.getNodeValue().equals("groupchat")) {
				// we are sending to the whole room.
				// format is room@domain
				jabberRoom = to.getNodeValue();
			} else {
				logger.debug("processIncomingMessages: unknown type: "
						+ type.getNodeValue());
				return;
			}

			ChatSession chatSession = this.getSessionByJabberRoom(jabberRoom);
			ChatUser toChatUser = getUserByLoginNameAndSessionId(toNick,
					chatSession.getSessionId());
			chatMessage.setChatSession(chatSession);
			chatMessage.setToUser(toChatUser);

			// setting from field
			int index = from.getNodeValue().lastIndexOf("@");
			if (index == -1) {
				logger
						.debug("processIncomingMessages: malformed 'from' attribute :"
								+ from.getNodeValue());
				return; // somethings wrong, ignore packet
			}
			String JidUsername = from.getNodeValue().substring(0, index);
			// NB: JID and userId are the same.
			Long userId;
			try {
				userId = new Long(JidUsername);
			} catch (NumberFormatException e) {
				logger
						.debug("processIncomingMessages: malformed JID username: "
								+ JidUsername);
				return;
			}
			ChatUser fromUser = getUserByUserIdAndSessionId(userId, chatSession
					.getSessionId());
			chatMessage.setFromUser(fromUser);

			chatMessage.setType(type.getNodeValue());
			Node bodyText = body.getFirstChild();
			String bodyTextStr = "";
			if (bodyText != null) {
				bodyTextStr = bodyText.getNodeValue();
			}
			chatMessage.setBody(bodyTextStr);
			chatMessage.setSendDate(new Date());
			chatMessage.setHidden(Boolean.FALSE);
			saveOrUpdateChatMessage(chatMessage);
		}
	}

	public List<Node> processIncomingPresence(Node presence) {
		NamedNodeMap nnm = presence.getAttributes();

		Node from = nnm.getNamedItem("from");
		Node to = nnm.getNamedItem("to");
		if (from == null || to == null) {
			// somethings wrong, return empty list
			logger.debug("malformed presence xml: no from or to attributes present");
			return null;
		}
		
		// TODO, do we really need to check this  ??
		// checking presence packet for correct values
		Node xElem = presence.getFirstChild();
		if (xElem == null) {
			logger.debug("malformed presence xml: no x element present");
		}
		nnm = xElem.getAttributes();
		Node xmlns = nnm.getNamedItem("xmlns");
		if (xmlns == null || !xmlns.getNodeValue().equals("http://jabber.org/protocol/muc")) {
			logger.debug("malformed presence xml: xmlns attribute for x element not available or incorrect");
			return null;
		}

		// get the Chat User
		String jabberID = from.getNodeValue().split("/")[0];
		String jabberRoom = to.getNodeValue().split("/")[0];

		ChatUser chatUser = getUserByJabberIDAndJabberRoom(jabberID, jabberRoom);

		List chatMessageList = getMessagesForUser(chatUser);
		logger.debug("MESSAGE COUNT" + chatMessageList.size());

		List<Node> xmlMessageList = new ArrayList<Node>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			for (Iterator iter = chatMessageList.iterator(); iter.hasNext();) {
				ChatMessage message = (ChatMessage) iter.next();

				Element messageElement = document.createElement("message");
				messageElement.setAttribute("from", jabberRoom + "/"
						+ message.getFromUser().getLoginName());
				messageElement
						.setAttribute("to", jabberID + "/lams_chatclient");
				messageElement.setAttribute("type", message.getType());

				Element bodyElement = document.createElement("body");
				Text bodyText = document.createTextNode(message.getBody());
				bodyElement.appendChild(bodyText);

				Element xElement = document.createElement("x");
				xElement.setAttribute("xmlns", "jabber:x:delay");
				xElement.setAttribute("stamp", "TODO"); // TODO generate the stamp attribute
				xElement.setAttribute("from", jabberRoom + "/"
						+ message.getFromUser().getLoginName());

				messageElement.appendChild(bodyElement);
				messageElement.appendChild(xElement);

				xmlMessageList.add(messageElement);
				//printXMLNode(messageElement, "");
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			logger.debug("parser configuration exception");
			return null;
		}
		return xmlMessageList;
	}

	private void printXMLNode(Node node, String tab) {
		System.out.print(tab + node.getNodeName() + ":");
		
		NamedNodeMap nnm = node.getAttributes();
		for (int j = 0; j < nnm.getLength(); j++) {
			Node m = nnm.item(j);
			System.out.print(" " + m.getNodeName() + "=" + m.getNodeValue());
		}
		System.out.print(" => " + node.getNodeValue() + "\n");
		
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);		
			printXMLNode(n, tab + "    ");
		}
		
	}

	public void filterMessage(Node message) {
		NamedNodeMap nnm = message.getAttributes();
		String from = nnm.getNamedItem("from").getNodeValue();

		// extracting jabber room.
		int index = from.lastIndexOf("/");
		String jabberRoom;
		if (index != -1) {
			jabberRoom = from.substring(0, index);
		} else {
			jabberRoom = from;
		}

		// get the chat content3
		Chat chat = getSessionByJabberRoom(jabberRoom).getChat();

		if (!chat.getFilteringEnabled()) {
			return;
		}

		// get the filter
		ChatMessageFilter filter = messageFilters.get(chat.getToolContentId());
		if (filter == null) {
			// this is the first message we have see for this toolContentId
			// update the available filters.
			filter = updateMessageFilters(chat);
		}

		// get the pattern
		Pattern pattern = filter.getPattern();
		if (pattern == null) {
			// no pattern available. This occurs when filtering is enabled but
			// no valid keywords have been defined.
			return;
		}

		// get the message body node
		Node body = getBodyElement(message);
		if (body == null) {
			// no body node present
			return;
		}

		// get the text node
		Node bodyText = body.getFirstChild();
		if (bodyText == null) {
			// no text present
			return;
		}

		// filter the message.
		Matcher matcher = pattern.matcher(bodyText.getNodeValue());
		bodyText.setNodeValue(matcher.replaceAll("***"));
	}

	public ChatMessageFilter updateMessageFilters(Chat chat) {
		ChatMessageFilter filter = new ChatMessageFilter(chat);
		messageFilters.put(chat.getToolContentId(), filter);
		return filter;
	}
	
	public ChatMessage getMessageByUID(Long messageUID) {
		return chatMessageDAO.getByUID(messageUID);
	}

		public List getLastestMessages(ChatSession chatSession, int max) {
		return chatMessageDAO.getLatest(chatSession, max);		
	}

	public IAuditService getAuditService() {
		return auditService;
	}

	public void setAuditService(IAuditService auditService) {
		this.auditService = auditService;
	}
	
	public void auditEditMessage(ChatMessage chatMessage, String messageBody) {
		auditService.logChange(ChatConstants.TOOL_SIGNATURE,
	 			chatMessage.getFromUser().getUserId(), chatMessage.getFromUser().getLoginName(),
	 			chatMessage.getBody(), messageBody);
	}

	public void auditHideShowMessage(ChatMessage chatMessage, boolean messageHidden) {
		if ( messageHidden ) {
			auditService.logHideEntry(ChatConstants.TOOL_SIGNATURE, chatMessage.getFromUser().getUserId(), 
					chatMessage.getFromUser().getLoginName(), chatMessage.toString());
		} else {
			auditService.logShowEntry(ChatConstants.TOOL_SIGNATURE, chatMessage.getFromUser().getUserId(), 
					chatMessage.getFromUser().getLoginName(), chatMessage.toString());
		}
	}

	/* ********** Private methods ********** */
	private Map<Long, ChatMessageFilter> messageFilters = new ConcurrentHashMap<Long, ChatMessageFilter>();

	private Node getBodyElement(Node message) {
		// get the body element
		NodeList nl = message.getChildNodes();
		Node body = null;
		for (int j = 0; j < nl.getLength(); j++) {
			// We are looking for the <body> element.
			// More than one may exists, we will take the first one we see
			// We ignore <subject> and <thread> elements
			// TODO check that the first one is right. may have problems with
			// multiple langauges.
			Node msgChild = nl.item(j);
			if (msgChild.getNodeName() == "body") {
				body = msgChild;
				break;
			}
		}
		return body;
	}

	/**
	 * Registers a new Jabber Id on the jabber server using the users login as
	 * the jabber name and password TODO This is only temporary, most likely it
	 * will need to be moved to the lams core service since it will be used by
	 * both IM Chat Tool. Users in the system should only have a single jabber
	 * id
	 */
	private String createJabberId(UserDTO user) {
		try {
			XMPPConnection con = new XMPPConnection(ChatConstants.XMPPDOMAIN);

			AccountManager manager = con.getAccountManager();
			if (manager.supportsAccountCreation()) {
				// using the lams userId as jabber username and password.
				manager.createAccount(user.getUserID().toString(), user
						.getUserID().toString());
			}

		} catch (XMPPException e) {
			logger.error(e);
			// TODO handle exception
		}
		return user.getUserID() + "@" + ChatConstants.XMPPDOMAIN;
	}

	private NodeKey processFile(FormFile file, String type) {
		NodeKey node = null;
		if (file != null && !StringUtils.isEmpty(file.getFileName())) {
			String fileName = file.getFileName();
			try {
				node = getChatToolContentHandler().uploadFile(
						file.getInputStream(), fileName, file.getContentType(),
						type);
			} catch (InvalidParameterException e) {
				throw new ChatException(
						"InvalidParameterException occured while trying to upload File"
								+ e.getMessage());
			} catch (FileNotFoundException e) {
				throw new ChatException(
						"FileNotFoundException occured while trying to upload File"
								+ e.getMessage());
			} catch (RepositoryCheckedException e) {
				throw new ChatException(
						"RepositoryCheckedException occured while trying to upload File"
								+ e.getMessage());
			} catch (IOException e) {
				throw new ChatException(
						"IOException occured while trying to upload File"
								+ e.getMessage());
			}
		}
		return node;
	}

	/**
	 * This method verifies the credentials of the SubmitFiles Tool and gives it
	 * the <code>Ticket</code> to login and access the Content Repository.
	 * 
	 * A valid ticket is needed in order to access the content from the
	 * repository. This method would be called evertime the tool needs to
	 * upload/download files from the content repository.
	 * 
	 * @return ITicket The ticket for repostory access
	 * @throws SubmitFilesException
	 */
	private ITicket getRepositoryLoginTicket() throws ChatException {
		repositoryService = RepositoryProxy.getRepositoryService();
		ICredentials credentials = new SimpleCredentials(
				ChatToolContentHandler.repositoryUser,
				ChatToolContentHandler.repositoryId);
		try {
			ITicket ticket = repositoryService.login(credentials,
					ChatToolContentHandler.repositoryWorkspaceName);
			return ticket;
		} catch (AccessDeniedException ae) {
			throw new ChatException("Access Denied to repository."
					+ ae.getMessage());
		} catch (WorkspaceNotFoundException we) {
			throw new ChatException("Workspace not found." + we.getMessage());
		} catch (LoginException e) {
			throw new ChatException("Login failed." + e.getMessage());
		}
	}

	/* ********** Used by Spring to "inject" the linked objects ************* */

	public IChatAttachmentDAO getChatAttachmentDAO() {
		return chatAttachmentDAO;
	}

	public void setChatAttachmentDAO(IChatAttachmentDAO attachmentDAO) {
		this.chatAttachmentDAO = attachmentDAO;
	}

	public IChatDAO getChatDAO() {
		return chatDAO;
	}

	public void setChatDAO(IChatDAO chatDAO) {
		this.chatDAO = chatDAO;
	}

	public IToolContentHandler getChatToolContentHandler() {
		return chatToolContentHandler;
	}

	public void setChatToolContentHandler(
			IToolContentHandler chatToolContentHandler) {
		this.chatToolContentHandler = chatToolContentHandler;
	}

	public IChatSessionDAO getChatSessionDAO() {
		return chatSessionDAO;
	}

	public void setChatSessionDAO(IChatSessionDAO sessionDAO) {
		this.chatSessionDAO = sessionDAO;
	}

	public ILamsToolService getToolService() {
		return toolService;
	}

	public void setToolService(ILamsToolService toolService) {
		this.toolService = toolService;
	}

	public IChatUserDAO getChatUserDAO() {
		return chatUserDAO;
	}

	public void setChatUserDAO(IChatUserDAO userDAO) {
		this.chatUserDAO = userDAO;
	}

	public IChatMessageDAO getChatMessageDAO() {
		return chatMessageDAO;
	}

	public void setChatMessageDAO(IChatMessageDAO messageDAO) {
		this.chatMessageDAO = messageDAO;
	}

	public ILearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(ILearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public IExportToolContentService getExportContentService() {
		return exportContentService;
	}

	public void setExportContentService(IExportToolContentService exportContentService) {
		this.exportContentService = exportContentService;
	}

}
