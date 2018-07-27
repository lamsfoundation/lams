package org.lamsfoundation.lams.tool.peerreview.dto;

import java.util.Date;

import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewUser;

/**
 * @author Dapeng Ni
 */
public class ReflectDTO {
    private Long userUid;
    private String fullName;
    private String loginName;
    private String reflectInstrctions;
    private boolean finishReflection;
    private String reflect;
    private Date date;

    public ReflectDTO(PeerreviewUser user) {
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

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }
}
