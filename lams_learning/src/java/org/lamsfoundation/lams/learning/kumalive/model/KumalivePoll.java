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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "lams_kumalive_poll")
public class KumalivePoll implements Serializable {
    private static final long serialVersionUID = 5154401897739494150L;

    @Id
    @Column(name = "poll_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pollId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kumalive_id")
    private Kumalive kumalive;

    @Column
    private String name;

    @Column(name = "votes_released")
    private Boolean votesReleased;

    @Column(name = "voters_released")
    private Boolean votersReleased;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "finish_date")
    private Date finishDate;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "poll_id")
    @OrderBy("order_id ASC")
    private Set<KumalivePollAnswer> answers;

    public KumalivePoll() {
    }

    public KumalivePoll(Kumalive kumalive, String name) {
	this.kumalive = kumalive;
	this.name = name;
	this.startDate = new Date();
	this.answers = new HashSet<KumalivePollAnswer>();
    }

    public Long getPollId() {
	return pollId;
    }

    public void setPollId(Long pollId) {
	this.pollId = pollId;
    }

    public Kumalive getKumalive() {
	return kumalive;
    }

    public void setKumalive(Kumalive kumalive) {
	this.kumalive = kumalive;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Boolean getVotesReleased() {
	return votesReleased;
    }

    public void setVotesReleased(Boolean votesReleased) {
	this.votesReleased = votesReleased;
    }

    public Boolean getVotersReleased() {
	return votersReleased;
    }

    public void setVotersReleased(Boolean votersReleased) {
	this.votersReleased = votersReleased;
    }

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public Date getFinishDate() {
	return finishDate;
    }

    public void setFinishDate(Date endDate) {
	this.finishDate = endDate;
    }

    public Set<KumalivePollAnswer> getAnswers() {
	return answers;
    }

    public void setAnswers(Set<KumalivePollAnswer> answers) {
	this.answers = answers;
    }
}