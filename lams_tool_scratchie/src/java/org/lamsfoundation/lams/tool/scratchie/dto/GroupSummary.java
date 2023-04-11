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

package org.lamsfoundation.lams.tool.scratchie.dto;

import java.util.Collection;

import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;

public class GroupSummary {

    private Long sessionId;
    private String sessionName;
    private double mark;
    private int totalAttempts;
    private Double totalPercentage;
    private Long leaderUid;
    private boolean scratchingFinished;

    //used for itemSummary page
    private int numberColumns;

    private Collection<ScratchieUser> users;
    private Collection<Long> usersWhoReachedActivity;
    private Collection<ScratchieItemDTO> itemDtos;
    private Collection<OptionDTO> optionDtos;

    public GroupSummary() {
    }

    /**
     * Contruction method for monitoring summary function.
     *
     * <B>Don't not set isInitGroup and viewNumber fields</B>
     *
     * @param sessionName
     * @param item
     * @param isInitGroup
     */
    public GroupSummary(ScratchieSession session) {
	this.sessionId = session.getSessionId();
	this.sessionName = session.getSessionName();
	this.mark = session.getMark();
    }

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

    public double getMark() {
	return mark;
    }

    public void setMark(double mark) {
	this.mark = mark;
    }

    public int getTotalAttempts() {
	return totalAttempts;
    }

    public void setTotalAttempts(int totalAttempts) {
	this.totalAttempts = totalAttempts;
    }

    public Double getTotalPercentage() {
	return totalPercentage;
    }

    public void setTotalPercentage(Double totalPercentage) {
	this.totalPercentage = totalPercentage;
    }

    public int getNumberColumns() {
	return numberColumns;
    }

    public void setNumberColumns(int numberColumns) {
	this.numberColumns = numberColumns;
    }

    public Long getLeaderUid() {
	return leaderUid;
    }

    public void setLeaderUid(Long leaderUid) {
	this.leaderUid = leaderUid;
    }

    public boolean isScratchingFinished() {
	return scratchingFinished;
    }

    public void setScratchingFinished(boolean scratchingFinished) {
	this.scratchingFinished = scratchingFinished;
    }

    public Collection<ScratchieUser> getUsers() {
	return users;
    }

    public void setItemDtos(Collection<ScratchieItemDTO> itemDtos) {
	this.itemDtos = itemDtos;
    }

    public Collection<ScratchieItemDTO> getItemDtos() {
	return itemDtos;
    }

    public void setUsers(Collection<ScratchieUser> users) {
	this.users = users;
    }

    public Collection<OptionDTO> getOptionDtos() {
	return optionDtos;
    }

    public void setOptionDtos(Collection<OptionDTO> optionDtos) {
	this.optionDtos = optionDtos;
    }

    public Collection<Long> getUsersWhoReachedActivity() {
	return usersWhoReachedActivity;
    }

    public void setUsersWhoReachedActivity(Collection<Long> usersWhoReachedActivity) {
	this.usersWhoReachedActivity = usersWhoReachedActivity;
    }

}
