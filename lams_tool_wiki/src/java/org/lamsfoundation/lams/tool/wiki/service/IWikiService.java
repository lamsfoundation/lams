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

package org.lamsfoundation.lams.tool.wiki.service;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.wiki.model.Wiki;
import org.lamsfoundation.lams.tool.wiki.model.WikiAttachment;
import org.lamsfoundation.lams.tool.wiki.model.WikiPage;
import org.lamsfoundation.lams.tool.wiki.model.WikiPageContent;
import org.lamsfoundation.lams.tool.wiki.model.WikiSession;
import org.lamsfoundation.lams.tool.wiki.model.WikiUser;
import org.lamsfoundation.lams.tool.wiki.util.WikiException;
import org.lamsfoundation.lams.tool.wiki.web.forms.WikiPageForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the Wiki Service
 */
public interface IWikiService {
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
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public Wiki getWikiByContentId(Long toolContentID);

    /**
     * @param toolContentId
     * @param file
     * @param type
     * @return
     */
    public WikiAttachment uploadFileToContent(Long toolContentId, FormFile file, String type);

    /**
     * @param uuid
     * @param versionID
     */
    public void deleteFromRepository(Long uuid, Long versionID) throws WikiException;

    /**
     * @param contentID
     * @param uuid
     * @param versionID
     * @param type
     */
    public void deleteInstructionFile(Long contentID, Long uuid, Long versionID, String type);

    /**
     * @param wiki
     */
    public void saveOrUpdateWiki(Wiki wiki);

    /**
     * 
     * @param wikiPage
     */
    public void saveOrUpdateWikiPage(WikiPage wikiPage);

    /**
     * 
     * @param wikiPageForm
     * @param wikiPage
     * @param user
     */
    public void updateWikiPage(WikiPageForm wikiPageForm, WikiPage wikiPage, WikiUser user);

    /**
     * 
     * @param wikiPageForm
     * @param wiki
     * @param user
     * @param session
     */
    public Long insertWikiPage(WikiPageForm wikiPageForm, Wiki wiki, WikiUser user, WikiSession session);

    /**
     * 
     * @param wikiPage
     */
    public void deleteWikiPage(WikiPage wikiPage);

    /**
     * 
     * @param wiki
     * @param title
     * @return
     */
    public WikiPage getWikiPageByWikiAndTitle(Wiki wiki, String title);

    /**
     * 
     * @param wikiSession
     * @param title
     * @return
     */
    public WikiPage getWikiBySessionAndTitle(WikiSession wikiSession, String title);

    /**
     * 
     * @param uid
     * @return
     */
    public WikiPage getWikiPageByUid(Long uid);

    /**
     * 
     * @param uid
     * @return
     */
    public WikiPageContent getWikiPageContent(Long uid);

    /**
     * 
     * @param wikiPageContent
     */
    public void saveOrUpdateWikiPageContent(WikiPageContent wikiPageContent);

    /**
     * @param toolSessionId
     * @return
     */
    public WikiSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param wikiSession
     */
    public void saveOrUpdateWikiSession(WikiSession wikiSession);

    /**
     * 
     * @param userId
     * @param toolSessionId
     * @return
     */
    public WikiUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     * 
     * @param uid
     * @return
     */
    public WikiUser getUserByUID(Long uid);

    /**
     * 
     * @param wikiUser
     */
    public void saveOrUpdateWikiUser(WikiUser wikiUser);

    /**
     * 
     * @param user
     * @param wikiSession
     * @return
     */
    public WikiUser createWikiUser(UserDTO user, WikiSession wikiSession);

    /**
     * Creates a core notebook entry
     * 
     * @param id
     * @param idType
     * @param signature
     * @param userID
     * @param entry
     * @return
     */
    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    /**
     * Gets the entry from the database
     * 
     */
    NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID);

    /**
     * Updates an existing notebook entry
     * 
     */
    void updateEntry(NotebookEntry notebookEntry);

    /**
     * 
     * @param old
     * @param current
     * @return
     */
    String comparePages(String old, String current);
}
