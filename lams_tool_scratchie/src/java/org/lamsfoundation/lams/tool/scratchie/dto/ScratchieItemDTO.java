package org.lamsfoundation.lams.tool.scratchie.dto;

import java.util.LinkedList;
import java.util.List;

/**
 * ScratchieItem's DTO.
 *
 * @author Andrey Balan
 */
public class ScratchieItemDTO implements Cloneable {

    private Long uid;
    private String title;
    private Integer type;
    private List<OptionDTO> optionDtos;
    private boolean unraveledOnFirstAttempt;
    private int userMark;
    private int userAttempts;
    //sequence of optionDtos selected by user in the form of "X, Y, Z"
    private String optionsSequence;

    /**
     * Default contruction method.
     */
    public ScratchieItemDTO() {
	optionDtos = new LinkedList<>();
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
    
    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public List<OptionDTO> getOptionDtos() {
	return optionDtos;
    }

    public void setOptionDtos(List<OptionDTO> optionDtos) {
	this.optionDtos = optionDtos;
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
     * @return sequence of optionDtos selected by user in the form of "X, Y, Z"
     */
    public String getOptionsSequence() {
	return optionsSequence;
    }

    /**
     * @param optionsSequence
     *            sequence of optionDtos selected by user in the form of "X, Y, Z"
     */
    public void setOptionsSequence(String optionsSequence) {
	this.optionsSequence = optionsSequence;
    }
}
