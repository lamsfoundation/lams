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

package org.lamsfoundation.lams.tool.vote.dto;

import java.util.SortedSet;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * DTO that holds summary data for all the sessions - down to the %s, number of votes etc but not the actual answers.
 */
public class SummarySessionDTO implements Comparable<Object> {
    private Long sessionUid;
    private Long toolSessionId;
    private String sessionName;
    private int sessionUserCount;
    private boolean sessionFinished;
    private SortedSet<SessionNominationDTO> nominations;
    private Integer openTextNumberOfVotes;
    private Double openTextPercentageOfVotes;

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("toolSessionId", getToolSessionId())
		.append("sessionName", getSessionName()).toString();
    }

    @Override
    public int compareTo(Object o) {
	SummarySessionDTO sessionDTO = (SummarySessionDTO) o;

	if (sessionDTO == null) {
	    return 1;
	} else {
	    return sessionUid.compareTo(sessionDTO.getSessionUid());
	}
    }

    /**
     * @return Returns the sessionName.
     */
    public String getSessionName() {
	return sessionName;
    }

    /**
     * @param sessionName
     *            The sessionName to set.
     */
    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public Long getSessionUid() {
	return sessionUid;
    }

    public void setSessionUid(Long sessionUid) {
	this.sessionUid = sessionUid;
    }

    public Long getToolSessionId() {
	return toolSessionId;
    }

    public void setToolSessionId(Long toolSessionId) {
	this.toolSessionId = toolSessionId;
    }

    public int getSessionUserCount() {
	return sessionUserCount;
    }

    public void setSessionUserCount(int sessionUserCount) {
	this.sessionUserCount = sessionUserCount;
    }

    public boolean isSessionFinished() {
	return sessionFinished;
    }

    public void setSessionFinished(boolean sessionFinished) {
	this.sessionFinished = sessionFinished;
    }

    public SortedSet<SessionNominationDTO> getNominations() {
	return nominations;
    }

    public void setNominations(SortedSet<SessionNominationDTO> nominations) {
	this.nominations = nominations;
    }

    public Integer getOpenTextNumberOfVotes() {
	return openTextNumberOfVotes;
    }

    public void setOpenTextNumberOfVotes(Integer openTextNumberOfVotes) {
	this.openTextNumberOfVotes = openTextNumberOfVotes;
    }

    public Double getOpenTextPercentageOfVotes() {
	return openTextPercentageOfVotes;
    }

    public void setOpenTextPercentageOfVotes(Double openTextPercentageOfVotes) {
	this.openTextPercentageOfVotes = openTextPercentageOfVotes;
    }

}
