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

import java.io.Serializable;

/**
 * @hibernate.class table="tl_lawiki10_user"
 * @author mtruong
 *
 * <p>The WikiUser class represents the learners that will participate
 * the wiki activity. Each learner will have a tool session id which represents
 * which group they belong to, or there is no grouping, then each learner will have their own
 * tool session id.</p>
 * <p>The userStatus can be of two values
 * <ul>
 * <li>INCOMPLETE: Which means that the user has not yet finished this activity.</li>
 * <li>COMPLETED: Which means the user has completed this activity (when the user clicks on finish) </li>
 * <ul>
 * </p>
 */
public class WikiUser implements Serializable {
    
    private Long uid;
    private Long userId;
    private WikiSession wikiSession;
    private String username;
    private String fullname;
    private String userStatus; 
    
    public final static String INCOMPLETE = "INCOMPLETE";
    
    public static final String COMPLETED = "COMPLETED";
    
    public WikiUser()
    {
        
    }
    /** minimal constructor */
    public WikiUser(Long userId, WikiSession wikiSession)
    {
        this.userId = userId;
        this.wikiSession = wikiSession;   
        this.userStatus = WikiUser.INCOMPLETE;
    }
    
    public WikiUser(Long userId)
    {
        this.userId = userId;
        this.userStatus = WikiUser.INCOMPLETE;
    }
    
    /** full constructor */
    public WikiUser(Long userId,
            				WikiSession wikiSession,
            				String username,
            				String fullname,
            				String status)
    {
        this.userId = userId;
        this.wikiSession = wikiSession;
        this.username = username;
        this.fullname = fullname;
        this.userStatus = status;
    }
    
    /**
     * @hibernate.property 
     * 		column="fullname" 
     * 		length="255"
     * 
     * @return Returns the fullname.
     */
    public String getFullname() {
        return fullname;
    }
    /**
     * @param fullname The fullname to set.
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    /**
     * @hibernate.many-to-one 
     * 		not-null="true" 
     * @hibernate.column 
     * 		name="wiki_session_uid"
     * 
     * @return Returns the wikiSession.
     */
    public WikiSession getWikiSession() {
        return wikiSession;
    }
    /**
     * @param wikiSession The wikiSession to set.
     */
    public void setWikiSession(WikiSession wikiSession) {
        this.wikiSession = wikiSession;
    }
    /**
     * @hibernate.id 
     * 		generator-class="native" 
     * 		type="java.lang.Long" 
     * 		column="uid"
     * 
     * @return Returns the uid.
     */
    public Long getUid() {
        return uid;
    }
    /**
     * @param uid The uid to set.
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }
    /**
     * @hibernate.property 
     * 		column="user_id" 
     * 		length="20" 
     * 		not-null="true"
     * 
     * @return Returns the userId.
     */
    public Long getUserId() {
        return userId;
    }
    /**
     * @param userId The userId to set.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    /**
     * @hibernate.property 
     * 		column="username" 
     * 		length="255"
     * 
     * @return Returns the username.
     */
    public String getUsername() {
        return username;
    }
    /**
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * @hibernate.property 
     * 		column="user_status" 
     * 		length="50"
     * 
     * @return Returns the userStatus.
     */
    public String getUserStatus() {
        return userStatus;
    }
    /**
     * @param userStatus The userStatus to set.
     */
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
