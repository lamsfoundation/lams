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


package org.lamsfoundation.lams.tool.spreadsheet.dto;

/**
 * @author Andrey Balan
 */
public class StatisticDTO {

    private String sessionName;
    private int markedCounter;
    private int notMarkedCounter;
    private int totalSpreadsheetsSentByLearners;

    public StatisticDTO() {
    }

    public StatisticDTO(String sessionName, int markedCounter, int notMarkedCounter,
	    int totalSpreadsheetsSentByLearners) {
	this.sessionName = sessionName;
	this.notMarkedCounter = notMarkedCounter;
	this.markedCounter = markedCounter;
	this.totalSpreadsheetsSentByLearners = totalSpreadsheetsSentByLearners;
    }

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public int getMarkedCounter() {
	return markedCounter;
    }

    public void setMarkedCount(int markedCounter) {
	this.markedCounter = markedCounter;
    }

    public int getNotMarkedCounter() {
	return notMarkedCounter;
    }

    public void setNotMarkedCounter(int notMarkedCounter) {
	this.notMarkedCounter = notMarkedCounter;
    }

    public int getTotalSpreadsheetsSentByLearners() {
	return totalSpreadsheetsSentByLearners;
    }

    public void setTotalSpreadsheetsSentByLearners(int totalSpreadsheetsSentByLearners) {
	this.totalSpreadsheetsSentByLearners = totalSpreadsheetsSentByLearners;
    }
}
