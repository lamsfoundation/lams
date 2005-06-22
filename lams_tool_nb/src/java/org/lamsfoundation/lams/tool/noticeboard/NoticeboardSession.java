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

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import java.io.Serializable;
import java.util.Date;

/**
 * @author mtruong
 *
 */

public class NoticeboardSession implements Serializable {

    
    public final static String NOT_ATTEMPTED = "NOT_ATTEMPTED";
    
    public final static String INCOMPLETE = "INCOMPLETE";
    
    public static final String COMPLETED = "COMPLETED";
    
	/** identifier field */
    private Long uid;
    
    /** persistent field*/
	private Long nbSessionId;
	
  	/** nullable persistent field */
	private Date sessionStartDate;
		
	/** nullable persistent field */
	private Date sessionEndDate;
	
	/** nullable persistent field */
	private String sessionStatus;
	
	/** persistent field */
	private NoticeboardContent nbContent;
	
	/** default constructor */
	public NoticeboardSession()
	{
				
	}
	
	/** full constructor */
	public NoticeboardSession(Long nbSessionId, 
							  NoticeboardContent nbContent,
							  Date sessionStartDate,
							  Date sessionEndDate,
							  String sessionStatus)
	{
		this.nbSessionId = nbSessionId;
		//this.nbContentId = nbContentId;
		this.nbContent = nbContent;
		this.sessionStartDate = sessionStartDate;
		this.sessionEndDate = sessionEndDate;
		this.sessionStatus = sessionStatus;
	}
	
	/**
	 * Constructor used when creating a new  noticeboardSession given
	 * the noticeboardContent id
	 */
	
	public NoticeboardSession(Long nbSessionId,
	        				  NoticeboardContent nbContent,
	        				  Date sessionStartDate,
	        				  String sessionStatus)
	{
	    this.nbSessionId = nbSessionId;
	    this.nbContent = nbContent;
	    this.sessionStartDate = sessionStartDate;
	    this.sessionEndDate = null;
	    this.sessionStatus = sessionStatus;
	}
	
	/**
	 * 		@hibernate.many-to-one
     *      not-null="true"
     *      @hibernate.column name="nb_content_id" 
	 */
	public NoticeboardContent getNbContent() {
		return nbContent;
	}
	
	public void setNbContent(NoticeboardContent nbContent) {
		this.nbContent = nbContent;
	}
		
	/**
	 *		@hibernate.property
     *      column="nb_session_id"
     *      length="20"
     *      not-null="true"
	 */
	public Long getNbSessionId() {
		return nbSessionId;
	}
	
	public void setNbSessionId(Long nbSessionId) {
		this.nbSessionId = nbSessionId;
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
	
}
