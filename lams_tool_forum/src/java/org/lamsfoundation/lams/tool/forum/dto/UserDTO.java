package org.lamsfoundation.lams.tool.forum.dto;

import org.lamsfoundation.lams.tool.forum.model.ForumUser;

public class UserDTO {

    private Long userUid;
    private String fullName;
    private String loginName;
    private int noOfPosts;
    private boolean hasRefection;
    private String reflectInstrctions;
    private boolean finishReflection;
    private String reflect;
    private boolean anyPostsMarked;

    public UserDTO() {
    }

    public UserDTO(ForumUser user) {
	this.setLoginName(user.getLoginName());
	this.setFullName(user.getFirstName() + " " + user.getLastName());
	this.setUserUid(user.getUid());

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

    public int getNoOfPosts() {
	return noOfPosts;
    }

    public void setNoOfPosts(int noOfPosts) {
	this.noOfPosts = noOfPosts;
    }

    public Long getUserUid() {
	return userUid;
    }

    public void setUserUid(Long userUid) {
	this.userUid = userUid;
    }

    public String getReflect() {
	return reflect;
    }

    public void setReflect(String reflection) {
	this.reflect = reflection;
    }

    public String getReflectInstrctions() {
	return reflectInstrctions;
    }

    public void setReflectInstrctions(String reflectInstrctions) {
	this.reflectInstrctions = reflectInstrctions;
    }

    public boolean isFinishReflection() {
	return finishReflection;
    }

    public void setFinishReflection(boolean finishReflection) {
	this.finishReflection = finishReflection;
    }

    public boolean isAnyPostsMarked() {
	return anyPostsMarked;
    }

    public void setAnyPostsMarked(boolean anyPostsMarked) {
	this.anyPostsMarked = anyPostsMarked;
    }
}
