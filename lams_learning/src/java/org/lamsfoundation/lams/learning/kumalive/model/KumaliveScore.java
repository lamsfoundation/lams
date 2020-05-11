/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.learning.kumalive.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.lamsfoundation.lams.usermanagement.User;

@Entity
@Table(name = "lams_kumalive_score")
public class KumaliveScore implements Serializable {
    private static final long serialVersionUID = -5191089091527630037L;

    @Id
    @Column(name = "score_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scoreId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rubric_id")
    private KumaliveRubric rubric;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Long batch;

    @Column
    private Short score;

    public KumaliveScore() {
    }

    public KumaliveScore(KumaliveRubric rubric, User user, Long batch, Short score) {
	this.rubric = rubric;
	this.user = user;
	this.batch = batch;
	this.score = score;
    }

    public Long getScoreId() {
	return scoreId;
    }

    public void setScoreId(Long scoreId) {
	this.scoreId = scoreId;
    }

    public KumaliveRubric getRubric() {
	return rubric;
    }

    public void setRubric(KumaliveRubric rubric) {
	this.rubric = rubric;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Long getBatch() {
	return batch;
    }

    public void setBatch(Long batch) {
	this.batch = batch;
    }

    public Short getScore() {
	return score;
    }

    public void setScore(Short score) {
	this.score = score;
    }
}