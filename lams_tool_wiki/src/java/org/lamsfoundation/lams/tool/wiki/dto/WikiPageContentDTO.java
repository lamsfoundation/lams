package org.lamsfoundation.lams.tool.wiki.dto;

import java.util.Date;

import org.lamsfoundation.lams.tool.wiki.model.WikiPageContent;

public class WikiPageContentDTO implements Comparable<WikiPageContentDTO> {

    public Long uid;
    public String body;
    public WikiUserDTO editorDTO;
    public Date editDate;
    public Long version;

    public WikiPageContentDTO() {
    }

    public WikiPageContentDTO(WikiPageContent pageContent) {
	this.uid = pageContent.getUid();
	this.body = pageContent.getBody().trim();
	this.editDate = pageContent.getEditDate();
	this.version = pageContent.getVersion();

	if (pageContent.getEditor() != null) {
	    this.editorDTO = new WikiUserDTO(pageContent.getEditor());
	}
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public String getBody() {
	return body;
    }

    public void setBody(String body) {
	this.body = body;
    }

    public WikiUserDTO getEditorDTO() {
	return editorDTO;
    }

    public void setEditorDTO(WikiUserDTO editorDTO) {
	this.editorDTO = editorDTO;
    }

    public Date getEditDate() {
	return editDate;
    }

    public void setEditDate(Date editDate) {
	this.editDate = editDate;
    }

    public Long getVersion() {
	return version;
    }

    public void setVersion(Long version) {
	this.version = version;
    }

    @Override
    public int compareTo(WikiPageContentDTO wikiPageContentDTO) {
	return wikiPageContentDTO.getUid().compareTo(uid) * -1;
    }
}
