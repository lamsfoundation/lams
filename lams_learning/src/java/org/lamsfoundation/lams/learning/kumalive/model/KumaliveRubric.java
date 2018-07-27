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

import org.lamsfoundation.lams.usermanagement.Organisation;

public class KumaliveRubric implements Serializable {

    private static final long serialVersionUID = 1425357203513480609L;

    private Long rubricId;
    private Organisation organisation;
    private Kumalive kumalive;
    private Short orderId;
    private String name;

    public KumaliveRubric() {
    }

    public KumaliveRubric(Organisation organisation, Kumalive kumalive, Short orderId, String name) {
	this.organisation = organisation;
	this.kumalive = kumalive;
	this.orderId = orderId;
	this.name = name;
    }

    public Long getRubricId() {
	return rubricId;
    }

    public void setRubricId(Long rubricId) {
	this.rubricId = rubricId;
    }

    public Organisation getOrganisation() {
	return organisation;
    }

    public void setOrganisation(Organisation organisation) {
	this.organisation = organisation;
    }

    public Kumalive getKumalive() {
	return kumalive;
    }

    public void setKumalive(Kumalive kumalive) {
	this.kumalive = kumalive;
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
}