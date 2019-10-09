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

import java.util.LinkedList;
import java.util.List;

import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.util.HashUtil;

/**
 * Tool may contain several questions. Which in turn contain options.
 *
 * @author Andrey Balan
 */
public class OptionDTO implements Comparable<OptionDTO> {
    private Long qbQuestionUid;
    private String answer;
    boolean correct;
    private boolean mcqType = false;
    
    private boolean scratched;
    private int attemptOrder;
    private int[] attempts;
    //VSA questions doesn't have confidenceLevelDto.qbOptionUid
    private List<ConfidenceLevelDTO> confidenceLevelDtos;
    
    //****for MCQ questions***
    private Long qbOptionUid;
    private int displayOrder;
    
    //****for VSA questions***
    private int answerHash;
    private Long userId;

    public OptionDTO() {
	//init confidenceLevelDtos list
	this.confidenceLevelDtos = new LinkedList<>();
    }
    
    public OptionDTO(QbOption qbOption) {
	this();
	this.qbQuestionUid = qbOption.getQbQuestion().getUid();
	this.answer = qbOption.getName();
	this.correct = qbOption.isCorrect();
	this.mcqType = true;
	this.qbOptionUid = qbOption.getUid();
	this.displayOrder = qbOption.getDisplayOrder();
    }

    @Override
    public int compareTo(OptionDTO o) {
	return mcqType ? Integer.compare(this.displayOrder, o.displayOrder) : this.answer.compareTo(o.answer);
    }
    
    public void setQbQuestionUid(Long qbQuestionUid) {
	this.qbQuestionUid = qbQuestionUid;
    }

    public Long getQbQuestionUid() {
	return this.qbQuestionUid;
    }
    
    public void setQbOptionUid(Long qbOptionUid) {
	this.qbOptionUid = qbOptionUid;
    }

    public Long getQbOptionUid() {
	return this.qbOptionUid;
    }
    
    public int getDisplayOrder() {
	return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
	this.displayOrder = displayOrder;
    }
    
    public void setAnswer(String answer) {
	this.answer = answer;
	this.answerHash = answer.hashCode();
    }

    public String getAnswer() {
	return this.answer;
    }
    
    public void setAnswerHash(int answerHash) {
	this.answerHash = answerHash;
    }

    public int getAnswerHash() {
	return this.answerHash;
    }
    
    public boolean isCorrect() {
	return this.correct;
    }

    public void setCorrect(boolean correct) {
	this.correct = correct;
    }
    
    public void setMcqType(boolean mcqType) {
	this.mcqType = mcqType;
    }

    public boolean isMcqType() {
	return mcqType;
    }
    
    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Long getUserId() {
	return this.userId;
    }

    public void setScratched(boolean complete) {
	this.scratched = complete;
    }

    public boolean isScratched() {
	return scratched;
    }

    /**
     * @return in which order the student selected it
     */
    public int getAttemptOrder() {
	return attemptOrder;
    }

    public void setAttemptOrder(int attemptOrder) {
	this.attemptOrder = attemptOrder;
    }

    /**
     * Used for item summary page in monitor
     */
    public int[] getAttempts() {
	return attempts;
    }

    public void setAttempts(int[] attempts) {
	this.attempts = attempts;
    }

    public List<ConfidenceLevelDTO> getConfidenceLevelDtos() {
	return confidenceLevelDtos;
    }

    public void setConfidenceLevelDtos(List<ConfidenceLevelDTO> confidenceLevelDtos) {
	this.confidenceLevelDtos = confidenceLevelDtos;
    }
}