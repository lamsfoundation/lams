package org.lamsfoundation.lams.monitoring.dto;

import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

public class TblUserDTO extends UserDTO {

    private boolean groupLeader;
    private Integer iraCorrectAnswerCount;
    private Integer traCorrectAnswerCount;

    public TblUserDTO(UserDTO userDto) {
	super(userDto.getUserID(), userDto.getFirstName(), userDto.getLastName(), userDto.getLocaleCountry(),
		userDto.getLocaleLanguage(), userDto.getLocaleCountry(), userDto.getDirection(), userDto.getEmail(),
		userDto.getTheme(), userDto.getTimeZone(), null, null, null, null, userDto.getPortraitUuid());
    }

    public void setGroupLeader(boolean groupLeader) {
	this.groupLeader = groupLeader;
    }

    public boolean isGroupLeader() {
	return groupLeader;
    }

    public Integer getIraCorrectAnswerCount() {
	return iraCorrectAnswerCount;
    }

    public void setIraCorrectAnswerCount(Integer iraCorrectAnswerCount) {
	this.iraCorrectAnswerCount = iraCorrectAnswerCount;
    }

    public void setTraCorrectAnswerCount(Integer traCorrectAnswerCount) {
	this.traCorrectAnswerCount = traCorrectAnswerCount;
    }

    public Integer getCorrectAnswerCountPercentDelta() {
	if (iraCorrectAnswerCount == null || traCorrectAnswerCount == null) {
	    return null;
	}
	return iraCorrectAnswerCount.equals(0) ? traCorrectAnswerCount * 100
		: (traCorrectAnswerCount - iraCorrectAnswerCount) * 100 / iraCorrectAnswerCount;
    }

}