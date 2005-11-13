/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * <p>Persistent  object/bean that defines the user for the MCQ tool.
 * Provides accessors and mutators to get/set attributes
 * It maps to database table: tl_lamc11_que_usr
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class McQueUsr implements Serializable {

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long queUsrId;

    /** nullable persistent field */
    private String username;

    /** nullable persistent field */
    private String fullname;

    
    /** nullable persistent field */
    private org.lamsfoundation.lams.tool.mc.McSession mcSession;

    /** persistent field */
    private Set mcUsrAttempts;

    /** full constructor */
    public McQueUsr(Long queUsrId, String username, String fullname,  org.lamsfoundation.lams.tool.mc.McSession mcSession, Set mcUsrAttempts) {
        this.queUsrId = queUsrId;
        this.username = username;
        this.fullname = fullname;
        this.mcSession = mcSession;
        this.mcUsrAttempts = mcUsrAttempts;
    }

    /** default constructor */
    public McQueUsr() {
    }

    /** minimal constructor */
    public McQueUsr(Long queUsrId, Set mcUsrAttempts) {
        this.queUsrId = queUsrId;
        this.mcUsrAttempts = mcUsrAttempts;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getQueUsrId() {
        return this.queUsrId;
    }

    public void setQueUsrId(Long queUsrId) {
        this.queUsrId = queUsrId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    
    public org.lamsfoundation.lams.tool.mc.McSession getMcSession() {
        return this.mcSession;
    }

    public void setMcSession(org.lamsfoundation.lams.tool.mc.McSession mcSession) {
        this.mcSession = mcSession;
    }

    public Set getMcUsrAttempts() {
    	if (this.mcUsrAttempts == null)
        	setMcUsrAttempts(new HashSet());
        return this.mcUsrAttempts;
    }
    
    
    public void setMcUsrAttempts(Set mcUsrAttempts) {
        this.mcUsrAttempts = mcUsrAttempts;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("uid", getUid())
            .toString();
    }

}
