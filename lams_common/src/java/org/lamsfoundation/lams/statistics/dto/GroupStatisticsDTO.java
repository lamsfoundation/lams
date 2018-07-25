package org.lamsfoundation.lams.statistics.dto;

import java.util.ArrayList;

/**
 * DTO for group specific statistics
 *
 * @author lfoxton
 *
 */
public class GroupStatisticsDTO {

    private String name;
    private long totalUsers;
    private long learners;
    private long monitors;
    private long authors;
    private long lessons;

    private ArrayList<GroupStatisticsDTO> subGroups;

    public GroupStatisticsDTO() {
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public long getTotalUsers() {
	return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
	this.totalUsers = totalUsers;
    }

    public long getLearners() {
	return learners;
    }

    public void setLearners(long learners) {
	this.learners = learners;
    }

    public long getMonitors() {
	return monitors;
    }

    public void setMonitors(long monitors) {
	this.monitors = monitors;
    }

    public long getAuthors() {
	return authors;
    }

    public void setAuthors(long authors) {
	this.authors = authors;
    }

    public long getLessons() {
	return lessons;
    }

    public void setLessons(long lessons) {
	this.lessons = lessons;
    }

    public ArrayList<GroupStatisticsDTO> getSubGroups() {
	return subGroups;
    }

    public void setSubGroups(ArrayList<GroupStatisticsDTO> subGroups) {
	this.subGroups = subGroups;
    }
}
