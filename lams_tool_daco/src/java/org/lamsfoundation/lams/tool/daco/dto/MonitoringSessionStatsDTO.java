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


package org.lamsfoundation.lams.tool.daco.dto;

/**
 * Basic statistics for a summary.
 *
 * @author Fiona Malikoff
 *
 */
public class MonitoringSessionStatsDTO {

    private Long sessionId;
    private String sessionName;
    private Integer numberLearners;
    private Integer totalRecordCount;

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    public Integer getNumberLearners() {
	return numberLearners != null ? numberLearners : 0;
    }

    public void setNumberLearners(Integer numberLearners) {
	this.numberLearners = numberLearners;
    }

    public Integer getTotalRecordCount() {
	return totalRecordCount != null ? totalRecordCount : 0;
    }

    public void setTotalRecordCount(Integer totalRecordCount) {
	this.totalRecordCount = totalRecordCount;
    }

    public Integer getAverageRecordCount() {
	if (numberLearners != null && numberLearners > 0 && totalRecordCount != null) {
	    return Math.round((float) totalRecordCount / numberLearners);
	}
	return 0;
    }
}