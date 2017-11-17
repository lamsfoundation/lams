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

public class KumalivePoll implements Serializable {

    private static final long serialVersionUID = 5154401897739494150L;

    private Long pollId;
    private Kumalive kumalive;
    private String name;
    private Date startDate;
    private Date finishDate;
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