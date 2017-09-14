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



package org.lamsfoundation.lams.tool.scribe.dto;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.scribe.model.ScribeReportEntry;
import org.lamsfoundation.lams.tool.scribe.model.ScribeSession;
import org.lamsfoundation.lams.tool.scribe.model.ScribeUser;

public class ScribeSessionDTO implements Comparable {

    Long sessionID;

    String sessionName;

    Set<ScribeUserDTO> userDTOs;

    int numberOfVotes;

    int numberOfLearners;

    int VotePercentage;

    String appointedScribe;
    Long appointedScribeUserId;

    Set<ScribeReportEntryDTO> reportDTOs;

    boolean forceComplete;

    boolean reportSubmitted;

    public ScribeSessionDTO(ScribeSession session) {
	this.sessionID = session.getSessionId();
	this.sessionName = session.getSessionName();
	this.forceComplete = session.isForceComplete();
	this.reportSubmitted = session.isReportSubmitted();

	ScribeUser appointedScribe = session.getAppointedScribe();
	if (appointedScribe == null) {
	    this.appointedScribe = null;
	    this.appointedScribeUserId = null;
	} else {
	    this.appointedScribe = appointedScribe.getFirstName() + " " + appointedScribe.getLastName();
	    this.appointedScribeUserId = appointedScribe.getUserId();
	}

	userDTOs = new TreeSet<ScribeUserDTO>();
	reportDTOs = new TreeSet<ScribeReportEntryDTO>();

	// add the report DTOs
	for (Iterator iter = session.getScribeReportEntries().iterator(); iter.hasNext();) {
	    ScribeReportEntry report = (ScribeReportEntry) iter.next();
	    reportDTOs.add(new ScribeReportEntryDTO(report));
	}
    }

    public ScribeSessionDTO() {
	userDTOs = new TreeSet<ScribeUserDTO>();
    }

    public Long getSessionID() {
	return sessionID;
    }

    public void setSessionID(Long sessionID) {
	this.sessionID = sessionID;
    }

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    @Override
    public int compareTo(Object o) {
	int returnValue;
	ScribeSessionDTO toSession = (ScribeSessionDTO) o;
	returnValue = this.sessionName.compareTo(toSession.sessionName);
	if (returnValue == 0) {
	    returnValue = this.sessionID.compareTo(toSession.sessionID);
	}
	return returnValue;
    }

    public Set<ScribeUserDTO> getUserDTOs() {
	return userDTOs;
    }

    public void setUserDTOs(Set<ScribeUserDTO> scribeUsers) {
	this.userDTOs = scribeUsers;
    }

    public int getNumberOfLearners() {
	return numberOfLearners;
    }

    public void setNumberOfLearners(int numberOfLearners) {
	this.numberOfLearners = numberOfLearners;
    }

    public int getNumberOfVotes() {
	return numberOfVotes;
    }

    public void setNumberOfVotes(int numberOfVotes) {
	this.numberOfVotes = numberOfVotes;
    }

    public Set<ScribeReportEntryDTO> getReportDTOs() {
	return reportDTOs;
    }

    public void setReportDTOs(Set<ScribeReportEntryDTO> reportDTOs) {
	this.reportDTOs = reportDTOs;
    }

    public String getAppointedScribe() {
	return appointedScribe;
    }

    public void setAppointedScribe(String appointedScribe) {
	this.appointedScribe = appointedScribe;
    }

    public int getVotePercentage() {
	return VotePercentage;
    }

    public void setVotePercentage(int votePercentage) {
	VotePercentage = votePercentage;
    }

    public boolean isForceComplete() {
	return forceComplete;
    }

    public void setForceComplete(boolean forceComplete) {
	this.forceComplete = forceComplete;
    }

    public boolean isReportSubmitted() {
	return reportSubmitted;
    }

    public void setReportSubmitted(boolean reportSubmitted) {
	this.reportSubmitted = reportSubmitted;
    }

    public Long getAppointedScribeUserId() {
        return appointedScribeUserId;
    }

    public void setAppointedScribeUserId(Long appointedScribeUserId) {
        this.appointedScribeUserId = appointedScribeUserId;
    }

}
