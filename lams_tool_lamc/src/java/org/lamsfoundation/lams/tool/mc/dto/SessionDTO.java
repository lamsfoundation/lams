package org.lamsfoundation.lams.tool.mc.dto;

public class SessionDTO {

    private Long sessionId;
    private String sessionName;

    public SessionDTO() {
    }

    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }
}
