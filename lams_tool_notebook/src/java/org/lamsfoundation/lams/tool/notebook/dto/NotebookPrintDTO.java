package org.lamsfoundation.lams.tool.notebook.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class NotebookPrintDTO {
    public static class NotebookPrintUserDTO {
	private String firstName;
	private String lastName;
	private String email;
	private String entry;
	private Date entryModifiedDate;
	private String teacherComment;

	public String getFirstName() {
	    return firstName;
	}

	public void setFirstName(String firstName) {
	    this.firstName = firstName;
	}

	public String getLastName() {
	    return lastName;
	}

	public void setLastName(String lastName) {
	    this.lastName = lastName;
	}

	public String getEmail() {
	    return email;
	}

	public void setEmail(String email) {
	    this.email = email;
	}

	public String getEntry() {
	    return entry;
	}

	public void setEntry(String entry) {
	    this.entry = entry;
	}

	public Date getEntryModifiedDate() {
	    return entryModifiedDate;
	}

	public void setEntryModifiedDate(Date entryModifiedDate) {
	    this.entryModifiedDate = entryModifiedDate;
	}

	public String getTeacherComment() {
	    return teacherComment;
	}

	public void setTeacherComment(String teacherComment) {
	    this.teacherComment = teacherComment;
	}
    }

    private String title;
    private String instructions;
    private boolean groupedActivity;
    private Map<String, List<NotebookPrintUserDTO>> usersBySession = new TreeMap<>();

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public boolean isGroupedActivity() {
	return groupedActivity;
    }

    public void setGroupedActivity(boolean groupedActivity) {
	this.groupedActivity = groupedActivity;
    }

    public Map<String, List<NotebookPrintUserDTO>> getUsersBySession() {
	return usersBySession;
    }

    public void setUsersBySession(Map<String, List<NotebookPrintUserDTO>> usersBySession) {
	this.usersBySession = usersBySession;
    }
}
