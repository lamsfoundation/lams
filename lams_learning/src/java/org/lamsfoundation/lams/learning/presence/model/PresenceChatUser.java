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



package org.lamsfoundation.lams.learning.presence.model;

import java.util.Date;

/**
 *
 */
public class PresenceChatUser implements java.io.Serializable, Cloneable {

    private String nickname;

    private Long lessonId;

    private Date lastPresence;

    public PresenceChatUser() {
    }

    public PresenceChatUser(String nickname, Long lessonId, Date lastPresence) {
	this.nickname = nickname;
	this.lessonId = lessonId;
	this.lastPresence = lastPresence;
    }

    /**
     *
     */
    public String getNickname() {
	return nickname;
    }

    public void setNickname(String nickname) {
	this.nickname = nickname;
    }

    /**
     *
     */
    public Long getLessonId() {
	return lessonId;
    }

    public void setLessonId(Long lessonId) {
	this.lessonId = lessonId;
    }

    /**
     *
     */
    public Date getLastPresence() {
	return lastPresence;
    }

    public void setLastPresence(Date lastPresence) {
	this.lastPresence = lastPresence;
    }
}