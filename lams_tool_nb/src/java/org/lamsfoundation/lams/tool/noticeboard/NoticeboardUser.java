/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.noticeboard;

import java.io.Serializable;
/*
 * Created on Jun 30, 2005
 */

/**
 * @author mtruong
 *
 * <p>The NoticeboardUser class represents the learners that will participate
 * the noticeboard activity. Each learner will have a tool session id which represents
 * which group they belong to, or there is no grouping, then each learner will have their own
 * tool session id.</p>
 * <p>The userStatus can be of two values
 * <ul>
 * <li>INCOMPLETE: Which means that the user has not yet finished this activity.</li>
 * <li>COMPLETED: Which means the user has completed this activity (when the user clicks on finish) </li>
 * <ul>
 * </p>
 */
public class NoticeboardUser implements Serializable {
    
    private Long uid;
    private Long userId;
    private NoticeboardSession nbSession;
    private String username;
    private String fullname;
    private String userStatus; 
    
    public final static String INCOMPLETE = "INCOMPLETE";
    
    public static final String COMPLETED = "COMPLETED";
    
    public NoticeboardUser()
    {
        
    }
    /** minimal constructor */
    public NoticeboardUser(Long userId, NoticeboardSession nbSession)
    {
        this.userId = userId;
        this.nbSession = nbSession;   
        this.userStatus = NoticeboardUser.INCOMPLETE;
    }
    
    public NoticeboardUser(Long userId)
    {
        this.userId = userId;
        this.userStatus = NoticeboardUser.INCOMPLETE;
    }
    
    /** full constructor */
    public NoticeboardUser(Long userId,
            				NoticeboardSession nbSession,
            				String username,
            				String fullname,
            				String status)
    {
        this.userId = userId;
        this.nbSession = nbSession;
        this.username = username;
        this.fullname = fullname;
        this.userStatus = status;
    }
    
    /**
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
     * @return Returns the nbSession.
     */
    public NoticeboardSession getNbSession() {
        return nbSession;
    }
    /**
     * @param nbSession The nbSession to set.
     */
    public void setNbSession(NoticeboardSession nbSession) {
        this.nbSession = nbSession;
    }
    /**
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
