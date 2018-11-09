/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.survey.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

/**
 *
 * @author Steve.Ni
 */

@Entity
@Table(name = "tl_lasurv11_option")
public class SurveyOption implements Cloneable {
    private static final Logger log = Logger.getLogger(SurveyOption.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "sequence_id")
    private int sequenceId;

    @Column
    private String description;

    //****************************************************
    // DTO fields: percentage of response for this option. For monitoring summary usage.
    @Transient
    private double response;

    @Transient
    private String responseFormatStr;

    @Transient
    private int responseCount;

    @Override
    public Object clone() {
	Object obj = null;
	try {
	    obj = super.clone();
	    ((SurveyOption) obj).setUid(null);
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + SurveyOption.class + " failed");
	}

	return obj;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long userID) {
	this.uid = userID;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public int getSequenceId() {
	return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
    }

    public double getResponse() {
	return response;
    }

    public void setResponse(double reponse) {
	this.response = reponse;
    }

    public int getResponseCount() {
	return responseCount;
    }

    public void setResponseCount(int responseCount) {
	this.responseCount = responseCount;
    }

    public String getResponseFormatStr() {
	return responseFormatStr;
    }

    public void setResponseFormatStr(String reponseFormatStr) {
	this.responseFormatStr = reponseFormatStr;
    }
}