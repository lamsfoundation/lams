package org.lamsfoundation.lams.tool.notebook.dto;

import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;

public class NotebookEntryDTO {

    private Long uid;

    private String entry;

    private String entryEscaped;

    private Date createDate;

    private Date lastModified;

    public NotebookEntryDTO(NotebookEntry entry) {
	this.uid = entry.getUid();
	this.entry = entry.getEntry();
	if (this.entry != null) {
	    this.entryEscaped = StringEscapeUtils.escapeJavaScript(this.entry);
	}
	this.createDate = entry.getCreateDate();
	this.lastModified = (entry.getLastModified() == null) ? entry.getCreateDate() : entry.getLastModified();
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

    public String getEntryEscaped() {
	return entryEscaped;
    }

    public void setEntryEscaped(String entryEscaped) {
	this.entryEscaped = entryEscaped;
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
