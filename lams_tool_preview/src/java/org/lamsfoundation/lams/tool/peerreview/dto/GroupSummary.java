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

package org.lamsfoundation.lams.tool.peerreview.dto;

public class GroupSummary implements Comparable<GroupSummary> {

    private Long sessionId;
    private String sessionName;
    private boolean emailsSent;

    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public boolean isEmailsSent() {
	return emailsSent;
    }

    public void setEmailsSent(boolean emailsSent) {
	this.emailsSent = emailsSent;
    }

    @Override
    public int compareTo(GroupSummary o) {
	String name1 = this.sessionName.toLowerCase();
	String name2 = o.sessionName.toLowerCase();
	String nameWithoutNumbers1 = name1.replaceAll("\\d+", "");
	String nameWithoutNumbers2 = name2.replaceAll("\\d+", "");
	if (!nameWithoutNumbers1.equals(nameWithoutNumbers2)) {
	    return name1.compareTo(name2);
	}
	String numbers1 = name1.replaceAll("\\D+", "");
	String numbers2 = name2.replaceAll("\\D+", "");
	if (numbers1.length() == 0 || numbers2.length() == 0) {
	    return name1.compareTo(name2);
	}
	try {
	    Long num1 = Long.parseLong(numbers1);
	    Long num2 = Long.parseLong(numbers2);
	    return num1.compareTo(num2);
	} catch (Exception e) {
	    return name1.compareTo(name2);
	}
    }
}