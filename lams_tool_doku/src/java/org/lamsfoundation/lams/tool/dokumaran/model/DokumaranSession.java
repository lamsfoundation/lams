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

package org.lamsfoundation.lams.tool.dokumaran.model;

import java.util.Date;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants;

/**
 * Dokumaran
 *
 * @author Andrey Balan
 */
public class DokumaranSession {

    private static Logger log = Logger.getLogger(DokumaranSession.class);

    private Long uid;
    private Long sessionId;
    private String sessionName;
    private Dokumaran dokumaran;
    private Date sessionStartDate;
    private Date sessionEndDate;
    // finish or not
    private int status;
    private DokumaranUser groupLeader;
    private String etherpadGroupId;
    private String etherpadReadOnlyId;

    // **********************************************************
    // Get/Set methods
    // **********************************************************
    /**
     * @return Returns the learnerID.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uuid) {
	this.uid = uuid;
    }

    /**
     * @return
     */
    public Date getSessionEndDate() {
	return sessionEndDate;
    }

    public void setSessionEndDate(Date sessionEndDate) {
	this.sessionEndDate = sessionEndDate;
    }

    /**
     * @return
     */
    public Date getSessionStartDate() {
	return sessionStartDate;
    }

    public void setSessionStartDate(Date sessionStartDate) {
	this.sessionStartDate = sessionStartDate;
    }

    /**
     * @return
     */
    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    /**
     * @return
     */
    public Dokumaran getDokumaran() {
	return dokumaran;
    }

    public void setDokumaran(Dokumaran dokumaran) {
	this.dokumaran = dokumaran;
    }

    /**
     * @return
     */
    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    /**
     * @return Returns the session name
     */
    public String getSessionName() {
	return sessionName;
    }

    /**
     * @param sessionName
     *            The session name to set.
     */
    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }
    
    /**
    */
   public DokumaranUser getGroupLeader() {
	return this.groupLeader;
   }

   public void setGroupLeader(DokumaranUser groupLeader) {
	this.groupLeader = groupLeader;
   }
   
   /**
    * @return Returns the etherpadReadOnlyId
    */
   public String getEtherpadGroupId() {
	return etherpadGroupId;
   }

   /**
    * @param etherpadReadOnlyId
    *            The etherpadReadOnlyId to set.
    */
   public void setEtherpadGroupId(String etherpadGroupId) {
	this.etherpadGroupId = etherpadGroupId;
   }
   
   /**
    * @return Returns the etherpadReadOnlyId
    */
   public String getEtherpadReadOnlyId() {
	return etherpadReadOnlyId;
   }

   /**
    * @param etherpadReadOnlyId
    *            The etherpadReadOnlyId to set.
    */
   public void setEtherpadReadOnlyId(String etherpadReadOnlyId) {
	this.etherpadReadOnlyId = etherpadReadOnlyId;
   }
   
    public String getPadId() {
	// HashUtil.sha1(DokumaranConstants.DEFAULT_PAD_NAME + sessionId);
	return etherpadGroupId + "$" + DokumaranConstants.DEFAULT_PAD_NAME;
    }

}
