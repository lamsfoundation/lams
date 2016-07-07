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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.monitoring.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class EmailScheduleMessageJobDTO implements Serializable, Comparable {

    private String triggerName;
    private Date triggerDate;
    private String emailBody;
    private int searchType;

    public Date getTriggerDate() {
	return triggerDate;
    }

    public void setTriggerDate(Date triggerDate) {
	this.triggerDate = triggerDate;
    }

    public String getEmailBody() {
	return emailBody;
    }

    public void setEmailBody(String emailBody) {
	this.emailBody = emailBody;
    }

    public int getSearchType() {
	return searchType;
    }

    public void setSearchType(int searchType) {
	this.searchType = searchType;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("triggerName", triggerName).append("emailBody", emailBody).append("triggerDate", triggerDate)
		.append("searchType", searchType).toString();
    }

    @Override
    public int compareTo(Object other) {
	EmailScheduleMessageJobDTO otherDto = (EmailScheduleMessageJobDTO) other;
	return new CompareToBuilder().append(triggerDate, otherDto.triggerDate).append(emailBody, otherDto.emailBody)
		.append(searchType, otherDto.searchType).append("triggerName", triggerName).toComparison();
    }

}
