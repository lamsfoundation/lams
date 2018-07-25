package org.lamsfoundation.lams.tool.commonCartridge.dto;

import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeUser;

/**
 *
 * @author Andrey Balan
 *
 */
public class ReflectDTO {
    private Long userUid;
    private Long userId;
    private String fullName;
    private String loginName;
    private boolean hasRefection;
    private String reflectInstrctions;
    private boolean finishReflection;
    private String reflect;

    public ReflectDTO(CommonCartridgeUser user) {
	this.setLoginName(user.getLoginName());
	this.setFullName(user.getFirstName() + " " + user.getLastName());
	this.setUserUid(user.getUid());
	this.setUserId(user.getUserId());
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

    public String getReflectInstrctions() {
	return reflectInstrctions;
    }

    public void setReflectInstrctions(String reflectInstrctions) {
	this.reflectInstrctions = reflectInstrctions;
    }

    public Long getUserUid() {
	return userUid;
    }

    public void setUserUid(Long userUid) {
	this.userUid = userUid;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }
}
