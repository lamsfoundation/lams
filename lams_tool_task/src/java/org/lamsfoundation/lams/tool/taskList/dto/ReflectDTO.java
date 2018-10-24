package org.lamsfoundation.lams.tool.taskList.dto;

import java.util.Date;

import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;

/**
 * DTO object used for representing reflection.
 *
 * @author Dapeng Ni
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
    private Date date;

    public ReflectDTO(TaskListUser user) {
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

    public void setReflectInstructions(String reflectInstrctions) {
	this.reflectInstrctions = reflectInstrctions;
    }

    public Long getUserUid() {
	return userUid;
    }

    public void setUserUid(Long userUid) {
	this.userUid = userUid;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }
}
