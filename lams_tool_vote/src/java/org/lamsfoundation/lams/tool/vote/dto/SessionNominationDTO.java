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

/**
 * DTO that holds summary data for a nomination, not the answers themselves
 */
public class SessionNominationDTO implements Comparable<Object> {

    Long questionUid;
    Integer numberOfVotes;
    Double percentageOfVotes;
    String nomination;

    @Override
    public int compareTo(Object o) {
	SessionNominationDTO dto = (SessionNominationDTO) o;
	if (dto == null) {
	    return 1;
	} else {
	    return questionUid.compareTo(dto.getQuestionUid());
	}
    }

    public Long getQuestionUid() {
	return questionUid;
    }

    public void setQuestionUid(Long questionUid) {
	this.questionUid = questionUid;
    }

    public Integer getNumberOfVotes() {
	return numberOfVotes;
    }

    public void setNumberOfVotes(Integer numberOfVotes) {
	this.numberOfVotes = numberOfVotes;
    }

    public Double getPercentageOfVotes() {
	return percentageOfVotes;
    }

    public void setPercentageOfVotes(Double percentageOfVotes) {
	this.percentageOfVotes = percentageOfVotes;
    }

    public String getNomination() {
	return nomination;
    }

    public void setNomination(String nomination) {
	this.nomination = nomination;
    }

}
