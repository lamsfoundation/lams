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


package org.lamsfoundation.lams.tool.scratchie.dto;

import org.lamsfoundation.lams.tool.scratchie.model.ScratchieBurningQuestion;

public class BurningQuestionDTO {
    private ScratchieBurningQuestion burningQuestion;
    private String escapedBurningQuestion;
    private String sessionName;

    private Integer likeCount;
    //whether the leader of specified group liked this burningQuestion
    private boolean userLiked;
    //whether the leader of specified group created this burningQuestion
    private boolean userAuthor;

    public ScratchieBurningQuestion getBurningQuestion() {
	return burningQuestion;
    }

    public void setBurningQuestion(ScratchieBurningQuestion burningQuestion) {
	this.burningQuestion = burningQuestion;
    }

    public String getEscapedBurningQuestion() {
	return escapedBurningQuestion;
    }

    public void setEscapedBurningQuestion(String escapedBurningQuestion) {
	this.escapedBurningQuestion = escapedBurningQuestion;
    }

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public Integer getLikeCount() {
	return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
	this.likeCount = likeCount;
    }

    public boolean isUserLiked() {
	return userLiked;
    }

    public void setUserLiked(boolean userLiked) {
	this.userLiked = userLiked;
    }
    
    /**
     * @return whether the leader of specified group created this burningQuestion
     */
    public boolean isUserAuthor() {
	return userAuthor;
    }

    public void setUserAuthor(boolean userAuthor) {
	this.userAuthor = userAuthor;
    }
}
