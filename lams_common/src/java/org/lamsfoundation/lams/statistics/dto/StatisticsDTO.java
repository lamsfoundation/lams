package org.lamsfoundation.lams.statistics.dto;

/**
 * Class representing the overall statistics for the server
 *
 * @author lfoxton
 *
 */
public class StatisticsDTO {

    private long users;
    private long groups;
    private long subGroups;
    private long sequences;
    private long lessons;
    private long activities;
    private long completedActivities;

    public StatisticsDTO() {
    }

    public long getUsers() {
	return users;
    }

    public void setUsers(long users) {
	this.users = users;
    }

    public long getGroups() {
	return groups;
    }

    public void setGroups(long groups) {
	this.groups = groups;
    }

    public long getSubGroups() {
	return subGroups;
    }

    public void setSubGroups(long subGroups) {
	this.subGroups = subGroups;
    }

    public long getSequences() {
	return sequences;
    }

    public void setSequences(long sequences) {
	this.sequences = sequences;
    }

    public long getLessons() {
	return lessons;
    }

    public void setLessons(long lessons) {
	this.lessons = lessons;
    }

    public long getActivities() {
	return activities;
    }

    public void setActivities(long activities) {
	this.activities = activities;
    }

    public long getCompletedActivities() {
	return completedActivities;
    }

    public void setCompletedActivities(long completedActivities) {
	this.completedActivities = completedActivities;
    }
}
