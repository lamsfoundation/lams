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


package org.lamsfoundation.lams.rating.dto;

import java.util.Date;

/**
 * Holds minimum information required for displaying in Rating.tag Created in order to lighten the data passed from DB.
 *
 * @author Andrey Balan
 */
public class RatingCommentDTO {

    private Long itemId;

    private Long userId;

    private String userFullName;

    private String comment;

    private Date postedDate;

    public RatingCommentDTO() {
    }

    public RatingCommentDTO(Long itemId, Long userId, String userName, String comment, Date postedDate) {
	this.itemId = itemId;
	this.userId = userId;
	this.userFullName = userName;
	this.comment = comment;
	this.postedDate = postedDate;
    }

    /**
     */
    public Long getItemId() {
	return itemId;
    }

    public void setItemId(Long itemId) {
	this.itemId = itemId;
    }

    /**
     */
    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public void setUserFullName(String userFullName) {
	this.userFullName = userFullName;
    }

    public String getUserFullName() {
	return this.userFullName;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public String getComment() {
	return this.comment;
    }

    public Date getPostedDate() {
	return postedDate;
    }

    public void setPostedDate(Date postedDate) {
	this.postedDate = postedDate;
    }
}
