package org.lamsfoundation.lams.tool.peerreview.dto;

public class EmailPreviewDTO {
    private Long dateTimeStamp;
    private String emailHTML;
    private Long toolSessionId;
    private Long learnerUserId;
    
    public EmailPreviewDTO(String emailHTML, Long toolSessionId, Long learnerUserId) {
	this.emailHTML = emailHTML;
	this.toolSessionId = toolSessionId;
	this.learnerUserId = learnerUserId;
	this.dateTimeStamp = System.currentTimeMillis();
    }
    public String getEmailHTML() {
        return emailHTML;
    }
    public void setEmailHTML(String emailHTML) {
        this.emailHTML = emailHTML;
    }
    public Long getToolSessionId() {
        return toolSessionId;
    }
    public void setToolSessionId(Long toolSessionId) {
        this.toolSessionId = toolSessionId;
    }
    public Long getLearnerUserId() {
        return learnerUserId;
    }
    public void setLearnerUserId(Long learnerUserId) {
        this.learnerUserId = learnerUserId;
    }
    public Long getDateTimeStamp() {
        return dateTimeStamp;
    }
    public void setDateTimeStamp(Long dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }
    @Override
    public String toString() {
	return "EmailPreviewDTO [dateTimeStamp=" + dateTimeStamp + ", toolSessionId=" + toolSessionId
		+ ", learnerUserId=" + learnerUserId + "]";
    }

}
