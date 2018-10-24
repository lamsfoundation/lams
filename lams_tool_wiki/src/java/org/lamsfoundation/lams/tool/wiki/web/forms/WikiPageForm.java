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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.wiki.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.util.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * This is a parent class for all wiki forms, it contains all the neccessary
 * information required to save a wiki page
 *
 * @author lfoxton
 *
 *
 */
public class WikiPageForm {

    @Autowired
    @Qualifier("wikiMessageService")
    private MessageService messageService;

    private static final long serialVersionUID = 234235265633376356L;

    String title;
    String wikiBody;
    Boolean isEditable;

    // Extra params for adding pages
    String newPageTitle;
    String newPageWikiBody;

    Long currentWikiPageId;
    String newPageName;
    Boolean newPageIsEditable;

    Long historyPageContentId;

    public WikiPageForm() {
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getWikiBody() {
	return wikiBody;
    }

    public void setWikiBody(String wikiBody) {
	this.wikiBody = wikiBody;
    }

    public Boolean getIsEditable() {
	return isEditable;
    }

    public void setIsEditable(Boolean isEditable) {
	this.isEditable = isEditable;
    }

    public Long getCurrentWikiPageId() {
	return currentWikiPageId;
    }

    public void setCurrentWikiPageId(Long currentWikiPageId) {
	this.currentWikiPageId = currentWikiPageId;
    }

    public String getNewPageTitle() {
	return newPageTitle;
    }

    public void setNewPageTitle(String newPageTitle) {
	this.newPageTitle = newPageTitle;
    }

    public String getNewPageWikiBody() {
	return newPageWikiBody;
    }

    public void setNewPageWikiBody(String newPageWikiBody) {
	this.newPageWikiBody = newPageWikiBody;
    }

    public String getNewPageName() {
	return newPageName;
    }

    public void setNewPageName(String newPageName) {
	this.newPageName = newPageName;
    }

    public Long getHistoryPageContentId() {
	return historyPageContentId;
    }

    public void setHistoryPageContentId(Long historyPageContentId) {
	this.historyPageContentId = historyPageContentId;
    }

    public Boolean getNewPageIsEditable() {
	return newPageIsEditable;
    }

    public void setNewPageIsEditable(Boolean newPageIsEditable) {
	this.newPageIsEditable = newPageIsEditable;
    }
}
