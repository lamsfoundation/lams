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

public class KumalivePollAnswer implements Serializable {

    private static final long serialVersionUID = -760184191959618734L;

    private Long answerId;
    private KumalivePoll poll;
    private Short orderId;
    private String name;
    private Map<Integer, Date> votes;

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