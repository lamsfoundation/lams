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


package org.lamsfoundation.lams.tool.wiki.service;

import java.util.List;

import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ICommonToolService;
import org.lamsfoundation.lams.tool.wiki.model.Wiki;
import org.lamsfoundation.lams.tool.wiki.model.WikiPage;
import org.lamsfoundation.lams.tool.wiki.model.WikiPageContent;
import org.lamsfoundation.lams.tool.wiki.model.WikiSession;
import org.lamsfoundation.lams.tool.wiki.model.WikiUser;
import org.lamsfoundation.lams.tool.wiki.web.forms.WikiPageForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the Wiki Service
 */
public interface IWikiService extends ICommonToolService {
    /**
     * Makes a copy of the default content and assigns it a newContentID
     *
     * @params newContentID
     * @return
     */
    public Wiki copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the Wiki tools default content.
     *
     * @return
     */
    public Wiki getDefaultContent();

    /**
     * Returns an instance of the Wiki tools default content.
     *
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * Gets a wiki instance by its content id
     *
     * @param toolContentID
     * @return
     */
    public Wiki getWikiByContentId(Long toolContentID);

    /**
     * Saves or updates a wiki instance
     *
     * @param wiki
     */
    public void saveOrUpdateWiki(Wiki wiki);

    /**
     * Saves or updates a wiki page
     *
     * @param wikiPage
     */
    public void saveOrUpdateWikiPage(WikiPage wikiPage);

    /**
     * Updates a wiki page with the given content in the WikiPageForm A new
     * WikiPageContent is to be created for each edit so we have a history
     *
     * If the user is null it signifies that this edit was done in author
     *
     * @param wikiPageForm
     * @param wikiPage
     * @param user
     */
    public void updateWikiPage(WikiPageForm wikiPageForm, WikiPage wikiPage, WikiUser user);

    /**
     * Creates a new wiki page from the content in the WikiPageForm
     *
     * If the user and session is null it signifies that this edit was done in
     * author
     *
     * @param wikiPageForm
     * @param wiki
     * @param user
     * @param session
     */
    public Long insertWikiPage(WikiPageForm wikiPageForm, Wiki wiki, WikiUser user, WikiSession session);

    /**
     * Delete an existing wiki page
     *
     * @param wikiPage
     */
    public void deleteWikiPage(WikiPage wikiPage);

    /**
     * Mark an existing wiki page as deleted, but do not remove it for real.
     *
     * @param wikiPage
     */
    public void markWikiPageAsDeleted(WikiPage wikiPage);

    /**
     * Mark an existing wiki page as not deleted.
     *
     * @param wikiPage
     */
    public void restoreWikiPage(WikiPage wikiPage);

    /**
     * Gets a wiki page by wiki and title
     *
     * This method should only be called by author as it will only retrieve
     * wikipages where the toolSession is null
     *
     * @param wiki
     * @param title
     * @return
     */
    public WikiPage getWikiPageByWikiAndTitle(Wiki wiki, String title);

    /**
     * Gets a wiki page by session and title
     *
     * This method should only be called at runtime because it requires a tool
     * session
     *
     * @param wikiSession
     * @param title
     * @return
     */
    public WikiPage getWikiBySessionAndTitle(WikiSession wikiSession, String title);

    /**
     * Gets a wiki page by its uid
     *
     * @param uid
     * @return
     */
    public WikiPage getWikiPageByUid(Long uid);

    /**
     * Gets a page content by its uid
     *
     * @param uid
     * @return
     */
    public WikiPageContent getWikiPageContent(Long uid);

    /**
     * Saves or updates a wiki page content
     *
     * @param wikiPageContent
     */
    public void saveOrUpdateWikiPageContent(WikiPageContent wikiPageContent);

    /**
     * Gets a tool session
     *
     * @param toolSessionId
     * @return
     */
    public WikiSession getSessionBySessionId(Long toolSessionId);

    /**
     * Saves or updates a wiki session
     *
     * @param wikiSession
     */
    public void saveOrUpdateWikiSession(WikiSession wikiSession);

    /**
     * Gets a WikiUser from the session and thier LAMS user id
     *
     * @param userId
     * @param toolSessionId
     * @return
     */
    public WikiUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     * Gets a WikiUser by their uid
     *
     * @param uid
     * @return
     */
    public WikiUser getUserByUID(Long uid);

    /**
     * Saves or updates a wiki user
     *
     * @param wikiUser
     */
    public void saveOrUpdateWikiUser(WikiUser wikiUser);

    /**
     * Creates a new wiki user
     *
     * @param user
     * @param wikiSession
     * @return
     */
    public WikiUser createWikiUser(UserDTO user, WikiSession wikiSession);

    /**
     * This method compares two strings and returns a java html diff of the two
     * The returned value will be a html div that prints unchanged lines as is
     * new lines with a green background and removed lines with a red background
     *
     * @param old
     * @param current
     * @return
     */
    String comparePages(String old, String current);

    /**
     * this method gets the number of edits by a learner in a tool session, this
     * is used for tool outputs
     *
     * @param learnerId
     * @param toolSessionId
     * @return
     */
    int getEditsNum(Long learnerId, Long toolSessionId);

    /**
     * this method gets the number of added pages by a learner in a tool
     * session, this is used for tool outputs
     *
     * @param learnerId
     * @param toolSessionId
     * @return
     */
    int getAddsNum(Long learnerId, Long toolSessionId);

    /**
     * Gets a message from resource bundle. Same as <code><fmt:message></code>
     * in JSP pages.
     *
     * @param key
     *            key of the message
     * @param args
     *            arguments for the message
     * @return message content
     */
    String getLocalisedMessage(String key, Object[] args);

    /**
     * Fetches the wiki notification service
     *
     * @return
     */
    IEventNotificationService getEventNotificationService();

    /**
     * Gets a list of monitors for the given tool session
     *
     * @param sessionId
     * @return
     */
    List<User> getMonitorsByToolSessionId(Long sessionId);

    /**
     * Return content folder (unique to each learner and lesson) which is used for storing user generated content.
     * It's been used by CKEditor.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    String getLearnerContentFolder(Long toolSessionId, Long userId);
    
    /** Copied from ToolSessionManager so that LearningController can access it */
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException;
}
