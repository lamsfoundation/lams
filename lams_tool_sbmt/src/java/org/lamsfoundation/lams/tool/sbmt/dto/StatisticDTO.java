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



package org.lamsfoundation.lams.tool.sbmt.dto;

/**
 *
 * @author Steve.Ni
 */
public class StatisticDTO {

    private long sessionId;
    private String sessionName;
    private int notMarkedCount;
    private int markedCount;
    private int totalUploadedFiles;

    public int getMarkedCount() {
	return markedCount;
    }

    public void setMarkedCount(int markedCount) {
	this.markedCount = markedCount;
    }

    public int getNotMarkedCount() {
	return notMarkedCount;
    }

    public void setNotMarkedCount(int notMarkedCount) {
	this.notMarkedCount = notMarkedCount;
    }

    public int getTotalUploadedFiles() {
	return totalUploadedFiles;
    }

    public void setTotalUploadedFiles(int totalUploadedFiles) {
	this.totalUploadedFiles = totalUploadedFiles;
    }

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public long getSessionId() {
	return sessionId;
    }

    public void setSessionId(long sessionId) {
	this.sessionId = sessionId;
    }
}
