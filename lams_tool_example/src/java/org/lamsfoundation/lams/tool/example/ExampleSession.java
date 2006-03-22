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

/* $Id$ */ 
package org.lamsfoundation.lams.tool.example;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * Represents the tool session.
 * 
 *        @hibernate.class
 *         table="tl_laex11_session"
 */

public class ExampleSession  implements java.io.Serializable {


    // Fields    
     private Long uid;
     private Date sessionEndDate;
     private Date sessionStartDate;
     private Integer status;
     private Long sessionId;
     private String sessionName;
     private Example example;
     private Set exampleUsers = new HashSet(0);

    // Constructors

    /** default constructor */
    public ExampleSession() {
    }

    
    /** full constructor */
    public ExampleSession(Date sessionEndDate, Date sessionStartDate, Integer status, Long sessionId, String sessionName, Example example, Set exampleUsers) {
        this.sessionEndDate = sessionEndDate;
        this.sessionStartDate = sessionStartDate;
        this.status = status;
        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.example = example;
        this.exampleUsers = exampleUsers;
    }

   
    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Long"
     *             column="uid"
     *         
     */

    public Long getUid() {
        return this.uid;
    }
    
    public void setUid(Long uid) {
        this.uid = uid;
    }
    /**       
     *            @hibernate.property
     *             column="session_end_date"
     *         
     */

    public Date getSessionEndDate() {
        return this.sessionEndDate;
    }
    
    public void setSessionEndDate(Date sessionEndDate) {
        this.sessionEndDate = sessionEndDate;
    }
    /**       
     *            @hibernate.property
     *             column="session_start_date"
     *         
     */

    public Date getSessionStartDate() {
        return this.sessionStartDate;
    }
    
    public void setSessionStartDate(Date sessionStartDate) {
        this.sessionStartDate = sessionStartDate;
    }
    /**       
     *            @hibernate.property
     *             column="status"
     *             length="11"
     *         
     */

    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**       
     *            @hibernate.property
     *             column="session_id"
     *      	   length="20"
     *         
     */

    public Long getSessionId() {
        return this.sessionId;
    }
    
    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }
    /**       
     *            @hibernate.property
     *             column="session_name"
     *             length="250"
     *         
     */

    public String getSessionName() {
        return this.sessionName;
    }
    
    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
    /**       
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="example_uid"         
     *         
     */

    public Example getExample() {
        return this.example;
    }
    
    public void setExample(Example example) {
        this.example = example;
    }
    /**       
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="session_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.tool.example.ExampleUser"
     *         
     */

    public Set getExampleUsers() {
        return this.exampleUsers;
    }
    
    public void setExampleUsers(Set exampleUsers) {
        this.exampleUsers = exampleUsers;
    }
   

    /**
     * toString
     * @return String
     */
     public String toString() {
	  StringBuffer buffer = new StringBuffer();

      buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
      buffer.append("sessionEndDate").append("='").append(getSessionEndDate()).append("' ");			
      buffer.append("sessionStartDate").append("='").append(getSessionStartDate()).append("' ");			
      buffer.append("status").append("='").append(getStatus()).append("' ");			
      buffer.append("sessionId").append("='").append(getSessionId()).append("' ");			
      buffer.append("sessionName").append("='").append(getSessionName()).append("' ");			
      buffer.append("]");
      
      return buffer.toString();
     }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof ExampleSession) ) return false;
		 ExampleSession castOther = ( ExampleSession ) other; 
         
		 return ( (this.getUid()==castOther.getUid()) || ( this.getUid()!=null && castOther.getUid()!=null && this.getUid().equals(castOther.getUid()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         result = 37 * result + ( getUid() == null ? 0 : this.getUid().hashCode() );
         return result;
   }   





}