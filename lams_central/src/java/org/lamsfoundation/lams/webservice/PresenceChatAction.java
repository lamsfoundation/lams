/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.webservice;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.presence.model.PresenceChatMessage;
import org.lamsfoundation.lams.presence.model.PresenceChatUser;
import org.lamsfoundation.lams.presence.service.IPresenceChatService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Gets all necessary content for Presence Chat in Learning.
 * 
 * @author Paul Georges, Marcin Cieslak
 * 
 *         ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/PresenceChat" parameter="method" validate="false"
 * 
 *                ----------------XDoclet Tags--------------------
 */
public class PresenceChatAction extends LamsDispatchAction {
    /**
     * Keeps information of users present in a Chat session. Needs to work with DB so presence is visible on clustered
     * environment.
     */
    private class Roster {
	private final Long lessonId;
	// when synchronisation with DB was last attempted
	private long lastCheckTime = 0;
	// users who currently poll messasages
	private final Map<String, Date> activeUsers = new HashMap<String, Date>();
	// users stored in DB
	private final Map<String, Date> roster = Collections.synchronizedMap(new HashMap<String, Date>());

	private Roster(Long lessonId) {
	    this.lessonId = lessonId;
	}

	private JSONArray getRosterJSON(String nickname) {
	    long currentTime = System.currentTimeMillis();

	    // roster is used also to get messages by other users, so we need to synchronise on it
	    synchronized (roster) {
		Date currentDate = new Date(currentTime);

		if (!StringUtils.isBlank(nickname)) {
		    // blank nickname means this is just check but user is not really using chat
		    activeUsers.put(nickname, currentDate);
		}

		if ((currentTime - lastCheckTime) > IPresenceChatService.PRESENCE_IDLE_TIMEOUT) {
		    // store active users
		    getPresenceChatService().updateUserPresence(lessonId, activeUsers);
		    activeUsers.clear();

		    // read active users from all nodes
		    List<PresenceChatUser> storedActiveUsers = getPresenceChatService().getUsersActiveByLessonId(
			    lessonId);
		    roster.clear();
		    for (PresenceChatUser activeUser : storedActiveUsers) {
			roster.put(activeUser.getNickname(), activeUser.getLastPresence());
		    }

		    lastCheckTime = currentTime;
		} else if (!StringUtils.isBlank(nickname)) {
		    roster.put(nickname, currentDate);
		}

		return new JSONArray(roster.keySet());
	    }
	}
    }

    private static IPresenceChatService presenceChatService;

    private static Logger logger = Logger.getLogger(PresenceChatAction.class);

    private static final Map<Long, Roster> rosters = Collections.synchronizedMap(new HashMap<Long, Roster>());

    public ActionForward getChatContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	try {
	    long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	    boolean getMessages = Boolean.parseBoolean(request.getParameter("getMessages"));
	    // this is the current user
	    String nickname = request.getParameter("to");

	    JSONObject responseJSON = new JSONObject();
	    // no need to fetch messages if presence is collapsed
	    if (getMessages && !StringUtils.isBlank(nickname)) {
		// this is the other user from opened tab, null if it is group chat
		String from = request.getParameter("from");
		if (StringUtils.isBlank(from)) {
		    from = null;
		}

		String lastMessageUid = request.getParameter("lastMessageUid");
		if (StringUtils.isBlank(lastMessageUid)) {
		    lastMessageUid = null;
		}

		getMessages(nickname, from, lessonId, lastMessageUid == null ? null : Long.valueOf(lastMessageUid),
			responseJSON);
	    }
	    // getRoster MUST be after getMessages
	    // as the latter depends on previous value of check date stored in roster
	    getRoster(nickname, lessonId, responseJSON);

	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(responseJSON.toString());
	} catch (Exception e) {
	    PresenceChatAction.logger.error("Error while getting chat content", e);
	}

	return null;
    }

    public ActionForward sendMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	try {
	    Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	    String from = request.getParameter("from");
	    String to = request.getParameter("to");
	    if (StringUtils.isBlank(to)) {
		to = null;
	    }
	    String message = request.getParameter("message");
	    getPresenceChatService().createPresenceChatMessage(lessonId, from, to, new Date(), message);
	} catch (Exception e) {
	    PresenceChatAction.logger.error(e.getMessage());
	}

	return null;
    }

    /**
     * Gets users currently using the Chat instance.
     */
    private void getRoster(String nickname, Long lessonId, JSONObject responseJSON) throws JSONException {
	// this is equivalent of a chat room
	Roster lessonRoster = PresenceChatAction.rosters.get(lessonId);
	if (lessonRoster == null) {
	    lessonRoster = new Roster(lessonId);
	    PresenceChatAction.rosters.put(lessonId, lessonRoster);
	}

	responseJSON.put("roster", lessonRoster.getRosterJSON(nickname));
    }

    private void getMessages(String nickname, String from, Long lessonId, Long lastMessageUid, JSONObject responseJSON)
	    throws JSONException {
	Set<String> newConversationsWith = new HashSet<String>();

	Roster roster = PresenceChatAction.rosters.get(lessonId);
	Date lastCheck = roster == null ? null : roster.roster.get(nickname);
	List<PresenceChatMessage> messages = getPresenceChatService().getNewMessages(lessonId, nickname, from,
		lastMessageUid, lastCheck);

	for (PresenceChatMessage message : messages) {
	    String messageFrom = message.getFrom();
	    String messageTo = message.getTo();
	    if (from == null ? messageTo == null : (from.equals(messageTo) && nickname.equals(messageFrom))
		    || (from.equals(messageFrom) && nickname.equals(messageTo))) {
		// this goes to opened tab, so get the whole message
		JSONObject messageJSON = new JSONObject();
		messageJSON.put("uid", message.getUid());
		messageJSON.put("from", messageFrom);
		messageJSON.put("dateSent", message.getDateSent());
		messageJSON.put("message", message.getMessage());

		responseJSON.append("messages", messageJSON);
	    } else if (!nickname.equals(messageFrom)) {
		// someone from other tab sent the user a message, just let him know
		newConversationsWith.add(messageTo == null ? "group" : messageFrom);
	    }

	    if (!newConversationsWith.isEmpty()) {
		responseJSON.put("newConversations", newConversationsWith);
	    }
	}
    }

    private IPresenceChatService getPresenceChatService() {
	if (PresenceChatAction.presenceChatService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(SessionManager.getSession()
		    .getServletContext());
	    PresenceChatAction.presenceChatService = (IPresenceChatService) ctx.getBean("presenceChatService");
	}
	return PresenceChatAction.presenceChatService;
    }
}