package org.lamsfoundation.lams.tool.scribe.dto;

import org.lamsfoundation.lams.tool.scribe.model.ScribeReportEntry;

public class ScribeReportEntryDTO implements Comparable<ScribeReportEntryDTO> {

    private Long uid;

    private String entryText;

    private ScribeHeadingDTO headingDTO;

    public ScribeReportEntryDTO(ScribeReportEntry reportEntry) {
	this.uid = reportEntry.getUid();
	this.entryText = reportEntry.getEntryText();
	this.headingDTO = new ScribeHeadingDTO(reportEntry.getScribeHeading());
    }

    @Override
    public int compareTo(ScribeReportEntryDTO o) {
	return this.headingDTO.compareTo(o.headingDTO);
    }

    // Getters / Setters
    public ScribeHeadingDTO getHeadingDTO() {
	return headingDTO;
    }

    public void setHeadingDTO(ScribeHeadingDTO headingDTO) {
	this.headingDTO = headingDTO;
    }

    public String getEntryText() {
	return entryText;
    }

    public void setEntryText(String entryText) {
	this.entryText = entryText;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

}