package org.lamsfoundation.lams.tool.pixlr.dto;

import java.util.Date;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;

public class NotebookEntryDTO {

    private Long uid;

    private String entry;

    private Date createDate;

    private Date lastModified;

    public NotebookEntryDTO(NotebookEntry entry) {
	this.uid = entry.getUid();
	this.entry = entry.getEntry();
	this.createDate = entry.getCreateDate();
	this.lastModified = entry.getLastModified();
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public String getEntry() {
	return entry;
    }

    public void setEntry(String entry) {
	this.entry = entry;
    }

    public Date getLastModified() {
	return lastModified;
    }

    public void setLastModified(Date lastModified) {
	this.lastModified = lastModified;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

}
