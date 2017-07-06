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
import java.util.Map;

import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;

public class Kumalive implements Serializable {

    private static final long serialVersionUID = -7932572065216398930L;

    private Long kumaliveId;
    private Organisation organisation;
    private User createdBy;
    private String name;
    private Boolean finished = false;
    private Map<User, Short> scores;
    
    public Kumalive(){
    }

    public Kumalive(Organisation organisation, User createdBy, String name) {
	this.organisation = organisation;
	this.createdBy = createdBy;
	this.name = name;
    }

    public Long getKumaliveId() {
	return kumaliveId;
    }

    public void setKumaliveId(Long kumaliveId) {
	this.kumaliveId = kumaliveId;
    }

    public Organisation getOrganisation() {
	return organisation;
    }

    public void setOrganisation(Organisation organisation) {
	this.organisation = organisation;
    }

    public User getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(User createdBy) {
	this.createdBy = createdBy;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Boolean getFinished() {
	return finished;
    }

    public void setFinished(Boolean finished) {
	this.finished = finished;
    }

    public Map<User, Short> getScores() {
	return scores;
    }

    public void setScores(Map<User, Short> scores) {
	this.scores = scores;
    }
}