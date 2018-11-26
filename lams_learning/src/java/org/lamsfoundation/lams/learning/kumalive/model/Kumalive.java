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

import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;

@Entity
@Table(name = "lams_kumalive")
public class Kumalive implements Serializable {
    private static final long serialVersionUID = -7932572065216398930L;

    @Id
    @Column(name = "kumalive_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kumaliveId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column
    private String name;

    @Column
    private Boolean finished = false;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "kumalive_id")
    @OrderBy("order_id ASC")
    private Set<KumaliveRubric> rubrics = new HashSet<>();

    public Kumalive() {
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

    public Set<KumaliveRubric> getRubrics() {
	return rubrics;
    }

    public void setRubrics(Set<KumaliveRubric> rubrics) {
	this.rubrics = rubrics;
    }
}