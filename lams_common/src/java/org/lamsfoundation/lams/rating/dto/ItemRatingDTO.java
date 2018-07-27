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

import java.util.Collection;
import java.util.List;

public class ItemRatingDTO {

    //common properties
    private Long itemId;
    private Collection<ItemRatingCriteriaDTO> criteriaDtos;

    //comments properties
    private boolean commentsEnabled;
    private Long commentsCriteriaId;
    private int commentsMinWordsLimit;
    private List<RatingCommentDTO> commentDtos;
    private RatingCommentDTO commentPostedByUser;

    //used only if certain options is ON
    private int countUsersRatedEachItem;

    public ItemRatingDTO() {
	commentsEnabled = false;
    }

    public Long getItemId() {
	return itemId;
    }

    public void setItemId(Long itemId) {
	this.itemId = itemId;
    }

    public Collection<ItemRatingCriteriaDTO> getCriteriaDtos() {
	return criteriaDtos;
    }

    public void setCriteriaDtos(Collection<ItemRatingCriteriaDTO> criteriaDtos) {
	this.criteriaDtos = criteriaDtos;
    }

    public boolean isCommentsEnabled() {
	return commentsEnabled;
    }

    public void setCommentsEnabled(boolean commentsCriteriaAvailable) {
	this.commentsEnabled = commentsCriteriaAvailable;
    }

    public Long getCommentsCriteriaId() {
	return commentsCriteriaId;
    }

    public void setCommentsCriteriaId(Long commentsCriteriaId) {
	this.commentsCriteriaId = commentsCriteriaId;
    }

    public int getCommentsMinWordsLimit() {
	return commentsMinWordsLimit;
    }

    public void setCommentsMinWordsLimit(int commentsMinWordsLimit) {
	this.commentsMinWordsLimit = commentsMinWordsLimit;
    }

    public List<RatingCommentDTO> getCommentDtos() {
	return commentDtos;
    }

    public void setCommentDtos(List<RatingCommentDTO> commentDtos) {
	this.commentDtos = commentDtos;
    }

    public RatingCommentDTO getCommentPostedByUser() {
	return commentPostedByUser;
    }

    public void setCommentPostedByUser(RatingCommentDTO commentPostedByUser) {
	this.commentPostedByUser = commentPostedByUser;
    }

    public int getCountUsersRatedEachItem() {
	return countUsersRatedEachItem;
    }

    public void setCountUsersRatedEachItem(int countUsersRatedEachItem) {
	this.countUsersRatedEachItem = countUsersRatedEachItem;
    }
}
