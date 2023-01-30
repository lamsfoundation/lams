package org.lamsfoundation.lams.monitoring.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.util.AlphanumComparator;

public class TblGroupDTO implements Comparable {

    private Long groupID;
    private String groupName;
    private int orderID;
    private List<TblUserDTO> userList;

    private TblUserDTO groupLeader;

    private Integer traCorrectAnswerCount;
    private Long correctAnswerCountPercentDelta;

    /**
     * Get the DTO for this group. Does not include the GroupBranchActivities as they will be in a separate array for
     * Flash.
     */
    public TblGroupDTO(Group group) {
	groupID = group.getGroupId();
	groupName = group.getGroupName();
	orderID = group.getOrderId();
	userList = new ArrayList<>();
    }

    @Override
    public int compareTo(Object o) {
	TblGroupDTO castOther = (TblGroupDTO) o;

	String grp1Name = castOther != null && castOther.getGroupName() != null
		? StringUtils.lowerCase(castOther.getGroupName())
		: "";
	String grp2Name = this.groupName != null ? StringUtils.lowerCase(this.groupName) : "";

	AlphanumComparator comparator = new AlphanumComparator();
	return -comparator.compare(grp1Name, grp2Name);
    }

    public Long getGroupID() {
	return groupID;
    }

    public void setGroupID(Long groupID) {
	this.groupID = groupID;
    }

    public String getGroupName() {
	return groupName;
    }

    public void setGroupName(String groupName) {
	this.groupName = groupName;
    }

    public int getOrderID() {
	return orderID;
    }

    public void setOrderID(int orderID) {
	this.orderID = orderID;
    }

    public List<TblUserDTO> getUserList() {
	return userList;
    }

    public void setUserList(List<TblUserDTO> userList) {
	this.userList = userList;
    }

    public TblUserDTO getGroupLeader() {
	return groupLeader;
    }

    public void setGroupLeader(TblUserDTO groupLeader) {
	this.groupLeader = groupLeader;
    }

    public Integer getTraCorrectAnswerCount() {
	return traCorrectAnswerCount;
    }

    public void setTraCorrectAnswerCount(Integer traCorrectAnswerCount) {
	this.traCorrectAnswerCount = traCorrectAnswerCount;
    }

    public Long getCorrectAnswerCountPercentDelta() {
	return correctAnswerCountPercentDelta;
    }

    public void setCorrectAnswerCountPercentDelta(Long correctAnswerCountPercentDelta) {
	this.correctAnswerCountPercentDelta = correctAnswerCountPercentDelta;
    }

    public Double getIraCorrectAnswerCountAverage() {
	OptionalDouble result = userList.stream().filter(u -> u.getIraCorrectAnswerCount() != null)
		.mapToDouble(TblUserDTO::getIraCorrectAnswerCount).average();
	return result.isPresent() ? result.getAsDouble() : null;
    }

    public Double getIraHighestCorrectAnswerCount() {
	OptionalDouble result = userList.stream().filter(u -> u.getIraCorrectAnswerCount() != null)
		.mapToDouble(TblUserDTO::getIraCorrectAnswerCount).max();
	return result.isPresent() ? result.getAsDouble() : null;
    }

    public Double getIraLowestCorrectAnswerCount() {
	OptionalDouble result = userList.stream().filter(u -> u.getIraCorrectAnswerCount() != null)
		.mapToDouble(TblUserDTO::getIraCorrectAnswerCount).min();
	return result.isPresent() ? result.getAsDouble() : null;
    }

    public Integer getCorrectAnswerCountPercentDeltaAverage() {
	OptionalDouble result = userList.stream().filter(u -> u.getCorrectAnswerCountPercentDelta() != null)
		.mapToDouble(TblUserDTO::getCorrectAnswerCountPercentDelta).average();
	return result.isPresent() ? Double.valueOf(result.getAsDouble()).intValue() : null;
    }

    public Double getHighestCorrectAnswerCountPercentDelta() {
	OptionalDouble result = userList.stream().filter(u -> u.getCorrectAnswerCountPercentDelta() != null)
		.mapToDouble(TblUserDTO::getCorrectAnswerCountPercentDelta).max();
	return result.isPresent() ? result.getAsDouble() : null;
    }

    public Double getLowestCorrectAnswerCountPercentDelta() {
	OptionalDouble result = userList.stream().filter(u -> u.getCorrectAnswerCountPercentDelta() != null)
		.mapToDouble(TblUserDTO::getCorrectAnswerCountPercentDelta).min();
	return result.isPresent() ? result.getAsDouble() : null;
    }
}