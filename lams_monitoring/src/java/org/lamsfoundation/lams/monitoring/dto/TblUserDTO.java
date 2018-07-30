package org.lamsfoundation.lams.monitoring.dto;

import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

public class TblUserDTO extends UserDTO {

    private boolean groupLeader;
    private Double iraScore;

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

    public void setIraScore(Double iraScore) {
	this.iraScore = iraScore;
    }

    public Double getIraScore() {
	return iraScore;
    }

}
