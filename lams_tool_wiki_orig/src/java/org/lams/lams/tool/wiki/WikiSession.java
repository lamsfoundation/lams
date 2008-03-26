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

/* $$Id$$ */
package org.lams.lams.tool.wiki;

import org.lams.lams.tool.wiki.WikiContent;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>The WikiSession class represents a tool session for a wiki activity.
 * Each tool session may represent a group of users, if grouping is used, or may 
 * represent one learner if there is no grouping for this particular activity.
 * </p>
 * <br>
 * <p>The session status has three possible status':
 * <ul><li>NOT_ATTEMPTED: which means that the tool session has been established, but no learners have reached this activity yet 
 * <li>INCOMPLETE: which means that a learner has reached this activity 
 * <li>COMPLETED: The session status will never be set to complete as you don't know when this tool is going to end.
 * </ul></p>
 * @author mtruong
 * @hibernate.class table="tl_lawiki10_session"
 */

public class WikiSession implements Serializable {

    
    public final static String NOT_ATTEMPTED = "NOT_ATTEMPTED";
    
    public final static String INCOMPLETE = "INCOMPLETE";
    
    public static final String COMPLETED = "COMPLETED";
    
	/** identifier field */
    private Long uid;
    
    /** persistent field*/
	private Long wikiSessionId;
	
	/** persistent field*/
	private String wikiSessionName;
	
  	/** nullable persistent field */
	private Date sessionStartDate;
		
	/** nullable persistent field */
	private Date sessionEndDate;
	
	/** nullable persistent field */
	private String sessionStatus;
	
	/** persistent field */
	private WikiContent wikiContent;
	
	/** persistent field */
	private Set wikiUsers = new HashSet();
	
	/** default constructor */
	public WikiSession()
	{
				
	}
	
	/** full constructor */
	public WikiSession(Long wikiSessionId, 
							  String wikiSessionName,
							  WikiContent wikiContent,
							  Date sessionStartDate,
							  Date sessionEndDate,
							  String sessionStatus)
	{
		this.wikiSessionId = wikiSessionId;
		this.wikiSessionName = wikiSessionName;
		//this.wikiContentId = wikiContentId;
		this.wikiContent = wikiContent;
		this.sessionStartDate = sessionStartDate;
		this.sessionEndDate = sessionEndDate;
		this.sessionStatus = sessionStatus;
	}
	
	/**
	 * Constructor used when creating a new  wikiSession given
	 * the wikiContent id
	 */
	
	public WikiSession(Long wikiSessionId,
							  String wikiSessionName,
	        				  WikiContent wikiContent,
	        				  Date sessionStartDate,
	        				  String sessionStatus)
	{
	    this.wikiSessionId = wikiSessionId;
	    this.wikiSessionName = wikiSessionName;
	    this.wikiContent = wikiContent;
	    this.sessionStartDate = sessionStartDate;
	    this.sessionEndDate = null;
	    this.sessionStatus = sessionStatus;
	}
	
	public WikiSession(Long wikiSessionId, 
							  String wikiSessionName,
							  WikiContent wikiContent)
	{
	    this.wikiSessionId = wikiSessionId;
	    this.wikiSessionName = wikiSessionName;
	    this.wikiContent = wikiContent;
	    this.sessionStartDate = new Date(System.currentTimeMillis());
		this.sessionStatus = WikiSession.INCOMPLETE;
	}
	
	public WikiSession(Long wikiSessionId, String wikiSessionName)
	{
	    this.wikiSessionId = wikiSessionId;
	    this.wikiSessionName = wikiSessionName;
	    this.sessionStartDate = new Date(System.currentTimeMillis());
		this.sessionStatus = WikiSession.INCOMPLETE;
	}
	
	public WikiSession(Long wikiSessionId)
	{
	    this.wikiSessionId = wikiSessionId;
	    this.sessionStartDate = new Date(System.currentTimeMillis());
		this.sessionStatus = WikiSession.INCOMPLETE;
	}
	
	/**
	 * 		@hibernate.many-to-one
     *      not-null="true"
     *      @hibernate.column name="wiki_content_uid" 
	 */
	public WikiContent getWikiContent() {
		return wikiContent;
	}
	
	public void setWikiContent(WikiContent wikiContent) {
		this.wikiContent = wikiContent;
	}
		
	/**
	 *		@hibernate.property
     *      column="wiki_session_id"
     *      length="20"
     *      not-null="true"
	 */
	public Long getWikiSessionId() {
		return wikiSessionId;
	}
	
	public void setWikiSessionId(Long wikiSessionId) {
		this.wikiSessionId = wikiSessionId;
	}
	
	/**
	 * 		@hibernate.property
     *      column="wiki_session_name"
     *      length="255"
     *      not-null="true"
	 */
	public String getWikiSessionName() {
		return wikiSessionName;
	}
	
	public void setWikiSessionName(String wikiSessionName) {
		this.wikiSessionName = wikiSessionName;
	}
	
	/**
	 *		@hibernate.property
     *      column="session_end_date"
     *      length="19"
	 */
	public Date getSessionEndDate() {
		return sessionEndDate;
	}
	
	public void setSessionEndDate(Date sessionEndDate) {
		this.sessionEndDate = sessionEndDate;
	}
	
	/**
	 *		@hibernate.property
     *      column="session_start_date"
     *      length="19"
	 */
	public Date getSessionStartDate() {
		return sessionStartDate;
	}
	
	public void setSessionStartDate(Date sessionStartDate) {
		this.sessionStartDate = sessionStartDate;
	}
	
	/**
	 * 		@hibernate.property
     *      column="session_status"
     *      length="100"
	 */
	public String getSessionStatus() {
		return sessionStatus;
	}
	
	public void setSessionStatus(String sessionStatus) {
		this.sessionStatus = sessionStatus;
	}
	
	/**
     *	  	@hibernate.id
     *      generator-class="native"
     *      type="java.lang.Long"
     *      column="uid"
     *      unsaved-value="0"
     */
    public Long getUid() {
        return uid;
    }
    
    public void setUid(Long uid) {
        this.uid = uid;
    }
	
    /**
     * @hibernate.set
     *      lazy="true"
     *      inverse="true"
     *      cascade="all-delete-orphan"
  	 *
 	 * @hibernate.collection-key
	 * 		column="wiki_session_uid"
	 * @hibernate.collection-one-to-many
	 *      class="org.lams.lams.tool.wiki.WikiUser"
     */
    public Set getWikiUsers() {
        if (this.wikiUsers == null)
		{
			setWikiUsers(new HashSet());
		}
    
        return wikiUsers;
    }
    /**
     * @param wikiUsers The wikiUsers to set.
     */
    public void setWikiUsers(Set wikiUsers) {
        this.wikiUsers = wikiUsers;
    }
    
   
}
