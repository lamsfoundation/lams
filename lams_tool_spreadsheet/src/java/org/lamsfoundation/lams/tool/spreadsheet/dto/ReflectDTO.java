package org.lamsfoundation.lams.tool.spreadsheet.dto;

import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetUser;

/**
 *
 * @author Andrey Balan
 *
 */
public class ReflectDTO {
    private Long userUid;
    private String fullName;
    private String loginName;
    private boolean hasRefection;
    private String reflectInstructions;
    private boolean finishReflection;
    private String reflect;

    public ReflectDTO(SpreadsheetUser user) {
	this.setLoginName(user.getLoginName());
	this.setFullName(user.getFirstName() + " " + user.getLastName());
	this.setUserUid(user.getUid());
    }

    public boolean isFinishReflection() {
	return finishReflection;
    }

    public void setFinishReflection(boolean finishReflection) {
	this.finishReflection = finishReflection;
    }

    public String getFullName() {
	return fullName;
    }

    public void setFullName(String fullName) {
	this.fullName = fullName;
    }

    public boolean isHasRefection() {
	return hasRefection;
    }

    public void setHasRefection(boolean hasRefection) {
	this.hasRefection = hasRefection;
    }

    public String getLoginName() {
	return loginName;
    }

    public void setLoginName(String loginName) {
	this.loginName = loginName;
    }

    public String getReflect() {
	return reflect;
    }

    public void setReflect(String reflect) {
	this.reflect = reflect;
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    public Long getUserUid() {
	return userUid;
    }

    public void setUserUid(Long userUid) {
	this.userUid = userUid;
    }
}
