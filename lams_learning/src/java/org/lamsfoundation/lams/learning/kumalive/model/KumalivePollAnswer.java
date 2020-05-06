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
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "lams_kumalive_poll_answer")
public class KumalivePollAnswer implements Serializable {
    private static final long serialVersionUID = -760184191959618734L;

    @Id
    @Column(name = "answer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "poll_id")
    private KumalivePoll poll;

    @Column(name = "order_id")
    private Short orderId;

    @Column
    private String name;

//	 <map table="lams_kumalive_poll_vote" name="votes" lazy="false" order-by="vote_date ASC">
//         <key column="answer_id"/>
//         <index column="user_id" type="java.lang.Integer"/>
//         <element column="vote_date" type="java.util.Date"/>
//	</map>

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "kumalive_id")
//    @OrderBy("sequence_id ASC")

    @ElementCollection(fetch = FetchType.LAZY)
    @OrderBy("vote_date ASC")
    @MapKeyColumn(name = "user_id")
    @Column(name = "vote_date")
    @CollectionTable(name = "lams_kumalive_poll_vote", joinColumns = @JoinColumn(name = "answer_id"))
    private Map<Integer, Date> votes = new TreeMap<>();

    public KumalivePollAnswer() {
    }

    public KumalivePollAnswer(KumalivePoll poll, Short orderId, String name) {
	this.poll = poll;
	this.orderId = orderId;
	this.name = name;
    }

    public Long getAnswerId() {
	return answerId;
    }

    public void setAnswerId(Long answerId) {
	this.answerId = answerId;
    }

    public KumalivePoll getPoll() {
	return poll;
    }

    public void setPoll(KumalivePoll poll) {
	this.poll = poll;
    }

    public Short getOrderId() {
	return orderId;
    }

    public void setOrderId(Short orderId) {
	this.orderId = orderId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Map<Integer, Date> getVotes() {
	return votes;
    }

    public void setVotes(Map<Integer, Date> votes) {
	this.votes = votes;
    }
}