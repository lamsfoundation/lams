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


package org.lamsfoundation.lams.tool.wiki.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 *
 * Represents the tool session.
 *
 *
 */

public class WikiSession implements java.io.Serializable {

    private static Logger log = Logger.getLogger(WikiSession.class);
    /**
     *
     */
    private static final long serialVersionUID = 7762187772612318324L;

    // Fields
    private Long uid;

    private Date sessionEndDate;

    private Date sessionStartDate;

    private Integer status;

    private Long sessionId;

    private String sessionName;

    private Wiki wiki;

    private Set<WikiUser> wikiUsers;

    private Set<WikiPage> wikiPages; // Need a runtime copy for each wikiPage

    private WikiPage mainPage; //Need a runtime reference to the main page

    // Constructors

    /** default constructor */
    public WikiSession() {
    }

    /** full constructor */
    public WikiSession(Date sessionEndDate, Date sessionStartDate, Integer status, Long sessionId, String sessionName,
	    Wiki wiki, Set<WikiUser> wikiUsers, Set<WikiPage> wikiPages) {
	this.sessionEndDate = sessionEndDate;
	this.sessionStartDate = sessionStartDate;
	this.status = status;
	this.sessionId = sessionId;
	this.sessionName = sessionName;
	this.wiki = wiki;
	this.wikiUsers = wikiUsers;
	this.wikiPages = wikiPages;
    }

    // Property accessors
    /**
     *
     *
     */

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     *
     */

    public Date getSessionEndDate() {
	return this.sessionEndDate;
    }

    public void setSessionEndDate(Date sessionEndDate) {
	this.sessionEndDate = sessionEndDate;
    }

    /**
     *
     *
     */

    public Date getSessionStartDate() {
	return this.sessionStartDate;
    }

    public void setSessionStartDate(Date sessionStartDate) {
	this.sessionStartDate = sessionStartDate;
    }

    /**
     *
     *
     */

    public Integer getStatus() {
	return this.status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    /**
     *
     *
     */

    public Long getSessionId() {
	return this.sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    /**
     *
     *
     */

    public String getSessionName() {
	return this.sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    /**
     *
     *
     *
     */

    public Wiki getWiki() {
	return this.wiki;
    }

    public void setWiki(Wiki wiki) {
	this.wiki = wiki;
    }

    /**
     *
     *
     *
     *
     */

    public Set<WikiUser> getWikiUsers() {
	return this.wikiUsers;
    }

    public void setWikiUsers(Set<WikiUser> wikiUsers) {
	this.wikiUsers = wikiUsers;
    }

    /**
     *
     * asc"
     *
     *
     *
     */
    public Set<WikiPage> getWikiPages() {
	return wikiPages;
    }

    public void setWikiPages(Set<WikiPage> wikiPages) {
	this.wikiPages = wikiPages;
    }

    /**
     *
     *
     *
     */
    public WikiPage getMainPage() {
	return mainPage;
    }

    public void setMainPage(WikiPage mainPage) {
	this.mainPage = mainPage;
    }

    /**
     * toString
     *
     * @return String
     */
    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();

	buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
	buffer.append("sessionEndDate").append("='").append(getSessionEndDate()).append("' ");
	buffer.append("sessionStartDate").append("='").append(getSessionStartDate()).append("' ");
	buffer.append("status").append("='").append(getStatus()).append("' ");
	buffer.append("sessionID").append("='").append(getSessionId()).append("' ");
	buffer.append("sessionName").append("='").append(getSessionName()).append("' ");
	buffer.append("]");

	return buffer.toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if ((other == null)) {
	    return false;
	}
	if (!(other instanceof WikiSession)) {
	    return false;
	}
	WikiSession castOther = (WikiSession) other;

	return ((this.getUid() == castOther.getUid())
		|| (this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid())));
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    @Override
    public Object clone() {

	WikiSession session = null;
	try {
	    session = (WikiSession) super.clone();
	    session.wikiUsers = new HashSet<WikiUser>();
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + WikiSession.class + " failed");
	}
	return session;
    }

}
