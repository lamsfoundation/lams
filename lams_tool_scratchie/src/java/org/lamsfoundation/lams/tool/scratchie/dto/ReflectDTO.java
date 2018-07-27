package org.lamsfoundation.lams.tool.scratchie.dto;

import org.lamsfoundation.lams.usermanagement.User;

/**
 *
 * @author Andrey Balan
 */
public class ReflectDTO {
    private String fullName;
    private boolean isGroupLeader;
    private String groupName;
    private String reflection;

    public ReflectDTO(User user) {
	this.setFullName(user.getFirstName() + " " + user.getLastName());
    }

    public String getFullName() {
	return fullName;
    }

    public void setFullName(String fullName) {
	this.fullName = fullName;
    }

    public boolean isGroupLeader() {
	return isGroupLeader;
    }

    public void setIsGroupLeader(boolean isGroupLeader) {
	this.isGroupLeader = isGroupLeader;
    }

    public String getGroupName() {
	return groupName;
    }

    public void setGroupName(String groupName) {
	this.groupName = groupName;
    }

    public String getReflection() {
	return reflection;
    }

    public void setReflection(String reflection) {
	this.reflection = reflection;
    }
}
