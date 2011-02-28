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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */  
 
/* $Id$ */  
package org.lamsfoundation.lams.tool.qa;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * ResponseRating
 * 
 * @author Andrey Balan
 * 
 * @hibernate.class table="tl_laqa11_response_rating"
 */
public class ResponseRating implements Serializable {

    private static final long serialVersionUID = 3500933985590642242L;

    private Long uid;
    private float rating;
    private QaQueUsr user;
    private QaUsrResp response;

    // **********************************************************
    // Function methods for ResponseRating
    // **********************************************************

    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (!(o instanceof ResponseRating))
	    return false;

	final ResponseRating genericEntity = (ResponseRating) o;

	return new EqualsBuilder().append(this.uid, genericEntity.uid).append(this.rating, genericEntity.rating)
		.append(this.user, genericEntity.user).isEquals();
    }

    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(rating).append(user).toHashCode();
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * @return Returns the log Uid.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * Returns user rated this response.
     * 
     * @hibernate.many-to-one column="user_id" cascade="none"
     * @return
     */
    public QaQueUsr getUser() {
	return user;
    }

    public void setUser(QaQueUsr user) {
	this.user = user;
    }

    /**
     * @hibernate.property column="rating"
     * @return
     */
    public float getRating() {
	return rating;
    }

    public void setRating(float rating) {
	this.rating = rating;
    }
    
    /**
     * @hibernate.many-to-one column="response_id" cascade="none"
     * @return
     */
    public QaUsrResp getResponse() {
	return response;
    }

    public void setResponse(QaUsrResp response) {
	this.response = response;
    }    
    
}
