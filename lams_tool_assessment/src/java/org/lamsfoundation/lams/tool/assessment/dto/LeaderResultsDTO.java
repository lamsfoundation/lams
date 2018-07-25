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


package org.lamsfoundation.lams.tool.assessment.dto;

public class LeaderResultsDTO {

    private Long contentId;
    private int numberGroupsLeaderFinished;
    private String minMark;
    private String maxMark;
    private String avgMark; 

    public LeaderResultsDTO() {
    }

    public LeaderResultsDTO(Long contentId) {
	this.contentId = contentId;
    }

    public Long getContentId() {
	return contentId;
    }

    public void setContentId(Long contentId) {
	this.contentId = contentId;
    }

    public int getNumberGroupsLeaderFinished() {
	return numberGroupsLeaderFinished;
    }

    public void setNumberGroupsLeaderFinished(int numberGroupsLeaderFinished) {
	this.numberGroupsLeaderFinished = numberGroupsLeaderFinished;
    }

    public String getAvgMark() {
	return avgMark;
    }

    public void setAvgMark(String avgMark) {
	this.avgMark = avgMark;
    }

    public String getMinMark() {
	return minMark;
    }

    public void setMinMark(String minMark) {
	this.minMark = minMark;
    }

    public String getMaxMark() {
	return maxMark;
    }

    public void setMaxMark(String maxMark) {
	this.maxMark = maxMark;
    }

}
