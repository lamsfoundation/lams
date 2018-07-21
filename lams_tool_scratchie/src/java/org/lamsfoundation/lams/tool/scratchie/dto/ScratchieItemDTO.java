package org.lamsfoundation.lams.tool.scratchie.dto;

import java.util.HashSet;
import java.util.Set;

import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswer;

/**
 * ScratchieItem's DTO.
 *
 * @author Andrey Balan
 */
public class ScratchieItemDTO implements Cloneable {

    private Long uid;
    private String title;
    private Set<ScratchieAnswer> answers;
    private boolean unraveledOnFirstAttempt;
    private int userMark;
    private int userAttempts;
    //sequence of answers selected by user in the form of "X, Y, Z" 
    private String answersSequence;

    /**
     * Default contruction method.
     */
    public ScratchieItemDTO() {
	answers = new HashSet<ScratchieAnswer>();
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************
    public Long getUid() {
	return uid;
    }

    public void setUid(Long userID) {
	this.uid = userID;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Set<ScratchieAnswer> getAnswers() {
	return answers;
    }

    public void setAnswers(Set<ScratchieAnswer> answers) {
	this.answers = answers;
    }

    public boolean isUnraveledOnFirstAttempt() {
	return unraveledOnFirstAttempt;
    }

    public void setUnraveledOnFirstAttempt(boolean isUnraveledOnFirstAttempt) {
	this.unraveledOnFirstAttempt = isUnraveledOnFirstAttempt;
    }

    public int getUserMark() {
	return userMark;
    }

    public void setUserMark(int userMark) {
	this.userMark = userMark;
    }

    public int getUserAttempts() {
	return userAttempts;
    }

    public void setUserAttempts(int userAttempts) {
	this.userAttempts = userAttempts;
    }

    /**
     * @return sequence of answers selected by user in the form of "X, Y, Z" 
     */
    public String getAnswersSequence() {
	return answersSequence;
    }

    /**
     * @param answersSequence sequence of answers selected by user in the form of "X, Y, Z" 
     */
    public void setAnswersSequence(String answersSequence) {
	this.answersSequence = answersSequence;
    }
}

