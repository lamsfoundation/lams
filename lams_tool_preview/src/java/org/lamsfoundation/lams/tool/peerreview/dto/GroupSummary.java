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
	String name1 = this.sessionName.replaceAll("\\D+", "");
	String name2 = o.sessionName.replaceAll("\\D+", "");
	if (name1.length() == 0 || name2.length() == 0) {
	    return name1.compareTo(name2);
	}
	Long num1 = Long.parseLong(name1);
	Long num2 = Long.parseLong(name2);
	return num1.compareTo(num2);
    }
}